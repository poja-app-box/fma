package api.poja.app.endpoint;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Some servlet request implementations (notably the AWS serverless-proxy adapter used to run Spring
 * behind Lambda) can return a {@code null} value array for a given key from {@link
 * HttpServletRequest#getParameterMap()} on multipart requests, instead of an empty array. That
 * trips up any code that does {@code String.join(",", entry.getValue())} without a null check (e.g.
 * the generated {@code RequestLoggerConfigurer}).
 *
 * <p>Rather than editing generated code, this filter wraps the request early in the chain so every
 * value array is guaranteed non-null by the time it reaches Spring's handler interceptors and
 * controllers.
 */
@Component
@Order(HIGHEST_PRECEDENCE)
public class NullSafeParameterMapFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest httpServletRequest) {
      chain.doFilter(new NullSafeParameterMapRequestWrapper(httpServletRequest), response);
    } else {
      chain.doFilter(request, response);
    }
  }

  private static final class NullSafeParameterMapRequestWrapper extends HttpServletRequestWrapper {

    private NullSafeParameterMapRequestWrapper(HttpServletRequest request) {
      super(request);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
      Map<String, String[]> sanitized = new LinkedHashMap<>();
      super.getParameterMap()
          .forEach((key, value) -> sanitized.put(key, value == null ? new String[0] : value));
      return sanitized;
    }

    @Override
    public String[] getParameterValues(String name) {
      String[] values = super.getParameterValues(name);
      return values == null ? new String[0] : values;
    }
  }
}

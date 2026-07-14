package api.poja.app.model;

import static lombok.AccessLevel.PRIVATE;

import java.net.URL;
import java.time.Instant;
import javax.annotation.Nullable;
import lombok.Builder;

@Builder(toBuilder = true, access = PRIVATE)
public record File(
    String id, String name, String uploaderEmail, Instant uploadedAt, @Nullable URL url) {
  public File url(URL url) {
    return this.toBuilder().url(url).build();
  }
}

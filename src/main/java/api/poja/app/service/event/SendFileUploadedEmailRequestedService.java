package api.poja.app.service.event;

import api.poja.app.endpoint.event.model.SendFileUploadedEmailRequested;
import api.poja.app.mail.Email;
import api.poja.app.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendFileUploadedEmailRequestedService
    implements Consumer<SendFileUploadedEmailRequested> {
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(SendFileUploadedEmailRequested event) {
    var uploaderEmail = event.getUploaderEmail();
    var downloadUrl = event.getPresignedDownloadUrl();

    InternetAddress recipientAddress = new InternetAddress(uploaderEmail);
    mailer.accept(
        new Email(
            recipientAddress,
            List.of(),
            List.of(),
            "",
            "Here is the file you uploaded: <a href='%s'>download</a>".formatted(downloadUrl),
            List.of()));
  }
}

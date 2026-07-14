package api.poja.app.service;

import static java.time.Instant.now;

import api.poja.app.endpoint.event.EventProducer;
import api.poja.app.endpoint.event.model.SendFileUploadedEmailRequested;
import api.poja.app.file.MultipartFileConverter;
import api.poja.app.file.bucket.BucketComponent;
import api.poja.app.model.File;
import api.poja.app.repository.FileRepository;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class FileService {

  private static final Duration DOWNLOAD_URL_EXP = Duration.ofHours(1);

  private final FileRepository repository;
  private final BucketComponent bucketComponent;
  private final MultipartFileConverter multipartFileConverter;
  private final EventProducer<SendFileUploadedEmailRequested> eventProducer;

  public List<File> findAllWithUrls(int pageFromOne, int itemsPerPage) {
    return repository.findAll(pageFromOne, itemsPerPage).stream()
        .map(e -> e.url(getPresignedUrl(e.id(), e.name())))
        .toList();
  }

  public URL getPresignedUrl(String fileId, String filename) {
    return bucketComponent.presign(getFileBucketKey(fileId, filename), DOWNLOAD_URL_EXP);
  }

  public File uploadFile(String fileId, String uploaderEmail, MultipartFile multipartFile) {
    var now = now();
    var filename = multipartFile.getOriginalFilename();
    var fileBucketKey = getFileBucketKey(fileId, filename);
    var presignedUrl = uploadFile(multipartFile, fileBucketKey);
    var file = new File(fileId, filename, uploaderEmail, now, presignedUrl);

    var saved = repository.save(file);

    eventProducer.accept(List.of(new SendFileUploadedEmailRequested(uploaderEmail, presignedUrl)));

    return saved;
  }

  private URL uploadFile(MultipartFile multipartFile, String fileBucketKey) {
    var file = multipartFileConverter.apply(multipartFile);
    bucketComponent.upload(file, fileBucketKey);
    return bucketComponent.presign(fileBucketKey, DOWNLOAD_URL_EXP);
  }

  private static String getFileBucketKey(String fileId, String filename) {
    return String.format("files/%s-%s", fileId.substring(0, 8), filename);
  }
}

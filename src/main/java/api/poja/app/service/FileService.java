package api.poja.app.service;

import api.poja.app.file.bucket.BucketComponent;
import api.poja.app.model.File;
import api.poja.app.repository.FileRepository;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FileService {

  private static final Duration DOWNLOAD_URL_EXP = Duration.ofHours(1);

  private final FileRepository repository;
  private final BucketComponent bucketComponent;

  public List<File> findAllWithUrls(int page, int count) {
    return repository.findAll(page, count).stream()
        .map(e -> e.url(getPresignedDownloadUrlByFileId(e.id())))
        .toList();
  }

  public URL getPresignedDownloadUrlByFileId(String fileId) {
    return bucketComponent.presign(getFileBucketKey(fileId), DOWNLOAD_URL_EXP);
  }

  private static String getFileBucketKey(String fileId) {
    return String.format("files/%s", fileId);
  }
}

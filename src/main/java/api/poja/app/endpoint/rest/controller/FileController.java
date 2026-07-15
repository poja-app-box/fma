package api.poja.app.endpoint.rest.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import api.poja.app.model.File;
import api.poja.app.service.FileService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class FileController {
  private final FileService service;

  @GetMapping("/files")
  public List<File> getFiles() {
    return service.findAllWithUrls(1, 100);
  }

  @PostMapping(value = "/files", consumes = MULTIPART_FORM_DATA_VALUE)
  public File uploadFile(
    @RequestPart("fileId") String fileId,
    @RequestPart("uploaderEmail") String uploaderEmail,
    @RequestPart("fileToUpload") MultipartFile fileToUpload) {
    return service.uploadFile(fileId, uploaderEmail, fileToUpload);
  }
}

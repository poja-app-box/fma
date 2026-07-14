package api.poja.app.endpoint.rest.controller;

import api.poja.app.model.File;
import api.poja.app.service.FileService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FileController {
  private final FileService service;

  @GetMapping("/files")
  public List<File> getFiles() {
    return service.findAllWithUrls(1, 100);
  }
}

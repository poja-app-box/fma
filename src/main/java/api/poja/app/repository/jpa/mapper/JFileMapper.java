package api.poja.app.repository.jpa.mapper;

import api.poja.app.model.File;
import api.poja.app.repository.jpa.model.JFile;
import org.springframework.stereotype.Component;

@Component
public class JFileMapper {
  public File toDomain(JFile jFile) {
    return new File(
        jFile.getId(), jFile.getName(), jFile.getUploaderEmail(), jFile.getUploadedAt(), null);
  }
}

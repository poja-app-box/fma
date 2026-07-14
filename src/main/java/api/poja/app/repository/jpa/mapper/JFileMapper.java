package api.poja.app.repository.jpa.mapper;

import api.poja.app.model.File;
import api.poja.app.repository.jpa.model.JFile;
import org.springframework.stereotype.Component;

@Component
public class JFileMapper {
  public File toDomain(JFile entity) {
    return new File(
        entity.getId(), entity.getName(), entity.getUploaderEmail(), entity.getUploadedAt(), null);
  }

  public JFile toEntity(File domain) {
    return JFile.builder()
        .id(domain.id())
        .name(domain.name())
        .uploadedAt(domain.uploadedAt())
        .uploaderEmail(domain.uploaderEmail())
        .build();
  }
}

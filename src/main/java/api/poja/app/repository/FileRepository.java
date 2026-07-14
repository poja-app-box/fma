package api.poja.app.repository;

import api.poja.app.model.File;
import api.poja.app.repository.jpa.JFileRepository;
import api.poja.app.repository.jpa.mapper.JFileMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class FileRepository {
  private final JFileRepository jRepository;
  private final JFileMapper jMapper;

  public List<File> findAll(int pageFromOne, int itemsPerPage) {
    var pageable = PageRequest.of(pageFromOne - 1, itemsPerPage);
    return jRepository.findAll(pageable).getContent().stream().map(jMapper::toDomain).toList();
  }

  public File save(File toSave) {
    var entity = jMapper.toEntity(toSave);
    return jMapper.toDomain(jRepository.save(entity));
  }
}

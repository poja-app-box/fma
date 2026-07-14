package api.poja.app.repository.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"file\"")
@Builder
public class JFile {
  @Id private String id;

  private String name;

  @Column(updatable = false)
  private String uploaderEmail;

  @Column(updatable = false)
  private Instant uploadedAt;
}

package ayyeka.assignment.ngnix_access_logs_parser.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class NginxLogfile {
    @Id @NonNull
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    @Version
    private Long version;
}

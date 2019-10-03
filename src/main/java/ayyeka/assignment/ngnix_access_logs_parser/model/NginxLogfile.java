package ayyeka.assignment.ngnix_access_logs_parser.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class NginxLogfile {
    @Id @NonNull
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToMany(mappedBy = "owningLogfile") @NonNull
    private List<NginxLogfileRow> rows;
}

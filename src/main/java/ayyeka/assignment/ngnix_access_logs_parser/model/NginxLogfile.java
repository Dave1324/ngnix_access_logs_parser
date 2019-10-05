package ayyeka.assignment.ngnix_access_logs_parser.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class NginxLogfile {
    @Id
    private Long id;
    @NonNull
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
}

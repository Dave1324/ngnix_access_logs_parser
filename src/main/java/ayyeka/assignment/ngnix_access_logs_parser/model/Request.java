package ayyeka.assignment.ngnix_access_logs_parser.model;

import ayyeka.assignment.ngnix_access_logs_parser.utils.IdFactory;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Request {
    @Id
    private Long id = IdFactory.getNextId();
    @NonNull
    private String request_method;
    @NonNull
    @Column(length = 3000)
    private String request_uri;
    @NonNull
    private String server_protocol;

    @Version
    private Long version;
}

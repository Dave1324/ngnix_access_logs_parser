package ayyeka.assignment.ngnix_access_logs_parser.model;

import ayyeka.assignment.ngnix_access_logs_parser.utils.IdFactory;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Request {
    @Id
    private Long id = IdFactory.getNextId();
    @NonNull
    private String method;
    @NonNull
    private String url;
}

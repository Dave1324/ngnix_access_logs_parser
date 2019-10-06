package ayyeka.assignment.ngnix_access_logs_parser.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String request_method;
    @NonNull
    @Column(length = 3000)
    private String request_uri;
    @Column(length = 3000)
    private String request_uri_query_string;
    @NonNull
    private String server_protocol;

    public Request(@NonNull String request_method, @NonNull String r_uri, @NonNull String server_protocol) {
        this.request_method = request_method;
        final boolean hasQueryString = r_uri.contains("?");
        this.request_uri = hasQueryString ? r_uri.substring(0, r_uri.indexOf("?")) : r_uri;
        this.request_uri_query_string = hasQueryString ?
                r_uri.substring(r_uri.indexOf("?") + 1) : "";
        this.server_protocol = server_protocol;
    }
}

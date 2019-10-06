package ayyeka.assignment.ngnix_access_logs_parser.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@Entity
public class NginxLogfileRow {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private NginxLogfile owningLogfile;
    private String remote_addr;
    private String remote_user;
    private LocalDateTime time_local;
    private String request_method;
    @Column(length = 3000)
    @Setter(AccessLevel.NONE)
    private String request_uri;
    @Column(length = 3000)
    @Setter(AccessLevel.NONE)
    private String request_uri_query_string;
    @Setter(AccessLevel.NONE)
    private String request_server_protocol;
    private String status;
    private Long body_bytes_sent;
    private String http_referer;
    private String unknown;//TODO - what is the variable between body bytes sent and user agent?
    private String user_agent;
    @Column(length = 3000)
    private String http_x_forwarded_for;
    private String upstream_time;
    private String response_time;

    private void setRequest(String requestString) {
        String[] values = requestString.split("\\s+");
        this.request_method = values[0];
        final boolean hasQueryString = values[1].contains("?");
        this.request_uri = hasQueryString ? values[1].substring(0, values[1].indexOf("?")) : values[1];
        this.request_uri_query_string = hasQueryString ?
                values[1].substring(values[1].indexOf("?") + 1) : "";
        this.request_server_protocol = values[2];
    }
    
    public NginxLogfileRow(Map<String, String> map, NginxLogfile owningLogfile){
        this.owningLogfile = owningLogfile;
        this.remote_addr = map.get("remote_addr");
        this.remote_user = map.get("remote_user");
        setTime_local(map.get("time_local"));
        this.setRequest(map.get("request"));
        this.status = map.get("status");
        this.body_bytes_sent = Long.valueOf(map.get("body_bytes_sent"));
        this.http_referer = map.get("http_referer");
        this.unknown = map.get("unknown");
        this.user_agent = map.get("user_agent");
        this.http_x_forwarded_for = map.get("http_x_forwarded_for");
        this.upstream_time = map.get("upstream_time");
        this.response_time = map.get("response_time");
    }

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
    private void setTime_local(String time_local) {
        this.time_local = LocalDateTime.parse(time_local, formatter);
    }
}

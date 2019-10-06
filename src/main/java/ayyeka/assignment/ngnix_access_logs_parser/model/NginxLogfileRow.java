package ayyeka.assignment.ngnix_access_logs_parser.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
public class NginxLogfileRow {
    @Id
    private Long id;
    @ManyToOne
    private NginxLogfile owningLogfile;
    private String remote_addr;
    private String remote_user;
    private String time_local;
    @OneToOne @Cascade(CascadeType.ALL)
    private Request request;
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
        this.request = new Request(values[0], values[1], values[2]);
    }
    
    public NginxLogfileRow(Map<String, String> map, NginxLogfile owningLogfile){
        this.owningLogfile = owningLogfile;
        this.remote_addr = map.get("remote_addr");
        this.remote_user = map.get("remote_user");
        this.time_local = map.get("time_local");
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
}

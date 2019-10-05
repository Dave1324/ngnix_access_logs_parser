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

    private NginxLogfileRow(){}
    public static NginxLogfileRow fromMap(Map<String, String> map, NginxLogfile owningLogfile){
        NginxLogfileRow instance = new NginxLogfileRow();
        instance.owningLogfile = owningLogfile;
        instance.remote_addr = map.get("remote_addr");
        instance.remote_user = map.get("remote_user");
        instance.time_local = map.get("time_local");
        instance.setRequest(map.get("request"));
        instance.status = map.get("status");
        instance.body_bytes_sent = Long.valueOf(map.get("body_bytes_sent"));
        instance.http_referer = map.get("http_referer");
        instance.unknown = map.get("unknown");
        instance.user_agent = map.get("user_agent");
        instance.http_x_forwarded_for = map.get("http_x_forwarded_for");
        instance.upstream_time = map.get("upstream_time");
        instance.response_time = map.get("response_time");
        return instance;
    }
}

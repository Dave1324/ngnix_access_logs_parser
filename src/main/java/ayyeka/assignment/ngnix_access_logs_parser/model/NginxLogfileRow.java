package ayyeka.assignment.ngnix_access_logs_parser.model;

import ayyeka.assignment.ngnix_access_logs_parser.utils.IdFactory;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class NginxLogfileRow {
    @Id
    private Long id = IdFactory.getNextId();
    @ManyToOne @NonNull
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

    public void setRequest(String requestString) {
        String[] values = requestString.split("\\s+");
        this.request = new Request(values[0], values[1], values[2]);
    }

    public void setBody_bytes_sent(String body_bytes_sent) {
        this.body_bytes_sent = Long.valueOf(body_bytes_sent);
    }

    @Version
    private Long version;
}

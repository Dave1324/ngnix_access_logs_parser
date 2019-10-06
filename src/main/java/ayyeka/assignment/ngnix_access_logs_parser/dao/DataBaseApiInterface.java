package ayyeka.assignment.ngnix_access_logs_parser.dao;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Transactional
public class DataBaseApiInterface {

    public Long insertLogfile(NginxLogfile nginxLogfile) {
        String insertStatement = "INSERT INTO nginx_logfile " +
                                    "(name)" +
                                    "VALUES(?)";
        var query = entityManager.createNativeQuery(insertStatement)
                .setParameter(1, nginxLogfile.getName());
        query.executeUpdate();
        return getInsertedId();
    }

    private long getInsertedId() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
    }

    public Long insertRow(NginxLogfileRow row) {
        String insertStatement = "INSERT INTO nginx_logfile_row" +
                                    "(body_bytes_sent," +
                                    "http_referer," +
                                    "http_x_forwarded_for," +
                                    "remote_addr," +
                                    "remote_user," +
                                    "response_time," +
                                    "status," +
                                    "time_local," +
                                    "unknown," +
                                    "upstream_time," +
                                    "user_agent," +
                                    "owning_logfile_id," +
                                    "request_method," +
                                    "request_uri," +
                                    "request_uri_query_string," +
                                    "request_server_protocol)" +
                                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        var query = entityManager
                .createNativeQuery(insertStatement)
                .setParameter(1, row.getBody_bytes_sent())
                .setParameter(2, row.getHttp_referer())
                .setParameter(3, row.getHttp_x_forwarded_for())
                .setParameter(4, row.getRemote_addr())
                .setParameter(5, row.getRemote_user())
                .setParameter(6, row.getResponse_time())
                .setParameter(7, row.getStatus())
                .setParameter(8, row.getTime_local())
                .setParameter(9, row.getUnknown())
                .setParameter(10, row.getUpstream_time())
                .setParameter(11, row.getUser_agent())
                .setParameter(12, row.getOwningLogfile().getId())
                .setParameter(13, row.getRequest_method())
                .setParameter(14, row.getRequest_uri())
                .setParameter(15, row.getRequest_uri_query_string())
                .setParameter(16, row.getRequest_server_protocol());
        query.executeUpdate();
        return getInsertedId();
    }

    @PersistenceContext
    private EntityManager entityManager;
    public List getTop5RequestedUrls(){
        return entityManager
                .createNativeQuery(
                        "SELECT request_uri, COUNT(*) AS magnitude " +
                                "FROM  nginx_logfile_row " +
                                "GROUP BY request_uri " +
                                "ORDER BY magnitude DESC " +
                                "LIMIT 5").getResultList();
    }

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH");
    public List getRange(String from){
        return entityManager
                .createNativeQuery(
                        "SELECT " +
                                "count(*), `request_uri`, " +
                                "YEAR(`time_local`), " +
                                "MONTH(`time_local`), " +
                                "DAY(`time_local`), " +
                                "HOUR(`time_local`)" +

                                "FROM `nginx_logfile_row`" +
                                "WHERE `time_local` < '" + LocalDateTime.parse(from, formatter) + ":00:00'" +
                                "GROUP BY " +
                                "`request_uri`, " +
                                "YEAR(`time_local`), " +
                                "MONTH(`time_local`), " +
                                "DAY(`time_local`), " +
                                "HOUR(`time_local`);")
                .getResultList();
    }


}

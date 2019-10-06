package ayyeka.assignment.ngnix_access_logs_parser.dao;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import ayyeka.assignment.ngnix_access_logs_parser.model.Request;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Component
@Transactional
public class DataBaseApiInterface {

    public Long insertLogfile(NginxLogfile nginxLogfile) {
        String insertStatement = "INSERT INTO nginx_logfile " +
                                    "(name," +
                                    "created_at)" +
                                    "VALUES(?,?)";
        var query = entityManager.createNativeQuery(insertStatement)
                .setParameter(1, nginxLogfile.getName())
                .setParameter(2, nginxLogfile.getCreatedAt());
        query.executeUpdate();
        return getInsertedId();
        //return logfileDao.save(nginxLogfile).getId();
    }

    private long getInsertedId() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).longValue();
    }

    public Long insertRequest(Request request) {
        String insertStatement = "INSERT INTO request" +
                                    "(request_method," +
                                    "request_uri," +
                                    "request_uri_query_string," +
                                    "server_protocol)" +
                                    "VALUES(?,?,?,?)";
        var query = entityManager
                .createNativeQuery(insertStatement)
                .setParameter(1, request.getRequest_method())
                .setParameter(2, request.getRequest_uri())
                .setParameter(3, request.getRequest_uri_query_string())
                .setParameter(4, request.getServer_protocol());
        query.executeUpdate();
        return getInsertedId();
        //return requestDao.save(request).getId();
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
                                    "request_id)" +
                                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                .setParameter(13, row.getRequest().getId());
        query.executeUpdate();
        return getInsertedId();
        //return rowDao.save(row).getId();
    }

    @PersistenceContext
    private EntityManager entityManager;
    public List getTop5RequestedUrls(){
        return entityManager
                .createNativeQuery(
                        "SELECT request_uri, COUNT(*) AS magnitude " +
                                "FROM request " +
                                "GROUP BY request_uri " +
                                "ORDER BY magnitude DESC " +
                                "LIMIT 5").getResultList();
    }
}

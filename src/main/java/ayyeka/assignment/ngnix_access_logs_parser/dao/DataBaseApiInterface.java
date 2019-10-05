package ayyeka.assignment.ngnix_access_logs_parser.dao;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import ayyeka.assignment.ngnix_access_logs_parser.model.Request;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class DataBaseApiInterface {

    @PersistenceContext
    private EntityManager entityManager;


    public void insertLogfile(NginxLogfile nginxLogfile) {
        String insertStatement = "INSERT INTO nginx_logfile " +
                                    "(name," +
                                    "created_at," +
                                    "version)" +
                                    "VALUES(?,?,?)";
        entityManager.createNativeQuery(insertStatement)
                .setParameter(1, nginxLogfile.getName())
                .setParameter(2, nginxLogfile.getCreatedAt())
                .setParameter(3, 0)
                .executeUpdate();
    }

    public void insertRequest(Request request) {
        String insertStatement = "INSERT INTO request" +
                                    "(id," +
                                    "request_method," +
                                    "request_uri," +
                                    "server_protocol," +
                                    "version)" +
                                    "VALUES(?,?,?,?,?)";
        entityManager
                .createNativeQuery(insertStatement)
                .setParameter(1, request.getId())
                .setParameter(2, request.getRequest_method())
                .setParameter(3, request.getRequest_uri())
                .setParameter(4, request.getServer_protocol())
                .setParameter(5, 0)//version
                .executeUpdate();
    }

    public void insertRow(NginxLogfileRow row) {
        String insertStatement = "INSERT INTO nginx_logfile_row" +
                                    "(id," +
                                    "body_bytes_sent," +
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
                                    "version," +
                                    "owning_logfile_name," +
                                    "request_id)" +
                                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        entityManager
                .createNativeQuery(insertStatement)
                .setParameter(1, row.getId())
                .setParameter(2, row.getBody_bytes_sent())
                .setParameter(3, row.getHttp_referer())
                .setParameter(4, row.getHttp_x_forwarded_for())
                .setParameter(5, row.getRemote_addr())
                .setParameter(6, row.getRemote_user())
                .setParameter(7, row.getResponse_time())
                .setParameter(8, row.getStatus())
                .setParameter(9, row.getTime_local())
                .setParameter(10, row.getUnknown())
                .setParameter(11, row.getUpstream_time())
                .setParameter(12, row.getUser_agent())
                .setParameter(13, 0)//version
                .setParameter(14, row.getOwningLogfile().getName())
                .setParameter(15, row.getRequest().getId())
                .executeUpdate();
    }
}

package ayyeka.assignment.ngnix_access_logs_parser.dao;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NginxLogfileDao extends JpaRepository<NginxLogfile, String> {

}

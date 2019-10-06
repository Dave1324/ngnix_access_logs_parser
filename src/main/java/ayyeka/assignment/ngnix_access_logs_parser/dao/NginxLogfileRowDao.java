package ayyeka.assignment.ngnix_access_logs_parser.dao;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NginxLogfileRowDao extends JpaRepository<NginxLogfileRow, Long> {
}

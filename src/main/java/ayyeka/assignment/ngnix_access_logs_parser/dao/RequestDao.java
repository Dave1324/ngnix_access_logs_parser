package ayyeka.assignment.ngnix_access_logs_parser.dao;

import ayyeka.assignment.ngnix_access_logs_parser.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDao extends JpaRepository<Request, Long> {
}

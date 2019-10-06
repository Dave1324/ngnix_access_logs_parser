package ayyeka.assignment.ngnix_access_logs_parser.parser;

import ayyeka.assignment.ngnix_access_logs_parser.dao.DataBaseApiInterface;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import lombok.val;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Parses an nginx file and saves its data into the database.
 * */

@Service
public class NginxLogfileParser {
    /**
     * opens up the logfile corresponding to 'key',
     * creates and saves a new instance of 'NginxLogfile',
     * and then parses & persists each line into a corresponding 'NginxLogfileRow'
     *
     * @param key*/
    @Transactional
    public void parseLogfile(String key) {
        printTime("starting", key);
        String line;
        BufferedReader br = logfileResolver.resolveFor(key);
        NginxLogfile nginxLogfile = new NginxLogfile(key);
        nginxLogfile.setId(db.insertLogfile(nginxLogfile));
        Detokenizer detokenizer = new Detokenizer(logFormat);
        try{
            while ((line = br.readLine()) != null) {
                Map<String, String> mappedResult = detokenizer.parse(line);
                val row = new NginxLogfileRow(mappedResult, nginxLogfile);
                db.insertRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printTime("ending", key);
    }

    @Autowired
    private DataBaseApiInterface db;
    @Autowired
    private LogfileResolver logfileResolver;
    @Value("${log-format}")
    private String logFormat;

    private final static Logger logger = Logger.getLogger(NginxLogfileParser.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private void printTime(String prefix, String key){
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);
        logger.log(Level.DEBUG, prefix + " : " + key + " @ " + formatDateTime);
    }
}

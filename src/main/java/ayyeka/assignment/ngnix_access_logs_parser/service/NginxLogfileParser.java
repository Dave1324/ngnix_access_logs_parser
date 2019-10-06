package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.dao.DataBaseApiInterface;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private void parseLogfile(String key) {
        String line;
        BufferedReader br = logfileResolver.resolveFor(key);
        NginxLogfile nginxLogfile = new NginxLogfile(key);
        Long logFileId = db.insertLogfile(nginxLogfile);
        nginxLogfile.setId(logFileId);
        int lines = 0;
        Detokenizer detokenizer = new Detokenizer(logFormat);
        LocalDateTime start = LocalDateTime.now();
        try{
            while ((line = br.readLine()) != null) {
                Map<String, String> parsedTokenMap = detokenizer.parse(line);
                final NginxLogfileRow row = new NginxLogfileRow(parsedTokenMap, nginxLogfile);
                db.insertRequest(row.getRequest());
                db.insertRow(row);
                lines++;
                if(lines > 1000){
                    long seconds = start.until(LocalDateTime.now(), ChronoUnit.SECONDS);
                    System.out.println(
                            lines + " lines in " + seconds + " seconds");
                    start = LocalDateTime.now();
                    lines = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Executor executor = Executors.newSingleThreadExecutor();
    public void enqueueLogfile(String key){
        executor.execute(() -> {
            printTime("starting", key);
            parseLogfile(key);
            printTime("ending", key);
        });
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
        /*logger.log(Level.DEBUG, */System.out.println(prefix + " : " + key + " @ " + formatDateTime);
    }

}

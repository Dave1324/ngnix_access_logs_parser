package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;

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
    public void parseLogfile(String key) {
        String line;
        BufferedReader br = logfileResolver.resolveFor(key);
        NginxLogfile nginxLogfile = new NginxLogfile(key, new ArrayList<>());
        try{
            while ((line = br.readLine()) != null)
                nginxLogfile.getRows().add(tokenParser.parseLogfileLine(line, nginxLogfile));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Autowired
    private LogfileResolver logfileResolver;
    @Autowired
    private TokenParser tokenParser;
    @Autowired
    private JpaRepository<NginxLogfile, String> nginxLogfileRepository;
    @Autowired
    private JpaRepository<NginxLogfileRow, Long> nginxLogfileRowRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    private BufferedReader getBufferedReaderFor(String rsc) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + rsc);
            InputStream inputStream = resource.getInputStream();
            return new BufferedReader(new InputStreamReader(inputStream));
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Value("${log-format}")
    private String logFormat;
}

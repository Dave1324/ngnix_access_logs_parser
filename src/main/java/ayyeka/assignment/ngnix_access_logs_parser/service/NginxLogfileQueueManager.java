package ayyeka.assignment.ngnix_access_logs_parser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class NginxLogfileQueueManager {
    @Autowired
    private ResourceLoader resourceLoader;
    @PostConstruct
    private void enqueueClasspathLogfiles(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("nginx_logs");
        assert url != null;
        String path = url.getPath();
        File[] logFiles = new File(path).listFiles();
        assert logFiles != null;
        for (File logFile : logFiles) {
            enqueueLogfile("nginx_logs/" + logFile.getName());
        }
    }

    @Autowired
    private NginxLogfileParser logfileParser;

    private Queue<String> logfileKeys = new LinkedList<>();

    public void enqueueLogfile(String key){
        logfileKeys.add(key);
    }

    @Scheduled(fixedDelay = 30000)//30 seconds
    public void processQueue(){
        while (!logfileKeys.isEmpty()){
            logfileParser.parseLogfile(logfileKeys.remove());
        }
    }
}

package ayyeka.assignment.ngnix_access_logs_parser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class NginxLogfileQueueManager {
    @Autowired
    private ResourceLoader resourceLoader;
    @PostConstruct
    private void enqueueClasspathLogfiles(){
        final ClassPathResource classPathResource =
                new ClassPathResource("target/classes/nginx_logs");
        File[] logFiles = new File(classPathResource.getPath()).listFiles();
        assert logFiles != null;
        assert logFiles.length > 0;
        for (File logFile : logFiles) {
            enqueueLogfile("nginx_logs/" + logFile.getName());
        }
    }

    @Autowired
    private NginxLogfileParser logfileParser;
    private Executor executor = Executors.newSingleThreadExecutor();
    public void enqueueLogfile(String key){
        executor.execute(() -> logfileParser.parseLogfile(key));
    }
}

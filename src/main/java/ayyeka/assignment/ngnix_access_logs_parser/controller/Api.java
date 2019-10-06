package ayyeka.assignment.ngnix_access_logs_parser.controller;

import ayyeka.assignment.ngnix_access_logs_parser.dao.DataBaseApiInterface;
import ayyeka.assignment.ngnix_access_logs_parser.service.NginxLogfileParser;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
public class Api {

    @GetMapping("/top5")
    public List top5Urls(){
        return db.getTop5RequestedUrls();
    }

    @Autowired
    private DataBaseApiInterface db;

    /*
    This is (roughly) the mechanism via which
    the log files would be received in real life.
    Note we're getting an array of strings - no direct file uploads here.
    The assumption is the files themselves have been dumped to
    an s3 bucket (or similar) and we're fetching them.
    */
    @Autowired
    private NginxLogfileParser logfileParser;
    private Executor executor = Executors.newFixedThreadPool(2);
    //@PostMapping("/enqueue-log-files")
    // - Since our 'LogfileResolver' bean is not currently set up for such a scenario, this is here for show only.
    public void enqueueLogFiles(@RequestParam("keys") String[] keys){
        for(String key : keys)
            executor.execute(() -> logfileParser.parseLogfile(key));
    }


    /*
    * This is what we're using in our little demo with
    * the log files being static classpath resources
    * */
    @PostConstruct
    private void enqueueClasspathLogfiles(){
        final ClassPathResource classPathResource =
                new ClassPathResource("target/classes/nginx_logs");
        File[] logFiles = new File(classPathResource.getPath()).listFiles();
        assert logFiles != null;
        assert logFiles.length > 0;
        String[] names = new String[logFiles.length];
        for (int i = 0; i < logFiles.length; i++) {
            names[i] = "nginx_logs/" + logFiles[i].getName();
        }
        enqueueLogFiles(names);
    }
}

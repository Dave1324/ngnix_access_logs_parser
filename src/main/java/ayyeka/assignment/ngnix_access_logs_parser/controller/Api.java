package ayyeka.assignment.ngnix_access_logs_parser.controller;

import ayyeka.assignment.ngnix_access_logs_parser.dao.DataBaseApiInterface;
import ayyeka.assignment.ngnix_access_logs_parser.dto.RangeTuple;
import ayyeka.assignment.ngnix_access_logs_parser.dto.Top5Tuple;
import ayyeka.assignment.ngnix_access_logs_parser.parser.NginxLogfileParser;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@SuppressWarnings("unchecked")
public class Api {

    @GetMapping("/top5")
    public List<Top5Tuple> top5Urls(){
        return Top5Tuple.top5Tuples(db.getTop5RequestedUrls());
    }


    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH");
    @GetMapping("/rate")
    public List<RangeTuple> rate(@RequestParam(value = "fromDate", required = false) String fromDate){
        if(fromDate == null) fromDate = formatter.format(LocalDateTime.now().minusHours(1));
        fromDate = fromDate.replaceAll("\"", "");
        return RangeTuple.mapRangeTuples(db.getRange(fromDate));
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
    private Executor executor = Executors.newFixedThreadPool(1);
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

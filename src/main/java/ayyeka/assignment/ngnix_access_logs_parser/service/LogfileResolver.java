package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/*
* Allows for the rest of the application to
* not care about where and how the actual files come from.
* this is loading them from the classpath but could easily
* be modified for s3, etc.
* */
@Service
public class LogfileResolver {
    @Autowired
    private ResourceLoader resourceLoader;
    public BufferedReader resolveFor(String key){
        Resource resource = resourceLoader.getResource("classpath:" + key);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert inputStream != null;
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}

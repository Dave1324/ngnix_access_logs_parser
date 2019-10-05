package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.dao.DataBaseApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsEngine {
    @Autowired
    private DataBaseApiInterface db;

    /*public String[] topNUrls(int n){

    }*/
}

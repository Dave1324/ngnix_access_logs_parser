package ayyeka.assignment.ngnix_access_logs_parser.controller;

import ayyeka.assignment.ngnix_access_logs_parser.dao.DataBaseApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {
    @GetMapping("/hi")
    public String test(){
        return "hello!";
    }
    /*@GetMapping("/top5")
    public String[] top5Urls(){

    }*/
}

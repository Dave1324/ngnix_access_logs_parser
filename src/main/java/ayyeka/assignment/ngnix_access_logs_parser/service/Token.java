package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Data
@NoArgsConstructor
public class Token {
    @NonNull
    private Boolean isStaticExpression;
    private String expression;
    public void setMappedValue(Map<String, String> resultsMap, String value){
        resultsMap.put(expression, value);
    }
}

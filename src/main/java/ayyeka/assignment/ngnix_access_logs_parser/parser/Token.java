package ayyeka.assignment.ngnix_access_logs_parser.parser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;

@Data
@NoArgsConstructor
public class Token {
    @NonNull
    private Boolean isStaticExpression;
    private String expression;
    public void setExpression(String expression){
        this.expression = expression;
    }
    public void setMappedValue(Map<String, String> resultsMap, String value){
        resultsMap.put(expression, value);
    }
}

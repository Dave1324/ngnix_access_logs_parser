package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Data
@NoArgsConstructor
public class Token {
    @NonNull
    private Boolean isStaticExpression;
    private String staticExpression;
    private String staticPrefix;
    private String staticPostfix;
    private Method dynamicVariableFieldSetter;
    public void setFieldValue(NginxLogfileRow instance, String value) {
        try {
            if(value.equals("-")) value = null;
            dynamicVariableFieldSetter.invoke(instance, value);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

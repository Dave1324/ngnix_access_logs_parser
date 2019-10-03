package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class TokenParser {





    public NginxLogfileRow parseLogfileLine(String line, NginxLogfile logfile){
        NginxLogfileRow row = new NginxLogfileRow(logfile);
        char[] chars = line.toCharArray();
        StringBuilder currentTokenValue = new StringBuilder();
        for (int lineIndex = 0, tokenIndex = 0; lineIndex < chars.length; lineIndex++) {
            boolean isWhiteSpace = chars[lineIndex] == ' ';
            boolean currentTokenValueInProgress = !currentTokenValue.toString().equals("");
            //if we've definitively reached the end of a token
            if(isWhiteSpace || lineIndex + 1 == chars.length){
                if(currentTokenValueInProgress){
                    //set the value and move on to the next token
                    expectedTokens.get(tokenIndex).setFieldValue(row, currentTokenValue.toString());
                    currentTokenValue = new StringBuilder();
                    tokenIndex++;
                }
            }//if we're still in the middle of a non-whitespace character continuum
            //if the current value evaluates to a static expression
            else if(matchesStaticExpression(tokenIndex, currentTokenValue.toString())){
                //mark the expression as evaluated and move on
                currentTokenValue = new StringBuilder();
                tokenIndex++;
            }//if the next character is not a legal character for a java variable declaration
            //then we've reached the end of this token
            else if(currentTokenValueInProgress && isNonAlphaNumericOrUnderscoreCharacter(chars[lineIndex + 1])){
                //set the value and move on to the next token
                expectedTokens.get(tokenIndex).setFieldValue(row, currentTokenValue.toString());
                currentTokenValue = new StringBuilder();
                tokenIndex++;
            }else {
                currentTokenValue.append(chars[lineIndex]);
            }
        }
        return row;
    }

    private boolean isNonAlphaNumericOrUnderscoreCharacter(char aChar) {
        return !Character.isLetter(aChar) && !Character.isDigit(aChar) && aChar != '_';
    }

    private boolean matchesStaticExpression(int tokenIndex, String value) {
        return expectedTokens.get(tokenIndex).getIsStaticExpression() &&
               expectedTokens.get(tokenIndex).getStaticExpression().equals(value);
    }
    private Token parseRawToken(String currentTokenTemplate){
        Token token = new Token();
        token.setIsStaticExpression(determineTokenType(currentTokenTemplate));
        currentTokenTemplate = currentTokenTemplate.substring(1);//strip '$' / '^' prefix
        if(!token.getIsStaticExpression())
             token.setDynamicVariableFieldSetter(determineCorrespondingFieldSetter(currentTokenTemplate));
        else token.setStaticExpression(currentTokenTemplate);
        return token;
    }
    @Value("${log-format}")
    private String logFormat;
    @PostConstruct
    private void parseLogFormat(){
        char[] charArray = logFormat.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if(c == '$' || c == '^')
                expectedTokens.add(parseRawToken(logFormat.substring(i, endOfToken(i, logFormat))));
        }
    }

    private int endOfToken(int i, String logFormat) {
        Pattern p = Pattern.compile("[\\s+$^]");
        Matcher m = p.matcher(logFormat.substring(i + 1));
        if (m.find())
            return i + m.start() + 1;
        return logFormat.length();
    }
    private List<Token> expectedTokens = new ArrayList<>();
    private Method determineCorrespondingFieldSetter(String s) {
        Method setter = logfileRowFieldSetters.get(s);
        if(setter == null)
            throw new IllegalArgumentException(
                    "invalid log-file format: No field in logFileRow corresponds to " + "\"" + s + "\"");
        return setter;
    }
    private boolean determineTokenType(String s) {
        if(s.startsWith("$")) return false;
        if(s.startsWith("^")) return true;
        throw new IllegalArgumentException("Invalid log-file format: " + s);
    }
    private static final Map<String, Method> logfileRowFieldSetters =
            getSetters().stream().collect(Collectors.toMap(
                    TokenParser::correspondingFieldName,
                    setter -> setter
            ));
    private static String correspondingFieldName(Method setter) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, setter.getName().split("set")[1]);
    }
    private static Collection<Method> getSetters() {
        return Arrays
                .stream(NginxLogfileRow.class.getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .collect(Collectors.toList());
    }
}

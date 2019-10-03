package ayyeka.assignment.ngnix_access_logs_parser.service;

import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfile;
import ayyeka.assignment.ngnix_access_logs_parser.model.NginxLogfileRow;
import com.google.common.base.CaseFormat;
import lombok.NonNull;
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
        try {
            Stack<Character> squareBrackets = new Stack<>();
            char[] chars = line.toCharArray();
            StringBuilder currentTokenValue = new StringBuilder();
            for (int lineIndex = 0, tokenIndex = 0; lineIndex < chars.length; lineIndex++) {
                handleAnyBracketsOrQuotes(chars, lineIndex, squareBrackets);
                if(isEndOfToken(chars, lineIndex, currentTokenValue, tokenIndex, squareBrackets)) {
                    assignTokenValueIfNonStatic(tokenIndex, currentTokenValue, row);
                    tokenIndex++;
                    currentTokenValue = new StringBuilder();
                }
                if(isMeaningfulCharacter(chars, lineIndex, squareBrackets))
                    currentTokenValue.append(chars[lineIndex]);
            }
        }catch (Exception e){
            System.out.println(line);
        }
        return row;
    }

    private boolean tokenWasStaticExpression(int tokenIndex) {
        return expectedTokens.get(tokenIndex).getIsStaticExpression();
    }

    private boolean isMeaningfulCharacter(char[] chars, int lineIndex, Stack<Character> squareBrackets) {
        return chars[lineIndex] != ' ' || isInBetweenBracketsOrQuotes(squareBrackets);
    }

    private boolean isInBetweenDoubleQuotes = false;
    private void handleAnyBracketsOrQuotes(char[] chars, int lineIndex, Stack<Character> squareBrackets) {
        final char c = chars[lineIndex];
        if(c == '[') squareBrackets.push('[');
        else if(c == '"') isInBetweenDoubleQuotes = !isInBetweenDoubleQuotes;
        else if(c == ']') if(!squareBrackets.empty()) squareBrackets.pop();
    }
    private boolean isInBetweenBracketsOrQuotes(Stack<Character> brackets){
        return !brackets.empty() || isInBetweenDoubleQuotes;
    }



    private boolean isEndOfToken(char[] chars, int lineIndex, StringBuilder currentTokenValue, int tokenIndex, Stack<Character> squareBrackets) {
        final boolean currentTokenValueInProgress = !currentTokenValue.toString().equals("");
        if(lineIndex + 1 == chars.length)
            return true;
        else if(chars[lineIndex] == ' ' && !isInBetweenBracketsOrQuotes(squareBrackets))
            return currentTokenValueInProgress;
        else if(isClosingBracketOrDoubleQuote(chars[lineIndex], squareBrackets))
            return true;
        else return matchesStaticExpression(tokenIndex, currentTokenValue);
    }



    private boolean isClosingBracketOrDoubleQuote(char c, Stack<Character> squareBrackets) {
        return (c == ']' && squareBrackets.empty())
                ||
                String.valueOf(c).equals("\"") && !isInBetweenDoubleQuotes;
    }

    private void assignTokenValueIfNonStatic(int tokenIndex, StringBuilder currentTokenValue, NginxLogfileRow row) {
        String value = currentTokenValue.toString();
        final Token expectedToken = expectedTokens.get(tokenIndex);
        if(!expectedToken.getIsStaticExpression())
            expectedToken.setFieldValue(row, value);
    }

    private boolean matchesStaticExpression(int tokenIndex, StringBuilder value) {
        return expectedTokens.get(tokenIndex).getIsStaticExpression() &&
               expectedTokens.get(tokenIndex).getStaticExpression().equals(value.toString());
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

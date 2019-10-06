package ayyeka.assignment.ngnix_access_logs_parser.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class RangeTuple {
    @NonNull
    private String url;
    @NonNull
    private String dateTime;
    @NonNull
    private Long hits;

    public static List<RangeTuple> mapRangeTuples(List<Object[]> raw){
        List<RangeTuple> rangeTupleList = new ArrayList<>();
        for (Object[] rawResult : raw) {
            rangeTupleList.add(
                    new RangeTuple(
                            (String) rawResult[1],
                            toDateTime(rawResult),
                            Long.valueOf(rawResult[0].toString())));
        }
        return rangeTupleList;
    }

    private static String toDateTime(Object[] rawResult) {
        return rawResult[2].toString() + "-" +
                rawResult[3].toString() + "-" +
                rawResult[4].toString() + " " +
                rawResult[5].toString() + ":00";
    }
}

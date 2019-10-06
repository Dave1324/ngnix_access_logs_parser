package ayyeka.assignment.ngnix_access_logs_parser.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class Top5Tuple {
    @NonNull
    private String url;
    @NonNull
    private Long hits;

    public static List<Top5Tuple> top5Tuples(List<Object[]> raw){
        List<Top5Tuple> top5Tuples = new ArrayList<>();
        for (Object[] rawResult : raw) {
            top5Tuples.add(new Top5Tuple(
                    rawResult[0].toString(),
                    Long.valueOf(rawResult[1].toString())
            ));
        }
        return top5Tuples;
    }
}

package fhv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    private String type;
    private String id;
    private int minTime;
    private int estTime;
    private int maxTime;
    private int duration;
    private int earlyStart;
    private int earlyFinish;
    private int lateStart;
    private int lateFinish;
    private LinkEnd source;
    private LinkEnd target;
    private int isCriticalPath = 0;
}
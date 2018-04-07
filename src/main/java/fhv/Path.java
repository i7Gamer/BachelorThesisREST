package fhv;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Path {
    List<Node> nodes = new ArrayList<>();
    int durationSum = 0;
}

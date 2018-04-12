package fhv;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Path {
    List<Node> nodes = new ArrayList<>();
    int durationSum = 0;
}

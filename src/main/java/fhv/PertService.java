package fhv;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class PertService {

    List<Node> elements;

    public List<Node> getPERT(List<Node> nodes) {
        elements = nodes.stream().filter(n -> !n.getType().equals("link")).collect(Collectors.toList());

        for (Node n : elements) {
            if (n.getEstTime() != 0 && n.getMinTime() != 0 && n.getMaxTime() != 0) {
                n.setDuration((n.getMinTime() + 4 * n.getEstTime() + n.getMaxTime()) / 6);
            }
        }

        return elements;
    }
}

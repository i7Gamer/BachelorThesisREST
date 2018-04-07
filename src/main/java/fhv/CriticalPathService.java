package fhv;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriticalPathService {

    public List<Node> getCP(List<Node> nodes) {
        List<Node> links = nodes.stream().filter(n -> n.getType().equals("link")).collect(Collectors.toList());
        List<Node> elements = nodes.stream().filter(n -> !n.getType().equals("link")).collect(Collectors.toList());

        Node start = elements.stream().findFirst().filter(n -> n.getType().equals("basic.Path")).get();

        List<Node> startElements = new ArrayList<>();

        List<Node> startLinks = links.stream().filter(n -> n.getTarget().getId().equals(start.getId()) || n.getSource().getId().equals(start.getId())).collect(Collectors.toList());

        for (Node node : startLinks) {
            if (node.getSource().getId().equals(start)) {
                startElements.add(elements.stream().findFirst().filter(n -> n.getId().equals(node.getTarget())).get());
            } else {
                startElements.add(elements.stream().findFirst().filter(n -> n.getId().equals(node.getSource())).get());
            }
        }

        // baum ab startElementen durchlaufen und kritischen pfad berechnen, anschließend rücklaufened restliche werte berechnen

        return nodes;
    }
}
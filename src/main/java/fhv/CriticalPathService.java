package fhv;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
public class CriticalPathService {

    List<Node> elements;
    List<Node> links;
    List<Path> allPaths = new ArrayList<>();

    public List<Node> getCP(List<Node> nodes) throws Exception {

        links = nodes.stream().filter(n -> n.getType().equals("link")).collect(Collectors.toList());
        elements = nodes.stream().filter(n -> !n.getType().equals("link")).collect(Collectors.toList());

        List<Node> startElements = new ArrayList<>();

        if (elements.size() == 0 || links.size() == 0) {
            throw new Exception("No elements or links found");
        }

        for (Node n : elements) {
            List<Node> linksToNode = links.stream().filter(l -> l.getTarget().getId().equals(n.getId())).collect(Collectors.toList());
            if (linksToNode.size() == 0) {
                startElements.add(n);
            }
            n.setEarlyFinish(0);
            n.setEarlyStart(0);
            n.setLateFinish(0);
            n.setLateStart(0);
        }

        if (startElements.size() == 0) {
            throw new Exception("No starting elements found");
        }

        // baum ab startElementen durchlaufen und kritischen pfad berechnen, anschließend rücklaufened restliche werte berechnen
        Path criticalPath = new Path();
        for (Node node: startElements) {
            Path path1 = getCriticalPath(new Path(), node);
            if (path1.durationSum > criticalPath.durationSum) {
                criticalPath = path1;
            }
        }

        // alle pfade von hinten nach vorne durchlaufen und restliche berechnungen durchführen
        for (Path p : allPaths) {
            int value = criticalPath.getDurationSum();
            for (int i = p.nodes.size() - 1; i >= 0; i--) {
                Node n = p.nodes.get(i);
                if (n.getLateFinish() > value || n.getLateFinish() == 0) {
                    n.setLateFinish(value);
                    value = value - n.getDuration();
                    n.setLateStart(value);
                } else {
                    value = value - n.getDuration();
                }
            }
        }

        // critical path durchlaufen
        for (Node node : elements) {
            if (node.getEarlyStart() == node.getLateStart() && node.getLateFinish() == node.getEarlyFinish()) {
                node.setIsCriticalPath(1);
            }
        }
        return elements;
    }

    public Path getCriticalPath(Path path, Node currentElement) throws Exception {

        if (path.nodes.contains(currentElement)) {
            throw new Exception("Cycle detected, critical path cannot be detected");
        }

        // insert early start and finish
        if (path.durationSum > currentElement.getEarlyStart()) {
            currentElement.setEarlyStart(path.durationSum);
        }
        path.durationSum =  path.durationSum + currentElement.getDuration();
        if (path.durationSum > currentElement.getEarlyFinish()) {
            currentElement.setEarlyFinish(path.durationSum);
        }
        // add current element
        path.nodes.add(currentElement);

        // get links and call function for every link, remove link so it won't be used again
        // if no links exist, give back critical path
        List<Node> links = this.links.stream().filter(n -> n.getSource().getId().equals(currentElement.getId()))
                .collect(Collectors.toList());

        if(links.isEmpty()){
            allPaths.add(path);
            return path;
        }
        else {
            List<Path> paths = new ArrayList<>();
            for(Node link: links ) {
                Path newPathToSend = new Path();
                for (Node node : path.nodes) {
                    newPathToSend.nodes.add(node);
                    newPathToSend.durationSum = path.durationSum;
                }

                Node nextElement = elements.stream().filter(n -> n.getId().equals(link.getTarget().getId())).findFirst().get();

                paths.add(getCriticalPath(newPathToSend, nextElement));
            }

            for(Path cp : paths){
                if(cp.durationSum > path.durationSum){
                    path = cp;
                }
            }
        }
        return path;
    }
}
package fhv;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriticalPathService {

    List<Node> elements;
    List<Node> links;
    List<Path> allPaths = new ArrayList<>();

    public List<Node> getCP(List<Node> nodes) {

        links = nodes.stream().filter(n -> n.getType().equals("link")).collect(Collectors.toList());
        elements = nodes.stream().filter(n -> !n.getType().equals("link")).collect(Collectors.toList());

        List<Node> startElements = new ArrayList<>();

        for (Node n : elements) {
            List<Node> linksToNode = links.stream().filter(l -> l.getTarget().getId().equals(n.getId())).collect(Collectors.toList());
            if (linksToNode.size() == 0) {
                startElements.add(n);
            }
        }

        // baum ab startElementen durchlaufen und kritischen pfad berechnen, anschließend rücklaufened restliche werte berechnen
        Path criticalPath = new Path();
        for (Node node: startElements) {
            Path path1 = getCriticalPath(new Path(), node);
            if (path1.durationSum > criticalPath.durationSum) {
                criticalPath = path1;
            }
        }

        // critical path durchlaufen
        for (Node node : criticalPath.nodes) {
            elements.stream().filter(n -> n.getId().equals(node.getId())).findFirst().get().setCriticalPath(true);
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
        return elements;
    }

    public Path getCriticalPath(Path path, Node currentElement){
        // insert early start and finish
        currentElement.setEarlyStart(path.durationSum);
        path.durationSum =  path.durationSum + currentElement.getDuration();
        currentElement.setEarlyFinish(path.durationSum);

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
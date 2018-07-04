package fhv;

import lombok.NoArgsConstructor;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class MonteCarloService {
    private int sampleSize = 100;

    @Autowired
    CriticalPathService criticalPathService;

    public List<Node> getMCNew(List<Node> nodes, int sampleSize) throws Exception {
        for (int i = 0; i < sampleSize; i++) {

            // get one duration for every element
            for (Node n : nodes.stream().filter(n -> n.getType().equals("html.Element")).collect(Collectors.toList())) {
                int result = getOneMCValue(n.getMinTime(), n.getEstTime(), n.getMaxTime(), 4);
                n.setDuration(result);
                n.setIsCriticalPath(0);
                if (n.getMcDurationValues() == null) {
                    n.setMcDurationValues(new ArrayList<>());
                }
                n.getMcDurationValues().add(result);
            }

            // get critical path
            List<Node> criticalPath = criticalPathService.getCP(nodes);

            // save which elements are on critial path
            for (Node n : criticalPath.stream().filter(n -> !n.getType().equals("link")).collect(Collectors.toList())) {
                if (n.getIsCriticalPath() == 1) {
                    n.setTimesOnCriticalPath(n.getTimesOnCriticalPath() + 1);
                }
            }
        }

        // calculate probability for each element to be on the critical path
        for (Node n : nodes.stream().filter(n -> !n.getType().equals("link")).collect(Collectors.toList())) {
            n.setProbabilityToBeOnCriticalPath((double) n.getTimesOnCriticalPath() / (double) sampleSize);
            n.setIsCriticalPath(0);

            int duration = (int) n.getMcDurationValues().stream().mapToDouble(a -> a).sum() / sampleSize;

            double variance = 0;
            for (int i : n.getMcDurationValues()) {
                variance += (i - duration) * (i - duration);
            }
            n.setVariance(variance / (sampleSize - 1));

            n.setDuration(duration);
        }

        List<Node> criticalPath = criticalPathService.getCP(nodes);

        return nodes;
    }

    private int getOneMCValue(double a, double m, double b, double k) {
        double mean = (a + k * m + b) / (k + 2);
        double sd = (b - a) / (k + 2);
        double alpha = ((mean - a) / (b - a)) * ((mean - a) * (b - mean) / (sd * sd) - 1);
        double beta = alpha * (b - mean) / (mean - a);

        BetaDistribution betaDistribution = new BetaDistribution(alpha, beta);
        double result = betaDistribution.sample();
        double x = a + result * (b - a);
        return (int) x;
    }

    private int getMCValue(double a, double m, double b, double k) {
        double mean = (a + k * m + b) / (k + 2);
        double sd = (b - a) / (k + 2);
        double alpha = ((mean - a) / (b - a)) * ((mean - a) * (b - mean) / (sd * sd) - 1);
        double beta = alpha * (b - mean) / (mean - a);

        List<Integer> results = new ArrayList();

        BetaDistribution betaDistribution = new BetaDistribution(alpha, beta);
        for (int i = 0; i < sampleSize; i++) {
            double result = betaDistribution.sample();
            double x = a + result * (b - a);
            results.add((int) x);
        }

        return mostCommon(results);
    }

    public <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }

    public List<Node> getMC(List<Node> nodes) {
        List<Node> elements = nodes.stream().filter(n -> !n.getType().equals("link")).collect(Collectors.toList());

        for (Node n : elements) {
            if (n.getEstTime() != 0 && n.getMinTime() != 0 && n.getMaxTime() != 0) {
                int result = getMCValue(n.getMinTime(), n.getEstTime(), n.getMaxTime(), 4);
                n.setDuration(result);
            }
        }
        return elements;
    }
}

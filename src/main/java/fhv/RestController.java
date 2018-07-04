package fhv;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    CriticalPathService criticalPathService;
    @Autowired
    PertService pertService;
    @Autowired
    MonteCarloService monteCarloService;

    @RequestMapping(value = "/criticalPath", method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getCP(@RequestBody Node[] nodes) throws Exception {
        return criticalPathService.getCP(Arrays.asList(nodes));
    }

    @RequestMapping(value = "/pert", method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getPERT(@RequestBody Node[] nodes) {
        return pertService.getPERT(Arrays.asList(nodes));
    }

    @RequestMapping(value = "/mc", method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getMC(@RequestBody Node[] nodes) throws Exception {
        // extract sample Size and remove sampleSize node
        ArrayList<Node> nodeList = new ArrayList<>(Arrays.asList(nodes));
        Node sampleSizeNode = nodeList.stream().filter(n -> n.getType().equals("sampleSize")).findFirst().get();
        int sampleSize = sampleSizeNode.getSampleSize();
        nodeList.remove(sampleSizeNode);

        // call with sample size
        return monteCarloService.getMCNew(nodeList, sampleSize);
    }
}
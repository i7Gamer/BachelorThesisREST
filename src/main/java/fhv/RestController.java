package fhv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    CriticalPathService criticalPathService;
    @Autowired
    PertService pertService;

    @RequestMapping(value = "/criticalPath", method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getCP(@RequestParam(value = "nodes", defaultValue = "") List<Node> nodes) {
        nodes = criticalPathService.getCP(nodes);
        return nodes;
    }

    @RequestMapping(value = "/pert", method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getPERT(@RequestParam(value = "nodes", defaultValue = "") List<Node> nodes) {
        nodes = pertService.getPERT(nodes);
        return nodes;
    }
}
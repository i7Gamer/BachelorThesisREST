package fhv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    CriticalPathService criticalPathService;
    @Autowired
    PertService pertService;

    @RequestMapping(value = "/criticalPath", method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getCP(@RequestBody Node[] nodes) {
        return criticalPathService.getCP(Arrays.asList(nodes));
    }

    @RequestMapping(value = "/pert", method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getPERT(@RequestBody Node[] nodes) {
        return pertService.getPERT(Arrays.asList(nodes));
    }
}
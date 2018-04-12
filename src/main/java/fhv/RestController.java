package fhv;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    CriticalPathService criticalPathService;
    @Autowired
    PertService pertService;

    @CrossOrigin(origins = "http://rzipas.win:8080")
    @RequestMapping(value = "/criticalPath",
            method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getCP(@RequestBody Node[] nodes) throws Exception {
        return criticalPathService.getCP(Arrays.asList(nodes));
    }

    @CrossOrigin(origins = "http://rzipas.win:8080")
    @RequestMapping(value = "/pert",
            method = RequestMethod.POST)
    @ResponseBody
    public List<Node> getPERT(@RequestBody Node[] nodes) {
        return pertService.getPERT(Arrays.asList(nodes));
    }
}
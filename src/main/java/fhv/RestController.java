package fhv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/pert", method = RequestMethod.GET)
    @ResponseBody
    public List<Node> getPERT(@RequestParam(name = "cells") List<Node> nodes) {
        return pertService.getPERT(nodes);
    }
}
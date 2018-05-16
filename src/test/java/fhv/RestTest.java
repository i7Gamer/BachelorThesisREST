package fhv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RestTest {

    @Autowired
    private PertService pertService;
    @Autowired
    private CriticalPathService criticalPathService;
    @Autowired
    private MonteCarloService monteCarloService;


    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();

        Node n1 = new Node();
        n1.setId("1");
        n1.setType("html.Element");
        n1.setMinTime(1);
        n1.setEstTime(2);
        n1.setMaxTime(4);

        Node n2 = new Node();
        n2.setId("2");
        n2.setType("html.Element");
        n2.setMinTime(2);
        n2.setEstTime(4);
        n2.setMaxTime(6);

        Node n3 = new Node();
        n3.setId("3");
        n3.setType("html.Element");
        n3.setMinTime(2);
        n3.setEstTime(5);
        n3.setMaxTime(12);

        Node n4 = new Node();
        n4.setId("4");
        n4.setType("html.Element");
        n4.setMinTime(2);
        n4.setEstTime(4);
        n4.setMaxTime(5);

        LinkEnd le1 = new LinkEnd();
        le1.setId(n1.getId());

        LinkEnd le2 = new LinkEnd();
        le2.setId(n2.getId());

        LinkEnd le3 = new LinkEnd();
        le3.setId(n3.getId());

        LinkEnd le4 = new LinkEnd();
        le4.setId(n4.getId());

        Node n5 = new Node();
        n5.setType("link");
        n5.setSource(le1);
        n5.setTarget(le2);

        Node n6 = new Node();
        n6.setType("link");
        n6.setSource(le1);
        n6.setTarget(le3);

        Node n7 = new Node();
        n7.setType("link");
        n7.setSource(le2);
        n7.setTarget(le4);

        Node n8 = new Node();
        n8.setType("link");
        n8.setSource(le3);
        n8.setTarget(le4);

        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        nodes.add(n7);
        nodes.add(n8);

        return nodes;
    }

    @Test
    public void criticalPathTest() throws Exception {
        List<Node> result = criticalPathService.getCP(getNodes());

        // TODO check if nodes are correct
    }

    @Test
    public void pertTest() {
        List<Node> result = pertService.getPERT(getNodes());

        // TODO check if nodes are correct
    }

    @Test
    public void mcTest() {
        List<Node> result = monteCarloService.getMC(getNodes());

        // TODO check if nodes are correct
    }
}

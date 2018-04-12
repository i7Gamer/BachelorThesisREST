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

    @Test
    public void criticalPathTest() throws Exception {
        List<Node> nodes = new ArrayList<>();
        criticalPathService.getCP(nodes);

        // TODO check if nodes are correct

    }

    @Test
    public void pertTest() {
        ArrayList<Node> nodes = new ArrayList();
        pertService.getPERT(nodes);

        // TODO check if nodes are correct

    }
}

package helpersTest;

import edu.illinois.yasgl.DirectedGraph;
import helpers.CreateTDG;
import maven.LoadAndStartJdeps;
import org.junit.Test;

import java.util.*;

public class TestTDG {
    @Test
    public void testTDG() {
        CreateTDG createTDG = new CreateTDG();
        // 测试反转后的存储格式
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "test01.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
        DirectedGraph<String> graph = createTDG.makeGraph(depMap);
        Map<String, Set<String>> resMap = createTDG.getTransitiveClosurePerClass(graph, Arrays.asList("TestUser"));
        for (String key : resMap.keySet()) {
            for (String value : resMap.get(key)) {
                System.out.println(key + "->" + value);
            }
        }
    }
}

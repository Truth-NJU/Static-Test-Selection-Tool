package helpersTest;

import command.Starts;
import org.junit.Test;

public class TestStarts {
    @Test
    public void testStarts() throws Exception {
        String rootPathOld = "/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01";
        String jarPathOld = "/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/out/artifacts/test01_jar/test01.jar";
        String rootPathNew = "/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test03";
        String jarPathNew = "/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test03/out/artifacts/test03_jar/test03.jar";
        Starts starts = new Starts();
        starts.runImpactedTests(starts.getImpactedTests(rootPathOld, jarPathOld, rootPathNew, jarPathNew),
                 rootPathNew);
    }
}

package helpersTest;

import helpers.ComputeDepency;
import helpers.LoadAndStartJdeps;
import org.junit.Test;

import java.util.*;

public class TestDependency {

    @Test
    public void testDepencyByTest(){
        ComputeDepency computeDepency=new ComputeDepency();
        // 获得jdeps的输出
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/out/artifacts/test01_jar/test01.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
        Map<String, Set<String>> resMap= computeDepency.testTotypeDependency("man",depMap);
        for (String key : resMap.keySet()) {
            for (String value : resMap.get(key)) {
                System.out.println(key + "->" + value);
            }
        }
    }

    @Test
    public void testTypeTotestDependency(){
        ComputeDepency computeDepency=new ComputeDepency();
        // 获得jdeps的输出
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/out/artifacts/test01_jar/test01.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);

        String rootPath="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/src";
        Map<String, Set<String>> resMap = computeDepency.typeTotestDependency(rootPath, depMap);
        for (String key : resMap.keySet()) {
            for (String value : resMap.get(key)) {
                System.out.println(key + "->" + value);
            }
        }

    }
}

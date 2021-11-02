package helpersTest;

import helpers.ComputeDepency;
import helpers.ImpactedTest;
import helpers.LoadAndStartJdeps;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class TestImpactedTest {
    @Test
    public void testReadFileAndCompile() throws IOException {
        ImpactedTest impactedTest=new ImpactedTest();
        String path1="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/oldCheckSum";
        String path2="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/newCheckSum";
        Map<String, Long> impactedType=impactedTest.readFileAndCompare(path1,path2);
        for(String key:impactedType.keySet()){
            System.out.println(key);
        }
    }

    @Test
    public void testfindImpactedTest() throws IOException {
        ComputeDepency computeDepency=new ComputeDepency();
        // 获得jdeps的输出
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "test01.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);

        // 获得类型到依赖于这个类型的测试的映射
        String rootPath="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/src";
        Map<String, Set<String>> resMap = computeDepency.typeTotestDependency(rootPath, depMap);

        ImpactedTest impactedTest=new ImpactedTest();
        String path1="oldCheckSum";
        String path2="newCheckSum";
        // 获得受影响的类型
        Map<String, Long> impactedType=impactedTest.readFileAndCompare(path1,path2);
        // 输出受影响的测试
        System.out.println(impactedTest.findImpactedTest(impactedType,resMap));
    }
}

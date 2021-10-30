package helpersTest;

import helpers.LoadAndStartJdeps;
import org.junit.Test;

import java.util.*;

public class TestJdeps {
    @Test
    public void testOutput(){
        // 测试jdeps的输出
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/out/artifacts/test01_jar/test01.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
        for (String key : depMap.keySet()) {
            for(String value: depMap.get(key)){
                System.out.println(key+"->"+value);
            }
        }
    }
}

package helpersTest;

import maven.LoadAndStartJdeps;
import org.junit.Test;

import java.util.*;

public class TestJdeps {
    @Test
    public void testOutput(){
        // 测试jdeps的输出
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "test01.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
        for (String key : depMap.keySet()) {
            for(String value: depMap.get(key)){
                System.out.println(key+"->"+value);
            }
        }
    }
}

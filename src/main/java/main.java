import maven.LoadAndStartJdeps;

import java.util.*;

public class main {

    public static void main(String[] args) {
        // 测试jdeps的输出
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "2.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
        for (String key : depMap.keySet()) {
            for(String value: depMap.get(key)){
                System.out.println(key+"->"+value);
            }
        }
    }
}

import edu.illinois.yasgl.DirectedGraph;
import helpers.CheckSum;
import helpers.CreateTDG;
import maven.LoadAndStartJdeps;

import java.util.*;

public class main {

    public static void main(String[] args) throws Exception {
        // 测试jdeps的输出
//        List<String> arg = new ArrayList<>(Arrays.asList("-v", "test01.jar"));
//        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
//        for (String key : depMap.keySet()) {
//            for(String value: depMap.get(key)){
//                System.out.println(key+"->"+value);
//            }
//        }
        CreateTDG createTDG=new CreateTDG();
        // 测试反转后的存储格式
        List<String> arg = new ArrayList<>(Arrays.asList("-v", "test01.jar"));
        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
        DirectedGraph<String> graph=createTDG.makeGraph(depMap);
        Map<String, Set<String>> resMap=createTDG.getTransitiveClosurePerClass(graph,Arrays.asList("TestUser"));
        for (String key : resMap.keySet()) {
            for(String value: resMap.get(key)){
                System.out.println(key+"->"+value);
            }
        }
        // 测试CRC校验码
//        CheckSum checkSum=new CheckSum();
//        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/src/main/java/helpers/CreateTDG.java";
//        System.out.println(checkSum.getSingleCheckSum(path));
    }

}

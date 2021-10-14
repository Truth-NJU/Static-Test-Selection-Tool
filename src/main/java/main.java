import helpers.CheckSum;
import maven.LoadAndStartJdeps;

import java.util.*;

public class main {

    public static void main(String[] args) throws Exception {
        // 测试jdeps的输出
//        List<String> arg = new ArrayList<>(Arrays.asList("-v", "2.jar"));
//        Map<String, Set<String>> depMap = LoadAndStartJdeps.runJdeps(arg);
//        for (String key : depMap.keySet()) {
//            for(String value: depMap.get(key)){
//                System.out.println(key+"->"+value);
//            }
//        }
        // 测试CRC校验码
//        CheckSum checkSum=new CheckSum();
//        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/src/main/java/helpers/CreateTDG.java";
//        System.out.println(checkSum.getSingleCheckSum(path));
    }

}

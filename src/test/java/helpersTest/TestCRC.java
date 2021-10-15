package helpersTest;

import helpers.ClassPath;
import org.junit.Test;
import helpers.CheckSum;

import java.util.Map;

public class TestCRC {

    @Test
    public void testCRC() throws Exception {
        //测试CRC校验码
        CheckSum checkSum=new CheckSum();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/src/main/java/helpers/CreateTDG.java";
        System.out.println(checkSum.getSingleCheckSum(path));
    }

    @Test
    public void testClassesCRC() throws Exception {
        ClassPath classPath=new ClassPath();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/src";
        Map<String,String> classPathMap=classPath.getClasspathSet(path);
        for (String key : classPathMap.keySet()) {
            String value = classPathMap.get(key);
            //测试CRC校验码
            CheckSum checkSum=new CheckSum();
            System.out.println(key + " -> " + checkSum.getSingleCheckSum(value));
        }
    }
}

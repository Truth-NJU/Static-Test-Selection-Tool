package helpersTest;

import org.junit.Test;
import helpers.CheckSum;

import java.util.Map;

public class TestCRC {

    @Test
    public void testCRC() throws Exception {
        //测试CRC校验码
        CheckSum checkSum=new CheckSum();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/src/main/java/helpers/CreateTDGWithYasgl.java";
        System.out.println(checkSum.getSingleCheckSum(path));
    }

    @Test
    public void testClassesCRC() throws Exception {
        CheckSum checkSum=new CheckSum();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/src";
        Map<String, Long> resMap = checkSum.setCheckSumMap(path);
        for (String key : resMap.keySet()) {
            System.out.println(key + " -> " + resMap.get(key));
        }
    }

    @Test
    public void testWriteFile() throws Exception {
        CheckSum checkSum=new CheckSum();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/src";
        Map<String, Long> resMap = checkSum.setCheckSumMap(path);
        checkSum.writeCheckSumToFile(resMap,"newCheckSum");
    }
}

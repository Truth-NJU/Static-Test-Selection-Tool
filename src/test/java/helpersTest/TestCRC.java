package helpersTest;

import org.junit.Test;
import helpers.CheckSum;

public class TestCRC {

    @Test
    public void testCRC() throws Exception {
        //测试CRC校验码
        CheckSum checkSum=new CheckSum();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/src/main/java/helpers/CreateTDG.java";
        System.out.println(checkSum.getSingleCheckSum(path));
    }
}

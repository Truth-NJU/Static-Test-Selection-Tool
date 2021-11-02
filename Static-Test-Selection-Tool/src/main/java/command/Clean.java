package command;

import java.io.*;

public class Clean {
    public void cleanSuccess() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("status"));
        // true代表下一次运行时，认为所有的类型都已经更改，需要选取所有测试运行
        writer.write("true");
        writer.flush();
        writer.close();
        System.out.println("重置STARTS成功");
    }
}
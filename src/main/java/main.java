import java.io.File;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        File file = new File("/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/Static-Test-Selection-Tool/src");
        File[] files = file.listFiles();
        for (File f : files) {
            f.isFile();
        }
        getpath(file);
    }

    //获取文件的绝对目录
    public static void getpath(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getpath(f);

                } else {
                    String path = f.getAbsolutePath();
                    if(path.endsWith(".java"))  System.out.println(path);
                }
            }
        }
    }

}


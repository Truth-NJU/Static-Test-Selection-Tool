package commandTest;

import helpers.ClassPath;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class TestImpacted {
    @Test
    public void testImpactedTestList(){
        ClassPath classPath = new ClassPath();
        Map<String, String> classpathMap = classPath.getClasspathSet("/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test03");
        // 获得所有类的名称
        ArrayList<String> allClass = classPath.getAllClassName(classpathMap);
        // 获得所有测试类的名称
        ArrayList<String> testClassNew = classPath.getAllTestClassesName(allClass);
        System.out.println(testClassNew);
        // 获得新添加的测试
        ClassPath classPath2 = new ClassPath();
        Map<String, String> classpathMapOld = classPath2.getClasspathSet("/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01");
        // 获得所有类的名称
        ArrayList<String> allClassOld = classPath2.getAllClassName(classpathMapOld);
        // 获得所有测试类的名称
        ArrayList<String> testClassOld = classPath2.getAllTestClassesName(allClassOld);
        System.out.println(testClassOld);
        ArrayList<String> newTest=new ArrayList<>();
        for(String test:testClassNew){
            if(testClassOld.indexOf(test)==-1){
                newTest.add(test);
            }
        }
        System.out.println(newTest);
    }
}

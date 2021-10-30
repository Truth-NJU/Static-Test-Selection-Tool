package helpersTest;

import helpers.ClassPath;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClassPath {
    @Test
    public void testClassPath(){
        ClassPath classPath=new ClassPath();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/src";
        Map<String,String> classPathMap=classPath.getClasspathSet(path);
        for (String key : classPathMap.keySet()) {
            String value = classPathMap.get(key);
            System.out.println(key + " -> " + value);
        }
    }

    @Test
    public void testGetAllClassName(){
        ClassPath classPath=new ClassPath();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/src";
        Map<String,String> classPathMap=classPath.getClasspathSet(path);
        List<String> names=classPath.getAllClassName(classPathMap);
        System.out.println(names);
    }

    @Test
    public void testGetAllTestClassName(){
        ClassPath classPath=new ClassPath();
        String path="/Users/taozehua/Downloads/大三上学习资料/自动化测试/工具实现/test/test01/src";
        Map<String,String> classPathMap=classPath.getClasspathSet(path);
        ArrayList<String> allClassNames=classPath.getAllClassName(classPathMap);
        ArrayList<String> testClassNames= classPath.getAllTestClassesName(allClassNames);
        System.out.println(testClassNames);
    }

}

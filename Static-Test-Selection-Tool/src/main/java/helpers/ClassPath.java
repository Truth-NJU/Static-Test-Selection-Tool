package helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 遍历待测试项目的根目录，获得所有类的绝对路径
 */
public class ClassPath {
    // 类名->绝对路径
    private Map<String, String> classpathMap=new HashMap<>();

    // 存储待测项目的所有类名
    private ArrayList<String> classes=new ArrayList<>();

    // 存储所有测试类的类名
    private ArrayList<String> testClasses=new ArrayList<>();

    public Map<String, String> getClasspathSet(String rootPath) {
        File file = new File(rootPath);
        File[] files = file.listFiles();
        for (File f : files) {
            f.isFile();
        }
        getpath(file);
        return classpathMap;
    }

    //获取文件的绝对目录
    public void getpath(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getpath(f);
                } else {
                    String path = f.getAbsolutePath();
                    if (path.endsWith(".java")) {
                        String[] temp = path.split("/");
                        // System.out.println(path);
                        String classname=temp[temp.length - 1];
                        classpathMap.put(classname.replace(".java",""), path);
                    }
                }
            }
        }
    }

    /**
     * 获得项目中所有的类名
     * @param classpathMap
     * @return
     */
    public ArrayList<String> getAllClassName(Map<String,String> classpathMap){
        classes=new ArrayList<>();
        for(String key:classpathMap.keySet()){
            if(key!=null) {
                classes.add(key);
                //System.out.println(key);
            }
           // System.out.println(key);
        }
        return this.classes;
    }

    /**
     * 识别待测项目中的所有测试类的名字
     * @param classes
     * @return
     */
    public ArrayList<String> getAllTestClassesName(ArrayList<String> classes){
        for(int i=0;i<classes.size();i++){
            String lowerCaseStr=classes.get(i).toLowerCase();
            if(lowerCaseStr.startsWith("test")){
                testClasses.add(classes.get(i));
            }
        }
        return testClasses;
    }

}

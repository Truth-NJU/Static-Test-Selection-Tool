package helpers;

import edu.illinois.yasgl.DirectedGraph;

import java.util.*;

/**
 * 计算类型到测试的依赖
 */
public class ComputeDepency {

    // 存储类型到依赖于该类型的测试的映射
    private Map<String, Set<String>> typeTotestDependencyMap=new HashMap<>();


    /**
     * 计算一个测试依赖的类型
     *
     * @param testName
     * @param deps
     * @return 测试到该测试依赖的所有的类型的映射
     */
    public Map<String, Set<String>> testTotypeDependency(String testName, Map<String, Set<String>> deps) {
        Map<String, Set<String>> res=new HashMap<>();
        Set<String> set=new HashSet<>();

        CreateTDGWithYasgl createTDGWithYasgl = new CreateTDGWithYasgl();
        // 构造TDG图
        DirectedGraph<String> DirectedGraph = createTDGWithYasgl.makeGraph(deps);
        // 得到该测试依赖的所有的类型,key代表类型,value代表该测试依赖的所有类型
        // 这里resDepency.size()=1
        Map<String, Set<String>> resDepency =
                createTDGWithYasgl.getTransitiveClosurePerClass(DirectedGraph, Arrays.asList(testName));

        for (String key : resDepency.keySet()) {
            for (String val : resDepency.get(key)) {
                // 得到该类型依赖的测试类型
                if (!Objects.equals(val, testName)) {
                    set.add(val);
                }
            }
        }
        res.put(testName,set);
        return res;
    }

    /**
     *  通过项目的根路径分析得到项目中类型到依赖于该类型的测试的映射
     * @param rootpath
     * @return
     */
    public Map<String, Set<String>> typeTotestDependency(String rootpath,Map<String, Set<String>> deps){
        ClassPath classPath=new ClassPath();
        Map<String, String> classpathMap = classPath.getClasspathSet(rootpath);
        // 获得所有类的名称
        ArrayList<String> allClass=classPath.getAllClassName(classpathMap);

        // 获得所有测试类的名称
        ArrayList<String> testClass=classPath.getAllTestClassesName(allClass);


        // 获得剩余的类型
        ArrayList<String> commonTypeClass=new ArrayList<>();
        for(int i=0;i<allClass.size();i++){
            if(testClass.indexOf(allClass.get(i))==-1){
                commonTypeClass.add(allClass.get(i));
            }
        }

        // 初始化typeTotestDependencyMap
        for(String type:commonTypeClass){
            Set<String> temp=new HashSet<>();
            typeTotestDependencyMap.put(type,temp);
        }

        // 计算赋值
        for(String test:testClass){
            // 得到该测试依赖的所有类型
            Map<String, Set<String>> resDepency=testTotypeDependency(test,deps);
            for (String key : resDepency.keySet()) {
                for (String val : resDepency.get(key)) {
                    if(typeTotestDependencyMap.containsKey(val)) {
                        // 将依赖于该类型的测试放到typeTotestDependencyMap中
                        typeTotestDependencyMap.get(val).add(key);
                    }
                }
            }
        }
        return typeTotestDependencyMap;
    }
}

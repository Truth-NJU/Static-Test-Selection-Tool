package command;

import helpers.*;

import java.util.*;

public class Select {
    /**
     * 显示(但不运行)自上次starts运行以来受更改影响的测试类
     *
     * @param rootPathOld
     * @param jarPathOld
     * @param rootPathNew
     * @param jarPathNew
     * @return
     * @throws Exception
     */
    public ArrayList<String> dealWithInput(String rootPathOld, String jarPathOld, String rootPathNew, String jarPathNew) throws Exception {
        // 处理旧版本
        ComputeDepency computeDepency = new ComputeDepency();
        List<String> argold = new ArrayList<>(Arrays.asList("-v", jarPathOld));
        Map<String, Set<String>> depMapOld = LoadAndStartJdeps.runJdeps(argold);
        // 旧版本的类型到依赖于该类型的测试的映射
        Map<String, Set<String>> typeTotestDependencyMapOld = computeDepency.typeTotestDependency(rootPathOld, depMapOld);
        CheckSum checkSum = new CheckSum();
        Map<String, Long> resMapOld = checkSum.setCheckSumMap(rootPathOld);
        // 将校验和写入文件
        checkSum.writeCheckSumToFile(resMapOld, "oldCheckSum");


        // 处理新版本
        List<String> argnew = new ArrayList<>(Arrays.asList("-v", jarPathNew));
        Map<String, Set<String>> depMapNew = LoadAndStartJdeps.runJdeps(argnew);
        ComputeDepency computeDepency2 = new ComputeDepency();
        // 新版本的类型到依赖于该类型的测试的映射
        Map<String, Set<String>> typeTotestDependencyMapNew = computeDepency2.typeTotestDependency(rootPathNew, depMapNew);

        CheckSum checkSum2 = new CheckSum();
        Map<String, Long> resMapNew = checkSum2.setCheckSumMap(rootPathNew);
        // 将校验和写入文件
        checkSum.writeCheckSumToFile(resMapNew, "newCheckSum");

        ImpactedTest impactedTest = new ImpactedTest();
        String path1 = "oldCheckSum";
        String path2 = "newCheckSum";
        // 获得受影响的所有类型
        Map<String, Long> changedType = impactedTest.readFileAndCompare(path1, path2);



        // 输出受影响的测试
        ArrayList<String> impactedTestList = impactedTest.findImpactedTest(changedType, typeTotestDependencyMapNew);

        ClassPath classPath = new ClassPath();
        Map<String, String> classpathMap = classPath.getClasspathSet(rootPathNew);
        // 获得所有类的名称
        ArrayList<String> allClass = classPath.getAllClassName(classpathMap);
        // 获得所有测试类的名称
        ArrayList<String> testClassNew = classPath.getAllTestClassesName(allClass);

        // 获得新添加的测试
        ClassPath classPath2 = new ClassPath();
        Map<String, String> classpathMapOld = classPath2.getClasspathSet(rootPathOld);
        // 获得所有类的名称
        ArrayList<String> allClassOld = classPath2.getAllClassName(classpathMapOld);
        // 获得所有测试类的名称
        ArrayList<String> testClassOld = classPath2.getAllTestClassesName(allClassOld);
        // 获得新添加的测试，且没有出现在受影响测试的列表中
        ArrayList<String> newTest=new ArrayList<>();
        for(String test:testClassNew){
            if(testClassOld.indexOf(test)==-1 && impactedTestList.indexOf(test)==-1){
                newTest.add(test);
            }
        }

        // 受影响的测试加上新的测试
        impactedTestList.addAll(newTest);
        //ArrayList<String> impactedTestList = impactedTest.findImpactedTest(impactedType, typeTotestDependencyMapOld);
        return impactedTestList;
    }
}

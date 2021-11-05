package command;

import helpers.*;

import java.util.*;

public class Impacted {
    /**
     * 显示所有受变更影响的类型(不仅仅是测试类)
     *
     * @param rootPathOld
     * @param jarPathOld
     * @param rootPathNew
     * @param jarPathNew
     * @throws Exception
     */
    public void dealWithInput(String rootPathOld, String jarPathOld, String rootPathNew, String jarPathNew) throws Exception {
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

        // 获得变更的类型(不包括新出现和删除掉的类型，但是这儿包含了新出现的类型，需要删除)
        Map<String, Long> changedType = impactedTest.readFileAndCompare(path1, path2);


        ClassPath classPath = new ClassPath();
        Map<String, String> classpathMap = classPath.getClasspathSet(rootPathNew);
        // 获得所有类的名称
        ArrayList<String> allClass = classPath.getAllClassName(classpathMap);
        // 获得所有测试类的名称
        ArrayList<String> testClassNew = classPath.getAllTestClassesName(allClass);
        // 获得不是测试类的类型
        ArrayList<String> commonTypeClass = new ArrayList<>();
        for (int i = 0; i < allClass.size(); i++) {
            if (testClassNew.indexOf(allClass.get(i)) == -1) {
                commonTypeClass.add(allClass.get(i));
            }
        }

        // 获得受更改的类型影响的类型
        ArrayList<String> typeImpactedByChange = new ArrayList<>();
        // 遍历所有不是测试类的类型
        for (String clazzName : commonTypeClass) {
            // 获得类型和该类型依赖的类型
            Map<String, Set<String>> typeDep = computeDepency.testTotypeDependency(clazzName, depMapOld);
            for (String key : typeDep.keySet()) {
                for (String name : typeDep.get(key)) {
                    // 若该类型依赖的某个类型在变更的类型中出现，则该类型会受到变更的影响，需要输出
                    if (changedType.containsKey(name)) {
                        if (typeImpactedByChange.indexOf(key) == -1)
                            typeImpactedByChange.add(key);
                        break;
                    }
                }
            }
        }


        String temp = ""; // 用来判断结果是否为空，既有没有类型发生变化
        for (String key : changedType.keySet()) {
            temp += key;
        }
        for (String key : typeImpactedByChange) {
            temp += key;
        }


        // 获得受影响的测试
        ArrayList<String> impactedTestList = impactedTest.findImpactedTest(changedType, typeTotestDependencyMapNew);
        //ArrayList<String> impactedTestList = impactedTest.findImpactedTest(impactedType, typeTotestDependencyMapOld);
        for (String test : impactedTestList) {
            temp += test;
        }
        // 获得新添加的测试
        ClassPath classPath2 = new ClassPath();
        Map<String, String> classpathMapOld = classPath2.getClasspathSet(rootPathOld);
        // 获得所有类的名称
        ArrayList<String> allClassOld = classPath2.getAllClassName(classpathMapOld);
        // 获得所有测试类的名称
        ArrayList<String> testClassOld = classPath2.getAllTestClassesName(allClassOld);
        ArrayList<String> newTest = new ArrayList<>();
        // 遍历所有的新出现的测试类，若不在受影响的测试列表中就要加入
        for (String test : testClassNew) {
            if (testClassOld.indexOf(test) == -1 && impactedTestList.indexOf(test) == -1) {
                newTest.add(test);
            }
        }
        for (String test : newTest) {
            temp += test;
        }
        if (Objects.equals(temp, "")) {
            System.out.println("没有类型和测试发生变化");
        } else {
            // 输出变更的类型（不包括删除的类型）
            for (String key : changedType.keySet()) {
                System.out.println(key);
            }
            // 输出受变更影响的类型
            for (String key : typeImpactedByChange) {
                System.out.println(key);
            }
            // 输出受影响的测试
            for (String test : impactedTestList) {
                System.out.println(test);
            }
            // 输出新增加的测试
            for (String test : newTest) {
                System.out.println(test);
            }
        }

    }
}

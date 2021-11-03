package command;

import helpers.CheckSum;
import helpers.ComputeDepency;
import helpers.ImpactedTest;
import helpers.LoadAndStartJdeps;

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

        // 获得受影响的类型
        Map<String, Long> impactedType = impactedTest.readFileAndCompare(path1, path2);
        String temp = ""; // 用来判断结果是否为空，既有没有类型发生变化
        for (String key : impactedType.keySet()) {
            temp += key;
        }

        // 获得受影响的测试
        //ArrayList<String> impactedTestList = impactedTest.findImpactedTest(impactedType, typeTotestDependencyMapNew);
        ArrayList<String> impactedTestList = impactedTest.findImpactedTest(impactedType, typeTotestDependencyMapOld);
        for (String test : impactedTestList) {
            temp += test;
        }
        if (Objects.equals(temp, "")) {
            System.out.println("没有类型和测试发生变化");
        } else {
            for (String key : impactedType.keySet()) {
                System.out.println(key);
            }
            // 输出受影响的测试
            for (String test : impactedTestList) {
                System.out.println(test);
            }
        }
    }
}

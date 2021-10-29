package command;

import helpers.CheckSum;
import helpers.ComputeDepency;
import helpers.ImpactedTest;
import helpers.LoadAndStartJdeps;

import java.util.*;

public class Diff {
    /**
     * 显示自上次运行Starts以来更改的所有Java类型(包括类、接口和枚举)。
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
        // 旧版本的类型到依赖于该类型的测试的映射
        Map<String, Set<String>> typeTotestDependencyMapNew = computeDepency2.typeTotestDependency(rootPathNew, depMapNew);

        CheckSum checkSum2 = new CheckSum();
        Map<String, Long> resMapNew = checkSum2.setCheckSumMap(rootPathNew);
        // 将校验和写入文件
        checkSum.writeCheckSumToFile(resMapNew, "newCheckSum");

        ImpactedTest impactedTest = new ImpactedTest();
        String path1 = "oldCheckSum";
        String path2 = "newCheckSum";
        // 获得受变更影响的类型
        Map<String, Long> impactedType = impactedTest.readFileAndCompile(path1, path2);
        for (String key : impactedType.keySet()) {
            System.out.println(key);
        }
    }
}

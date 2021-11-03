package command;

import helpers.CheckSum;
import helpers.ComputeDepency;
import helpers.ImpactedTest;
import helpers.LoadAndStartJdeps;

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
        Map<String, Long> impactedType = impactedTest.readFileAndCompare(path1, path2);
        // 输出受影响的测试
        //ArrayList<String> impactedTestList = impactedTest.findImpactedTest(impactedType, typeTotestDependencyMapNew);
        ArrayList<String> impactedTestList = impactedTest.findImpactedTest(impactedType, typeTotestDependencyMapOld);
        return impactedTestList;
    }
}

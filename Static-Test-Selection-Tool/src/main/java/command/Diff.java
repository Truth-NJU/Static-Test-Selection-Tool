package command;

import helpers.CheckSum;
import helpers.ComputeDepency;
import helpers.ImpactedTest;
import helpers.LoadAndStartJdeps;

import java.util.*;

public class Diff {
    /**
     * 显示自上次运行Starts以来更改的所有Java类型(包括类、接口和枚举)。
     *
     * @param rootPathOld
     * @param rootPathNew
     * @throws Exception
     */
    public void dealWithInput(String rootPathOld, String rootPathNew) throws Exception {
//        // 处理旧版本
//        ComputeDepency computeDepency = new ComputeDepency();
//        List<String> argold = new ArrayList<>(Arrays.asList("-v", jarPathOld));
//        Map<String, Set<String>> depMapOld = LoadAndStartJdeps.runJdeps(argold);
//        // 旧版本的类型到依赖于该类型的测试的映射
//        Map<String, Set<String>> typeTotestDependencyMapOld = computeDepency.typeTotestDependency(rootPathOld, depMapOld);

        // 计算旧版本的校验和并写入文件
        CheckSum checkSum = new CheckSum();
        Map<String, Long> resMapOld = checkSum.setCheckSumMap(rootPathOld);
        // 将校验和写入文件
        checkSum.writeCheckSumToFile(resMapOld, "oldCheckSum");


//        // 处理新版本
//        List<String> argnew = new ArrayList<>(Arrays.asList("-v", jarPathNew));
//        Map<String, Set<String>> depMapNew = LoadAndStartJdeps.runJdeps(argnew);
//        ComputeDepency computeDepency2 = new ComputeDepency();
//        // 旧版本的类型到依赖于该类型的测试的映射
//        Map<String, Set<String>> typeTotestDependencyMapNew = computeDepency2.typeTotestDependency(rootPathNew, depMapNew);

        // 计算新版本的校验和并写入文件
        CheckSum checkSum2 = new CheckSum();
        Map<String, Long> resMapNew = checkSum2.setCheckSumMap(rootPathNew);
        // 将校验和写入文件
        checkSum.writeCheckSumToFile(resMapNew, "newCheckSum");

        ImpactedTest impactedTest = new ImpactedTest();
        String path1 = "oldCheckSum";
        String path2 = "newCheckSum";
        // 获得变更的类型(不包括删除掉的类型，且要删去新出现的类型)
        Map<String, Long> changedType = impactedTest.readFileAndCompare(path1, path2);
        // 获得新出现的类型
        Map<String, Long> newType=impactedTest.getNewType();

        // 去除新出现的类型和删除掉的类型，只显示更改的类型
        ArrayList<String> changeTypeList=new ArrayList<>();
        for(String key:changedType.keySet()){
            if(!newType.containsKey(key)){
                changeTypeList.add(key);
            }
        }
        String temp = ""; // 判断是否有类型发生变化
        for (String key : changeTypeList) {
            temp += key;
        }
        if (Objects.equals(temp, "")) System.out.println("没有类型发生变化");
        else {
            for (String key : changeTypeList) {
                System.out.println(key);
            }
        }
    }
}

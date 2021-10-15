import helpers.CheckSum;
import helpers.ComputeDepency;
import helpers.ImpactedTest;
import helpers.LoadAndStartJdeps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("欢迎使用Starts工具");
        System.out.println("请输入旧版本项目的绝对路径地址：");
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String rootPathOld = br1.readLine();
        System.out.println("请输入旧版本项目的jar包地址：");
        BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        String jarPathOld = br2.readLine();
        System.out.println("请输入新版本项目的绝对路径地址：");
        BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
        String rootPathNew = br3.readLine();
        System.out.println("请输入新版本项目的jar包地址：");
        BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
        String jarPathNew = br4.readLine();
        // 处理输入
        dealWithInput(rootPathOld,jarPathOld,rootPathNew,jarPathNew);
        String out = "Starts工具提供了一下五种命令，请选择：" + "\n" +
                "-help: 列出starts所有功能" + "\n" + "-diff: 显示自上次运行Starts以来更改的所有Java类型(包括类、接口和枚举)"
                + "\n" + "-impacted: 显示所有受变更影响的类型(不仅仅是测试类)" + "\n"
                + "-select: 显示(但不运行)自上次starts运行以来受更改影响的测试类" + "\n"
                + "-starts: 运行受影响的测试" + "\n" + "-clean: 重置starts";
        System.out.println(out);
        System.out.println("请输入命令");
        BufferedReader br5 = new BufferedReader(new InputStreamReader(System.in));
        String commoand = br5.readLine();
        if (commoand.equals("help")) {

        } else if (commoand.equals("diff")) {

        } else if (commoand.equals("impacted")) {

        } else if (commoand.equals("select")) {

        } else if (commoand.equals("starts")) {

        } else if (commoand.equals("clean")) {

        }
    }

    public static void dealWithInput(String rootPathOld,String jarPathOld,String rootPathNew,String jarPathNew) throws Exception {
        // 处理旧版本
        ComputeDepency computeDepency=new ComputeDepency();
        List<String> argold = new ArrayList<>(Arrays.asList("-v", jarPathOld));
        Map<String, Set<String>> depMapOld = LoadAndStartJdeps.runJdeps(argold);
        // 旧版本的类型到依赖于该类型的测试的映射
        Map<String, Set<String>> typeTotestDependencyMapOld = computeDepency.typeTotestDependency(rootPathOld, depMapOld);
        CheckSum checkSum=new CheckSum();
        Map<String, Long> resMapOld = checkSum.setCheckSumMap(rootPathOld);
        // 将校验和写入文件
        checkSum.writeCheckSumToFie(resMapOld,"oldCheckSum");



        // 处理新版本
        List<String> argnew = new ArrayList<>(Arrays.asList("-v", jarPathNew));
        Map<String, Set<String>> depMapNew = LoadAndStartJdeps.runJdeps(argnew);
        ComputeDepency computeDepency2=new ComputeDepency();
        // 旧版本的类型到依赖于该类型的测试的映射
        Map<String, Set<String>> typeTotestDependencyMapNew = computeDepency2.typeTotestDependency(rootPathNew, depMapNew);

        CheckSum checkSum2=new CheckSum();
        Map<String, Long> resMapNew = checkSum2.setCheckSumMap(rootPathNew);
        // 将校验和写入文件
        checkSum.writeCheckSumToFie(resMapNew,"newCheckSum");

        ImpactedTest impactedTest=new ImpactedTest();
        String path1="oldCheckSum";
        String path2="newCheckSum";
        // 获得受影响的类型
        Map<String, Long> impactedType=impactedTest.readFileAndCompile(path1,path2);
        // 输出受影响的测试
        System.out.println(impactedTest.findImpactedTest(impactedType,typeTotestDependencyMapNew));
    }
}


import command.*;
import helpers.ComputeDepency;
import helpers.LoadAndStartJdeps;

import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("欢迎使用Starts工具");
        System.out.println("> 请输入旧版本项目的绝对路径地址：");
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String rootPathOld = br1.readLine();
        System.out.println("> 请输入旧版本项目的jar包地址：");
        BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        String jarPathOld = br2.readLine();
        System.out.println("> 请输入新版本项目的绝对路径地址：");
        BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
        String rootPathNew = br3.readLine();
        System.out.println("> 请输入新版本项目的jar包地址：");
        BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
        String jarPathNew = br4.readLine();
        // 获得所有的类型和依赖于这些类型的测试的映射
        Map<String, Set<String>> typeTotestDependencyMap = getTypeToTestMap(rootPathNew, jarPathNew);
        String out = "STARTS工具提供了以下七种命令，请选择：" + "\n" +
                "-help: 列出STARTS所有功能" + "\n" + "-diff: 显示自上次运行STARTS以来更改的所有Java类型(包括类、接口和枚举)"
                + "\n" + "-impacted: 显示所有受变更影响的类型(不仅仅是测试类)" + "\n"
                + "-select: 显示(但不运行)自上次STARTS运行以来受更改影响的测试类" + "\n"
                + "-starts: 运行受影响的测试" + "\n" + "-clean: 重置STARTS" + "\n" + "-exit: 退出STARTS";
        System.out.println();
        System.out.println(out);
        System.out.println();
        BufferedReader reader = new BufferedReader(new FileReader("status"));
        // status=true代表这次运行时，认为所有的类型都已经更改，需要选取所有测试运行
        // status=false代表正常运行
        String status = reader.readLine();
        while (true) {
            System.out.println("请输入命令:");
            System.out.print("> ");
            BufferedReader br5 = new BufferedReader(new InputStreamReader(System.in));
            String commoand = br5.readLine();
            if (commoand.equals("help")) {
                Help help = new Help();
                help.allPurpose();
                System.out.println();
            } else if (commoand.equals("diff")) {
                // status=false代表正常运行
                if (Objects.equals(status, "false")) {
                    Diff diff = new Diff();
                    diff.dealWithInput(rootPathOld, rootPathNew);
                    System.out.println();
                } else {
                    // status=true代表这次运行时，认为所有的类型都已经更改
                    // 输出所有类型
                    for (String type : typeTotestDependencyMap.keySet()) {
                        System.out.println(type);
                    }
                    System.out.println();
                }
            } else if (commoand.equals("impacted")) {
                // status=false代表正常运行
                if (Objects.equals(status, "false")) {
                    Impacted impacted = new Impacted();
                    impacted.dealWithInput(rootPathOld, jarPathOld, rootPathNew, jarPathNew);
                    System.out.println();
                } else {
                    // status=true代表这次运行时，认为所有的类型都已经更改
                    // 输出所有类型和依赖于这些类型的测试
                    ArrayList<String> temp = new ArrayList<>();// 去除重复的测试，防止重复输出
                    for (String type : typeTotestDependencyMap.keySet()) {
                        System.out.println(type);
                        for (String test : typeTotestDependencyMap.get(type)) {
                            if (temp.indexOf(test) == -1) temp.add(test);
                        }
                    }
                    for (String test : temp) {
                        System.out.println(test);
                    }
                    System.out.println();
                }
            } else if (commoand.equals("select")) {
                // status=false代表正常运行
                if (Objects.equals(status, "false")) {
                    Select select = new Select();
                    ArrayList<String> impactedTestList = select.dealWithInput(rootPathOld, jarPathOld, rootPathNew, jarPathNew);
                    if (impactedTestList.size() == 0) {
                        System.out.println("没有受影响的测试");
                    } else {
                        for (String test : impactedTestList) {
                            System.out.println(test);
                        }
                    }
                    System.out.println();
                } else {
                    // status=true代表这次运行时，认为所有的类型都已经更改，需要选取所有的测试
                    // 输出依赖于所有类型的测试
                    ArrayList<String> temp = new ArrayList<>();// 去除重复的测试，防止重复输出
                    for (String type : typeTotestDependencyMap.keySet()) {
                        for (String test : typeTotestDependencyMap.get(type)) {
                            if (temp.indexOf(test) == -1) temp.add(test);
                        }
                    }
                    for (String test : temp) {
                        System.out.println(test);
                    }
                    System.out.println();
                }
            } else if (commoand.equals("starts")) {
                // status=false代表正常运行
                if (Objects.equals(status, "false")) {
                    Starts starts = new Starts();
                    starts.runImpactedTests(starts.getImpactedTests(rootPathOld, jarPathOld, rootPathNew, jarPathNew),
                            rootPathNew);
                    System.out.println();
                } else {
                    // status=true代表这次运行时，认为所有的类型都已经更改，需要选取所有的测试运行
                    // 运行所有依赖于所有类型的测试
                    System.out.println("请在终端使用以下命令重新运行测试类：");
                    System.out.println("cd " + rootPathNew);
                    ArrayList<String> temp = new ArrayList<>();// 去除重复的测试，防止重复输出
                    for (String type : typeTotestDependencyMap.keySet()) {
                        for (String test : typeTotestDependencyMap.get(type)) {
                            if (temp.indexOf(test) == -1) temp.add(test);
                        }
                    }
                    for (String test : temp) {
                        System.out.println("mvn test -Dtest=" + test);
                    }
                    System.out.println();
                }
            } else if (commoand.equals("clean")) {
                Clean clean = new Clean();
                clean.cleanSuccess();
                System.out.println();
                break;
            } else if (commoand.equals("exit")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("status"));
                writer.write("false");
                writer.flush();
                writer.close();
                break;
            } else {
                System.out.println("命令错误，请重新输入！");
                System.out.println();
            }
        }
    }

    public static Map<String, Set<String>> getTypeToTestMap(String rootPathNew, String jarPathNew) {
        // 处理新版本
        List<String> argnew = new ArrayList<>(Arrays.asList("-v", jarPathNew));
        Map<String, Set<String>> depMapNew = LoadAndStartJdeps.runJdeps(argnew);
        ComputeDepency computeDepency2 = new ComputeDepency();
        // 旧版本的类型到依赖于该类型的测试的映射
        Map<String, Set<String>> typeTotestDependencyMap = computeDepency2.typeTotestDependency(rootPathNew, depMapNew);
        return typeTotestDependencyMap;
    }
}


package command;

import helpers.ClassPath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 运行受影响的测试
 */
public class Starts {
    /**
     * 获得受影响的测试
     *
     * @return 受影响的测试列表
     */
    public ArrayList<String> getImpactedTests(String rootPathOld, String jarPathOld, String rootPathNew, String jarPathNew) throws Exception {
        Select select = new Select();
        ArrayList<String> impactedTestList = select.dealWithInput(rootPathOld, jarPathOld, rootPathNew, jarPathNew);
        return impactedTestList;
    }

    /**
     * 运行受影响的测试
     *
     * @param impactedTestList
     */
    public void runImpactedTests(ArrayList<String> impactedTestList, String rootPathNew) {
        // 运行受影响的测试类
        System.out.println("请在终端使用以下命令重新运行测试类：");
        System.out.println("cd "+rootPathNew);
        for(String impactedTest:impactedTestList)
            System.out.println("mvn test -Dtest="+impactedTest);
    }


}

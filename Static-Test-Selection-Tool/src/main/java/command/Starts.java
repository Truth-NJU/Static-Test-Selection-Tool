package command;

import java.util.ArrayList;

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
        if (impactedTestList.size() == 0) {
            System.out.println("没有受影响的测试需要运行");
            return;
        }
        // 运行受影响的测试类
        System.out.println("请在终端使用以下命令重新运行测试类：");
        System.out.println("cd " + rootPathNew);
        for (String impactedTest : impactedTestList) {
//            // 出现点的位置，只要截取类名即可
//            int location=impactedTest.lastIndexOf(".");
//            String testName="";
//            for(int i=location+1;i<impactedTest.length();i++){
//                testName+=impactedTest.charAt(i);
//            }
            System.out.println("mvn test -Dtest=" + impactedTest);
        }
    }


}

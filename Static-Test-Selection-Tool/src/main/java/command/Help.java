package command;

public class Help {
    /**
     * 列出STARTS工具所有用途
     */
    public void allPurpose(){
        String out = "STARTS工具提供了以下六种功能：" + "\n" +
                "-help: 列出STARTS的所有功能" + "\n" + "-diff: 显示自上次运行STARTS以来更改的所有Java类型(包括类、接口和枚举)"
                + "\n" + "-impacted: 显示所有受变更影响的类型(不仅仅是测试类)" + "\n"
                + "-select: 显示(但不运行)自上次STARTS运行以来受更改影响的测试类" + "\n"
                + "-starts: 运行受影响的测试" + "\n" + "-clean: 重置STARTS";
        System.out.println(out);
    }
}

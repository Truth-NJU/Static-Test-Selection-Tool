package helpers;

import constants.StartsConstants;
import edu.illinois.yasgl.DirectedGraph;
import edu.illinois.yasgl.DirectedGraphBuilder;
import edu.illinois.yasgl.GraphVertexVisitor;

import java.util.*;


/**
 * 使用yasgl自定义图形库来构造图形，并查找可以传递到某些已更改类型的测试
 * 我们将每种类型作为一个节点添加到yasgl图中，并添加由jdeps计算的依赖项作为图中节点之间的边
 */
public class CreateTDGWithYasgl implements StartsConstants {
    /**
     * 将使用jdeps得到的依赖构造成图形
     * @param deps
     * @return DirectedGraph<String>
     */
    public DirectedGraph<String> makeGraph(Map<String, Set<String>> deps){
        DirectedGraphBuilder<String> builder=new DirectedGraphBuilder<>();
        // 将jdeps的依赖分析结果的作为边添加到图中
        for(String key:deps.keySet()){
            for(String val:deps.get(key)){
                builder.addEdge(key,val);
            }
        }
        return builder.build();
    }

    /**
     * 得到每一个待分析的类的依赖的传递闭包
     * @param graph
     * @param classesToAnalyze
     * @return transitiveClosurePerClass
     */
    public Map<String, Set<String>> getTransitiveClosurePerClass(DirectedGraph<String> graph,
                                                                        List<String> classesToAnalyze) {
        Map<String, Set<String>> transitiveClosurePerClass = new HashMap<>();
        // 遍历每一个待分析的类
        for (String test : classesToAnalyze) {
            // 获得该类依赖的所有其他的类
            Set<String> deps = computeReachabilityFromChangedClasses(
                    new HashSet<>(Arrays.asList(test)), graph);
            deps.add(test);
            transitiveClosurePerClass.put(test, deps);
        }
        return transitiveClosurePerClass;
    }

    /**
     * 计算图中一个类能达到的其它所有的类，即和当前类之间存在依赖关系其它所有的类
     * @param changed
     * @param graph
     * @return
     */
    public static Set<String> computeReachabilityFromChangedClasses(Set<String> changed, DirectedGraph<String> graph) {
        final Set<String> reachable = graph.acceptForward(changed, new GraphVertexVisitor<String>() {
            @Override
            public void visit(String name) {
            }
        });
        return reachable;
    }
}

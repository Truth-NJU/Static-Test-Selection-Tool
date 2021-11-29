package helpers;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import constants.StartsConstants;

/**
 * starts通过只读取每个类文件中的常量池来确定类文件中的类型可能依赖的所有类型，从而改进了类型之间的计算依赖关系。
 * 使用jdeps工具来读取常量池。在将应用程序的新修订编译为生成类文件之后，
 * STARTS进行单个jdeps调用(通过jdeps API)来一次解析应用程序中的所有类文件，然后在内存中处理jdeps输出，以找到每种类型的依赖项。
 */
public final class LoadAndStartJdeps implements StartsConstants {
    private static final String TOOLS_JAR_NAME = "tools.jar";
    private static final String CLASSES_JAR_NAME = "classes.jar";
    private static final String LIB = "lib";


    private static File findToolsJar() {

        // 获取java的安装目录
        String javaHome = System.getProperty(JAVA_HOME);
        File javaHomeFile = new File(javaHome);
        File tjf = new File(javaHomeFile, LIB + File.separator + TOOLS_JAR_NAME);


        if (!tjf.exists()) {
            tjf = new File(System.getenv("java_home"), LIB + File.separator + TOOLS_JAR_NAME);
        }

        if (!tjf.exists() && javaHomeFile.getAbsolutePath().endsWith(File.separator + "jre")) {
            javaHomeFile = javaHomeFile.getParentFile();
            tjf = new File(javaHomeFile, LIB + File.separator + TOOLS_JAR_NAME);
        }

        if (!tjf.exists() && isMac() && javaHomeFile.getAbsolutePath().endsWith(File.separator + "Home")) {
            javaHomeFile = javaHomeFile.getParentFile();
            tjf = new File(javaHomeFile, "Classes" + File.separator + CLASSES_JAR_NAME);
        }

        return tjf;
    }

    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
    }


    /**
     *  加载并启动jdeps
     * @param args
     * @return StringWriter
     */
    public static StringWriter loadAndRunJdeps(List<String> args) {
        StringWriter output = new StringWriter();
        try {
            File toolsJarFile = findToolsJar();
            if (!toolsJarFile.exists()) {
                // 通过 java.util.spi.ToolProvider来加载jdeps
                Class<?> toolProvider = ClassLoader.getSystemClassLoader().loadClass("java.util.spi.ToolProvider");
                // getMethod获取并调用了toolProvider的findFirst方法
                // findFirst方法返回具有给定名称的ToolProvider的第一个实例，由ServiceLoader使用系统类加载器加载。
                // invoke用来调用findFirst方法
                Object jdeps = toolProvider.getMethod("findFirst", String.class).invoke(null, "jdeps");
                // 加载jdeps完成
                jdeps = Optional.class.getMethod("get").invoke(jdeps);
                // 启动jdeps
                // 运行jdeps实例，成功运行返回零。 任何非零返回值都表示执行期间特定于工具的错误。
                // 应提供两个流，用于“预期”输出，以及任何错误消息。 如果不必区分输出，则可以将相同的流用于两者。
                // 将jdeps的输出赋给output
                toolProvider.getMethod("run", PrintWriter.class, PrintWriter.class, String[].class)
                        .invoke(jdeps, new PrintWriter(output), new PrintWriter(output), args.toArray(new String[0]));
            } else {
                URLClassLoader loader = new URLClassLoader(new URL[] { toolsJarFile.toURI().toURL() },
                        ClassLoader.getSystemClassLoader());
                Class<?> jdepsMain = loader.loadClass("com.sun.tools.jdeps.Main");
                jdepsMain.getMethod("run", String[].class, PrintWriter.class)
                        .invoke(null, args.toArray(new String[0]), new PrintWriter(output));
            }
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
        return output;
    }

    /**
     * 将jdeps的输出存放到map中
     * @param jdepsOutput
     * @return Map<String, Set<String>>
     */
    public static Map<String, Set<String>> getDepsFromJdepsOutput(StringWriter jdepsOutput) {
        Map<String, Set<String>> deps = new HashMap<>();
        // 按行分割jdepsOutput
        List<String> lines = Arrays.asList(jdepsOutput.toString().split(System.lineSeparator()));
        for (String line : lines) {
            String[] parts = line.split("->");
            String clazz = parts[0].trim();
            if (clazz.startsWith(CLASSES) || clazz.startsWith(TEST_CLASSES) || clazz.endsWith(JAR_EXTENSION)) {
                continue;
            }
            // 截取类名
            // 最后一次出现点的位置
            if(clazz.contains(".")) {
                int maxI = 0;
                for (int i = 0; i < clazz.length(); i++) {
                    if (clazz.charAt(i) == '.') {
                        if (maxI < i) maxI = i;
                    }
                }
                clazz = "";
                for (int i = maxI + 1; i < parts[0].trim().length(); i++) {
                    clazz += parts[0].trim().charAt(i);
                }
            }
            // 过滤不需要的类
            String right = parts[1].trim().split(" ")[0];
            if(!right.startsWith("java") && !right.startsWith("org")) {
                if(right.contains(".")) {
                    int maxI = 0;
                    for (int i = 0; i < right.length(); i++) {
                        if (right.charAt(i) == '.') {
                            if (maxI < i) maxI = i;
                        }
                    }
                    right = "";
                    for (int i = maxI + 1; i <parts[1].trim().split(" ")[0].length(); i++) {
                        right += parts[1].trim().split(" ")[0].charAt(i);
                    }
                }
                if (deps.keySet().contains(clazz)) {
                    deps.get(clazz).add(right);
                } else {
                    deps.put(clazz, new HashSet<>(Arrays.asList(right)));
                }
            }
        }
        return deps;
    }


    /**
     * 启动jdeps获得输出
     * @return 类之间的依赖关系
     *
     * */
    public static Map<String, Set<String>> runJdeps(List<String> args) {

        StringWriter output = LoadAndStartJdeps.loadAndRunJdeps(args);

        if(output.getBuffer().length()!=0){
            return getDepsFromJdepsOutput(output);
        }
        return new HashMap<String, Set<String>>();
    }

}

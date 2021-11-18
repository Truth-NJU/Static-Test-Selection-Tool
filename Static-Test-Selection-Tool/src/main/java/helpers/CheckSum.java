package helpers;

import constants.StartsConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class CheckSum implements StartsConstants {
    /**
     * 一个项目中所有类和对应校验和的映射
     */
    private Map<String, Long> checkSumMap = new HashMap<>();

    /**
     * 获得一系列文件的校验和，并存储到checkSumMap中
     *
     * @param classpaths
     * @return
     */
    public Long getCheckSum(List<String> classpaths) throws Exception {
        Long res = 0L;
        for (String classpath : classpaths) {
            // 计算每个文件的校验和
            Long crcNum = getFileCRCCode(classpath);
            res += crcNum;
            if (!checkSumMap.containsKey(classpath)) {
                checkSumMap.put(classpath, crcNum);
            }
        }
        return res;
    }

    /**
     * 计算一个项目中所有类型（除去测试类）和对应校验和的映射
     *
     * @param rootPath 项目的根路径
     */
    public Map<String, Long> setCheckSumMap(String rootPath) throws Exception {
        ClassPath classPath = new ClassPath();
        // 获得类名和对应的绝对路径的映射
        Map<String, String> classPathMap = classPath.getClasspathSet(rootPath);
        // 获得所有的类名
        ArrayList<String> allClassNames = classPath.getAllClassName(classPathMap);
        // 获得所有的测试类名
        ArrayList<String> testClassNames = classPath.getAllTestClassesName(allClassNames);
        for (String key : classPathMap.keySet()) {
            // 不是测试类才计算它的校验和
            if (testClassNames.indexOf(key) == -1) {
                String value = classPathMap.get(key);
                // 测试CRC校验码
                CheckSum checkSum = new CheckSum();
                // 根据绝对路径计算校验和
                Long sum = checkSum.getSingleCheckSum(value);
                // 存储在checkSumMap中
                checkSumMap.put(key, sum);
            }
        }

        return checkSumMap;
    }

    /**
     * 计算单个文件的CRC校验码
     *
     * @param classpath
     * @return
     * @throws Exception
     */
    public Long getSingleCheckSum(String classpath) throws Exception {
        return getFileCRCCode(classpath);
    }


    /**
     * 获得文件的CRC校验和
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Long getFileCRCCode(String path) throws Exception {
        File file = new File(path);
        FileInputStream fileinputstream = new FileInputStream(file);
        CRC32 crc32 = new CRC32();
        for (CheckedInputStream checkedinputstream =
             new CheckedInputStream(fileinputstream, crc32);
             checkedinputstream.read() != -1;
        ) {
        }
        return crc32.getValue();
    }

    /**
     * 将校验和写入名为filename的文件
     *
     * @param checkSumMap
     * @param filename
     */
    public void writeCheckSumToFile(Map<String, Long> checkSumMap, String filename) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        for (String key : checkSumMap.keySet()) {
            // 如果类名包含.，代表类名包含包的名字，就要进行截取。
            // 截取最后一部分，获得类名
            if (key.contains(".")) {
                int maxI = 0;
                for (int i = 0; i < key.length(); i++) {
                    if (key.charAt(i) == '.') {
                        if (maxI < i) maxI = i;
                    }
                }
                String keyRes = "";
                for (int i = maxI + 1; i < key.length(); i++) {
                    keyRes += key.charAt(i);
                }
                String str = keyRes + " " + checkSumMap.get(key) + "\n";
                out.write(str);
            } else {
                String str = key + " " + checkSumMap.get(key) + "\n";
                out.write(str);
            }
        }
        out.close();
    }
}

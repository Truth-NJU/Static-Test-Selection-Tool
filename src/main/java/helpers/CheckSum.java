package helpers;

import constants.StartsConstants;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class CheckSum implements StartsConstants {
    /**
     * 一个项目中所有类和对应校验和的映射
     */
    private Map<String, Long> checkSumMap=new HashMap<>();

    /**
     * 获得一系列文件的校验和，并存储到checkSumMap中
     *
     * @param classpaths
     * @return
     */
    public Long getCheckSum(List<String> classpaths) throws Exception {
        Long res = 0L;
        for (String classpath : classpaths) {
            Long crcNum = getFileCRCCode(classpath);
            res += crcNum;
            if (!checkSumMap.containsKey(classpath)) {
                checkSumMap.put(classpath, crcNum);
            }
        }
        return res;
    }

    /**
     * 计算一个项目中所有类和对应校验和的映射
     * @param rootPath 项目的根路径
     */
    public void setCheckSumMap(String rootPath) throws Exception {
        ClassPath classPath=new ClassPath();
        Map<String,String> classPathMap=classPath.getClasspathSet(rootPath);
        for (String key : classPathMap.keySet()) {
            String value = classPathMap.get(key);
            //测试CRC校验码
            CheckSum checkSum=new CheckSum();
            Long sum=checkSum.getSingleCheckSum(value);
            checkSumMap.put(key,sum);
        }
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
     * 获得文件的CRC校验码
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
     * 将校验和写入文件
     */
    // 还剩下将校验和写入文件，然后比较两个文件中校验和不同的类型，并根据类型到依赖于该类型的测试的映射，选择受影响的测试。
}

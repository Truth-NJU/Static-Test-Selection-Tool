package helpers;

import java.io.*;
import java.util.*;

/**
 * 选择受影响的测试
 */
public class ImpactedTest {

    // 变更的类型（包括新出现的类型）
    private Map<String, Long> changedType = new HashMap<>();

    // 新出现的类型
    private Map<String, Long> newType = new HashMap<>();

    // 被删除的类型
    private ArrayList<String> deleteType = new ArrayList<>();


    /**
     * 比较新旧校验和文件，找出已更改的的类型
     *
     * @param filePathOld
     * @param filePathNew
     * @return
     */
    public Map<String, Long> readFileAndCompare(String filePathOld, String filePathNew) throws IOException {

        Map<String, Long> oldCheckSum = new HashMap<>();
        Map<String, Long> newCheckSum = new HashMap<>();

        //BufferedReader是可以按行读取文件
        // 读取旧的校验和文件
        FileInputStream inputStream = new FileInputStream(filePathOld);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            String[] temp = str.split(" ");
            String typeName = temp[0];
            Long checkSum = Long.parseLong(temp[1]);
            oldCheckSum.put(typeName, checkSum);
        }

        //close
        inputStream.close();
        bufferedReader.close();

        // 读取新的校验和文件
        FileInputStream inputStream1 = new FileInputStream(filePathNew);
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));

        String str1 = null;
        while ((str1 = bufferedReader1.readLine()) != null) {
            String[] temp = str1.split(" ");
            String typeName = temp[0];
            Long checkSum = Long.parseLong(temp[1]);
            newCheckSum.put(typeName, checkSum);
        }

        //close
        inputStream1.close();
        bufferedReader1.close();


        for (String key : newCheckSum.keySet()) {
            // 该类型之前有
            if (oldCheckSum.containsKey(key)) {
                if (!Objects.equals(newCheckSum.get(key), oldCheckSum.get(key))) {
                    changedType.put(key, newCheckSum.get(key));
                }
            }
            // 新出现的类型
            if (!oldCheckSum.containsKey(key)) {
                changedType.put(key, newCheckSum.get(key));
                newType.put(key, newCheckSum.get(key));
            }
        }

        // 找到删除了的类型
        for (String key : oldCheckSum.keySet()) {
            if (!newCheckSum.containsKey(key)) {
                deleteType.add(key);
            }
        }
        return changedType;
    }

    public ArrayList<String> getDeleteType() {
        return this.deleteType;
    }
    public Map<String, Long> getNewType() {
        return this.newType;
    }


    /**
     * 根据已更改的类型找到受影响的测试
     *
     * @param typeTotestDependencyMap
     * @return
     */
    public ArrayList<String> findImpactedTest(Map<String, Long> impactedType,
                                              Map<String, Set<String>> typeTotestDependencyMap) {
        ArrayList<String> impactedTest = new ArrayList<>();
        for (String type : impactedType.keySet()) {
            Set<String> tests = typeTotestDependencyMap.get(type);
            for (Iterator it = tests.iterator(); it.hasNext(); ) {
                impactedTest.add(it.next().toString());
            }
        }
        return impactedTest;
    }
}

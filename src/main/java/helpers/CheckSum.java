package helpers;

import constants.StartsConstants;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class CheckSum  implements StartsConstants {
    public Long getSingleCheckSum(String classpath) throws Exception {
        return getFileCRCCode(classpath);
    }


    /**
     * 获得文件的CRC校验码
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


    public  String toClassName(String fqn) {
        return fqn.replace(DOT, File.separator) + CLASS_EXTENSION;
    }

}

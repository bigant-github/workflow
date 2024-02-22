package org.bigant.fw.lark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author galen
 * @date 2024/2/2211:32
 */
public class IOUtils {
    public static void copyFile(URL url, File file) throws IOException {

        if (file.exists()) {
            file.delete();
        }

        InputStream var2 = url.openStream();
        FileOutputStream var3 = new FileOutputStream(file);
        byte[] var4 = new byte[1024];

        int var5;
        while ((var5 = var2.read(var4)) != -1) {
            var3.write(var4, 0, var5);
        }

        var3.close();
        var2.close();
        file.setReadOnly();
        file.setReadable(true, false);
    }
}

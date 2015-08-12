package org.andydyer.gifwatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by andy on 8/11/15.
 */
public class Utils {

    public static byte[] inputStreamToByteArray(InputStream inputStream) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte data[] = new byte[1024];
            int count;

            while ((count = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, count);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}

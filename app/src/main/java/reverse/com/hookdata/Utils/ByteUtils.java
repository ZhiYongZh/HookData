package reverse.com.hookdata.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Created by GuanJh on 2018/6/7/007.
 */
public class ByteUtils {

    public static String bytesToHexString(byte[] data){
        int dataLen = data.length;
        StringBuilder sb = new StringBuilder(dataLen);
        String hexchar;
        for (int i =0; i < dataLen; i++){
            hexchar = Integer.toHexString((data[i]&0xFF));
            if (hexchar.length() < 2)
                sb.append(0);
            sb.append(hexchar.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] uncompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }

        return out.toByteArray();
    }
}

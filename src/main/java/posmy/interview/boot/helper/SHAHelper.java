package posmy.interview.boot.helper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAHelper {

    public static String hash(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHexString(hash);
    }

    static String bytesToHexString(byte[] data) {
        StringBuilder retString = new StringBuilder();
        for (byte datum : data) {
            retString.append(Integer.toHexString(0x0100 + (datum & 0x00FF))
                    .substring(1));
        }
        return retString.toString();
    }
}

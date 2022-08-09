package fusionchain;

import java.security.*;
import java.util.Base64;

/**
 * @notice The StringUtil class takes a string as input, hashes it with the SHA256 algorithm, and returns
 * the hashed string.
 */

public class StringUtil {

    /**
     * @notice The applySha256() function applies Sha256 to a string and returns the result.
     * @param input The input string to be hashed.
     */

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

    /**
     * @notice The applyECDSASig() function applies the ECDSA Signature and returns the result in bytes.
     * @param privateKey The private key of the sender.
     * @param input The data to be signed by the sender.
     */

    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];

        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception error) {
            throw new RuntimeException(error);
        }

        return output;
    }

    /**
     * @notice The verifyECDSASig() function verifies a String signature.
     * @param publicKey The public key of the sender.
     * @param data The data contained within the transaction.
     * @param signature The signature of the sender.
     */

    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            
            return ecdsaVerify.verify(signature);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

    /**
     * @notice The getStringFromKey() function returns an encoded String for a given key.
     * @param key The key to be encoded.
     */

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}

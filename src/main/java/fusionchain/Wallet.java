package fusionchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * @notice The Wallet class contains the basic parameters required to create user wallets.
 */

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    // Wallet Constructor
    public Wallet() {
        generateKeyPair();
    }

    /**
     * @notice The generateKeyPair() function generates a pair of keys (public and private) for a new wallet.
     */

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // Initialize KeyPairGenerator
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Generate keys
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

}

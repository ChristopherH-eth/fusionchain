package fusionchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * @notice The Wallet class contains the basic parameters required to create user wallets.
 */

public class Wallet {

    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

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

    /**
     * @notice The getBalance() function returns the balance and stores the UTXOs owned by this wallet in
     * this.UTXOs.
     */

    public float getBalance() {
        float total = 0;

        for(Map.Entry<String,TransactionOutput> item : FusionChain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();

            if(UTXO.isMine(publicKey)) {
                UTXOs.put(UTXO.id,UTXO);
                total += UTXO.value;
            }
        }

        return total;
    }

    /**
     * @notice The sendFunds() function generates and returns a new transaction from this wallet.
     */

    public Transaction sendFunds(PublicKey _recipient, float value) {
        if(getBalance() < value) {
            System.out.println("Insufficient funds to send transaction.");

            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        float total = 0;

        for(Map.Entry<String,TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }

        Transaction newTranaction = new Transaction(publicKey, _recipient, value, inputs);
        newTranaction.generateSignature(privateKey);

        for(TransactionInput input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }

        return newTranaction;
    }

}

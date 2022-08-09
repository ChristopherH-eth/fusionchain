package fusionchain;

import java.security.*;
import java.util.ArrayList;

/**
 * @notice The Transaction class allows users to transact with each other over the blockchain.
 */

public class Transaction {

    public String transactionId;
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    // Transaction Constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    /**
     * @notice The calculateHash() function takes the sender address, the recipient address, the value of the
     * transaction, and the transaction number (sequence) and returns the transaction hash.
     */

    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(
            StringUtil.getStringFromKey(sender) + 
            StringUtil.getStringFromKey(recipient) + 
            Float.toString(value) + 
            sequence
        );
    }

    /**
     * @notice The generateSignature() function generates a signature from the sender's private key to sign a transaction.
     * @param privateKey Takes a privateKey of the sender as a parameter to sign the transaction.
     */

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    /**
     * @notice The verifySignature() function checks the data within a signed transaction. If the data has been tampered with
     * is returns false, otherwise it returns true.
     */

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
    
}

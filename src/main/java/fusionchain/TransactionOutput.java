package fusionchain;

import java.security.PublicKey;

public class TransactionOutput {

    public String id;
    public PublicKey recipient;
    public float value;
    public String parentTransactionId;

    // TransactionOutput Constructor
    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
    }

    /**
     * @notice The isMine() function verifies the recipient address.
     */

    public boolean isMine(PublicKey publicKey) {
        return publicKey == recipient;
    }

}

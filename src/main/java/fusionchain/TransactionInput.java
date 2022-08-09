package fusionchain;

/**
 * @notice The TransactionInput class will be used to reference TransactionOutputs that have 
 * not been spent, tracked via the transactionOutputId.
 */

public class TransactionInput {

    public String transactionOutputId; // transactiondId reference
    public TransactionOutput UTXO; // Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

}

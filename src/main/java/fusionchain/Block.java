package fusionchain;

import java.util.Date;
import java.util.ArrayList;

/**
 * @notice The Block class contains the basic parameters required to create each block.
 */

public class Block {

    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public String blockHash;
    public String previousBlockHash;
    public String merkleRoot;
    public long timeStamp;
    public int nonce;

    // Block Constructor
    public Block(String previousHash) {
        this.previousBlockHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.blockHash = calculateHash();
    }

    /**
     * @notice The calculateHash() function applies the SHA256 algorithm to a block using the previous block's hash,
     * the current block's timestamp, and the current block's data (merkleRoot) as input.
     */

    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(previousBlockHash + 
            Long.toString(timeStamp) + 
            Long.toString(nonce) + 
            merkleRoot
        );

        return calculatedHash;
    }

    /**
     * @notice The mineBlock() function mines a block.
     */

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');

        while(!blockHash.substring(0, difficulty).equals(target)) {
            blockHash = calculateHash();
            nonce ++;
        }

        System.out.println("Block Mined: " + blockHash);
    }

    /**
     * @notice The addTransaction() function adds transactions to each block.
     */

    public boolean addTransaction(Transaction transaction) {
        if(transaction == null) return false;
        
        if(previousBlockHash != "0") {
            if(transaction.processTransaction() != true) {
                System.out.println("Transaction failed to process.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction added to block successfully.");

        return true;
    }
    
}
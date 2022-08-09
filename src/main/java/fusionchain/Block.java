package fusionchain;

import java.util.Date;

/**
 * @notice The Block class contains the basic parameters required to create each block.
 */

public class Block {

    public String blockHash;
    public String previousBlockHash;
    private String data;
    private long timeStamp;
    private long nonce;

    // Block Constructor
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousBlockHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.blockHash = calculateHash();
    }

    /**
     * @notice The calculateHash() function applies the SHA256 algorithm to a block using the previous block's hash,
     * the current block's timestamp, and the current block's data as input.
     */

    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(previousBlockHash + Long.toString(timeStamp) + Long.toString(nonce) + data);

        return calculatedHash;
    }

    /**
     * @notice The mineBlock() function mines a block.
     */

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        //nonce = 0;

        while(!blockHash.substring(0, difficulty).equals(target)) {
            blockHash = calculateHash();
            nonce ++;
        }

        System.out.println("Block Mined: " + blockHash);
    }
    
}
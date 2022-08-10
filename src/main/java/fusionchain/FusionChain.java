package fusionchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.GsonBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @title FusionChain
 * @author 0xChristopher
 */

public class FusionChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); // List of all unspent transactions

    public static int difficulty = 5;
    public static float minimumTransaction = 0.1f;
    public static Wallet walletA;
    public static Wallet walletB;
    public static Transaction genesisTransaction;

    public static void main(String[] args) {

        // Security provider for public and private key generation algorithm
        Security.addProvider(new BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        // Create Genesis Transaction which adds 100 Fusion to walletA.
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey); // Manually sign
        genesisTransaction.transactionId = "0"; // Manually set transaction id
        genesisTransaction.outputs.add(new TransactionOutput(
            genesisTransaction.recipient, 
            genesisTransaction.value, 
            genesisTransaction.transactionId)
        ); // Manually add transaction output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); // Store first tx

        System.out.println("Creating and mining Genesis Block...");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        // System.out.println("Public and private keys:");
        // System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
        // System.out.println(StringUtil.getStringFromKey(walletA.privateKey));

        // Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        // transaction.generateSignature(walletA.privateKey);

        // System.out.println("Signature verification: ");
        // System.out.println(transaction.verifySignature());

        // blockchain.add(new Block("The first of many blocks", "0"));
        // System.out.println("Mining block 1...");
        // blockchain.get(0).mineBlock(difficulty);

        // blockchain.add(new Block("Second block", blockchain.get(blockchain.size()-1).blockHash));
        // System.out.println("Mining block 2...");
        // blockchain.get(1).mineBlock(difficulty);

        // blockchain.add(new Block("Third block", blockchain.get(blockchain.size()-1).blockHash));
        // System.out.println("Mining block 3...");
        // blockchain.get(2).mineBlock(difficulty);

        // System.out.println("\nBlockchain is Valid: " + isChainValid());

        // String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        // System.out.println("\nFusionChain: ");
        // System.out.println(blockchainJson);
    }

    /**
     * @notice The isChainValid() function iterates through the blockchain to validate the current block against itself,
     * validates the hash of the previous block, and checks to make sure each block is mined.
     */

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // if(!currentBlock.blockHash.equals(currentBlock.calculateHash())) {
            //     System.out.println("Current block's hash doesn't match its calculated hash");
            //     System.out.println("Current hash: " + currentBlock.blockHash);
            //     System.out.println("Calculated hash: " + currentBlock.calculateHash());
            //     return false;
            // } else 
            if (!previousBlock.blockHash.equals(currentBlock.previousBlockHash)) {
                System.out.println("Current block doesn't contain previous block's hash");
                return false;
            } else if (!currentBlock.blockHash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }

        return true;
    }

    /**
     * @notice The addBlock() function mines and adds new blocks to the blockchain.
     * @param newBlock The next block to be added to the blockchain.
     */

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

}

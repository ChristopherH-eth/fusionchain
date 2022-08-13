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
    public static boolean connectedToBlockchain;

    public static void fusionChain() {

        // Security provider for public and private key generation algorithm
        Security.addProvider(new BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();
        connectedToBlockchain = true;

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

        // Test Blocks
        Block block1 = new Block(genesis.blockHash);
        System.out.println("\nWallet A's balance is: " + walletA.getBalance());
        System.out.println("\nWallet A is attemping to send funds (40) to Wallet B...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWallet A's balance is: " + walletA.getBalance());
        System.out.println("Wallet B's balance is: " + walletB.getBalance());

        Block block2 = new Block(block1.blockHash);
        System.out.println("\nWallet A is attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWallet A's balance is: " + walletA.getBalance());
        System.out.println("Wallet B's balance is: " + walletB.getBalance());

        Block block3 = new Block(block2.blockHash);
        System.out.println("\nWallet B is attempting to send funds (20) to Wallet A...");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
        addBlock(block3);
        System.out.println("\nWallet A's balance is: " + walletA.getBalance());
        System.out.println("Wallet B's balance is: " + walletB.getBalance());

        isChainValid();
    }

    /**
     * @notice The isChainValid() function iterates through the blockchain to validate the current block against itself,
     * validates the hash of the previous block, and checks to make sure each block is mined. It will also validate the 
     * transactions within each block.
     */

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); // Temp list at a given state
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        for(int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // Verify blockchain hashes
            if(!currentBlock.blockHash.equals(currentBlock.calculateHash())) {
                System.out.println("Current block's hash doesn't match its calculated hash");
                System.out.println("Current hash: " + currentBlock.blockHash);
                System.out.println("Calculated hash: " + currentBlock.calculateHash());
                return false;
            } else if (!previousBlock.blockHash.equals(currentBlock.previousBlockHash)) {
                System.out.println("Current block doesn't contain previous block's hash");
                return false;
            } else if (!currentBlock.blockHash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

            // Verify transactions
            TransactionOutput tempOutput;

            for(int j = 0; j < currentBlock.transactions.size(); j++) {
                Transaction currentTransaction = currentBlock.transactions.get(j);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("Signature on transaction (" + j + ") is invalid.");
                    
                    return false;
                } else if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("Inputs are not equal to outputs on transaction (" + j + ").");

                    return false;
                }

                for(TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("Referenced transaction (" + j + ") is missing.");

                        return false;
                    } else if(input.UTXO.value != tempOutput.value) {
                        System.out.println("Referenced transaction (" + j + ") value is invalid.");

                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if(currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("Transaction (" + j + ") recipient mismatch.");

                    return false;
                }

                if(currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("Transaction (" + j + ") sender mismatch.");
                }
            }
        }

        System.out.println("Blockchain is valid.");

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

    /* Getters and Setters */

    public static void setConnectedToBlockchain() {
        Node.connectedToBlockchain = connectedToBlockchain;
    }

    public static boolean getConnectedToBlockchain() {
        return connectedToBlockchain;
    }

}

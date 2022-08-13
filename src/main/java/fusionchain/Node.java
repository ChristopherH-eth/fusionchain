package fusionchain;

import java.security.Security;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Node extends JFrame implements ActionListener {

    private JLabel fusionWalletText, blockText, clicked, publicKeyText, privateKeyText;
    private JButton button, clickButton;
    private JPanel containerPanel, mainPanel, iconPanel, keyPanel, dataPanel;
    private boolean _clickMeMode = true;
    public static boolean connectedToBlockchain = true;
    public Wallet wallet;
    public int count = 0;

    private JFrame fusionNode;

    private Node() {
        initialize();
    }

    /**
     * @notice The initialize() function initializes the wallet/node instance.
     */

    public void initialize() {
        fusionNode = new JFrame();

        fusionWalletText = new JLabel("FusionCore Wallet");
        blockText = new JLabel("");
        clicked = new JLabel("Wallet Created!");
        publicKeyText = new JLabel("");
        privateKeyText = new JLabel("");

        fusionNode.setLayout(new BorderLayout(10,5));
        fusionNode.setSize(400,500);
        fusionNode.setTitle("FusionCore Wallet");
        fusionNode.setLocationRelativeTo(null);
        fusionNode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new Dimension(600,520));
        containerPanel.setLayout(new BorderLayout(5,5));
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(575,500));
        mainPanel.setLayout(new BorderLayout(5,5));

        button = new JButton("Generate Wallet Keys");
        button.addActionListener(this); // Add button as an event listener

        // clickButton = new JButton("Clear Keys");
        // clickButton.addActionListener(this); // Add button as an event listener

        // iconPanel Settings
        iconPanel = new JPanel();
        iconPanel.setLayout(new BorderLayout(10,5));
        iconPanel.setBackground(Color.gray);
        iconPanel.setPreferredSize(new Dimension(175,500));
        iconPanel.add(button, (BorderLayout.NORTH));

        // keyPanel Settings
        keyPanel = new JPanel();
        keyPanel.setLayout(new GridLayout(6,1,10,5));
        keyPanel.setBackground(Color.white);
        keyPanel.setPreferredSize(new Dimension(400,300));
        keyPanel.add(fusionWalletText);
        keyPanel.add(publicKeyText);
        keyPanel.add(privateKeyText);

        // dataPanel Settings
        dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout(10,5));
        dataPanel.setBackground(Color.gray);
        dataPanel.setPreferredSize(new Dimension(575,200));
        dataPanel.add(blockText);

        // Add components to JFrame
        fusionNode.add(containerPanel);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(iconPanel, BorderLayout.WEST);
        mainPanel.add(keyPanel, BorderLayout.CENTER);
        mainPanel.add(dataPanel, BorderLayout.SOUTH);

        // This listener allows the window to be closed
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        fusionNode.addWindowListener(l);
        fusionNode.pack();
        fusionNode.setVisible(true);
        fusionNode.setResizable(false);

        // SwingWorker worker1 is the thread for the blockchain.
        SwingWorker<Void, Void> worker1 = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                FusionChain.fusionChain();
                return null;
            }
        };

        // SwingWorker worker2 is used to update the GUI.
        SwingWorker<Void, Void> worker2 = new SwingWorker<Void,Void>() {
            @Override
            public Void doInBackground() {

                while(connectedToBlockchain) {
                    FusionChain.getConnectedToBlockchain();

                    if(!connectedToBlockchain) {
                        break;
                    }

                    if(Block.getLastBlock() != null) {
                        blockText.setText("Block mined. Last hash: " + Block.getLastBlock());
                    } else {
                        blockText.setText("Waiting for blocks...");
                    }
                }
                
                return null;
            }
        };

        worker1.execute();
        worker2.execute();
    }

    /**
     * @notice The actionPerformed() function listens for user events.
     */

    public void actionPerformed(ActionEvent event) {
        // Object source = event.getSource();

        if(_clickMeMode) {
            wallet = new Wallet();
            publicKeyText.setText(wallet.getPublicKey());
            privateKeyText.setText(wallet.getPrivateKey());
            button.setText("Clear Keys");
            _clickMeMode = false;
        } else {
            publicKeyText.setText("");
            privateKeyText.setText("");
            button.setText("Generate Wallet Keys");
            _clickMeMode = true;
        }
    }

    public static void main(String[] args) {

        Security.addProvider(new BouncyCastleProvider());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Node fusionNode = new Node();
            }
        });
    }

}

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

    JLabel text, text2, blockText, clicked, publicKeyText, privateKeyText;
    JButton button, clickButton;
    private JPanel containerPanel, mainPanel, iconPanel, keyPanel, dataPanel;
    private boolean _clickMeMode = true;
    public Wallet wallet;

    private JFrame fusionNode;

    private Node() {
        initialize();
    }

    public void initialize() {
        fusionNode = new JFrame();
        text = new JLabel("FusionCore Wallet");
        text2 = new JLabel("Other information here");
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
        keyPanel.add(text);
        keyPanel.add(publicKeyText);
        keyPanel.add(privateKeyText);

        // dataPanel Settings
        dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout(10,5));
        dataPanel.setBackground(Color.gray);
        dataPanel.setPreferredSize(new Dimension(575,200));
        dataPanel.add(text2);
        dataPanel.add(blockText);

        // Add components to JFrame
        fusionNode.add(containerPanel);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(iconPanel, BorderLayout.WEST);
        mainPanel.add(keyPanel, BorderLayout.CENTER);
        mainPanel.add(dataPanel, BorderLayout.SOUTH);

        // This code lets you close the window
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        fusionNode.addWindowListener(l);
        // This code lets you see the frame
        fusionNode.pack();
        fusionNode.setVisible(true);
        fusionNode.setResizable(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                FusionChain.fusionChain();
                return null;
            }
        };

        worker.execute();
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

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

        if(Block.getBlockMined()) {
            blockText.setText("Block mined. Last hash: " + Block.getLastBlock());
            Block.resetMinedFlag();
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

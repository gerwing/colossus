package colossus.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import colossus.cipher.Decoding;
import colossus.cipher.Encoding;
import colossus.cipher.KeyGenerator;

public class Colossus_GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTextArea encodePlain;
	private JTextArea encodeCipher;
	private JTextField encodeKey;
	private JTextArea decodePlain;
	private JTextArea decodeCipher;
	private JTextField decodeKey;
	
	/**
	 * Construct new Colossus GUI for testing the colossus cipher
	 */
	public Colossus_GUI(){
		super("Colossus Cipher");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initWidgets();
	}
	
	/*
	 * Construct the widgets
	 */
	private void initWidgets() {
		/*
		 * Encode Panel
		 */
		JPanel pnlEncode = new JPanel();
		pnlEncode.setLayout(new BoxLayout(pnlEncode, BoxLayout.Y_AXIS));
		
		//initialize widgets
		encodePlain = new JTextArea("type plain text here");
		encodePlain.setPreferredSize(new Dimension(200,100));
		encodeCipher = new JTextArea();
		encodeCipher.setPreferredSize(new Dimension(200,100));

		JPanel pnlSubmit = new JPanel();
		encodeKey = new JTextField(14);
		JButton btnRandomKey = new JButton("Generate Key");
		btnRandomKey.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				encodeKey.setText(KeyGenerator.getRandomKey());
			}
		});
		JButton btnSubmit = new JButton("Encode");
		btnSubmit.addActionListener(this);
		btnSubmit.setActionCommand("encode");
		pnlSubmit.add(btnRandomKey);
		pnlSubmit.add(new JLabel("Key:"));
		pnlSubmit.add(encodeKey);
		pnlSubmit.add(btnSubmit);
		
		//add widgets
		pnlEncode.add(encodePlain);
		pnlEncode.add(pnlSubmit);
		pnlEncode.add(encodeCipher);
		
		/*
		 * Decode Panel
		 */
		JPanel pnlDecode = new JPanel();
		pnlDecode.setLayout(new BoxLayout(pnlDecode, BoxLayout.Y_AXIS));
		
		//initialize widgets
		decodeCipher = new JTextArea("type cipher text here");
		decodeCipher.setPreferredSize(new Dimension(200,100));
		decodePlain = new JTextArea();
		decodePlain.setPreferredSize(new Dimension(200,100));

		JPanel pnlSubmit2 = new JPanel();
		decodeKey = new JTextField(14);
		JButton btnSubmit2 = new JButton("Decode");
		btnSubmit2.addActionListener(this);
		btnSubmit2.setActionCommand("decode");
		pnlSubmit2.add(new JLabel("Key:"));
		pnlSubmit2.add(decodeKey);
		pnlSubmit2.add(btnSubmit2);
		
		//add widgets
		pnlDecode.add(decodeCipher);
		pnlDecode.add(pnlSubmit2);
		pnlDecode.add(decodePlain);
		
		/*
		 * Tab Pane
		 */
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Encoding", pnlEncode);
		tabPane.addTab("Decoding", pnlDecode);
		
		/*
		 * Content Pane
		 */
		getContentPane().add(tabPane);
		
		pack();
		setVisible(true);
	}

	/* (non-Javadoc)
	 * Submit button pressed, either in the encode or decode tab
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//Encode Text
		if(e.getActionCommand().equals("encode"))
		{
			Encoding encoder = new Encoding();
			String cipherText = encoder.encode(encodePlain.getText(), encodeKey.getText());
			encodeCipher.setText(cipherText);
		}
		//Decode Text
		else if(e.getActionCommand().equals("decode"))
		{
			Decoding decoder = new Decoding();
			String plainText = decoder.decode(decodeCipher.getText(), decodeKey.getText());
			decodePlain.setText(plainText);
		}
		
	}
	
	
}

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gui implements NativeKeyListener {

	private static JFrame frame;
	private JTextField txtHelloWorld;
	private JTextField txtEs;
	private Spam s = new Spam();
	private String text;
	private long ms;
	private int keyCode;
	private JButton btnNewButton;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
				logger.setLevel(Level.OFF);
				logger.setUseParentHandlers(false);
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(frame);
					GlobalScreen.registerNativeHook();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread(Gui::unregisterGlobalListener));
	}

	public Gui() {
		initialize();
		GlobalScreen.addNativeKeyListener(this);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 687, 413);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Inserisci testo:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblNewLabel.setBounds(33, 11, 151, 58);
		frame.getContentPane().add(lblNewLabel);

		txtHelloWorld = new JTextField();
		txtHelloWorld.setForeground(Color.GRAY);
		txtHelloWorld.setText("Hello World!");
		txtHelloWorld.setFont(new Font("Tahoma", Font.PLAIN, 22));
		txtHelloWorld.setBounds(301, 31, 326, 29);
		frame.getContentPane().add(txtHelloWorld);
		txtHelloWorld.setColumns(10);

		txtHelloWorld.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtHelloWorld.getText().equals("Hello World!")) {
					txtHelloWorld.setText("");
					txtHelloWorld.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtHelloWorld.getText().isEmpty()) {
					txtHelloWorld.setText("Hello World!");
					txtHelloWorld.setForeground(Color.GRAY);
				}
			}

		});

		JLabel lblInserisciLaFrequenza = new JLabel("Inserisci la frequenza (ms):");
		lblInserisciLaFrequenza.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblInserisciLaFrequenza.setBounds(10, 80, 267, 58);
		frame.getContentPane().add(lblInserisciLaFrequenza);

		txtEs = new JTextField();
		txtEs.setForeground(Color.GRAY);
		txtEs.setFont(new Font("Tahoma", Font.PLAIN, 22));
		txtEs.setText("es:100");
		txtEs.setColumns(10);
		txtEs.setBounds(301, 100, 301, 29);
		frame.getContentPane().add(txtEs);

		btnNewButton = new JButton("F6 START");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnNewButton.getText().equals("F6 START")) {
					btnNewButton.setText("F5 STOP");
					btnNewButton.repaint();
					keyCode = NativeKeyEvent.VC_F6;
					text = txtHelloWorld.getText();
					if (txtEs.getText().equals("es:100")) {
						ms = Long.valueOf(txtEs.getText().substring(3));
					} else {
						ms = Long.valueOf(txtEs.getText());
					}
					try {
						s.Start(text, ms, keyCode);
					} catch (AWTException | InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					frame.repaint();
				} else if (btnNewButton.getText().equals("F5 STOP")) {
					btnNewButton.setText("F6 START");
					keyCode = NativeKeyEvent.VC_F5;
					btnNewButton.repaint();
					s.stop();
				}

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnNewButton.setBounds(116, 210, 400, 120);
		frame.getContentPane().add(btnNewButton);

		txtEs.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtEs.getText().equals("es:100")) {
					txtEs.setText("");
					txtEs.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtEs.getText().isEmpty()) {
					txtEs.setText("es:100");
					txtEs.setForeground(Color.GRAY);
				}
			}

		});

	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		keyCode = e.getKeyCode();
		if (keyCode == NativeKeyEvent.VC_F6 && btnNewButton.getText().equals("F6 START")) {
			btnNewButton.doClick();
		} else if (keyCode == NativeKeyEvent.VC_F5 && btnNewButton.getText().equals("F5 STOP")) {
			btnNewButton.doClick();
		}

	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	public static void unregisterGlobalListener() {
	    try {
	        GlobalScreen.unregisterNativeHook();
	    } catch (NativeHookException e) {
	        e.printStackTrace();
	    }
	}
	
}

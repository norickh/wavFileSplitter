package test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import test.PathTool;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class wavFileSplitter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textSplit;
	private JLabel label;
	private String wavFilepath = PathTool.get_currentpath();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					wavFileSplitter frame = new wavFileSplitter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public wavFileSplitter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 250, 300);
		setTitle("wav File Splitter alpha");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(6, 54, 130, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		label = new JLabel("Input separation time (sec)");
		label.setBounds(6, 107, 192, 26);
		contentPane.add(label);
		
		
		textSplit = new JTextField();
		textSplit.setBounds(6, 134, 130, 26);
		contentPane.add(textSplit);
		textSplit.setColumns(10);
		
		JLabel lblSelectwavFile = new JLabel("Select .wav file");
		lblSelectwavFile.setBounds(6, 28, 192, 26);
		contentPane.add(lblSelectwavFile);

		
		JButton btnFile = new JButton("Browse");
		btnFile.setBounds(133, 54, 85, 29);
		contentPane.add(btnFile);
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser(textField.getText());
				//wavファイルしか選べないようにこのメソッドをoverrideしている(が実際には選べる)
				filechooser.addChoosableFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return null;
					}

					@Override
					public boolean accept(File f) {
						if (f.isDirectory() == true) {
							return false;
						}

						if (f.getName().endsWith(".wav")) {
							return true;
						}

						return false;
					}
				});
				int selected = filechooser.showOpenDialog(btnFile);
				if (selected == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					System.err.println(file.getAbsolutePath());
					wavFilepath = file.getAbsolutePath();
				}	
				if ((new File(wavFilepath)).isFile() == false || wavFilepath.endsWith("wav") == false) {
					return;
				}else{
					textField.setText(wavFilepath);
				}
			}
		});
		
		JButton btnSplit = new JButton("Split");
		btnSplit.setBounds(6, 187, 117, 29);
		contentPane.add(btnSplit);
		btnSplit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File wavFile = new File(wavFilepath);
				float slotSec = (float) Float.parseFloat(textSplit.getText());
				new WavSplitFixedTime(wavFile, slotSec);
			}
		});

	}
}

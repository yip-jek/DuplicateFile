import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DFWin {

	private static final int FRAME_WIDTH = 570;
	private static final int FRAME_HEIGHT = 405;
	private static final int LABEL_IN_POS_X = 15;
	private static final int LABEL_IN_POS_Y = 15;
	private static final int LABEL_IN_WIDTH = 100;
	private static final int LABEL_IN_HEIGHT = 20;
	private static final int TEXT_POS_X = 15;
	private static final int TEXT_POS_Y = 35;
	private static final int TEXT_WIDTH = 400;
	private static final int TEXT_HEIGHT = 260;
	private static final int LABEL_OUT_POS_X = 15;
	private static final int LABEL_OUT_POS_Y = 310;
	private static final int LABEL_OUT_WIDTH = 100;
	private static final int LABEL_OUT_HEIGHT = 20;
	private static final int FIELD_POS_X = 15;
	private static final int FIELD_POS_Y = 330;
	private static final int FIELD_WIDTH = 400;
	private static final int FIELD_HEIGHT = 30;
	private static final int BUTTON_CHK_POS_X = 450;
	private static final int BUTTON_CHK_POS_Y = 120;
	private static final int BUTTON_CHK_WIDTH = 80;
	private static final int BUTTON_CHK_HEIGHT = 30;
	private static final int BUTTON_CLN_POS_X = 450;
	private static final int BUTTON_CLN_POS_Y = 220;
	private static final int BUTTON_CLN_WIDTH = 80;
	private static final int BUTTON_CLN_HEIGHT = 30;

	private static final String TITLE_BUTTON_CHK = "查重";
	private static final String TITLE_BUTTON_CLN = "清空";
	private static final String LABEL_INPUT = "输入路径";
	private static final String LABEL_OUTPUT = "输出文件";

	private DFManager m_mgr = null;
	private JFrame m_frame = null;
	private JTextArea m_textInput = null;
	private JTextField m_textOutput = null;
	private JButton m_buttonCheck = null;
	private JButton m_buttonClean = null;

	public DFWin(DFManager mgr) {
//		JFrame.setDefaultLookAndFeelDecorated(true);
		m_mgr = mgr;

		Init();
	}

	private void Init() {
		InitFrame();
		AddPanel();
	}

	private void InitFrame() {
		m_frame = new JFrame(GlobalDef.PROGRAM_NAME + " " + GlobalDef.VERSION);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		m_frame.setResizable(false);

		// Screen centered
		Dimension scr_size = Toolkit.getDefaultToolkit().getScreenSize();
		int f_x = (int)(scr_size.getWidth() - m_frame.getWidth())/2;
		int f_y = (int)(scr_size.getHeight() - m_frame.getHeight())/2;
		m_frame.setLocation(f_x, f_y);
	}

	private void AddPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		m_frame.add(panel);

		AddInputText(panel);
		AddOutputText(panel);
		AddButton(panel);
	}

	private void AddInputText(JPanel p) {
		JLabel label = new JLabel(LABEL_INPUT);
		label.setBounds(LABEL_IN_POS_X, LABEL_IN_POS_Y, LABEL_IN_WIDTH, LABEL_IN_HEIGHT);
		p.add(label);

		m_textInput = new JTextArea();
		JScrollPane scroll = new JScrollPane(m_textInput);

		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(TEXT_POS_X, TEXT_POS_Y, TEXT_WIDTH, TEXT_HEIGHT);
		p.add(scroll);
	}

	private void AddOutputText(JPanel p) {
		JLabel label = new JLabel(LABEL_OUTPUT);
		label.setBounds(LABEL_OUT_POS_X, LABEL_OUT_POS_Y, LABEL_OUT_WIDTH, LABEL_OUT_HEIGHT);
		p.add(label);

		m_textOutput = new JTextField();
		JScrollPane scroll = new JScrollPane(m_textOutput);

		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(FIELD_POS_X, FIELD_POS_Y, FIELD_WIDTH, FIELD_HEIGHT);
		p.add(scroll);
	}

	private void AddButton(JPanel p) {
		// Button: Check duplicate
		m_buttonCheck = new JButton(TITLE_BUTTON_CHK);
		m_buttonCheck.setBounds(BUTTON_CHK_POS_X, BUTTON_CHK_POS_Y, BUTTON_CHK_WIDTH, BUTTON_CHK_HEIGHT);
		m_buttonCheck.addActionListener(new CheckDuplicateAction(this));
		p.add(m_buttonCheck);

		// Button: Clean
		m_buttonClean = new JButton(TITLE_BUTTON_CLN);
		m_buttonClean.setBounds(BUTTON_CLN_POS_X, BUTTON_CLN_POS_Y, BUTTON_CLN_WIDTH, BUTTON_CLN_HEIGHT);
		m_buttonClean.addActionListener(new CleanAction(this));
		p.add(m_buttonClean);
	}

	public void ShowUp() {
		m_frame.pack();
		m_frame.setVisible(true);
	}

	public void CleanAll() {
		m_textInput.setText("");
		m_textOutput.setText("");
	}

	public void DoCheckDuplicate() {
		SetEnabled(false);
		CheckDuplicate();
	}

	public void SetEnabled(boolean b) {
		m_buttonCheck.setEnabled(b);
		m_buttonClean.setEnabled(b);
		m_textInput.setEnabled(b);
		m_textOutput.setEditable(b);
	}

	private void CheckDuplicate() {
		try {
			m_mgr.Handle(GetInputText(), GetOutputText());
		} catch (DFException e) {
			MessageDialog("ERROR", e.toString(), JOptionPane.ERROR_MESSAGE);
			SetEnabled(true);
		}
	}

	public void MessageDialog(String title, String msg, int msg_type) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, msg, title, msg_type);
	}

	private String[] GetInputText() throws DFException {
		String text = m_textInput.getText().trim();
		if (text.isEmpty()) {
			throw new DFException("Input text is Blank!");
		}

		return text.split("\n");
	}

	private String GetOutputText() throws DFException {
		String text = m_textOutput.getText().trim();
		if (text.isEmpty()) {
			throw new DFException("Output text is Blank!");
		}

		return text;
	}

}

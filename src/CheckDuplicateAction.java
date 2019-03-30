import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckDuplicateAction implements ActionListener {

	private DFWin m_win = null;

	public CheckDuplicateAction(DFWin win) {
		m_win = win;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		m_win.DoCheckDuplicate();
	}

}

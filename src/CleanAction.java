import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CleanAction implements ActionListener {

	private DFWin m_win = null;

	public CleanAction(DFWin win) {
		m_win = win;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		m_win.CleanAll();
	}

}

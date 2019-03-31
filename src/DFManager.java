
public class DFManager {

	private DFWin m_win = null;

	public DFManager() {
		Init();
	}

	private void Init() {
		m_win = new DFWin(this);
	}

	public void Exec() {
		m_win.ShowUp();
	}

	public void Handle(String[] inputs, String output) throws DFException {
		Duplicater dup = new Duplicater(m_win, inputs, output);
		dup.Start();
	}

	public static void main(String[] args) {

		DFManager df_mgr = new DFManager();
		df_mgr.Exec();
	}

}

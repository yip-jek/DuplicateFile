import java.io.File;

public class FileInfo {

	private String m_path = null;
	private String m_name = null;
	private String m_algo = null;

	public FileInfo(File f, String algo) {
		m_path = f.getPath();
		m_name = f.getName();
		m_algo = algo;
	}

	public String GetPath() {
		return m_path;
	}

	public String GetName() {
		return m_name;
	}

	public String GetAlgo() {
		return m_algo;
	}

	public String GetInfo1() {
		StringBuilder buf = new StringBuilder(m_path);
		buf.append(", ").append(Duplicater.ALGORITHM).append("=").append(m_algo);
		return buf.toString();
	}

	public String GetInfo2() {
		StringBuilder buf = new StringBuilder("PATH=");
		buf.append(m_path).append(", NAME=").append(m_name);
		return buf.toString();
	}

}

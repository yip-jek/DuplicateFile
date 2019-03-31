import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Duplicater implements Runnable {

//	public static final String ALGORITHM   = "MD5";
	public static final String ALGORITHM   = "SHA-256";
	private static final int   BUFFER_SIZE = 256 * 1024;

	private Thread              m_thread    = null;
	private DFWin               m_win       = null;
	private File[]              m_inputs    = null;
	private DFOutput            m_output    = null;
	private ArrayList<FileInfo> m_listFiles = null;
	private MessageDigest       m_msgDigest = null;
	private byte[]              m_buffer    = null;

	private HashMap<String, ArrayList<FileInfo> > m_mapListDup = null;

	public Duplicater(DFWin win, String[] in, String out) throws DFException {
		m_win = win;

		Init(in, out);
	}

	private void Init(String[] in, String out) throws DFException {
		InitInputPath(in);
		InitOutput(out);
		InitFileList();
		InitDupMap();
		InitDigest();
	}

	private void InitInputPath(String[] inputs) throws DFException {
		int index = 0;
		m_inputs = new File[inputs.length];

		for ( String in : inputs ) {
			File f = new File(in);
			try {
				GlobalFunc.ValidatePath(f, GlobalDef.FILE_READ);
			} catch ( IOException e ) {
				throw new DFException(e.toString());
			}

			m_inputs[index++] = f;
		}
	}

	private void InitOutput(String out) throws DFException {
		m_output = new DFOutput(out);
	}

	private void InitFileList() {
		m_listFiles = new ArrayList<FileInfo>();
	}

	private void InitDupMap() {
		m_mapListDup = new HashMap<String, ArrayList<FileInfo> >();
	}

	private void InitDigest() throws DFException {
		m_buffer = new byte[BUFFER_SIZE];

		try {
			m_msgDigest = MessageDigest.getInstance(ALGORITHM);
		} catch ( NoSuchAlgorithmException e ) {
			throw new DFException(e.toString());
		}
	}

	@Override
	public void run() {

		try {
			LoadFileList(m_inputs);
			FindDuplicate();
			PrintOut();
		} catch ( DFException e ) {
			m_win.MessageDialog("THREAD ERROR", e.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		} finally {
			m_win.SetEnabled(true);

			try {
				m_output.Close();
			} catch ( DFException e ) {
				m_win.MessageDialog("THREAD ERROR", e.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		m_win.MessageDialog("CONFIRM", "Check duplicate OK!", JOptionPane.INFORMATION_MESSAGE);
	}

	public void Start() {
		if ( m_thread == null ) {
			m_thread = new Thread(this);
			m_thread.start();
		}
	}

	public void Stop() {
		if ( m_thread.isAlive() ) {
			try {
				m_thread.join();
			} catch (InterruptedException e) {
				m_win.MessageDialog("THREAD ERROR", e.toString(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void LoadFileList(File[] list) throws DFException {
		if ( list != null ) {
			for ( File f : list ) {

				if ( f.isDirectory() ) {
					LoadFileList(f.listFiles());
				} else {
					FileInfo info = new FileInfo(f, GenerateFileAlgo(f));
					m_listFiles.add(info);
				}
			}
		}
	}

	@SuppressWarnings("resource")
	private String GenerateFileAlgo(File f) throws DFException {
		DigestInputStream dig_is = null;
		byte[]            digest = null;

		m_msgDigest.reset();
		try {
			dig_is = new DigestInputStream(new FileInputStream(f), m_msgDigest);
			while ( dig_is.read(m_buffer) > 0 );

			digest = dig_is.getMessageDigest().digest();
			dig_is.close();
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}

		return new BigInteger(1, digest).toString(16);
	}

	private void FindDuplicate() {
		for ( FileInfo info : m_listFiles) {
			if ( m_mapListDup.containsKey(info.GetAlgo()) ) {
				m_mapListDup.get(info.GetAlgo()).add(info);
			} else {
				ArrayList<FileInfo> list = new ArrayList<FileInfo>();
				list.add(info);
				m_mapListDup.put(info.GetAlgo(), list);
			}
		}
	}

	private void PrintOut() throws DFException {
		m_output.PrintDuplicateFile(m_mapListDup);
		m_output.PrintIntervalLine();
		m_output.PrintFileList(m_listFiles);
	}

}

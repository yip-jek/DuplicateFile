import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

public class Duplicater implements Runnable {

//	private static final String ALGORITHM_MD5    = "MD5";
	private static final String ALGORITHM_SHA256 = "SHA-256";
	private static final String CHARSET_OUTPUT   = "UTF-8";
	private static final int    BUFFER_SIZE      = 256 * 1024;

	private Thread        m_thread    = null;
	private DFWin         m_win       = null;
	private File[]        m_inputs    = null;
	private Writer        m_output    = null;
	private int           m_counter   = 0;
	private MessageDigest m_msgDigest = null;
	private byte[]        m_buffer    = null;

	public Duplicater(DFWin win, String[] in, String out) throws DFException {
		m_win = win;

		Init(in, out);
	}

	private void Init(String[] in, String out) throws DFException {
		InitInputPath(in);
		InitOutputPath(out);
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

	private void InitOutputPath(String out) throws DFException {
		File file = new File(out);

		try {
			if ( file.exists() ) {
				if ( !file.delete() ) {
					throw new DFException("Output: delete old file failed!");
				}
			}

			if ( !file.createNewFile() ) {
				throw new DFException("Output: create new file failed!");
			}

			FileOutputStream   out_stream = new FileOutputStream(file);
			OutputStreamWriter os_writer  = new OutputStreamWriter(out_stream, CHARSET_OUTPUT);
			m_output = new BufferedWriter(os_writer);
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

	private void InitDigest() throws DFException {
		m_buffer = new byte[BUFFER_SIZE];

		try {
			m_msgDigest = MessageDigest.getInstance(ALGORITHM_SHA256);
		} catch ( NoSuchAlgorithmException e ) {
			throw new DFException(e.toString());
		}
	}

	@Override
	public void run() {

		try {
			CheckFileList(m_inputs);
		} catch ( DFException e ) {
			m_win.MessageDialog("THREAD ERROR", e.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		} finally {
			m_win.SetEnabled(true);

			try {
				m_output.close();
			} catch ( IOException e ) {
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

	private int CheckFileList(File[] list) throws DFException {
		if ( list != null ) {
			for ( File f : list ) {
				StringBuilder str_buf = new StringBuilder();
				str_buf.append(++m_counter).append(") ").append(f.getPath());

				if ( f.isDirectory() ) {
					OutputInfo(str_buf.toString());

					CheckFileList(f.listFiles());
				} else {
					str_buf.append(", [SHA-256: ").append(GenerateFileAlgo(f)).append("]");
					OutputInfo(str_buf.toString());
				}
			}

			return list.length;
		}

		return 0;
	}

	private void OutputInfo(String info) throws DFException {
		try {
			m_output.write(info+"\n");
			m_output.flush();
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

	@SuppressWarnings("resource")
	private String GenerateFileAlgo(File f) throws DFException {
		DigestInputStream dig_is = null;

		m_msgDigest.reset();
		try {
			dig_is = new DigestInputStream(new FileInputStream(f), m_msgDigest);

			while ( dig_is.read(m_buffer) > 0 );
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}

		return new BigInteger(1, dig_is.getMessageDigest().digest()).toString(16);
	}

}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DFOutput {

	private static final char   CHAR_SEPARATOR = '-';
	private static final String CHARSET        = "UTF-8";

	private Writer m_writer = null;

	public DFOutput(String out) throws DFException {
		Init(out);
	}

	private void Init(String out) throws DFException {
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
			OutputStreamWriter os_writer  = new OutputStreamWriter(out_stream, CHARSET);
			m_writer = new BufferedWriter(os_writer);
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

	private void Write(String s) throws DFException {
		try {
			m_writer.write(s);
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

	private void Flush() throws DFException {
		try {
			m_writer.flush();
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

	public void Close() throws DFException {
		try {
			m_writer.close();
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

	public void PrintFileList(ArrayList<FileInfo> list) throws DFException {
		int           count = 0;
		StringBuilder buf   = new StringBuilder();

		Write("[THE FILE LIST]\n");

		for ( FileInfo info : list ) {
			buf.setLength(0);
			buf.append(++count).append(") ").append(info.GetInfo1()).append('\n');
			Write(buf.toString());
		}

		Flush();
	}

	public void PrintDuplicateFile(HashMap<String, ArrayList<FileInfo> > map_list) throws DFException {
		Write("[DUPLICATE FILE]\n");

		StringBuilder buf = new StringBuilder();
		for ( Map.Entry<String, ArrayList<FileInfo> > entry : map_list.entrySet() ) {
			ArrayList<FileInfo> list = entry.getValue();

			if ( list.size() > 1 ) {
				buf.setLength(0);
				buf.append("[").append(Duplicater.ALGORITHM).append("=").append(entry.getKey()).append("]\n");
				Write(buf.toString());

				int count = 0;
				for ( FileInfo info : list ) {
					buf.setLength(0);
					buf.append(++count).append("> ").append(info.GetInfo2()).append('\n');
					Write(buf.toString());
				}

				Write("\n");
			}
		}

		Flush();
	}

	public void PrintIntervalLine() throws DFException {
		StringBuilder buf = new StringBuilder("\n");
		for ( int i = 0; i < 80; ++i ) {
			buf.append(CHAR_SEPARATOR);
		}
		buf.append("\n");

		Write(buf.toString());
		Flush();
	}

}

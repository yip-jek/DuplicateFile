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

	private static final String CHARSET = "UTF-8";

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

	public void Close() throws DFException {
		try {
			m_writer.close();
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

	public void PrintFileList(ArrayList<FileInfo> list) throws DFException {
		final int LIST_SIZE = list.size();
		for ( int i = 0; i < LIST_SIZE; ++i ) {
			try {
				m_writer.write((i+1)+") "+list.get(i).GetInfo1());
				m_writer.flush();
			} catch (IOException e) {
				throw new DFException(e.toString());
			}
		}
	}

	public void PrintDuplicateFile(HashMap<String, ArrayList<FileInfo> > map_list) {
		for ( Map.Entry<String, ArrayList<FileInfo> > entry : map_list.entrySet() ) {
			;
		}
	}

	public void PrintIntervalLine() throws DFException {
		try {
			m_writer.write("\n");
			m_writer.write("------------------------------------------------------------");
			m_writer.write("\n");
			m_writer.flush();
		} catch ( IOException e ) {
			throw new DFException(e.toString());
		}
	}

}

import java.io.File;
import java.io.IOException;

public class GlobalFunc {

	public static void ValidatePath(File fp, int wr) throws IOException {
		// 是否存在？
		if ( !fp.exists() ) {
			throw new IOException("Non-existent file path: "+fp.getPath());
		}

		// 是否为目录？
		if ( !fp.isDirectory() ) {
			throw new IOException("Non-directory file path: "+fp.getPath());
		}

		// 是否可读？
		if ( ((wr & GlobalDef.FILE_READ) == GlobalDef.FILE_READ) && !fp.canRead() ) {
			throw new IOException("Can not read file path: "+fp.getPath());
		}

		// 是否可写？
		if ( ((wr & GlobalDef.FILE_WRITE) == GlobalDef.FILE_WRITE) && !fp.canWrite() ) {
			throw new IOException("Can not write file path: "+fp.getPath());
		}
	}

}

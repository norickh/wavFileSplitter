package test;

import java.io.File;

public class PathTool {
	
	static String get_currentpath() {

		String cp = System.getProperty("java.class.path");
		String fs = System.getProperty("file.separator");
		String acp = (new File(cp)).getAbsolutePath();
		int p, q;
		for (p = 0; (q = acp.indexOf(fs, p)) >= 0; p = q + 1)
			;

		return acp.substring(0, p);

	}

}

package in.sel.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class AppLogger {

	private static File file = null;

	private AppLogger() {

	}

	/** Writes simple log to text file */
	public static void writeLog(String msg) {

		File dir = new File(Environment.getExternalStorageDirectory() + "/AppLog");

		if (!dir.exists()) {
			boolean isCreated = dir.mkdir();
			if (isCreated) {

			}
		}

		if (file == null)
			file = new File(dir, "applog.txt");

		if (file != null && !file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter buffWriter = new BufferedWriter(fw);

			buffWriter.write("\r\n"+msg);
			buffWriter.flush();
			buffWriter.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Display toast message long time */
	public static void ToastLong(Context con, String msg) {
		Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
	}

	/** Display toast message short time */
	public static void ToastShort(Context con, String msg) {
		Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
	}

}

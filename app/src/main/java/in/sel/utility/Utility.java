package in.sel.utility;

import in.sel.logging.AppLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;

public class Utility {

	/** For start Time and End Time */
	long startTime;
	long endTime;

	private static String TAG = "Utility";

	public Utility() {
		/* start time analysis */
		startTime = System.currentTimeMillis();
	}

	/** Give end time Of Object */
	public String endTime(Utility gm) {
		endTime = System.currentTimeMillis();
		/* end time of process */

		long millis = endTime - gm.startTime;

		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		return hour + ":" + minute + ":" + second;
	}

	/** Return Time in Formated Manner */
	public String getTime(Utility gm) {
		endTime = System.currentTimeMillis();
		/* end time of process */

		long millis = endTime - gm.startTime;

		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60));

		return hour + ":" + minute + ":" + second;
	}

	/** GIVES THE TIME IN MILLI SECONDS */
	public static long getTimeInMillis() {
		final Calendar c = Calendar.getInstance();
		return (c.getTimeInMillis());
	}

	/** Convert Date from 24-4-2012 to 24-Apr-2012 */
	public String formatDate(String date) {

		if (date != null) {
			if (date.length() >= 10) {
				date = date.substring(0, 10);// String dateString =
												// "2001/03/09"; 2012-08-11
			}
			Date dt = new Date(date.replace("-", "/"));
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			date = formatter.format(dt);
		}
		return date;
	}

	/** Convert Date from 24-Apr-2012 to 24-4-2012 */
	public Calendar formatDateCalender(String date) {
		Calendar cal = Calendar.getInstance();
		try {

			cal.setTime(new SimpleDateFormat("dd-MMM-yyyy").parse(date));
		} catch (ParseException e) {
			if (AppConstants.DEBUG)
				Log.e("ValidationControl", e.toString()
						+ "--> Not Parsing date");
		}
		return cal;
	}

	/**
	 * Validate Start Date Should not be grater than End date
	 * 
	 * @param Startdate
	 *            Identifiy Start Date
	 * @param Enddate
	 *            Identifiy End Date
	 */
	public boolean dateValidate(String Startdate, String Enddate) {
		if (Startdate != null && Enddate != null) {

			Calendar cal_strtdate = Calendar.getInstance();
			Calendar cal_enddate = Calendar.getInstance();

			cal_strtdate.setTime(new Date(Startdate.replace("-", "/")));
			cal_enddate.setTime(new Date(Enddate.replace("-", "/")));

			if (cal_strtdate.after(cal_enddate))
				return false;
		}
		return true;
	}

	public static String getCurrentDate() {
		Calendar month = Calendar.getInstance();
		int mYear = month.get(Calendar.YEAR);
		int mMonth = month.get(Calendar.MONTH) + 1;
		int mDay = month.get(Calendar.DAY_OF_MONTH);
		String date = mYear + "-" + mMonth + "-" + mDay;
		return date;
	}

	public static String getCurrentTime() {
		Calendar time = Calendar.getInstance();
		int hr = time.get(Calendar.HOUR);
		int mnt = time.get(Calendar.MINUTE);

		String date = hr + ":" + mnt;
		return date;
	}

	/** Write file Using file path and byte code */
	public static String writeFile(File file, String strByte) {
		try {
			if (file.exists()) {
				file.delete();
			}

			FileOutputStream out = new FileOutputStream(file);
			byte[] fbyt = Base64.decode(strByte, 0);
			out.write(fbyt);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			AppLogger.writeLog(TAG + e.toString());
			if (AppConstants.DEBUG)
				Log.e(TAG, e.toString() + "error<-- in getting");
			return e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			AppLogger.writeLog(TAG + e.toString());
			if (AppConstants.DEBUG)
				Log.e(TAG, e.toString() + "error<-- in getting");
			return e.toString();
		}

		return file.getPath();
	}

	/* make image rounded */
	public static Bitmap getRoundedRectBitmap(Bitmap myBitmap, int dim,
			int margin) {
		Bitmap result = null;
		try {
			Bitmap bitmap = Bitmap.createScaledBitmap(myBitmap, dim, dim, true);
			result = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);
			// int color = 0xFFFFFFFF;

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.WHITE);
			paint.setFilterBitmap(true);

			Canvas canvas = new Canvas(result);
			canvas.drawARGB(0, 255, 255, 255);
			canvas.drawCircle(dim / 2, dim / 2, ((dim / 2) - margin), paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			Rect rect = new Rect(0, 0, dim, dim);
			canvas.drawBitmap(bitmap, null, rect, paint);

		} catch (NullPointerException e) {
			AppLogger.writeLog(TAG + " " + e.toString());
			Log.e(TAG, e.toString());
		} catch (OutOfMemoryError o) {
			AppLogger.writeLog(TAG + " " + o.toString());
			Log.e(TAG, o.toString());
		}
		return result;
	}

}

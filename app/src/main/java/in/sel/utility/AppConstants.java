package in.sel.utility;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class AppConstants {
	/** */
	public static final int SIMULATED_REFRESH_LENGTH = 2000;

	/**
	 * If DEBUG is FALSE then No Need to write Log File <strong>If Developer is
	 * in Debug Mode then he should change this variable to true otherwise make
	 * it false at the time of installation<strong>
	 */
	public static boolean DEBUG = true;

	/** It Will Decide Service Download For Module */
	public static boolean IS_MODULE = false;

	/** It will give info whether service is running or not */
	public static boolean IS_SERVICE_RUNNING = false;

	/** Use In Service for Retrieving all running Module */
	public static String APP_BASE_PACKAGE = "in.sel.indianbabyname";

	public static int sort = 0;

	public static final long ANIM_DURATION = 1000;

	public final static AccelerateInterpolator ACCELERATE = new AccelerateInterpolator();
	public final static AccelerateDecelerateInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
	public final static DecelerateInterpolator DECELERATE = new DecelerateInterpolator();

}

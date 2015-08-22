package in.sel.indianbabyname;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import in.sel.dbhelper.DBHelper;
import in.sel.fragment.FragmentAlphabetHome;
import in.sel.fragment.FragmentNaviDrawer;
import in.sel.logging.AppLogger;
import in.sel.utility.AppConstants;

public class ActivityAlphabetMain extends AppCompatActivity {

    public static final String SELECTED_ALPHA_BET = "alpha";
    public static final String TAG = "ActivityMain";

    //ListView lsView;

    private Toolbar toolbar;

    public static int centerX;
    public static int centerY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        centerX = (int) event.getX();
        centerY = (int) event.getY();

        //Toast.makeText(this, centerX + " " + centerY, Toast.LENGTH_LONG).show();

        return super.dispatchTouchEvent(event);
    }

    /**
     * Initialize Variable and list
     */
    private void init(Bundle savedInstanceState) {

        toolbar = (Toolbar) findViewById(R.id.tb_app_bar);
        setSupportActionBar(toolbar);

        /** */
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //lsView = (ListView) findViewById(R.id.lv_left_drawer);

        /** */
//        FragmentNaviDrawer fragNaviDrawer = (FragmentNaviDrawer) getSupportFragmentManager().findFragmentById(R.id.frag_navi_container);
//        fragNaviDrawer.setUp(R.id.frag_navi_container, (DrawerLayout) findViewById(R.id.dl_drawer_layout), toolbar);

        /** */
//        ListView lsView = (ListView) findViewById(R.id.lv_left_drawer);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.drawer_menu,
//                android.R.layout.simple_list_item_1);
//        lsView.setAdapter(adapter);
//
//        lsView.setOnItemClickListener(new DrawerListListener());

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            display(0);
        }

    }

//    /* Called whenever we call invalidateOptionsMenu() */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(lsView);
//        menu.findItem(R.id.action_database_copy).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }


//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        fragNaviDrawer.mActionBarDrawerToggle.syncState();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

		/* Inflate the menu; this adds items to the action bar if it is present */
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_about:
                Intent in = new Intent(this, ActivityAbout.class);
                startActivity(in);
                break;

            case R.id.action_database_copy:
                writeDataBase();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * display
     */
    public void display(int position) {
        /** */
        if (position == 0 && getSupportFragmentManager().findFragmentByTag("FragAlpha") == null) {
            FragmentAlphabetHome fragAlpha = FragmentAlphabetHome.getInstance();
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            fragTrans.replace(R.id.fl_container, fragAlpha, "FragAlpha");
            fragTrans.commit();
        }

        //   mDrawerLayout.closeDrawer(lsView);
    }

    /* */
    public void writeDataBase() {
        String dataBase = getApplicationInfo().dataDir + DBHelper.DB_SUFFIX + DBHelper.DB_NAME;
        File f = new File(dataBase);
        InputStream fis = null;
        OutputStream fos = null;

        try {

            fis = new FileInputStream(f);
            fos = new FileOutputStream("/mnt/sdcard/Download/" + DBHelper.DB_NAME);

            byte buffer[] = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();

            if (AppConstants.DEBUG)
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            AppLogger.writeLog(TAG + " -- " + e.toString());
            Log.e("", e.toString());
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException ioe) {
                Log.e("", ioe.toString());
            }
        }
    }

}

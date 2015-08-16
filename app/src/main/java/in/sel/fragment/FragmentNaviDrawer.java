package in.sel.fragment;

/**
 * Created by Lokesh on 01-06-2015.
 */
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sel.indianbabyname.R;
import in.sel.utility.AppUtility;


public class FragmentNaviDrawer extends Fragment {

    /**
     * Shared Preference Name and Key
     */
    private static final String PREF_KEY_STORE_IS_LEARNED = "is_user_learned";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    public  ActionBarDrawerToggle mActionBarDrawerToggle;

    /* to make user learning track for Navigation Drawer*/
    private boolean isUserLearned;
    private boolean isSavedInstanceState;


    /* */
    private View containerView;

    public FragmentNaviDrawer() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isSavedInstanceState = savedInstanceState != null ? true : false;
        isUserLearned = Boolean.valueOf(AppUtility.readFromPreferences(getActivity(), PREF_KEY_STORE_IS_LEARNED, "false"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navi_drawer, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;
        containerView = getActivity().findViewById(fragmentId);

        /** */
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                /* mark as user Learned Navigation Drawer*/
                if (!isUserLearned) {
                    isUserLearned = true;
                    AppUtility.saveToPreferences(getActivity(), PREF_KEY_STORE_IS_LEARNED, "true");
                }

                /* Invalidate to means draw option menu again*/
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                 /* Invalidate to means draw option menu again*/
                getActivity().invalidateOptionsMenu();
            }

//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                //L.log(getActivity(), slideOffset + "");
//
//                /* To adjust aplha of toolbar*/
//                if (slideOffset < 0.6)
//                    toolbar.setAlpha(1 - slideOffset);
//            }
        };

        /* If it is very first then it should be visible*/
        if (!isUserLearned && !isSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }

        /* Attach listener to Drawer*/
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        /* Sync Drawerlayout with Hamburger Icon*/
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}
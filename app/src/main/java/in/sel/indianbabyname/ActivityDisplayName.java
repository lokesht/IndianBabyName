package in.sel.indianbabyname;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.sel.adapter.NameRecycleViewAdapter;
import in.sel.customview.CustomDividerItemDecoration;
import in.sel.dbhelper.DBHelper;
import in.sel.dbhelper.TableContract;
import in.sel.framework.SimpleAnimationListener;
import in.sel.framework.HidingScrollListener;
import in.sel.logging.AppLogger;
import in.sel.model.EngNameAsc;
import in.sel.model.FreNameAsc;
import in.sel.model.HinNameAsc;
import in.sel.model.M_Name;
import in.sel.model.SortingValueHolder;
import in.sel.utility.AppConstants;
import in.sel.utility.L;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Class is designed for Developer For Marking of Name
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ActivityDisplayName extends AppCompatActivity {
    private String TAG = getClass().getName();

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    /** */
    private static String selectedAlphabet = "";

    /** */
    private DBHelper dbHelper;

    /**
     * Parent View
     */
    private View bgViewGroup;
    private View viewContainer;
    private View viewBelowActionBar;
    private CardView cardViewSearch;

    private int centerX;
    private int centerY;

    private List<M_Name> lsName;

    /**
     * Search Edit Box Edit
     */
    private EditText editText;
    private NameRecycleViewAdapter nameRecycleViewAdapter;

    private boolean isSearchOpen = false;
    private BottomSheetLayout bottomSheet;
    private View bottomSheetView;

    private SortingValueHolder mSortingValueHolder;
    private SortingValueHolder mSecondarySortingValueHolder;
    private List<M_Name> visibleObjects;
    private FloatingActionButton mFabActionButton;
    private List<Integer> mWishList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {/**/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list_recycle_view);

        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        centerX = (int) event.getX();
        centerY = (int) event.getY();

        if (AppConstants.DEBUG)
            L.log(this, centerX + " " + centerY);
        return super.dispatchTouchEvent(event);
    }

    private void init() {

        /* Default Object*/
        mSortingValueHolder = new SortingValueHolder(true);

        /** */
        setupLayout();
        setupWindowAnimations();

        /** Set Up Toolbar*/
        toolbar = (Toolbar) findViewById(R.id.tb_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        /** This is will select only elligible Name */
        selectedAlphabet = getIntent().getStringExtra(ActivityAlphabetMain.SELECTED_ALPHA_BET);

        String where = null;
        where = getNameSelectionQuery();

        Cursor c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[]{TableContract.Name.AUTO_ID,
                TableContract.Name.NAME_EN, TableContract.Name.NAME_MA, TableContract.Name.NAME_FRE,
                TableContract.Name.GENDER_CAST}, where);

        if (c != null && c.getCount() > 0) {
            if (AppConstants.DEBUG)
                AppLogger.ToastLong(this, c.getCount() + "");

            lsName = parseListName(c);
            displayList(lsName);

			/* */
            TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
            String s = String.format(getResources().getString(R.string.lable_total_cout), c.getCount());
            tvTotal.setText(s);
        }

        /** This will find all wishlist of User*/
        c = dbHelper.getTableValue(TableContract.FavourateName.TABLE_NAME, new String[]{
                TableContract.FavourateName.NAME_ID}, null);
        mWishList = parseWishList(c);

        bottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheet);

        /**
         * Implement Text Watcher
         */
        onSearchListener();

    }

    public void showBottomSheet() {
        if (isSearchOpen)
            hideSearchBar(cardViewSearch);

        if (bottomSheetView == null) {
            bottomSheetView = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_filter, bottomSheet, false);
            sortAndFilter();
        }

        mSecondarySortingValueHolder = new SortingValueHolder(mSortingValueHolder);
        bottomSheet.showWithSheetView(bottomSheetView);
    }

    /* */
    private void onDoneSort() {
        /** */
        if (!mSecondarySortingValueHolder.equals(mSortingValueHolder)) {
            if (!mSecondarySortingValueHolder.getSortingColumn().equals(mSortingValueHolder.getSortingColumn()))
                switch (mSecondarySortingValueHolder.getSortingColumn()) {
                    case TableContract.Name.NAME_EN:
                        Collections.sort(lsName, new EngNameAsc());
                        break;

                    case TableContract.Name.NAME_MA:
                        Collections.sort(lsName, new HinNameAsc());
                        break;

                    case TableContract.Name.NAME_FRE:
                        Collections.sort(lsName, new FreNameAsc());
                        break;
                }

            visibleObjects = new ArrayList<>();
            List<Integer> test = mSecondarySortingValueHolder.getGenderCategory();

            for (M_Name item : lsName) {
                String temp = item.getGender_cast();

                if (test.contains(Integer.parseInt(temp)))
                    visibleObjects.add(item);
            }

            nameRecycleViewAdapter.setSort(visibleObjects);
            mSecondarySortingValueHolder = mSortingValueHolder;
        }
        bottomSheet.dismissSheet();
    }

    private void sortAndFilter() {
        Button btn = (Button) bottomSheetView.findViewById(R.id.btn_done);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneSort();
            }
        });

        CheckBox cbMale = (CheckBox) bottomSheetView.findViewById(R.id.cb_male);
        CheckBox cbFemale = (CheckBox) bottomSheetView.findViewById(R.id.cb_female);
        CheckBox cbTransGender = (CheckBox) bottomSheetView.findViewById(R.id.cb_transgender);

        cbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSecondarySortingValueHolder.getGenderCategory().remove(0);
                if (isChecked) {
                    mSecondarySortingValueHolder.getGenderCategory().add(0, 0);
                    // L.log(ActivityDisplayName.this, mSecondarySortingValueHolder.getGenderCategory().size() + " " + mSortingValueHolder.getGenderCategory().size());
                } else {
                    mSecondarySortingValueHolder.getGenderCategory().add(0, -1);
                    // L.log(ActivityDisplayName.this,mSecondarySortingValueHolder.getGenderCategory().size() + " " + mSortingValueHolder.getGenderCategory().size());
                }

            }
        });

        cbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSecondarySortingValueHolder.getGenderCategory().remove(1);
                if (isChecked) {
                    // L.log(ActivityDisplayName.this,mSecondarySortingValueHolder.getGenderCategory().size() + " " + mSortingValueHolder.getGenderCategory().size());
                    mSecondarySortingValueHolder.getGenderCategory().add(1, 1);
                } else {
                    mSecondarySortingValueHolder.getGenderCategory().add(1, -1);
                    // L.log(ActivityDisplayName.this, mSecondarySortingValueHolder.getGenderCategory().size() + " " + mSortingValueHolder.getGenderCategory().size());
                }
            }
        });

        cbTransGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSecondarySortingValueHolder.getGenderCategory().remove(2);

                if (isChecked) {
                    mSecondarySortingValueHolder.getGenderCategory().add(2, 2);
                } else {
                    mSecondarySortingValueHolder.getGenderCategory().add(2, -1);
                }
            }
        });

        //sortingColumn
        RadioGroup radioGroup = (RadioGroup) bottomSheetView.findViewById(R.id.rg_sorting);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_eng_name:
                        mSecondarySortingValueHolder.setSortingColumn(TableContract.Name.NAME_EN);
                        break;
                    case R.id.rb_hind_name:
                        mSecondarySortingValueHolder.setSortingColumn(TableContract.Name.NAME_MA);
                        break;
                    case R.id.rb_frequency:
                        mSecondarySortingValueHolder.setSortingColumn(TableContract.Name.NAME_FRE);
                        break;
                }
            }
        });
    }

    /* */
    private String getNameSelectionQuery() {
        String where = TableContract.Name.GENDER_CAST + " IN(0,1,2) " + " AND " +
                TableContract.Name.NAME_EN + " like '" + selectedAlphabet + "%' ORDER BY " + TableContract.Name.NAME_FRE + " DESC";

        return where;
    }

    /* */
    private void onSearchListener() {
        editText = (EditText) findViewById(R.id.et_search_box);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query(s.toString());
            }
        });
    }

    private void query(String finding) {
        if (finding.length() > 0) {
            if (visibleObjects != null && visibleObjects.size() > 0)
                nameRecycleViewAdapter.setFilter(finding.toLowerCase(), visibleObjects);
            else
                nameRecycleViewAdapter.setFilter(finding.toLowerCase(), lsName);
        } else {
            nameRecycleViewAdapter.flushFilter();
        }
    }

    private void setupLayout() {
        bgViewGroup = findViewById(R.id.view_cover_view);
        viewContainer = findViewById(R.id.container);
        cardViewSearch = (CardView) findViewById(R.id.cv_search);
        viewBelowActionBar = findViewById(R.id.ll_below_actionbar);

        mFabActionButton = (FloatingActionButton) findViewById(R.id.fab_bottom);
        mFabActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
    }


    private void setupWindowAnimations() {
        setupEnterAnimations();
    }

    private void setupEnterAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bgViewGroup.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    //toggleInformationView(view);
                    animateRevealShow(bgViewGroup);
                }
            });
        } else {

        }
    }

    /**
     * Activity Launch Animation
     */
    private void animateRevealShow(View viewRoot) {
        viewRoot.setVisibility(View.VISIBLE);

        // int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        // int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;

        int cx = ActivityAlphabetMain.centerX;
        int cy = ActivityAlphabetMain.centerY;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        anim.setInterpolator(AppConstants.ACCELERATE);
        anim.setDuration(AppConstants.ANIM_DURATION / 2);


        anim.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onRaisMenu();
                super.onAnimationEnd(animation);
            }
        });

        anim.start();
    }

    private void onRaisMenu() {
        viewContainer.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(bgViewGroup, "bottom", bgViewGroup.getBottom(), bgViewGroup.getTop() + getSupportActionBar().getHeight() + viewBelowActionBar.getHeight());
        objectAnimator.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                bgViewGroup.setVisibility(View.GONE);

                appearFab();
            }
        });
        objectAnimator.setInterpolator(AppConstants.ACCELERATE_DECELERATE);
        objectAnimator.setDuration(AppConstants.ANIM_DURATION / 2);
        objectAnimator.start();
    }

    private void appearFab() {

        mFabActionButton.setVisibility(View.VISIBLE);

//        int cx = (mFabActionButton.getLeft() + mFabActionButton.getRight()) / 2;
//        int cy = (mFabActionButton.getTop() + mFabActionButton.getBottom()) / 2;
//
//        int finalRadius = Math.max(mFabActionButton.getWidth(), mFabActionButton.getHeight());
//
//        if (AppConstants.DEBUG)
//            L.log(this, cx + " " + cy + " " + mFabActionButton.getWidth()+" "+finalRadius);
//
//        Animator anim = ViewAnimationUtils.createCircularReveal(mFabActionButton, 481, 815, 0, finalRadius);
//        anim.setInterpolator(AppConstants.DECELERATE);
//
//        anim.setDuration(10000);
//        anim.addListener(new SimpleAnimationListener() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//
//            }
//        });
//        anim.start();
    }

    private void animateRevealHide(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int initialRadius = viewRoot.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewRoot.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(AppConstants.ANIM_DURATION);
        anim.start();
    }

    /** */
    public void displayList(List<M_Name> name) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_frequency_list);
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(this, null));

        nameRecycleViewAdapter = new NameRecycleViewAdapter(this, name, mWishList);
        final VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);

        /* Connect the recycler to the scroller (to let the scroller scroll the list)*/
        fastScroller.setRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(fastScroller.getOnScrollListener());
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideFloatingViews();
            }

            @Override
            public void onShow() {
                showFloatingViews();
            }
        });

        setRecyclerViewLayoutManager(recyclerView);
        recyclerView.setAdapter(nameRecycleViewAdapter);
    }

    private void hideFloatingViews() {
        // viewBelowActionBar.animate().translationY(-viewBelowActionBar.getHeight()*2).setInterpolator(AppConstants.ACCELERATE);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabActionButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabActionButton.animate().translationY(mFabActionButton.getHeight() + fabBottomMargin).setInterpolator(AppConstants.ACCELERATE);
    }

    private void showFloatingViews() {
        // viewBelowActionBar.animate().translationY(0).setInterpolator(AppConstants.DECELERATE);
        mFabActionButton.animate().translationY(0).setInterpolator(AppConstants.DECELERATE);
    }

    /**
     * Set RecyclerView's LayoutManager
     */
    private void setRecyclerViewLayoutManager(RecyclerView recyclerView) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition =
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    /** */
    private List<M_Name> parseListName(Cursor c) {
        List<M_Name> lsName = new ArrayList<M_Name>();
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                int id = c.getInt(c.getColumnIndex(TableContract.Name.AUTO_ID));

                String en = c.getString(c.getColumnIndex(TableContract.Name.NAME_EN));
                String ma = c.getString(c.getColumnIndex(TableContract.Name.NAME_MA));
                int fre = c.getInt(c.getColumnIndex(TableContract.Name.NAME_FRE));

                String s = c.getString(c.getColumnIndex(TableContract.Name.GENDER_CAST));

				/* Considering default value as -1 */
                String desc = "-1";
                if (s != null && s.length() > 0) {

                    /* For First Release Avoiding Transgender*/
                    if (s.equals("2"))
                        s = "1";

                    desc = s;
                }

                M_Name temp = new M_Name(ma, en, fre, id, desc);
                lsName.add(temp);
            } while (c.moveToNext());

            /** Close database */
            c.close();
        }
        return lsName;
    }

    private List<Integer> parseWishList(Cursor c) {

        if (c != null && c.moveToFirst()) {
            do {
                Integer in = c.getInt(0);
                mWishList.add(in);
            } while (c.moveToNext());
            c.close();
        }
        return mWishList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

		/* Inflate the menu; this adds items to the action bar if it is present */
        getMenuInflater().inflate(R.menu.menu_display_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_search:
                showSearchBar(cardViewSearch, centerX, centerY);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* On Collapse Click Listener*/
    public void onCollapseSearch(View view) {
        hideSearchBar(cardViewSearch);
    }

    /**
     * Search bar animation Constructer
     */
    private void showSearchBar(View myView) {
        showSearchBar(myView, 0, 0);
    }

    /**
     * Animate to show Search bar
     */
    private void showSearchBar(View myView, int cx, int cy) {
        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        isSearchOpen = true;

        // get the center for the clipping circle

        if (cx == 0 || cy == 0) {
            cx = (myView.getLeft() + myView.getRight());
            cy = (myView.getTop() + myView.getBottom());
        }

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        anim.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationStart(Animator animation) {

//                ObjectAnimator obj = ObjectAnimator.ofInt(viewBelowActionBar,"bottom",viewBelowActionBar.getBottom(),viewBelowActionBar.getTop());
//                obj.start();
//                super.onAnimationStart(animation);
            }
        });


        anim.start();
    }

    /**
     * Animate to show Search bar
     */
    private void hideSearchBar(final View myView) {
        /** Refresh*/
        isSearchOpen = false;
        editText.setText("");

        // get the center for the clipping circle
        int cx = (myView.getLeft() + myView.getRight());
        int cy = (myView.getTop() + myView.getBottom());

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0);

        // make the view visible and start the animation
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
//                ObjectAnimator obj = ObjectAnimator.ofInt(viewBelowActionBar, "bottom", viewBelowActionBar.getTop(), viewBelowActionBar.getBottom());
//                obj.start();
//                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpen) {
            hideSearchBar(cardViewSearch);
        } else {

            super.onBackPressed();
        }
    }

    //    @Override
//    public void onClick(View v) {
//        String where = "";
//        Cursor c = null;
//
//        switch (v.getId()) {
//            case R.id.tvFrequency:
//
//                /** This is will select only those which are not marked */
//                where = TableContract.Name.NAME_EN + " like '" + selectedAlphabet + "%' AND " + TableContract.Name.GENDER_CAST
//                        + " = ''" + " ORDER BY " + TableContract.Name.NAME_FRE + " DESC";
//
//                c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[]{TableContract.Name.AUTO_ID,
//                        TableContract.Name.NAME_EN, TableContract.Name.NAME_MA, TableContract.Name.NAME_FRE,
//                        TableContract.Name.GENDER_CAST}, where);
//
//
//                break;
//
//            case R.id.tvEnglish:
//
//
//                /** This is will select only those which are not marked */
//                where = TableContract.Name.NAME_EN + " like '" + selectedAlphabet + "%' AND " + TableContract.Name.GENDER_CAST
//                        + " = ''" + " ORDER BY " + TableContract.Name.NAME_EN + " ASC";
//
//                c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[]{TableContract.Name.AUTO_ID,
//                        TableContract.Name.NAME_EN, TableContract.Name.NAME_MA, TableContract.Name.NAME_FRE,
//                        TableContract.Name.GENDER_CAST}, where);
//
//
//                break;
//
//            case R.id.tvHindi:
//                /** This is will select only those which are not marked */
//                where = TableContract.Name.NAME_EN + " like '" + selectedAlphabet + "%' AND " + TableContract.Name.GENDER_CAST
//                        + " = ''" + " ORDER BY " + TableContract.Name.NAME_MA + " ASC";
//
//                c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[]{TableContract.Name.AUTO_ID,
//                        TableContract.Name.NAME_EN, TableContract.Name.NAME_MA, TableContract.Name.NAME_FRE,
//                        TableContract.Name.GENDER_CAST}, where);
//
//
//                break;
//        }
//
//    }

}
package in.sel.indianbabyname;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewAnimationUtils;


import java.util.ArrayList;
import java.util.List;

import in.sel.adapter.FavouriteNameRecycleViewAdapter;
import in.sel.customview.MarginDecoration;
import in.sel.dbhelper.DBHelper;
import in.sel.dbhelper.TableContract;
import in.sel.framework.OnItemRemoveListener;
import in.sel.framework.SimpleAnimationListener;
import in.sel.logging.AppLogger;
import in.sel.model.M_Name;
import in.sel.utility.AppConstants;

/**
 * Class is designed for Developer For Marking of Name
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ActivityWishListDisplayName extends AppCompatActivity implements OnItemRemoveListener {
    private String TAG = getClass().getName();

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    /** */
    private DBHelper dbHelper;

    private List<M_Name> lsName;
    private View bgViewGroup;
    private View viewContainer;

    /**
     * Search Edit Box Edit
     */
    private FavouriteNameRecycleViewAdapter mNameRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {/**/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_recycle_view);

        initialize();
    }

    private void initialize() {

        setupWindowAnimations();

        /** Set Up Toolbar*/
        toolbar = (Toolbar) findViewById(R.id.tb_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        String where = null;
        Cursor c = dbHelper.getTableValue(TableContract.FavourateName.TABLE_NAME, new String[]{TableContract.FavourateName.AUTO_ID,
                TableContract.FavourateName.NAME_EN, TableContract.FavourateName.NAME_MA, TableContract.FavourateName.NAME_FRE, TableContract.FavourateName.NAME_ID,
                TableContract.FavourateName.GENDER_CAST}, where);

        if (c != null && c.getCount() > 0) {
            if (AppConstants.DEBUG)
                AppLogger.ToastLong(this, c.getCount() + "");

            lsName = parseListName(c);
            displayList(lsName);
        }

//        /** This will find all wishlist of User*/
//        c = dbHelper.getTableValue(TableContract.FavourateName.TABLE_NAME, new String[]{
//                TableContract.FavourateName.NAME_ID}, null);
//        mWishList = parseWishList(c);

    }

    private void setupWindowAnimations() {
        setupEnterAnimations();
    }

    private void setupEnterAnimations() {
        viewContainer = findViewById(R.id.container);
        bgViewGroup = findViewById(R.id.view_cover_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bgViewGroup.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    animateRevealShow(bgViewGroup);
                }
            });
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
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(bgViewGroup, "bottom", bgViewGroup.getBottom(), bgViewGroup.getTop() + getSupportActionBar().getHeight());
        objectAnimator.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                bgViewGroup.setVisibility(View.GONE);
            }
        });
        objectAnimator.setInterpolator(AppConstants.ACCELERATE_DECELERATE);
        objectAnimator.setDuration(AppConstants.ANIM_DURATION / 2);
        objectAnimator.start();
    }

    /** */
    public void displayList(List<M_Name> name) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_frequency_list);
        recyclerView.addItemDecoration(new MarginDecoration(this, getResources().getDimensionPixelOffset(R.dimen.small_margin)));

        // ItemTouchHelper.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mNameRecycleViewAdapter = new FavouriteNameRecycleViewAdapter(this, name,this);
        recyclerView.setAdapter(mNameRecycleViewAdapter);

        setRecyclerViewLayoutManager();
    }

    /**
     * Set RecyclerView's LayoutManager
     */
    private void setRecyclerViewLayoutManager() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                mNameRecycleViewAdapter.remove(viewHolder.getLayoutPosition());
                setupSnakBar();
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupSnakBar() {
        final View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                mNameRecycleViewAdapter.addUndo();
            }
        };

        final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
        Snackbar
                .make(coordinatorLayoutView, R.string.msg_item_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.btn_undo, clickListener)
                .show();
    }

    /** */
    private List<M_Name> parseListName(Cursor c) {
        List<M_Name> lsName = new ArrayList<M_Name>();
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                //int id = c.getInt(c.getColumnIndex(TableContract.FavourateName.AUTO_ID));
                int id = c.getInt(c.getColumnIndex(TableContract.FavourateName.NAME_ID));

                String en = c.getString(c.getColumnIndex(TableContract.FavourateName.NAME_EN));
                String ma = c.getString(c.getColumnIndex(TableContract.FavourateName.NAME_MA));
                int fre = c.getInt(c.getColumnIndex(TableContract.FavourateName.NAME_FRE));

                String s = c.getString(c.getColumnIndex(TableContract.FavourateName.GENDER_CAST));

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

//    private List<Integer> parseWishList(Cursor c) {
//
//        if (c != null && c.moveToFirst()) {
//            do {
//                Integer in = c.getInt(0);
//                mWishList.add(in);
//            } while (c.moveToNext());
//            c.close();
//        }
//        return mWishList;
//    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null)
            dbHelper.close();
        super.onDestroy();
    }

    @Override
    public void onRemove(int position) {
        mNameRecycleViewAdapter.remove(position);
        setupSnakBar();
    }
}
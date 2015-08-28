package in.sel.indianbabyname;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import in.sel.adapter.FavouriteNameRecycleViewAdapter;
import in.sel.dbhelper.DBHelper;
import in.sel.dbhelper.TableContract;
import in.sel.framework.SimpleAnimationListener;
import in.sel.logging.AppLogger;
import in.sel.model.M_Name;
import in.sel.utility.AppConstants;

/**
 * Class is designed for Developer For Marking of Name
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ActivityWishListDisplayName extends AppCompatActivity {
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
    private FavouriteNameRecycleViewAdapter nameRecycleViewAdapter;

    private List<Integer> mWishList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {/**/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_recycle_view);

        init();
    }

    private void init() {

        setupWindowAnimations();

        /** Set Up Toolbar*/
        toolbar = (Toolbar) findViewById(R.id.tb_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        String where = null;
        Cursor c = dbHelper.getTableValue(TableContract.FavourateName.TABLE_NAME, new String[]{TableContract.FavourateName.AUTO_ID,
                TableContract.FavourateName.NAME_EN, TableContract.FavourateName.NAME_MA, TableContract.FavourateName.NAME_FRE,
                TableContract.FavourateName.GENDER_CAST}, where);

        if (c != null && c.getCount() > 0) {
            if (AppConstants.DEBUG)
                AppLogger.ToastLong(this, c.getCount() + "");

            lsName = parseListName(c);
            displayList(lsName);
        }

        /** This will find all wishlist of User*/
        c = dbHelper.getTableValue(TableContract.FavourateName.TABLE_NAME, new String[]{
                TableContract.FavourateName.NAME_ID}, null);
        mWishList = parseWishList(c);

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
        //recyclerView.addItemDecoration(new CustomDividerItemDecoration(this, null));


        // ItemTouchHelper.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        nameRecycleViewAdapter = new FavouriteNameRecycleViewAdapter(this, name, mWishList);
        recyclerView.setAdapter(nameRecycleViewAdapter);

        setRecyclerViewLayoutManager();
    }

    /**
     * Set RecyclerView's LayoutManager
     */
    private void setRecyclerViewLayoutManager() {

        final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new RecyclerViewAdapter(recyclerView),
                        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerViewAdapter view, int position) {
                                nameRecycleViewAdapter.remove(position);
                            }
                        });

        recyclerView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        recyclerView.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
        recyclerView.addOnItemTouchListener(new SwipeableItemClickListener(this,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.txt_delete) {
                            touchListener.processPendingDismisses();
                        } else if (view.getId() == R.id.txt_undo) {
                            touchListener.undoPendingDismiss();
                        } else { // R.id.txt_data
                            Toast.makeText(ActivityWishListDisplayName.this, "Position " + position, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));



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
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
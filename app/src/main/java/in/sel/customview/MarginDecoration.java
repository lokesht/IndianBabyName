package in.sel.customview;

/**
 * Created by Lokesh on 08-08-2015.
 */

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import in.sel.indianbabyname.R;

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public MarginDecoration(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.margin_small);
    }

    public MarginDecoration(Context context,int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}
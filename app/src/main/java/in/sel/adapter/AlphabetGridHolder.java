package in.sel.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.sel.indianbabyname.R;

/**
 * Created by Lokesh on 05-08-2015.
 */
public class AlphabetGridHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView tvAlpha;
    TextView tvCount;

    public AlphabetGridHolder(View itemView, boolean isFavourite) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cv);
        if (!isFavourite)
            tvAlpha = (TextView) itemView.findViewById(R.id.tv_alphabet);
        tvCount = (TextView) itemView.findViewById(R.id.tv_count);
    }
}

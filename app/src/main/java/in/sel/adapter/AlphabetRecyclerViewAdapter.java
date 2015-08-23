package in.sel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sel.framework.OnAlphabetListener;
import in.sel.indianbabyname.R;
import in.sel.model.M_AlphaCount;

/**
 * Created by Lokesh on 05-08-2015.
 */
public class AlphabetRecyclerViewAdapter extends RecyclerView.Adapter<AlphabetGridHolder> {

    private static final int TYPE_FAVOURATE = 2;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private SparseArray array;
    private OnAlphabetListener onAlphabetListener;

    public AlphabetRecyclerViewAdapter(Context context, SparseArray array, OnAlphabetListener onAlphabetListener) {
        mContext = context;
        this.array = array;
        this.onAlphabetListener = onAlphabetListener;
    }

    @Override
    public AlphabetGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        boolean isFavourite = false;
        if (viewType == TYPE_ITEM) {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_alphabet, parent, false);
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_alphabet_favourate, parent, false);
            isFavourite = true;
        }
        AlphabetGridHolder pvh = new AlphabetGridHolder(v,isFavourite);
        return pvh;
    }

    @Override
    public void onBindViewHolder(AlphabetGridHolder holder, final int position) {

        M_AlphaCount m_alphaCount = (M_AlphaCount) array.get(position);

        if(!isPositionFavourite(position)) {
            holder.tvAlpha.setText(m_alphaCount.getAlphabet());
        }
        holder.tvCount.setText(m_alphaCount.getCount() + "");
        holder.cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                onAlphabetListener.onAlphaClickListener(v, (M_AlphaCount) array.get(position));
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return isPositionFavourite(position) ? TYPE_FAVOURATE : TYPE_ITEM;
    }

    public boolean isPositionFavourite(int position) {
        return (position == 0 && array.size() == 27);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
}

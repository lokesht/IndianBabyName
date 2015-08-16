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

    private Context mContext;
    public SparseArray array;
    OnAlphabetListener onAlphabetListener;

    public AlphabetRecyclerViewAdapter(Context context, SparseArray array, OnAlphabetListener onAlphabetListener) {
        mContext = context;
        this.array = array;
        this.onAlphabetListener = onAlphabetListener;
    }

    @Override
    public AlphabetGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alphabet, parent, false);
        AlphabetGridHolder pvh = new AlphabetGridHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(AlphabetGridHolder holder, final int position) {
        M_AlphaCount m_alphaCount = (M_AlphaCount) array.get(position);
        holder.tvAlpha.setText(m_alphaCount.getAlphabet());
        holder.tvCount.setText(m_alphaCount.getCount() + "");

        holder.cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                onAlphabetListener.onAlphaClickListener(v,(M_AlphaCount)array.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }
}

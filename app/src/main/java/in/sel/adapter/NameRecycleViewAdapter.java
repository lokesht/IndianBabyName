package in.sel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import in.sel.indianbabyname.R;
import in.sel.model.M_Name;
import in.sel.utility.AppConstants;
import in.sel.utility.L;

public class NameRecycleViewAdapter extends RecyclerView.Adapter<NameRecycleViewAdapter.MyViewHolder> {

    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;

    public interface OnFavouriteClick {
        void onMakeFavourite(int id, int position);
    }

    public static List<M_Name> allElementDetails = Collections.emptyList();
    private static  List<Integer> wishList = Collections.emptyList();

    private LayoutInflater mInflater;

    public NameRecycleViewAdapter(Context context, List<M_Name> results) {
        allElementDetails = results;
        mInflater = LayoutInflater.from(context);
        wishList = new ArrayList<Integer>();
    }

    @Override
    public int getItemCount() {
        return allElementDetails.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_list_name, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        M_Name info = allElementDetails.get(position);

        if (info != null) {

            java.util.Formatter formatter = new java.util.Formatter();

            if (AppConstants.DEBUG)
                L.lshow(mInflater.getContext(), holder.c1.getId() + "");

            holder.c1.setText(info.getName_en());
            holder.c2.setText(info.getName_ma());

            String temp = String.format(mInflater.getContext().getResources().getString(R.string.name_frequency),info.getFrequency());
            holder.c3.setText(temp);

            Formatter count = formatter.format("% 5d", (position + 1));
            holder.c4.setText(count.toString());

            if (wishList.contains(allElementDetails.get(position).getId())) {
                holder.ivSmile.setImageResource(R.mipmap.ic_favorite_black_24dp);
            }else
            {
                holder.ivSmile.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
            }

/*** Animate Recycle View*/
//			if(position<mPreviousPosition)
//			{
//				AnimationUtil.animateHeal(holder, false);
//			}else{
//				AnimationUtil.animateHeal(holder, true);
//			}
            mPreviousPosition = position;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView c1;
        TextView c2;
        TextView c3;
        TextView c4;
        ImageView ivSmile;

        public MyViewHolder(View convertView) {
            super(convertView);

            c1 = (TextView) convertView.findViewById(R.id.c1);
            c2 = (TextView) convertView.findViewById(R.id.c2);
            c3 = (TextView) convertView.findViewById(R.id.c3);
            c4 = (TextView) convertView.findViewById(R.id.tv_count);

            ivSmile = (ImageView) convertView.findViewById(R.id.ivSmily);

            ivSmile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getLayoutPosition();
                    Integer id = allElementDetails.get(position).getId();
                    if (wishList.contains(id)) {
                        ivSmile.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                        wishList.remove(id);
                        //notifyItemChanged(position);
                    } else {
                        ivSmile.setImageResource(R.mipmap.ic_favorite_black_24dp);
                        wishList.add(id);
                        //notifyItemChanged(position);
                    }
                }
            });
        }
    }


}

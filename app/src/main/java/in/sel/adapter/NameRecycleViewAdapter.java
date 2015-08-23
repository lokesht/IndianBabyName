package in.sel.adapter;

import android.content.ContentValues;
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

import in.sel.dbhelper.DBHelper;
import in.sel.dbhelper.TableContract;
import in.sel.indianbabyname.R;
import in.sel.model.M_Name;
import in.sel.utility.AppConstants;
import in.sel.utility.L;

public class NameRecycleViewAdapter extends RecyclerView.Adapter<NameRecycleViewAdapter.MyViewHolder> {

    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;

    private static List<M_Name> allElementDetails = Collections.emptyList();
    private List<M_Name> visibleObjects = Collections.emptyList();
    List<Integer> mWishList = Collections.emptyList();

    private LayoutInflater mInflater;

    public NameRecycleViewAdapter(Context context, List<M_Name> results, List<Integer> wishList) {
        allElementDetails = results;
        visibleObjects = new ArrayList<>(allElementDetails);

        mInflater = LayoutInflater.from(context);
        mWishList = wishList;
    }

    @Override
    public int getItemCount() {
        return visibleObjects.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_list_name, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        M_Name info = visibleObjects.get(position);

        if (info != null) {

            java.util.Formatter formatter = new java.util.Formatter();

            if (AppConstants.DEBUG)
                L.lshow(mInflater.getContext(), holder.c1.getId() + "");

            holder.c1.setText(info.getName_en());
            holder.c2.setText(info.getName_ma());

            String temp = String.format(mInflater.getContext().getResources().getString(R.string.name_frequency), info.getFrequency());
            holder.c3.setText(temp);

            Formatter count = formatter.format("% 5d", (position + 1));
            holder.c4.setText(count.toString());

            if (mWishList.contains(visibleObjects.get(position).getId())) {
                holder.ivSmile.setImageResource(R.mipmap.ic_favorite_black_24dp);
            } else {
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

                    DBHelper dbh = new DBHelper(mInflater.getContext());
                    int position = getLayoutPosition();
                    Integer id = visibleObjects.get(position).getId();
                    if (mWishList.contains(id)) {
                        ivSmile.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                        mWishList.remove(id);

                        String where = TableContract.FavourateName.NAME_ID + " = " + id;
                        dbh.deleteRow(TableContract.FavourateName.TABLE_NAME, where);

                    } else {
                        ivSmile.setImageResource(R.mipmap.ic_favorite_black_24dp);

                        ContentValues cv = new ContentValues();
                        cv.put(TableContract.FavourateName.NAME_EN,visibleObjects.get(position).getName_en());
                        cv.put(TableContract.FavourateName.NAME_MA,visibleObjects.get(position).getName_ma());
                        cv.put(TableContract.FavourateName.NAME_FRE,visibleObjects.get(position).getFrequency());
                        cv.put(TableContract.FavourateName.GENDER_CAST,visibleObjects.get(position).getGender_cast());
                        cv.put(TableContract.FavourateName.NAME_ID,visibleObjects.get(position).getId());

                        long tempId = dbh.insertInTable(TableContract.FavourateName.TABLE_NAME,null,cv);
                        mWishList.add(id);

                    }

                    dbh.close();
                }
            });
        }
    }

    /** */
    public void flushFilter() {
        visibleObjects = new ArrayList<>();
        visibleObjects.addAll(allElementDetails);
        notifyDataSetChanged();
    }

    /** */
    public void setFilter(String queryText, List<M_Name> newList) {
        visibleObjects = new ArrayList<>();
        for (M_Name item : newList) {
            if (item.getName_en().toLowerCase().contains(queryText))
                visibleObjects.add(item);
        }
        notifyDataSetChanged();
    }

    /**
     * Sort
     */
    public void setSort(List<M_Name> lsName) {
        visibleObjects = new ArrayList<>();
        visibleObjects.addAll(lsName);
        notifyDataSetChanged();
    }

    public List<M_Name> getVisibleObject() {
        return visibleObjects;
    }
}

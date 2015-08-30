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

import in.sel.anim.AnimationUtil;
import in.sel.dbhelper.DBHelper;
import in.sel.dbhelper.TableContract;
import in.sel.indianbabyname.R;
import in.sel.model.M_Name;
import in.sel.utility.AppConstants;
import in.sel.utility.L;

public class FavouriteNameRecycleViewAdapter extends RecyclerView.Adapter<FavouriteNameRecycleViewAdapter.MyViewHolder> {

    //keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
    private int mPreviousPosition = 0;

    private List<M_Name> visibleObjects = Collections.emptyList();
    private LayoutInflater mInflater;
    private M_Name undoObject;

    public FavouriteNameRecycleViewAdapter(Context context, List<M_Name> results) {

        visibleObjects = new ArrayList<>(results);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return visibleObjects.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_favourite_list_name, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        M_Name info = visibleObjects.get(position);

        if (info != null) {

            Formatter formatter = new Formatter();

            if (AppConstants.DEBUG)
                L.lshow(mInflater.getContext(), holder.c1.getId() + "");

            holder.c1.setText(info.getName_en());
            holder.c2.setText(info.getName_ma());

            String temp = String.format(mInflater.getContext().getResources().getString(R.string.name_frequency), info.getFrequency());
            holder.c3.setText(temp);

            Formatter count = formatter.format("% 5d", (position + 1));
            holder.c4.setText(count.toString());

            holder.ivSmile.setImageResource(R.mipmap.ic_favorite_black_24dp);


            /*** Animate Recycle View*/
            if (position < mPreviousPosition) {
                AnimationUtil.animateHeal(holder, false);
            } else {
                AnimationUtil.animateHeal(holder, true);
            }
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

                    ivSmile.setImageResource(R.mipmap.ic_favorite_border_black_24dp);

                    int position = getLayoutPosition();
                    remove(position);

                }
            });
        }
    }


    public void remove(int position) {

        undoObject = visibleObjects.get(position);

        DBHelper dbh = new DBHelper(mInflater.getContext());
        String where = TableContract.FavourateName.NAME_ID + " = " + visibleObjects.get(position).getId();
        dbh.deleteRow(TableContract.FavourateName.TABLE_NAME, where);

        visibleObjects.remove(position);
        notifyItemRemoved(position);
        if (dbh != null)
            dbh.close();
    }

    private void addUndo(int position, M_Name name) {
        DBHelper dbh = new DBHelper(mInflater.getContext());
        ContentValues cv = new ContentValues();
        cv.put(TableContract.FavourateName.NAME_EN, name.getName_en());
        cv.put(TableContract.FavourateName.NAME_MA, name.getName_ma());
        cv.put(TableContract.FavourateName.NAME_FRE, name.getFrequency());
        cv.put(TableContract.FavourateName.GENDER_CAST, name.getGender_cast());
        cv.put(TableContract.FavourateName.NAME_ID, name.getId());

        long tempId = dbh.insertInTable(TableContract.FavourateName.TABLE_NAME, null, cv);
        visibleObjects.add(position, name);
        notifyItemRemoved(position);

        if (dbh != null)
            dbh.close();
    }
}

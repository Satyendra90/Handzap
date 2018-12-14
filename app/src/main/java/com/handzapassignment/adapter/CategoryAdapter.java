package com.handzapassignment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handzapassignment.R;
import com.handzapassignment.activity.CategoryActivity;
import com.handzapassignment.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Category> mCategoryList, mSelectedCategories;
    private boolean isSelected[];
    private OnCategorySelectedListener onCategorySelectedListener;

    public interface OnCategorySelectedListener{
        void onCategorySelected(ArrayList<Category> selectedCategory);
    }

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.mContext = context;
        this.mCategoryList = categories;
        this.mSelectedCategories = new ArrayList<>();
        isSelected = new boolean[categories.size()];
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener listener){
        this.onCategorySelectedListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_category, null);
        final ViewHolder vh = new ViewHolder(v);

        final Animation scaleIn = AnimationUtils.loadAnimation(mContext, R.anim.scale_in);
        final Animation scaleOut = AnimationUtils.loadAnimation(mContext, R.anim.scale_out);
        scaleIn.setAnimationListener(new Animation.AnimationListener() {

            int itemPosition = 0;

            @Override
            public void onAnimationStart(Animation animation) {
                itemPosition = vh.getAdapterPosition();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isSelected[itemPosition]) {
                    vh.rlContainer.setVisibility(View.INVISIBLE);
                    vh.txtSelectedCategory.setVisibility(View.VISIBLE);
                    vh.txtSelectedCategory.startAnimation(scaleOut);
                } else {
                    vh.rlContainer.setVisibility(View.VISIBLE);
                    vh.txtSelectedCategory.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scaleOut.setAnimationListener(new Animation.AnimationListener() {

            int itemPosition = 0;

            @Override
            public void onAnimationStart(Animation animation) {
                itemPosition = vh.getAdapterPosition();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isSelected[itemPosition]) {
                    vh.rlContainer.setVisibility(View.INVISIBLE);
                    vh.txtSelectedCategory.setVisibility(View.VISIBLE);
                } else {
                    vh.txtSelectedCategory.setVisibility(View.INVISIBLE);
                    vh.rlContainer.setVisibility(View.VISIBLE);
                    vh.rlContainer.startAnimation(scaleOut);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        vh.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedCategories.size() < CategoryActivity.MAX_ALLOWED_CATEGORY){
                    vh.rlContainer.startAnimation(scaleIn);
                    isSelected[vh.getAdapterPosition()] = true;
                    countSelectedCategory();
                }
            }
        });
        vh.txtSelectedCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.txtSelectedCategory.startAnimation(scaleIn);
                isSelected[vh.getAdapterPosition()] = false;
                countSelectedCategory();
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Category category = mCategoryList.get(position);
        CategoryAdapter.ViewHolder holder = (ViewHolder) viewHolder;
        holder.txtCategoryName.setText(category.getCategoryName());
        holder.txtSelectedCategory.setText(category.getCategoryName());

        if (isSelected[position]) {
            holder.txtSelectedCategory.setVisibility(View.VISIBLE);
            holder.rlContainer.setVisibility(View.INVISIBLE);
        } else {
            holder.txtSelectedCategory.setVisibility(View.INVISIBLE);
            holder.rlContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView imgCategory;
        private TextView txtCategoryName, txtSelectedCategory;
        private RelativeLayout rlContainer;

        public ViewHolder(View v) {
            super(v);
            imgCategory = v.findViewById(R.id.imgCategory);
            txtCategoryName = v.findViewById(R.id.txtCategoryName);
            txtSelectedCategory = v.findViewById(R.id.txtCheckedCategory);
            rlContainer = v.findViewById(R.id.rlContainer);
        }
    }

    private void countSelectedCategory(){
        mSelectedCategories.clear();
        for(int i = 0; i < isSelected.length; i++){
            if(isSelected[i]){
                mSelectedCategories.add(mCategoryList.get(i));
            }
        }
        if(onCategorySelectedListener != null){
            onCategorySelectedListener.onCategorySelected(mSelectedCategories);
        }
    }
}

package com.handzapassignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.handzapassignment.R;
import com.handzapassignment.adapter.CategoryAdapter;
import com.handzapassignment.model.Category;
import com.handzapassignment.utils.ItemOffsetDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Will provide interface for selecting category in the post
 * @author Satyendra
 * @version 1.0
 */
public class CategoryActivity extends BaseActivity {

    @BindView(R.id.rcCategory)
    RecyclerView rcCategory;
    @BindView(R.id.txtMaxAllowedCategory)
    TextView txtMaxAllowCategory;
    @BindView(R.id.txtSelectedCategoryCount)
    TextView txtSelectedCategoryCount;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<Category> categories, mSelectedCategories;
    private CategoryAdapter mAdapter;

    public static final int MAX_ALLOWED_CATEGORY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initializeToolbar(toolbar, getString(R.string.post_categories));
        init();
        configureRecyclerView();
    }

    private void init() {
        txtSelectedCategoryCount.setText(getString(R.string.selected_category, 0));
        txtMaxAllowCategory.setText(getString(R.string.maximum_selected_category,
                MAX_ALLOWED_CATEGORY));

        categories = new ArrayList<>();
        mSelectedCategories = new ArrayList<>();
        //initialize some category data
        for (int i = 0; i < 20; i++) {
            Category category = new Category();
            category.setCategoryId(i);
            category.setCategoryName(String.format("C %03d", i + 1));
            categories.add(category);
        }
    }

    private void configureRecyclerView() {
        rcCategory.setHasFixedSize(true);
        LinearLayoutManager llManager = new GridLayoutManager(this, 3);
        rcCategory.setLayoutManager(llManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.margin_grid);
        rcCategory.addItemDecoration(itemDecoration);

        mAdapter = new CategoryAdapter(this, categories);
        rcCategory.setAdapter(mAdapter);

        mAdapter.setOnCategorySelectedListener(new CategoryAdapter.OnCategorySelectedListener() {
            @Override
            public void onCategorySelected(ArrayList<Category> selectedCategory) {
                mSelectedCategories.clear();
                mSelectedCategories.addAll(selectedCategory);
                txtSelectedCategoryCount.setText(getString(R.string.selected_category, mSelectedCategories.size()));

                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_category, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mSelectedCategories.size() != 0){
            menu.getItem(0).setEnabled(true);
        }
        else{
            menu.getItem(0).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.actionSelect){
            Intent intent = new Intent();
            intent.putExtra(NewPostActivity.PARAM_SELECTED_CATEGORY, mSelectedCategories);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return false;
    }
}

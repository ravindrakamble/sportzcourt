package com.sportzcourt.booking.ui.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.happiestminds.wheelviewlib.adapter.WheelItem;
import com.example.happiestminds.wheelviewlib.api.CompositeWheelView;
import com.example.happiestminds.wheelviewlib.api.MaterialColorAdapter;
import com.example.happiestminds.wheelviewlib.ui.TextDrawable;
import com.example.happiestminds.wheelviewlib.ui.WheelView;
import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.data.Category;
import com.sportzcourt.booking.model.data.Item;
import com.sportzcourt.booking.ui.adapter.CategoriesAdapter;
import com.sportzcourt.booking.ui.adapter.ItemAdapter;

import java.util.ArrayList;

public class WheelSampleActivity extends AppCompatActivity implements CompositeWheelView.OnWheelItemClickListener, CompositeWheelView.OnWheelItemSelectListener {

    private CompositeWheelView compositeWheelView;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_sample);

        compositeWheelView = (CompositeWheelView) findViewById(R.id.composite_wheel);

        setInnerWheelAdapter();
        setOuterWheelAdapter();

        initalizeCategories();
        initalizeListings();
        initalizeTendingList();
    }

    private void initalizeListings() {
        RecyclerView recList = initializeListDefaults((RecyclerView) findViewById(R.id.popularListings));

        ArrayList<Item> items = new ArrayList<>();

        items.add(new Item("Cheddar Cheese", R.drawable.cheese_1, "Rs. 10000"));
        items.add(new Item("Blue Cheese", R.drawable.cheese_2, "Rs. 5000"));
        items.add(new Item("Brussels Cheese", R.drawable.cheese_5, "Rs. 15000"));

        ItemAdapter adapter = new ItemAdapter(items);
        recList.setAdapter(adapter);
    }

    private void initalizeCategories() {
        RecyclerView recList = initializeListDefaults((RecyclerView) findViewById(R.id.populatCategories));

        ArrayList<Category> categories = new ArrayList<>();

        categories.add(new Category("Electronics", R.drawable.background));
        categories.add(new Category("Mobiles and Tablets", R.drawable.background));
        categories.add(new Category("Cameras and Accessories", R.drawable.background));
        categories.add(new Category("Audio and Videos", R.drawable.background));
        categories.add(new Category("Household", R.drawable.background));

        CategoriesAdapter adapter = new CategoriesAdapter(categories);
        recList.setAdapter(adapter);
    }

    private void initalizeTendingList() {
        RecyclerView recList = initializeListDefaults((RecyclerView) findViewById(R.id.trendingList));

        ArrayList<Item> items = new ArrayList<>();

        items.add(new Item("Indian Paneer", R.drawable.cheese_5, "Rs. 500"));
        items.add(new Item("European Cheese", R.drawable.cheese_3, "Rs. 1000"));
        items.add(new Item("American Cheese", R.drawable.cheese_4, "Rs. 5000"));

        ItemAdapter adapter = new ItemAdapter(items);
        recList.setAdapter(adapter);
    }

    private RecyclerView initializeListDefaults(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        return recyclerView;
    }

    private void setInnerWheelAdapter() {
        ArrayList<WheelItem> items = new ArrayList<>();

        items.add(new WheelItem(0X212121, "", R.drawable.household));
        items.add(new WheelItem(0X212121, "", R.drawable.electronics));
        items.add(new WheelItem(0XFFFFFF, "", R.drawable.mobiles));
        items.add(new WheelItem(0X004d40, "", R.drawable.cameras));
        items.add(new WheelItem(0X212121, "", R.drawable.audio));

        MaterialColorAdapter adapter = new MaterialColorAdapter(items, this, TextDrawable.TextPosition.RIGHT);
        compositeWheelView.setInnerWheelAdapter(adapter);
    }

    private void setOuterWheelAdapter() {
        ArrayList<WheelItem> items = new ArrayList<>();

        items.add(new WheelItem(0XFFFFFF, "", R.drawable.wheel_cheese_1));
        items.add(new WheelItem(0X004d40, "", R.drawable.wheel_cheese_2));
        items.add(new WheelItem(0X212121, "", R.drawable.wheel_cheese_3));
        items.add(new WheelItem(0X212121, "", R.drawable.wheel_cheese_4));
        items.add(new WheelItem(0X212121, "", R.drawable.wheel_cheese_5));

        MaterialColorAdapter adapter = new MaterialColorAdapter(items, this, TextDrawable.TextPosition.RIGHT);
        compositeWheelView.setOuterWheelAdapter(adapter);
    }

    private void setOuterWheelAdapter(ArrayList<WheelItem> items) {
        MaterialColorAdapter adapter = new MaterialColorAdapter(items, this, TextDrawable.TextPosition.RIGHT);
        compositeWheelView.setOuterWheelAdapter(adapter);
    }

    @Override
    public void onInnerWheelItemClick(WheelView wheelView, int position, boolean isSelected) {

        if (mToast == null)
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        mToast.setText("Clicked " + ((MaterialColorAdapter) wheelView.getAdapter()).getItem(position).getText() + position);
        mToast.show();
    }

    @Override
    public void onOuterWheelItemClick(WheelView wheelView, int position, boolean isSelected) {
        if (mToast == null)
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        mToast.setText("Outer Clicked " + ((MaterialColorAdapter) wheelView.getAdapter()).getItem(position).getText() + position);
        mToast.show();
    }

    @Override
    public void onInnerWheelItemSelected(WheelView wheelView, Drawable itemDrawable, int position) {
        changeOuterAdapter(position);
    }

    @Override
    public void onOuterWheelItemSelected(WheelView wheelView, Drawable itemDrawable, int position) {
        //TODO - Handle this
    }

    private void changeOuterAdapter(int position) {
        ArrayList<WheelItem> items = new ArrayList<>();


        switch (position) {
            case 1:
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_41));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_42));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_43));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_44));
                setOuterWheelAdapter(items);
                break;
            case 2:
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_31));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_32));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_33));
                setOuterWheelAdapter(items);
                break;
            case 3:
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_21));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_22));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_23));
                setOuterWheelAdapter(items);
                break;
            case 4:
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_11));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_12));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.outer_13));
                setOuterWheelAdapter(items);
                break;
            default:
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.wheel_cheese_1));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.wheel_cheese_2));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.wheel_cheese_3));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.wheel_cheese_4));
                items.add(new WheelItem(0XFFFFFF, "", R.drawable.wheel_cheese_5));
                setOuterWheelAdapter(items);
                break;
        }

    }
}

package com.handzapassignment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.handzapassignment.R;
import com.handzapassignment.adapter.PlaceAutocompleteAdapter;
import com.handzapassignment.utils.AppLocationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SearchLocationActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lvSuggestedList)
    ListView lvSuggestedList;

    @BindView(R.id.etSearchInput)
    EditText etSearchInput;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PlaceAutocompleteAdapter mAdapter;
    private AppLocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ButterKnife.bind(this);

        initializeToolbar(toolbar, "");
        mLocationManager = new AppLocationManager(this);
        mLocationManager.init();
        mLocationManager.onStart();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(0, 0));

        mAdapter = new PlaceAutocompleteAdapter(this, mLocationManager.getGoogleApiClient(), builder.build(), null);
        lvSuggestedList.setAdapter(mAdapter);
        lvSuggestedList.setOnItemClickListener(this);
    }

    @OnTextChanged(R.id.etSearchInput)
    void handleSearchTextChanged(CharSequence s, int start, int before, int count) {
        mAdapter.getFilter().filter(s);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
             /*Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.*/
        final AutocompletePrediction item = mAdapter.getItem(position);
        final String placeId = item.getPlaceId();
        final CharSequence primaryText = item.getPrimaryText(null);
        hideKeyboard(SearchLocationActivity.this);

        Intent intent = new Intent();
        intent.putExtra(NewPostActivity.PARAM_SELECTED_LOCATION, primaryText);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

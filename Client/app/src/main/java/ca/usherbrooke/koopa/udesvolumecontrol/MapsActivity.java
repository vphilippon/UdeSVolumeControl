package ca.usherbrooke.koopa.udesvolumecontrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

import message.PutConfigRequest;
import model.Config;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, TextWatcher, View.OnClickListener {

    private Config mConfig;
    private Vector<Config> mOtherConfigs;

    private GoogleMap mMap;
    private EditText editNameTextView;
    private EditText editRadiusTextView;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mOtherConfigs = new Vector<Config>();
        mOtherConfigs.add(new Config(1, "other", 45.3798844,-71.9277048, 500, 3, 3));

        mConfig = new Config(2,"Home",45.3783470,-71.8973207,200,1,1);
        mConfig.setConfigName("Home");
        mConfig.setRadius(50);

        editNameTextView = (EditText)findViewById(R.id.editProfileNameText);
        editNameTextView.setText(mConfig.getConfigName());

        editRadiusTextView = (EditText)findViewById(R.id.editRadiusText);
        editRadiusTextView.setText(mConfig.getRadius().toString());
        editRadiusTextView.addTextChangedListener(this);

        mSaveButton = (Button) findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        updateMapRadius();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mConfig.setCenterX(latLng.longitude);
        mConfig.setCenterY(latLng.latitude);
        updateMapRadius();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mConfig.setRadius(Integer.parseInt(editRadiusTextView.getText().toString()));
        updateMapRadius();
    }

    private void updateMapRadius()
    {
        if (mMap != null)
        {
            mMap.clear();
            if (mConfig != null)
            {
                LatLng position = new LatLng(mConfig.getCenterX(), mConfig.getCenterY());
                mMap.addMarker(new MarkerOptions().position(position).title(mConfig.getConfigName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                try{
                        CircleOptions circleOptions = new CircleOptions()
                                .center(position)
                                .radius(mConfig.getRadius())
                                .strokeWidth(2)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.parseColor("#500084d3"));
                        mMap.addCircle(circleOptions);
                } catch (NumberFormatException e)
                {

                }
            }
            for (Config config: mOtherConfigs)
            {
                LatLng otherCenter = new LatLng(config.getCenterX(), config.getCenterY());
                mMap.addMarker(new MarkerOptions().position(otherCenter).title(config.getConfigName()));
                try{
                    CircleOptions circleOptions = new CircleOptions()
                            .center(otherCenter)
                            .radius(config.getRadius())
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.argb(50,0,0,0));
                    mMap.addCircle(circleOptions);
                } catch (NumberFormatException e)
                {

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent resultIntent = new Intent();
        // TODO Add extras or a data URI to this intent as appropriate.
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
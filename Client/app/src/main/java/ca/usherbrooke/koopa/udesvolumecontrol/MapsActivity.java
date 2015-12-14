package ca.usherbrooke.koopa.udesvolumecontrol;

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
    private GoogleMap mMap;
    private LatLng mCurrentLatLng;
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

        mConfig = new Config(1,"Home",20.d,10.d,100,1,1);
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
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mCurrentLatLng = latLng;
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
        updateMapRadius();
    }

    private void updateMapRadius()
    {
        if (mMap != null)
        {
            mMap.clear();
            if (mCurrentLatLng != null)
            {
                mMap.addMarker(new MarkerOptions().position(mCurrentLatLng).title("Selected Position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng));
                try{
                        CircleOptions circleOptions = new CircleOptions()
                                .center(mCurrentLatLng)
                                .radius(Double.parseDouble(editRadiusTextView.getText().toString()))
                                .strokeWidth(2)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.parseColor("#500084d3"));
                        mMap.addCircle(circleOptions);
                } catch (NumberFormatException e)
                {

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
package ca.usherbrooke.koopa.udesvolumecontrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import model.VolumeConfig;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, TextWatcher, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private VolumeConfig m_VolumeConfig;
    private ArrayList<VolumeConfig> m_OtherConfigs;

    private GoogleMap m_Map;
    private EditText m_EditNameTextView;
    private EditText m_EditRadiusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        m_OtherConfigs = (ArrayList<VolumeConfig>) getIntent().getSerializableExtra("otherConfigs");
        m_VolumeConfig = (VolumeConfig)getIntent().getSerializableExtra("configToEdit");
        if (m_VolumeConfig == null)
        {
            m_VolumeConfig = new VolumeConfig(null,"",0.d,0.d,0,0);
        }


        m_EditNameTextView = (EditText)findViewById(R.id.editProfileNameText);
        m_EditNameTextView.setText(m_VolumeConfig.getName());
        m_EditNameTextView.addTextChangedListener(this);

        m_EditRadiusTextView = (EditText)findViewById(R.id.editRadiusText);
        m_EditRadiusTextView.setText(m_VolumeConfig.getRadius().toString());
        m_EditRadiusTextView.addTextChangedListener(this);

        Button m_SaveButton = (Button) findViewById(R.id.saveButton);
        m_SaveButton.setOnClickListener(this);

        RadioGroup m_RadioGroup = (RadioGroup) findViewById(R.id.radioGroupProfile);
        m_RadioGroup.setOnCheckedChangeListener(this);
        switch(SoundProfiles.values()[m_VolumeConfig.getProfile()])
        {
            case SILENT:
                m_RadioGroup.check(R.id.rbMuted);
                break;
            case VIBRATE:
                m_RadioGroup.check(R.id.rbVibrate);
                break;
            case SOUND:
                m_RadioGroup.check(R.id.rbLoud);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_Map = googleMap;
        m_Map.setMyLocationEnabled(true);
        m_Map.setOnMapClickListener(this);
        updateMapRadius();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        m_VolumeConfig.setLongitude(latLng.longitude);
        m_VolumeConfig.setLatitude(latLng.latitude);
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
        m_VolumeConfig.setName(m_EditNameTextView.getText().toString());
        if (m_EditRadiusTextView.getText().toString().length() > 0)
        {
            m_VolumeConfig.setRadius(Integer.parseInt(m_EditRadiusTextView.getText().toString()));
        }
        updateMapRadius();
    }

    private void updateMapRadius()
    {
        if (m_Map != null)
        {
            m_Map.clear();
            if (m_VolumeConfig != null)
            {
                LatLng position = new LatLng(m_VolumeConfig.getLatitude(), m_VolumeConfig.getLongitude());
                m_Map.addMarker(new MarkerOptions().position(position).title(m_VolumeConfig.getName()));
                m_Map.moveCamera(CameraUpdateFactory.newLatLng(position));
                try{
                        CircleOptions circleOptions = new CircleOptions()
                                .center(position)
                                .radius(m_VolumeConfig.getRadius())
                                .strokeWidth(2)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.parseColor("#500084d3"));
                        m_Map.addCircle(circleOptions);
                } catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
            for (VolumeConfig config: m_OtherConfigs)
            {
                LatLng otherCenter = new LatLng(config.getLatitude(), config.getLongitude());
                m_Map.addMarker(new MarkerOptions().position(otherCenter).title(config.getName()));
                try{
                    CircleOptions circleOptions = new CircleOptions()
                            .center(otherCenter)
                            .radius(config.getRadius())
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.argb(50,0,0,0));
                    m_Map.addCircle(circleOptions);
                } catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("VolumeConfig", m_VolumeConfig);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("VolumeConfig", m_VolumeConfig);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId)
        {
            case R.id.rbLoud:
                m_VolumeConfig.setProfile(SoundProfiles.SOUND.ordinal());
                break;
            case R.id.rbVibrate:
                m_VolumeConfig.setProfile(SoundProfiles.VIBRATE.ordinal());
                break;
            case R.id.rbMuted:
                m_VolumeConfig.setProfile(SoundProfiles.SILENT.ordinal());
                break;
        }
    }
}
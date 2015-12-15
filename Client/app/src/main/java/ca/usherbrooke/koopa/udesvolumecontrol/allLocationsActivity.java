//Maxime Cloutier 10 076 464
//Zachary Duquette 11 011 978
//Kevin Labrie 12 113 777
//Julien Meunier 10 078 943
//TODO Vincent Philippon add matricule
package ca.usherbrooke.koopa.udesvolumecontrol;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Vector;

public class allLocationsActivity extends Activity {

    private String mUsername;
    private ListAdapter m_locationListAdapter;
    private ArrayList<OurLocation> m_allOurLocations = new ArrayList<OurLocation>();
    private Boolean testBool = false;
    VolumeControlService m_volumeControlServ = null;
    boolean m_isBound = false;
    private ServiceConnection m_volumeControlServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            m_volumeControlServ = ((VolumeControlService.VolumeControlServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            m_volumeControlServ = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_locations_activity);
        mUsername = getIntent().getStringExtra("userName");
        doBindService();
        //update();
    };

    void doBindService()
    {
        bindService(new Intent(this,
                VolumeControlService.class), m_volumeControlServiceConnection, Context.BIND_AUTO_CREATE);
        m_isBound = true;
    }

    void doUnbindService()
    {
        if (m_isBound)
        {
            unbindService(m_volumeControlServiceConnection);
            m_isBound = false;
        }
    }

    public void onPause()
    {
        super.onPause();
        doUnbindService();
    }

    public void onResume()
    {
        super.onResume();
        doBindService();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void onUpdateClicked(View v) {
        update();
    }

    public void onSignOut(View v){
        signOut();
    }

    private void signOut(){
        doUnbindService();
    }

    private void update()
    {
        ListView eventList = (ListView) findViewById(R.id.locationList);

        VolumeEntry currentLocation = m_volumeControlServ.getCurrentVolumeEntry();
        if(currentLocation == null){
            LinearLayout knownLocationLayout = (LinearLayout)findViewById(R.id.knownLocation);
            knownLocationLayout.setVisibility(View.GONE);

            LinearLayout unknownLocationLayout = (LinearLayout)findViewById(R.id.unknownLocation);
            unknownLocationLayout.setVisibility(View.VISIBLE);
        }
        else{
            LinearLayout knownLocationLayout = (LinearLayout)findViewById(R.id.knownLocation);
            knownLocationLayout.setVisibility(View.VISIBLE);

            LinearLayout unknownLocationLayout = (LinearLayout)findViewById(R.id.unknownLocation);
            unknownLocationLayout.setVisibility(View.GONE);
        }

        //TODO merge with Vincent Get real list from server
        if (eventList != null) {
            m_allOurLocations.add(new OurLocation("Home", new SoundProfile(SoundProfiles.SOUND, 100)));
//            m_allOurLocations.add(new OurLocation("Work", new SoundProfile(SoundProfiles.SILENT, 100)));
//            m_allOurLocations.add(new OurLocation("Home depot", new SoundProfile(SoundProfiles.SOUND, 100)));
//            m_allOurLocations.add(new OurLocation("Bus stop", new SoundProfile(SoundProfiles.SOUND, 100)));
//            m_allOurLocations.add(new OurLocation("Longest name ever for a location", new SoundProfile(SoundProfiles.SILENT, 100)));
//            m_allOurLocations.add(new OurLocation("Bar", new SoundProfile(SoundProfiles.VIBRATE, 100)));
//            m_allOurLocations.add(new OurLocation("School", new SoundProfile(SoundProfiles.VIBRATE, 100)));
            m_locationListAdapter = new ListAdapter(this, R.layout.location_main, m_allOurLocations);
            eventList.setAdapter(m_locationListAdapter);

            // TODO MAXIME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // TODO Voici comment moi je faisait pour set toutes les choses. Je ne comprends pas a quoi sert le Location que vous avez cree (que j'ai renomme OurLocation pour pas avoir de conflit avec Location d'Android
            // Et j'ai pas compris a quoi sers SoundProfile et SoundProfileS
            Vector<VolumeEntry> TemporaireAllEntries = new Vector<VolumeEntry>();
            Location aLocationFromJavaLocationClass = new Location(LocationManager.GPS_PROVIDER);
            aLocationFromJavaLocationClass.setLatitude(45.381);
            aLocationFromJavaLocationClass.setLongitude(-71.9272000);
            int radius = 20;
            int ringtone = 0;
            int notificatiuon = 0;
            boolean vibrate = false;

            VolumeEntry TempLoc = new VolumeEntry("Un Nom", aLocationFromJavaLocationClass, radius, ringtone, notificatiuon, vibrate);
            TemporaireAllEntries.add(TempLoc);

            // Ca erase les anciens entry et les remplace par les nouveaux.
            m_volumeControlServ.setAllEntries(TemporaireAllEntries);

        }
    }
}
//Maxime Cloutier 10 076 464
//Zachary Duquette 11 011 978
//Kevin Labrie 12 113 777
//Julien Meunier 10 078 943
//Vincent Philippon
package ca.usherbrooke.koopa.udesvolumecontrol.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import ca.usherbrooke.koopa.udesvolumecontrol.common.Location;
import ca.usherbrooke.koopa.udesvolumecontrol.common.SoundProfile;
import ca.usherbrooke.koopa.udesvolumecontrol.common.SoundProfiles;

public class allLocationsActivity extends Activity {

    private ListAdapter m_locationListAdapter;
    private ArrayList<Location> m_allLocations = new ArrayList<Location>();

    //Service
    //HockeyNightUpdaterService mService;
    //HockeyNightServiceConnection mServiceConnection;
    //boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_locations_activity);
        update();
    };

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
        //Intent serviceIntent = new Intent(this, HockeyNightUpdaterService.class);
        //bindService(serviceIntent, mServiceConnection, Context.BIND_ADJUST_WITH_ACTIVITY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void onUpdateClicked(View v) {
        update();
    }

    private void update()
    {
        ListView eventList = (ListView) findViewById(R.id.locationList);

        //Refresh current location

        //Get real list
        if (eventList != null) {
            m_allLocations.add(new Location("Home", new SoundProfile(SoundProfiles.SOUND, 100)));
//            m_allLocations.add(new Location("Work", new SoundProfile(SoundProfiles.SILENT, 100)));
//            m_allLocations.add(new Location("Home depot", new SoundProfile(SoundProfiles.SOUND, 100)));
//            m_allLocations.add(new Location("Bus stop", new SoundProfile(SoundProfiles.SOUND, 100)));
//            m_allLocations.add(new Location("Longest name ever for a location", new SoundProfile(SoundProfiles.SILENT, 100)));
//            m_allLocations.add(new Location("Bar", new SoundProfile(SoundProfiles.VIBRATE, 100)));
//            m_allLocations.add(new Location("School", new SoundProfile(SoundProfiles.VIBRATE, 100)));
            m_locationListAdapter = new ListAdapter(this, R.layout.location_main, m_allLocations);
            eventList.setAdapter(m_locationListAdapter);
        }
    }
}
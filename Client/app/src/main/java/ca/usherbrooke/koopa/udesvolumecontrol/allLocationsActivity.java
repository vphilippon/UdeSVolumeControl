//Maxime Cloutier 10 076 464
//Zachary Duquette 11 011 978
//Kevin Labrie 12 113 777
//Julien Meunier 10 078 943
//TODO Vincent Philippon add matricule
package ca.usherbrooke.koopa.udesvolumecontrol;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import message.GetUserConfigsReply;
import message.GetUserConfigsRequest;
import message.PostNewUserReply;
import message.PostNewUserRequest;
import utils.ClientUDP;
import utils.Serializer;

public class allLocationsActivity extends Activity {

    private ListAdapter m_locationListAdapter;
    private ArrayList<Location> m_allLocations = new ArrayList<Location>();
    private Boolean testBool = false;
    //TODO merge with julien, add service variables here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_locations_activity);

        //TODO merge with Julien start and bind service here (Or make sure it is bound :P)

        update();
    };

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
        //TODO merge with Julien. Make sure service is bound (If we unbind it in pause, etc.)
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
        //TODO merge with Julien Unbind service

        Intent myIntent = new Intent(allLocationsActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void update()
    {
        ListView eventList = (ListView) findViewById(R.id.locationList);

        //TODO merge with julien Refresh current location
        //currentLocation = service.get
        testBool = !testBool;
        if(testBool){ //currentLocation == null{
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
            ClientUDP cl = null;
            try {
                cl = new ClientUDP(1000);
                cl.connect("10.44.88.174", 9005);

                cl.send(Serializer.serialize(new GetUserConfigsRequest("ff")));

                DatagramPacket rep = cl.receive();
                GetUserConfigsReply mess = (GetUserConfigsReply) Serializer.deserialize(rep.getData());

            }  catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
//            m_allLocations.add(new Location("Home", new SoundProfile(SoundProfiles.SOUND, 100)));
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
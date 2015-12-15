//Maxime Cloutier 10 076 464
//Zachary Duquette 11 011 978
//Kevin Labrie 12 113 777
//Julien Meunier 10 078 943
//TODO Vincent Philippon add matricule
package ca.usherbrooke.koopa.udesvolumecontrol;

import android.app.Activity;
import android.content.Intent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Vector;

import message.GetVolumeConfigsReply;
import message.GetVolumeConfigsRequest;
import model.VolumeConfig;
import utils.ClientUDP;
import utils.Serializer;

public class allLocationsActivity extends Activity {

    protected enum DatabaseRequests{ REFRESH, DELETE, EDIT, ADD  };

    private ListAdapter m_locationListAdapter;
    private ArrayList<VolumeConfig> mVolumeConfigs = new ArrayList<VolumeConfig>();
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
        doBindService();

        m_locationListAdapter = new ListAdapter(this, R.layout.location_main, mVolumeConfigs);

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

        Intent myIntent = new Intent(allLocationsActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void update()
    {

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

            m_locationListAdapter = new ListAdapter(this, R.layout.location_main, mVolumeConfigs);
            LinearLayout unknownLocationLayout = (LinearLayout)findViewById(R.id.unknownLocation);
            unknownLocationLayout.setVisibility(View.GONE);
        }

        ListView eventList = (ListView) findViewById(R.id.locationList);
        if (eventList != null) {

            DatabaseRequestTask refreshTask = new DatabaseRequestTask(DatabaseRequests.REFRESH, "ff");
            refreshTask.execute();

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

    public class DatabaseRequestTask extends AsyncTask<Void, Void, Boolean> {

        private final DatabaseRequests mRequest;
        private final String mUsername;

        DatabaseRequestTask(DatabaseRequests request, String username) {
            mRequest = request;
            mUsername = username;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ClientUDP cl = new ClientUDP(1000);
                cl.connect("10.44.88.174", 9005);

                switch (mRequest)
                {
                    case REFRESH:
                        cl.send(Serializer.serialize(new GetVolumeConfigsRequest(mUsername)));
                        break;
                    case EDIT:
                        //cl.send(Serializer.serialize(new GetUserConfigsRequest(mUsername)));
                        break;
                    case DELETE:
                        //cl.send(Serializer.serialize(new GetUserConfigsRequest(mUsername)));
                        break;
                    case ADD:
                        //cl.send(Serializer.serialize(new PutConfigRequest(mUsername)));
                        break;
                }

                DatagramPacket rep = cl.receive();

                switch (mRequest)
                {
                    case REFRESH:
                        GetVolumeConfigsReply mess = (GetVolumeConfigsReply) Serializer.deserialize(rep.getData());
                        addConfigs(mess.getConfigs());
                        return mess.isSuccess();
                    case EDIT:
                        //mess = (PostNewUserReply) Serializer.deserialize(rep.getData());
                        break;
                    case DELETE:
                        //mess = (PostNewUserReply) Serializer.deserialize(rep.getData());
                        break;
                    case ADD:
                        //mess = (PostNewUserReply) Serializer.deserialize(rep.getData());
                        break;
                }

            }  catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Toast.makeText(getApplication(), "Success!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplication(), "Fail!", Toast.LENGTH_SHORT).show();
            }
        }

        private void addConfigs(ArrayList<VolumeConfig> configs)
        {
            mVolumeConfigs.clear();
            for (VolumeConfig conf : configs) {
                mVolumeConfigs.add(conf);
            }

        }
    }


}
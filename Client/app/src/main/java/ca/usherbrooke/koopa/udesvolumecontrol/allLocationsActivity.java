//Maxime Cloutier 10 076 464
//Zachary Duquette 11 011 978
//Kevin Labrie 12 113 777
//Julien Meunier 10 078 943
//Vincent Philippon 12 098 838
package ca.usherbrooke.koopa.udesvolumecontrol;

import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import model.VolumeConfig;

public class AllLocationsActivity extends Activity {
    protected enum DatabaseRequests{ REFRESH, DELETE, EDIT, ADD  };

    static final int MODIFY_CONFIG = 1;
    static final int ADD_CONFIG = 2;

    private String mUsername;
    private ListAdapter m_locationListAdapter;
    private ArrayList<VolumeConfig> m_volumeConfigs = new ArrayList<VolumeConfig>();
    VolumeControlService m_volumeControlService = null;
    boolean m_isBound = false;

    private ServiceConnection m_volumeControlServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            m_volumeControlService = ((VolumeControlService.VolumeControlServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            m_volumeControlService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_locations_activity);
        mUsername = getIntent().getStringExtra("userName");
        doBindService();

        m_locationListAdapter = new ListAdapter(this, R.layout.location_main, m_volumeConfigs);

        update();
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

    public void onEditCurrentClicked(View v){
        ImageButton button = (ImageButton) v;
        VolumeConfig configToEdit = null;
        if(button.getId() == R.id.locationListEdit)
        {
            ListView listView = (ListView) findViewById(R.id.locationList);
            int position = listView.getPositionForView(v);
            configToEdit = (VolumeConfig)listView.getItemAtPosition(position);
        }
        else
        {
            TextView tv = (TextView)findViewById(R.id.currentLocationName);
            String configName = tv.getText().toString();
            Iterator<VolumeConfig>it = m_volumeConfigs.iterator();
            while(it.hasNext())
            {
                VolumeConfig current = it.next();
                if(current.getName().equals(configName))
                {
                    configToEdit = current;
                    break;
                }
            }
        }
        Intent myIntent = new Intent(AllLocationsActivity.this, MapsActivity.class);
        myIntent.putExtra("configToEdit",configToEdit);
        myIntent.putExtra("otherConfigs", m_volumeConfigs);
        startActivityForResult(myIntent, MODIFY_CONFIG);
    }

    public void onDeleteCurrentClicked(View v){
        ImageButton button = (ImageButton) v;
        String currentLocationName;
        VolumeConfig configtoDelete;
        if(button.getId() == R.id.locationListDelete)
        {
            ListView listView = (ListView) findViewById(R.id.locationList);
            int position = listView.getPositionForView(v);
            configtoDelete = (VolumeConfig)listView.getItemAtPosition(position);
            currentLocationName = configtoDelete.getName();
        }
        else
        {
            TextView tv = (TextView)findViewById(R.id.currentLocationName);
            currentLocationName = tv.getText().toString();
            Iterator<VolumeConfig>it = m_volumeConfigs.iterator();
            while(it.hasNext())
            {
                VolumeConfig current = it.next();
                if(current.getName().equals(currentLocationName))
                {
                    configtoDelete = current;
                    break;
                }
            }
        }
        AlertDialog dlg = new AlertDialog.Builder(v.getContext())
                .setMessage("Are you sure you want to delete " + currentLocationName + " ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //send to server DELETE LOCATION
//                      config.getId();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .show();
    }

    public void onAddNewLocationClicked(View v){
        Intent myIntent = new Intent(AllLocationsActivity.this, MapsActivity.class);
        myIntent.putExtra("otherConfigs", m_volumeConfigs);
        startActivityForResult(myIntent,ADD_CONFIG);
    }

    public void onUpdateClicked(View v) {
        update();
    }

    public void onSignOutClicked(View v){
        signOut();
    }

    private void signOut(){
        doUnbindService();

        Intent myIntent = new Intent(AllLocationsActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void update()
    {
        VolumeEntry currentLocation = m_volumeControlService != null ? m_volumeControlService.getCurrentVolumeEntry() : null;
        if (currentLocation == null) {
            LinearLayout knownLocationLayout = (LinearLayout) findViewById(R.id.knownLocation);
            knownLocationLayout.setVisibility(View.GONE);

            LinearLayout unknownLocationLayout = (LinearLayout) findViewById(R.id.unknownLocation);
            unknownLocationLayout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout knownLocationLayout = (LinearLayout) findViewById(R.id.knownLocation);
            knownLocationLayout.setVisibility(View.VISIBLE);

            m_locationListAdapter = new ListAdapter(this, R.layout.location_main, m_volumeConfigs);
            LinearLayout unknownLocationLayout = (LinearLayout) findViewById(R.id.unknownLocation);
            unknownLocationLayout.setVisibility(View.GONE);
        }

        ListView eventList = (ListView) findViewById(R.id.locationList);
        if (eventList != null) {

            DatabaseRequestTask refreshTask = new DatabaseRequestTask(DatabaseRequests.REFRESH, "ff"); // TODO change for real username
            refreshTask.execute();

            eventList.setAdapter(m_locationListAdapter);

            // TODO MAXIME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // TODO Voici comment moi je faisait pour set toutes les choses. Je ne comprends pas a quoi sert le Location que vous avez cree (que j'ai renomme OurLocation pour pas avoir de conflit avec Location d'Android
            // Et j'ai pas compris a quoi sers SoundProfile et SoundProfileS


            Vector<VolumeEntry> TemporaireAllEntries = new Vector<VolumeEntry>();

            for (VolumeConfig conf : m_volumeConfigs) {
                Location aLocationFromJavaLocationClass = new Location(LocationManager.GPS_PROVIDER);
                aLocationFromJavaLocationClass.setLatitude(conf.getLatitude());
                aLocationFromJavaLocationClass.setLongitude(conf.getLongitude());

                int ringtone = 0;
                boolean vibrate = false;

                switch (SoundProfiles.values()[conf.getProfile()]){
                    case SILENT:
                        break;
                    case VIBRATE:
                        vibrate = true;
                        break;
                    case SOUND:
                        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                        ringtone = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
                        break;
                }

                TemporaireAllEntries.add(new VolumeEntry(conf.getName(), aLocationFromJavaLocationClass, conf.getRadius(), ringtone, ringtone, vibrate));
            }

            //VolumeEntry TempLoc = new VolumeEntry("Un Nom", aLocationFromJavaLocationClass, radius, ringtone, notificatiuon, vibrate);
            //TemporaireAllEntries.add(TempLoc);
            // Ca erase les anciens entry et les remplace par les nouveaux.
            if (m_volumeControlService != null) {
                m_volumeControlService.setAllEntries(TemporaireAllEntries);
            }

        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        //TODO replace with sending to server async task.
        VolumeConfig config = (VolumeConfig) data.getSerializableExtra("VolumeConfig");
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
//            try {
//                ClientUDP cl = new ClientUDP(1000);
//                cl.connect("10.44.88.174", 9005);
//
//                switch (mRequest)
//                {
//                    case REFRESH:
//                        cl.send(Serializer.serialize(new GetVolumeConfigsRequest(mUsername)));
//                        break;
//                    case EDIT:
//                        cl.send(Serializer.serialize(new PutVolumeConfigRequest(mUsername, new VolumeConfig(/* ADD DATA */))));
//                        break;
//                    case DELETE:
//                        //cl.send(Serializer.serialize(new GetUserConfigsRequest(mUsername)));
//                        break;
//                    case ADD:
//                        cl.send(Serializer.serialize(new PutVolumeConfigRequest(mUsername, new VolumeConfig(null, /* ADD DATA */))));
//                        break;
//                }
//
//                DatagramPacket rep = cl.receive();
//
//                switch (mRequest)
//                {
//                    case REFRESH:
//                        GetVolumeConfigsReply mess = (GetVolumeConfigsReply) Serializer.deserialize(rep.getData());
//                        addConfigs(mess.getConfigs());
//                        return mess.isSuccess();
//                    case EDIT:
//                        //mess = (PutVolumeConfigReply) Serializer.deserialize(rep.getData());
//                        break;
//                    case DELETE:
//                        //mess = (PostNewUserReply) Serializer.deserialize(rep.getData());
//                        break;
//                    case ADD:
//                        //mess = (PostNewUserReply) Serializer.deserialize(rep.getData());
//                        break;
//                }
                addConfigs(new ArrayList<VolumeConfig>());

//            }  catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        private void addConfigs(ArrayList<VolumeConfig> configs)
        {
            m_volumeConfigs.clear();
            for (VolumeConfig conf : configs) {
                m_volumeConfigs.add(conf);
            }
            m_volumeConfigs.add(new VolumeConfig(0, "Test", 2.0, 2.0, 50, 1));

        }
    }
}
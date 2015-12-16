//Maxime Cloutier 10 076 464
//Zachary Duquette 11 011 978
//Kevin Labrie 12 113 777
//Julien Meunier 10 078 943
//Vincent Philippon 12 098 838
package ca.usherbrooke.koopa.udesvolumecontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import message.DeleteVolumeConfigReply;
import message.DeleteVolumeConfigRequest;
import message.GetVolumeConfigsReply;
import message.GetVolumeConfigsRequest;
import message.PutVolumeConfigReply;
import message.PutVolumeConfigRequest;
import model.VolumeConfig;
import utils.ClientTCP;
import utils.Serializer;

public class AllLocationsActivity  extends Activity {

    protected enum DatabaseRequests{ REFRESH, DELETE, EDIT, ADD  }

    static final int MODIFY_CONFIG = 1;
    static final int ADD_CONFIG = 2;

    private String mUsername;
    private ListAdapter m_locationListAdapter;
    private ArrayList<VolumeConfig> m_volumeConfigs = new ArrayList<>();
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
    }

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
            for (VolumeConfig current : m_volumeConfigs)
            {
                if (current.getName().equals(configName))
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
        VolumeConfig configtoDelete = null;
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
            for (VolumeConfig current : m_volumeConfigs)
            {
                if (current.getName().equals(currentLocationName))
                {
                    configtoDelete = current;
                    break;
                }
            }
        }
        final VolumeConfig confFinal = configtoDelete; // Need to access in the inner AsyncTask

        new AlertDialog.Builder(v.getContext())
                .setMessage("Are you sure you want to delete " + currentLocationName + " ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //send to server DELETE LOCATION
                        //config.getId();
                        onDeleteYesClick(confFinal);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
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

    private void update(){
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
        if (eventList != null)
        {

            DatabaseRequestTask refreshTask = new DatabaseRequestTask(this, DatabaseRequests.REFRESH, mUsername, null);
            refreshTask.execute();

            eventList.setAdapter(m_locationListAdapter);

            Vector<VolumeEntry> allNewEntries = new Vector<>();

                for (VolumeConfig conf : m_volumeConfigs)
                {
                    Location aLocation = new Location(LocationManager.GPS_PROVIDER);
                    aLocation.setLatitude(conf.getLatitude());
                    aLocation.setLongitude(conf.getLongitude());
                    allNewEntries.add(new VolumeEntry(conf.getName(),aLocation,conf.getRadius(),SoundProfiles.values()[conf.getProfile()]));
                }

              if (m_volumeControlService != null) {
                m_volumeControlService.setAllEntries(allNewEntries);
            }

        }
    }

    private void onDeleteYesClick(VolumeConfig conf){

        DatabaseRequestTask refreshTask = new DatabaseRequestTask(this, DatabaseRequests.DELETE, mUsername, conf);
        refreshTask.execute();
        update();
    }
    
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        VolumeConfig config = (VolumeConfig) data.getSerializableExtra("VolumeConfig");
        DatabaseRequestTask refreshTask;

        if (requestCode == MODIFY_CONFIG) {
            refreshTask = new DatabaseRequestTask(this, DatabaseRequests.EDIT, mUsername, config);
        }
        else
        {
            refreshTask = new DatabaseRequestTask(this, DatabaseRequests.ADD, mUsername, config);
        }

        refreshTask.execute();
        update();
    }


    protected void refreshList()
    {
        ListView eventList = (ListView) findViewById(R.id.locationList);
        if (eventList != null) {

            eventList.setAdapter(m_locationListAdapter);
        }
    }

    public class DatabaseRequestTask extends AsyncTask<Void, Void, Boolean> {

        private final DatabaseRequests mRequest;
        private final String mUsername;
        private AllLocationsActivity listener;
        private VolumeConfig m_config;

        DatabaseRequestTask(AllLocationsActivity listener, DatabaseRequests request, String username, VolumeConfig conf) {
            mRequest = request;
            mUsername = username;
            this.listener = listener;
            m_config = conf;
        }

        @Override
        protected Boolean doInBackground(Void ... parms) {
            ClientTCP cl = null;
            try {
                cl = new ClientTCP();
                cl.connect("10.44.88.174", 9005);

                switch (mRequest)
                {
                    case REFRESH:
                        cl.send(Serializer.serialize(new GetVolumeConfigsRequest(mUsername)));
                        break;
                    case EDIT:
                        cl.send(Serializer.serialize(new PutVolumeConfigRequest(mUsername, m_config)));
                        break;
                    case DELETE:
                        cl.send(Serializer.serialize(new DeleteVolumeConfigRequest(mUsername, m_config.getId())));
                        break;
                    case ADD:
                        cl.send(Serializer.serialize(new PutVolumeConfigRequest(mUsername, m_config)));
                        break;
                }

                Object rep = cl.receive();

                switch (mRequest)
                {
                    case REFRESH:
                        GetVolumeConfigsReply messRefresh = (GetVolumeConfigsReply) rep;
                        addConfigs(messRefresh.getConfigs());
                        return messRefresh.isSuccess();
                    case EDIT:
                        PutVolumeConfigReply messEdit = (PutVolumeConfigReply) rep;
                        return messEdit.isSuccess();
                    case DELETE:
                        DeleteVolumeConfigReply messDelete = (DeleteVolumeConfigReply) rep;
                        return messDelete.isSuccess();
                    case ADD:
                        PutVolumeConfigReply messAdd = (PutVolumeConfigReply) rep;
                        return messAdd.isSuccess();
                }
                //addConfigs(new ArrayList<VolumeConfig>());

            }  catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                if(cl != null) {
                    try {
                        cl.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            listener.refreshList();
        }

        private void addConfigs(ArrayList<VolumeConfig> configs) {
            m_volumeConfigs.clear();
            for (VolumeConfig conf : configs) {
                m_volumeConfigs.add(conf);
            }
        }
    }
}

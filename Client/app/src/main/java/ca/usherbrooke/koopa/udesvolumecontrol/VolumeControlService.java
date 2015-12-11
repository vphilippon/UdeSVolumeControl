package ca.usherbrooke.koopa.udesvolumecontrol;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Random;
import java.util.Vector;

/**
 * Created by meuj1902 on 2015-11-27.
 */
public class VolumeControlService extends Service implements LocationListener
{
    private LocationManager locMan;
    private AudioManager audioMan;
    static final int MIN_BTW_UPDATE = 10 * 1000; // 10 sec
    static final int DIS_BTW_UPDATE = 10; // 10 meter

    private Vector<VolumeEntry> allEntries;

    //START With ==> ComponentName service = startService(new Intent(getBaseContext(), VolumeControlService.class));
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate()
    {
        Toast.makeText(this, "Service : Started ", Toast.LENGTH_SHORT).show();
        audioMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        allEntries = new Vector<VolumeEntry>();
        /*TODO: temp Code*/
        {

            Location tempLoc = new Location(locMan.GPS_PROVIDER);
            tempLoc.setLatitude(45.380868);
            tempLoc.setLongitude(-71.928636);
            allEntries.add(new VolumeEntry("SVE", tempLoc  /*locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER)*/, 50, 0,0));
        }
        {
            Vector<Integer> temp = new Vector<Integer>();
            Random rand = new Random();
            for (int i = 0; i < audioMan.NUM_STREAMS; ++i)
            {
                temp.add(100);
            }
            Location tempLoc = new Location(locMan.GPS_PROVIDER);
            tempLoc.setLatitude(45.3800588);
            tempLoc.setLongitude(-71.9269598);
            allEntries.add(new VolumeEntry("Science", tempLoc  /*locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER)*/, 50, audioMan.getStreamMaxVolume(audioMan.STREAM_RING), audioMan.getStreamMaxVolume(audioMan.STREAM_NOTIFICATION) ));
        }
        /*TODO: temp Code*/



        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_BTW_UPDATE,
                    DIS_BTW_UPDATE, this);
        }
        else
        {
            Toast.makeText(this, "Service : Permissions are not granted ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service : Destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        //Toast.makeText(this, "Service : Location changed", Toast.LENGTH_SHORT).show();
        String msg = "New Latitude: "+location.getLatitude()+" New Longitude: "+location.getLongitude();
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        for(VolumeEntry entry : allEntries)
        {
            if (entry.isInside(location))
            {
                msg = "Zone " + entry.getEntryName();
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();

                audioMan.setStreamVolume(audioMan.STREAM_NOTIFICATION, entry.getNotificationVolume(),/* TODO: no flag 0*/AudioManager.FLAG_SHOW_UI);
                audioMan.setStreamVolume(audioMan.STREAM_RING, entry.getRingtoneVolume(),/* TODO: no flag 0*/AudioManager.FLAG_SHOW_UI);

                break;
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

        Toast.makeText(this, "Service : status changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider)
    {

        Toast.makeText(this, "Service : Provider enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "Service : Provider disabled", Toast.LENGTH_SHORT).show();
    }

    public void AddVolumeEntry(VolumeEntry entry)
    {
        allEntries.add(entry);
    }

    public void removeEntry(VolumeEntry entry)
    {
        allEntries.remove(entry);
    }
    public void removeEntry(int index)
    {
        allEntries.remove(index);
    }
}



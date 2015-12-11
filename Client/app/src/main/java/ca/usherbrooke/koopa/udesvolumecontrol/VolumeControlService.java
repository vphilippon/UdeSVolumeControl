package ca.usherbrooke.koopa.udesvolumecontrol;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.Vector;

public class VolumeControlService extends Service implements LocationListener
{
    private AudioManager audioMan;
    private String currentZoneName;
    private static final int MIN_BTW_UPDATE = 10 * 1000; // 10 sec
    private static final int DIS_BTW_UPDATE = 10; // 10 meter

    private Vector<VolumeEntry> allEntries;

    //START With ==> ComponentName service = startService(new Intent(getBaseContext(), VolumeControlService.class));
    @Override
    public void onCreate()
    {
        Toast.makeText(this, "Service : Started ", Toast.LENGTH_SHORT).show();
        audioMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        allEntries = new Vector<>();
//        /*TODO: temp Code*/
//        {
//
//            Location tempLoc = new Location(locMan.GPS_PROVIDER);
//            tempLoc.setLatitude(45.381);
//            tempLoc.setLongitude(-71.9272000);
//            allEntries.add(new VolumeEntry("DOWN", tempLoc  /*locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER)*/, 30, 0, 0));
//        }
//        {
//            Vector<Integer> temp = new Vector<Integer>();
//            Random rand = new Random();
//            for (int i = 0; i < audioMan.NUM_STREAMS; ++i)
//            {
//                temp.add(100);
//            }
//            Location tempLoc = new Location(locMan.GPS_PROVIDER);
//            tempLoc.setLatitude(45.38);
//            tempLoc.setLongitude(-71.9272);
//            allEntries.add(new VolumeEntry("UP", tempLoc  /*locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER)*/, 30, audioMan.getStreamMaxVolume(audioMan.STREAM_RING), audioMan.getStreamMaxVolume(audioMan.STREAM_NOTIFICATION)));
//        }
//        /*TODO: temp Code*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Service : Permissions are not granted ", Toast.LENGTH_SHORT).show();
            return;
        }
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_BTW_UPDATE,
                DIS_BTW_UPDATE, this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Toast.makeText(getBaseContext(), "New Latitude: " + location.getLatitude() + " New Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        for(VolumeEntry entry : allEntries)
        {
            if (entry.isInside(location, getBaseContext()))
            {
                if(!entry.getEntryName().equals(currentZoneName))
                {
                    currentZoneName = entry.getEntryName();
                    Toast.makeText(getBaseContext(), "Entering Zone " + currentZoneName, Toast.LENGTH_SHORT).show();
                    audioMan.setStreamVolume(AudioManager.STREAM_NOTIFICATION, entry.getNotificationVolume(), 0);
                    audioMan.setStreamVolume(AudioManager.STREAM_RING, entry.getRingtoneVolume(), 0);
                }
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
    }

    public void AddVolumeEntry(VolumeEntry entry)
    {
        allEntries.add(entry);
    }

    public void removeEntry(VolumeEntry entry)
    {
        allEntries.remove(entry);
    }


}



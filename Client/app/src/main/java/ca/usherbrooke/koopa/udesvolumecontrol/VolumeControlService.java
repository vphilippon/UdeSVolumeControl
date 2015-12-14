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
import android.os.Binder;
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
    private final IBinder binder = new VolumeControlServiceBinder();
    private Vector<VolumeEntry> allEntries;
    public class VolumeControlServiceBinder extends Binder
    {
        VolumeControlService getService() {
            return VolumeControlService.this;
        }
    }
    //START With ==> ComponentName service = startService(new Intent(getBaseContext(), VolumeControlService.class));
    @Override
    public void onCreate()
    {
        Toast.makeText(this, "Service : Started ", Toast.LENGTH_SHORT).show();
        audioMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);




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
        return binder;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        for(VolumeEntry entry : allEntries)
        {
            if (entry.isInside(location))
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
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    public void setAllEntries(Vector<VolumeEntry> newAllEntries)
    {
        Toast.makeText(this, "Service: " + newAllEntries.size() + " entries added", Toast.LENGTH_SHORT).show();
        allEntries = newAllEntries;
    }


}



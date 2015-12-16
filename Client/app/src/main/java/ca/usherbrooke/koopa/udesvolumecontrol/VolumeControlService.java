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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.Vector;

public class VolumeControlService extends Service implements LocationListener
{
    private AudioManager audioMan;
    private Integer currentZoneIndex = -1;
    private static final int MIN_BTW_UPDATE = 10 * 1000; // 10 sec
    private static final int DIS_BTW_UPDATE = 10; // 10 meter
    private final IBinder binder = new VolumeControlServiceBinder();
    private Vector<VolumeEntry> allEntries = new Vector<>();

    public class VolumeControlServiceBinder extends Binder
    {
        VolumeControlService getService() {
            return VolumeControlService.this;
        }
    }

    @Override
    public void onCreate()
    {
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
        synchronized (allEntries)
        {
            synchronized (currentZoneIndex)
            {
                for (VolumeEntry entry : allEntries)
                {
                    if (entry.isInside(location))
                    {
                        if (currentZoneIndex == -1 || !entry.getEntryName().equals(allEntries.elementAt(currentZoneIndex).getEntryName()))
                        {
                            currentZoneIndex = allEntries.indexOf(entry);

                            Toast.makeText(getBaseContext(), "Entering Zone " + entry.getEntryName(), Toast.LENGTH_SHORT).show();

                            SoundProfiles profile = entry.getSoundProfile();

                            switch (profile)
                            {
                                case SOUND:
                                    audioMan.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioMan.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
                                    audioMan.setStreamVolume(AudioManager.STREAM_RING, audioMan.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                                    audioMan.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                    break;
                                case VIBRATE:
                                    audioMan.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
                                    audioMan.setStreamVolume(AudioManager.STREAM_RING,0, 0);
                                    audioMan.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                    break;
                                case SILENT:
                                    audioMan.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
                                    audioMan.setStreamVolume(AudioManager.STREAM_RING,0, 0);
                                    audioMan.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                    break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {}

    @Override
    public void onProviderEnabled(String provider)
    {}

    @Override
    public void onProviderDisabled(String provider)
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setAllEntries(Vector<VolumeEntry> newAllEntries)
    {
        synchronized (allEntries)
        {
            synchronized (currentZoneIndex)
            {
                if(currentZoneIndex != -1)
                {
                    for (VolumeEntry entry : newAllEntries)
                    {
                        if(entry.getEntryName().equals(allEntries.elementAt(currentZoneIndex).getEntryName()))
                        {
                            currentZoneIndex = newAllEntries.indexOf(entry);
                            break;
                        }
                    }
                    Toast.makeText(VolumeControlService.this, "New index = " + currentZoneIndex, Toast.LENGTH_SHORT).show();
                }
                allEntries = newAllEntries;
            }
        }
    }

    @Nullable
    public VolumeEntry getCurrentVolumeEntry()
    {
        synchronized (allEntries)
        {
            synchronized (currentZoneIndex)
            {
                if (currentZoneIndex == -1)
                {
                    Toast.makeText(VolumeControlService.this, "Service has no clue where he is", Toast.LENGTH_SHORT).show();
                    return null;
                } else
                {
                    return allEntries.elementAt(currentZoneIndex);
                }
            }
        }
    }
}



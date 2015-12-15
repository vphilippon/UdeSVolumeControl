package ca.usherbrooke.koopa.udesvolumecontrol;

import android.content.Context;
import android.location.Location;
import android.media.AudioManager;

public class VolumeEntry
{
    private Location location;
    private int radius;
    private String entryName;
    private SoundProfiles mSoundProfile;

    public VolumeEntry(String name, Location loc, int rad,int ringtone, int notification, boolean vibration)
    {
        entryName = name;
        location = loc;
        radius = rad;
    }

    public boolean isInside(Location otherLocation)
    {
        float locationDist = location.distanceTo(otherLocation);
        return locationDist < radius;
    }

    public Location getLocation()
    {
        return location;
    }

    public int getRadius()
    {
        return radius;
    }

    public String getEntryName()
    {
        return entryName;
    }

    public Integer getRingtoneVolume()
    {
        return mSoundProfile == SoundProfiles.SOUND ? 100 : 0; // TODO getMaxValue for volume
    }

    public Integer getNotificationVolume()
    {
        return mSoundProfile == SoundProfiles.SOUND ? 100 : 0; // TODO getMaxValue for volume
    }

    public boolean doesVibrate()
    {
        return mSoundProfile == SoundProfiles.VIBRATE;
    }
}

package ca.usherbrooke.koopa.udesvolumecontrol;

import android.location.Location;

public class VolumeEntry
{
    private Location location;
    private int radius;
    private String entryName;
    private SoundProfiles mSoundProfile;

    public VolumeEntry(String name, Location loc, int rad, SoundProfiles soundProfile)
    {
        entryName = name;
        location = loc;
        radius = rad;
        mSoundProfile = soundProfile;
    }

    public boolean isInside(Location otherLocation)
    {
        float locationDist = location.distanceTo(otherLocation);
        return locationDist < radius;
    }


    public String getEntryName()
    {
        return entryName;
    }

    public SoundProfiles getSoundProfile()
    {
        return mSoundProfile;
    }
}

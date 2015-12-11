package ca.usherbrooke.koopa.udesvolumecontrol;

import android.location.Location;

import java.util.Vector;

/**
 * Created by meuj1902 on 2015-11-30.
 */
public class VolumeEntry
{
    private Integer  ringtoneVolume;
    private Integer  notificationVolume;
    private Location location;
    private int radius;
    private String entryName;

    public Integer getRingtoneVolume()
    {
        return ringtoneVolume;
    }

    public Integer getNotificationVolume()
    {
        return notificationVolume;
    }

    /*
            @param loc : location
            @param rad : radius in meter

         */
    public VolumeEntry(String name, Location loc, int rad,int ringtone, int notification)
    {
        entryName = name;
        ringtoneVolume = ringtone;
        notificationVolume = notification;
        location = loc;
        radius = rad;
    }

    public boolean isInside(Location otherLocation)
    {
        float locationDist = location.distanceTo(otherLocation);
        return locationDist < radius;
    }


    public String getEntryName() { return entryName; }
}

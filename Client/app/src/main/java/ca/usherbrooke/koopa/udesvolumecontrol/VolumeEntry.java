package ca.usherbrooke.koopa.udesvolumecontrol;

import android.content.Context;
import android.location.Location;

public class VolumeEntry
{
    private Integer  ringtoneVolume;
    private Integer  notificationVolume;
    private Location location;
    private int radius;
    private String entryName;

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
        return ringtoneVolume;
    }

    public Integer getNotificationVolume()
    {
        return notificationVolume;
    }
}

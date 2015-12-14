package ca.usherbrooke.koopa.udesvolumecontrol;

import android.location.Location;

public class VolumeEntry
{
    private Integer  ringtoneVolume;
    private Integer  notificationVolume;
    private Location location;
    private int radius;
    private String entryName;
    private boolean doesVibrate;

    public VolumeEntry(String name, Location loc, int rad,int ringtone, int notification, boolean vibration)
    {
        entryName = name;
        ringtoneVolume = ringtone;
        notificationVolume = notification;
        location = loc;
        radius = rad;
        doesVibrate = vibration;
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

    public boolean doesVibrate()
    {
        return doesVibrate;
    }
}

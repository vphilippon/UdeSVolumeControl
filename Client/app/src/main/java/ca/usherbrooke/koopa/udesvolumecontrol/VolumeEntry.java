package ca.usherbrooke.koopa.udesvolumecontrol;

import android.location.Location;

import java.util.Vector;

/**
 * Created by meuj1902 on 2015-11-30.
 */
public class VolumeEntry
{
    private Vector<Integer> allVolumes;
    private Location location;
    private int radius;
    private String entryName;

    /*
        @param loc : location
        @param rad : radius in meter
        @param volumes : volumes for thi device. see

     */
    public VolumeEntry(String name, Location loc, int rad,Vector<Integer> volumes)
    {
        entryName = name;
        allVolumes = volumes;
        location = loc;
        radius = rad;
    }

    public boolean isInside(Location otherLocation)
    {
        float locationDist = location.distanceTo(otherLocation);
        return locationDist < radius;
    }

    public Vector<Integer> getAllVolumes()
    {
        return allVolumes;
    }
    public String getEntryName() { return entryName; }
}

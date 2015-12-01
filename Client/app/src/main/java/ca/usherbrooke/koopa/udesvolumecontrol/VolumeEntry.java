package ca.usherbrooke.koopa.udesvolumecontrol;

import android.location.Location;

import java.util.Vector;

/**
 * Created by meuj1902 on 2015-11-30.
 */
public class VolumeEntry
{
    Vector<Integer> allVolumes;
    Location location;
    int radius;

    /*
        @param loc : location
        @param rad : radius in meter
        @param volumes : volumes for thi device. see

     */
    public VolumeEntry(Location loc, int rad,Vector<Integer> volumes)
    {
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
}

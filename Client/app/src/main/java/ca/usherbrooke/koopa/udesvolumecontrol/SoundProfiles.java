package ca.usherbrooke.koopa.udesvolumecontrol;

/**
 * Created by clom1806 on 2015-12-11.
 */
public enum SoundProfiles{
    SILENT(0), VIBRATE(1), SOUND(2);

    private final int value;
    private SoundProfiles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

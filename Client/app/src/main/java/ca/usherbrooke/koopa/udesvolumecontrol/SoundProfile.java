package ca.usherbrooke.koopa.udesvolumecontrol;

/**
 * Created by clom1806 on 2015-12-11.
 */
public class SoundProfile {


    public SoundProfile(SoundProfiles profile, Integer value){
        m_profile = profile;
        m_soundValue = value;
    }

    public SoundProfiles m_profile;
    public Integer m_soundValue;

}

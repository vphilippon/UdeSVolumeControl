package ca.usherbrooke.koopa.udesvolumecontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.VolumeConfig;


/**
 * Created by clom1806 on 2015-12-02.
 */
public class ListAdapter extends ArrayAdapter<VolumeConfig> {

        public ListAdapter(Context context, int resource, List<VolumeConfig> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.location_main, null);
            }

            VolumeConfig volumeConfig = getItem(position);

            if (volumeConfig != null) {

                TextView locationName = (TextView) v.findViewById(R.id.locationName);
                ImageView currentProfile = (ImageView) v.findViewById(R.id.currentSoundProfile);

                if(locationName != null){
                    locationName.setText(volumeConfig.getName());
                }

                if(currentProfile != null){

                    switch (SoundProfiles.values()[volumeConfig.getProfile()]){
                        case SILENT:
                            currentProfile.setImageResource(R.drawable.ic_silent);
                            break;
                        case SOUND:
                            currentProfile.setImageResource(R.drawable.ic_sound);
                            break;
                        case VIBRATE:
                            currentProfile.setImageResource(R.drawable.ic_vibrate);
                            break;
                        default:
                            assert false : "Add your new sound Profile here with an icon";
                            break;
                    }
                    currentProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }

            return v;
        }
}

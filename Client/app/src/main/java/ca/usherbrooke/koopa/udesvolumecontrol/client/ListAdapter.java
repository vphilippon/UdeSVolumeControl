package ca.usherbrooke.koopa.udesvolumecontrol.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.usherbrooke.koopa.udesvolumecontrol.common.Location;

/**
 * Created by clom1806 on 2015-12-02.
 */
public class ListAdapter extends ArrayAdapter<Location> {

        public ListAdapter(Context context, int resource, List<Location> items) {
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

            Location locationDetails = getItem(position);

            if (locationDetails != null) {

                TextView locationName = (TextView) v.findViewById(R.id.locationName);
                ImageView currentProfile = (ImageView) v.findViewById(R.id.currentSoundProfile);

                if(locationName != null){
                    locationName.setText(locationDetails.m_name);
                }

                if(currentProfile != null){

                    switch (locationDetails.m_profile.m_profile){
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

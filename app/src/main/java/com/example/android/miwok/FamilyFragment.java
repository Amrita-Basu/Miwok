package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

    private MediaPlayer mp;
    private AudioManager am;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK || focusChange == AUDIOFOCUS_LOSS) {

                if (mp != null) {
                    mp.release();
                    mp = null; // we cant resume playback for transient loss , because if a clip in another in-app fragment plays, that will also be a case of transient loss
                }               // so the previously truncated clip will follow after the currently selected clip finishees playing, and that isn't desirable
            }                  // so we should not handle the focus gain callback(which happens only for transient losses), because there would be a null pointer
            // so whenever focus is lost, the user would need to click an item explicitly which isn't inconvenient since our clips are short


        }
    };

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.words_list, container, false);
        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        ArrayList<Word> familyList = new ArrayList<>();

        familyList.add(new Word("Father", "әpә", R.drawable.family_father, R.raw.family_father));
        familyList.add(new Word("Mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        familyList.add(new Word("Son", "angsi", R.drawable.family_son, R.raw.family_son));
        familyList.add(new Word("Daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        familyList.add(new Word("Elder Brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        familyList.add(new Word("Younger Brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyList.add(new Word("Elder Sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        familyList.add(new Word("Younger Sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyList.add(new Word("Grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        familyList.add(new Word("Grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        //Log.v("NumbersActivity" , "Number at index 4: " + numbersList.get(7));

        final WordAdapter numbersAdapter = new WordAdapter(getActivity(), familyList, R.color.category_family);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(numbersAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) { // position corresponds to the the position of the object in our array adapter that this particular clicked list item is associated with currently
                if (mp != null) { // if at the time this click event has happened, there has been one or more clicks already and the mp is playing something currently because of those clicks
                    mp.release(); // the object is released at this point, so the previously playing clip stops exactly here and now mp is configured to play the current clip
                    mp = null;

                }


                int result = am.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { // the clip will only play if audio focus request is granted by some other app that may already be running// Otherwise, the user will need to click the list item again
                    mp = MediaPlayer.create(getActivity(), numbersAdapter.getItem(position).getAudioResourceId());
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            mp.release();
                            mp = null; // forcing immediate garbage collection, to be sure
                            //Log.v("NumbersActivity" , mp.toString());
                            am.abandonAudioFocus(onAudioFocusChangeListener);// once we no longer require the audio
                        }
                    });
                }

            }


        });


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mp != null) {
            mp.release();
            mp = null;

        }
        am.abandonAudioFocus(onAudioFocusChangeListener);

    }
}

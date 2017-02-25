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

import static android.media.AudioManager.AUDIOFOCUS_LOSS;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColoursFragment extends Fragment {
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


    public ColoursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        ArrayList<Word> coloursList = new ArrayList<>();

        coloursList.add(new Word("Red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        coloursList.add(new Word("Green", "chokokki", R.drawable.color_green, R.raw.color_green));
        coloursList.add(new Word("Brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        coloursList.add(new Word("Grey", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        coloursList.add(new Word("Black", "kululli", R.drawable.color_black, R.raw.color_black));
        coloursList.add(new Word("White", "kelelli", R.drawable.color_white, R.raw.color_white));
        coloursList.add(new Word("Dusty Yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        coloursList.add(new Word("Mustard Yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));


        //Log.v("NumbersActivity" , "Number at index 4: " + numbersList.get(7));

        final WordAdapter coloursAdapter = new WordAdapter(getActivity(), coloursList, R.color.category_colors);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(coloursAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) { // position corresponds to the the position of the object in our array adapter that this particular clicked list item is associated with currently
                if (mp != null) { // if at the time this click event has happened, there has been one or more clicks already and the mp is playing something currently because of those clicks
                    mp.release(); // the object is released at this point, so the previously playing clip stops exactly here and now mp is configured to play the current clip
                    mp = null;

                }


                int result = am.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { // the clip will only play if audio focus request is granted by some other app that may already be running// Otherwise, the user will need to click the list item again
                    mp = MediaPlayer.create(getActivity(), coloursAdapter.getItem(position).getAudioResourceId());
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

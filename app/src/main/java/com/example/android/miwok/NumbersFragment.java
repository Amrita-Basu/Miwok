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
public class NumbersFragment extends Fragment {
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


    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.words_list, container, false);

        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        ArrayList<Word> numbersList = new ArrayList<>();

        numbersList.add(new Word("One", "lutti", R.drawable.number_one, R.raw.number_one));
        numbersList.add(new Word("Two", "otiiko", R.drawable.number_two, R.raw.number_two));
        numbersList.add(new Word("Three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        numbersList.add(new Word("Four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        numbersList.add(new Word("Five", "massokkai", R.drawable.number_five, R.raw.number_five));
        numbersList.add(new Word("Six", "temmokka", R.drawable.number_six, R.raw.number_six));
        numbersList.add(new Word("Seven", "kanekaku", R.drawable.number_seven, R.raw.number_seven));
        numbersList.add(new Word("Eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        numbersList.add(new Word("Nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        numbersList.add(new Word("Ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));

        //Log.v("NumbersActivity" , "Number at index 4: " + numbersList.get(7));

        final WordAdapter numbersAdapter = new WordAdapter(getActivity(), numbersList, R.color.category_numbers);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(numbersAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) { // position corresponds to the the position of the object in our array adapter that this particular clicked list item is associated with currently
                if (mp != null) { // if the mp is already playing a clip because of  an immediately previous click, this check cuts off that clip and configures the mp to play the current clip
                    mp.release();
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

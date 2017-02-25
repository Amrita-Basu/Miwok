package com.example.android.miwok;

/**
 * Created by DELL on 18-02-2017.
 */

public class Word {

    private String englishWord;
    private String miwokWord;
    private int imageResourceId;
    private int audioResourceId;

    public Word(String eng, String miwok, int audio) {
        englishWord = eng;
        miwokWord = miwok;
        audioResourceId = audio;
    }

    public Word(String eng, String miwok, int image, int audio) {
        englishWord = eng;
        miwokWord = miwok;
        imageResourceId = image;
        audioResourceId = audio;

    }

    public int getImageResourceId(){

        return imageResourceId;
    }

    public String getEnglishWord() {

        return englishWord;
    }


    public String getMiwokWord() {


        return miwokWord;

    }
    public int getAudioResourceId() {


        return audioResourceId;

    }



}

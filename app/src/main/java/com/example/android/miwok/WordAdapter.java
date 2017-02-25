package com.example.android.miwok;

import android.app.Activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DELL on 18-02-2017.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int colourResourceId ;

    public WordAdapter(Activity context, ArrayList<Word> l , int colourId) {

        super(context, 0, l);
        colourResourceId = colourId ;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // parent here is our ListView viewgroup// passed automatically from the caller ListView calling the getView() of the ArrayAdapter as and when it needs particular populated list items to be displayed

        //int bgcolour = ContextCompat.getColor(getContext() , colourResourceId); // getting the int form of the colour represented by this colour resource id
       //  parent.setBackgroundResource(colourResourceId); // passing  the colour resource ID directly. however, this would apply and reapply the same bgcolour to the list view each time the getView method is called for a particular context

        //Log.v("NumbersActivity" , "The colour is:" + bgcolour);
        View v = convertView; // think of v as an instance of our custom list item view layout// it refers to the root Linear Layout of our custom List item layout file
        if (v == null) { // loading for the first time, all views on screen are uninflated. as and when views go into scrap, they become used and would not need to be inflated and can simply be reused
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false); // v is inflated using our own custom list item layout file or set of instructions; so now v has two text views just as we specified

        }

        Word w = getItem(position); // the word that needs to be displayed// getItem() fetches the reqd object sitting at the given position in the datalist residing inside the adapter//in our case it is the Word obj sitting at the given position in our ArrayList of Word objects
        TextView en = (TextView) v.findViewById(R.id.engnum);
        en.setText(w.getEnglishWord()); // setting the text of the first textview to the value of the eng word in the current instance of the Word class
        TextView m = (TextView) v.findViewById(R.id.miwoknum);
        m.setText(w.getMiwokWord()); //setting the text of the first textview to the value of the miwok word in the current instance of the Word class

        ImageView im = (ImageView)v.findViewById(R.id.image_view);
        ImageView bt = (ImageView)v.findViewById(R.id.play_button);

       if(w.getImageResourceId()== 0){ // if no associated image, then image view should not occupy any space in the layout
           im.setVisibility(View.GONE);


       }
       else {
           im.setImageResource(w.getImageResourceId());
       }
        // setting the background colour of each list item to the appropriate colour , as and when it needs to be displayed
        RelativeLayout listItemParent = (RelativeLayout)v.findViewById(R.id.list_item_container) ; // finding the parent viewgroup of the list item
      listItemParent.setBackgroundResource(colourResourceId);

        return v;
    }


}

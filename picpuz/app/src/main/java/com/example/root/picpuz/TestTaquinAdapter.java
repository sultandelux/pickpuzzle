package com.example.root.picpuz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by jdavid on 27/10/2017.
 */

public class TestTaquinAdapter extends BaseAdapter {

    private Context mContext;

    /**
     * Keep the original order of chunks
     * Do not modify it during the game
     */
    private Bitmap[] chunks;
    private int gridWidth;
    /**
     * Shuffled chunks on which we are playing
     */
    private Bitmap[] shuffled;

    private Bitmap chunksh; private int zeropos=0;

    public TestTaquinAdapter(Context mContext, Bitmap picture, int gridWidth, int disHeight, int disWidth) {
        this.gridWidth=gridWidth;
        int pw = picture.getWidth();
        int ph = picture.getHeight();
        picture=Bitmap.createScaledBitmap(picture, disHeight, disWidth, false);
        /*if(ph>disHeight){
            if (ph*disWidth>pw*disHeight){
                picture.setHeight(disHeight);
                picture.setWidth((int)(pw*((double)(disHeight/ph))));
            }
        else if (pw>disWidth){
                picture.setHeight((int)(ph*((double)(disWidth/pw))));
                picture.setWidth(disWidth);
            }
        }*/

        this.mContext=mContext;
        chunks = new Bitmap[gridWidth*gridWidth];
        int chunkWidth = picture.getWidth()/gridWidth;
        int chunkHeight = picture.getHeight()/gridWidth;
        for (int i=1 ; i<chunks.length ; i++) {
            chunks[i] = Bitmap.createBitmap(picture,(i%gridWidth)*chunkWidth,(i/gridWidth)*chunkHeight, chunkWidth, chunkHeight);
            //chunks[i]= picture;
            //chunks[i]= Bitmap.createBitmap(...) ; It is up to you to find the solution :-)
        }
       // Toast.makeText(mContext,"Bitmap size = "+chunks.length,Toast.LENGTH_LONG).show();
        chunks[0] = Bitmap.createBitmap(chunkWidth, chunkHeight, Bitmap.Config.ALPHA_8);
        shuffle();
    }

    public void shuffle() {
        shuffled = Arrays.copyOf(chunks, chunks.length);
        Random r = new Random();
        for (int i = chunks.length - 1; i > 0; i--)
        {

            int index = r.nextInt(gridWidth*gridWidth);

            // you can cross if it isn't necessary
            while (index==i) {
                //r = new Random();
                index = r.nextInt(gridWidth*gridWidth);
            }
            chunksh = shuffled[index];
            shuffled[index] = shuffled[i];
            shuffled[i] = chunksh;
        }


            while (!shuffled[zeropos].equals(chunks[0])){
                zeropos+=1;
            }

        // You have to do a loop in order to switch chunks a random number of times
    }

    @Override
    public int getCount() {
        return shuffled.length;
    }

    @Override
    public Object getItem(int i) {
        return shuffled[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        } else {
            imageView = (ImageView) view;
        }

        imageView.setImageBitmap(shuffled[i]);
        return imageView;

    }


    public void move(int i, int j){
        if((i%j==0 && (i+1==zeropos) ||
           ((i%j!=0&&i%j!=j-1) && (i+1==zeropos||i-1==zeropos)) ||
           (i%j==j-1 && (i-1==zeropos)) ||
           (i+j==zeropos)||(i-j==zeropos))
           )
        {
            chunksh=shuffled[zeropos];
            shuffled[zeropos]=shuffled[i];
            shuffled[i]=chunksh;
            zeropos=i;
        }
    }

    public boolean gameover(){
        boolean b=true;
        for (int i=0; i<gridWidth*gridWidth; i++){
            if (!shuffled[i].equals(chunks[i]))
            {b=false; break; }}
        return b;
    }

    public int zp(){return zeropos;}

    /*public void Resize(){
        int Bridgesize=chunks[0].getWidth();
        for (Bitmap bm:chunks){
            bm.setWidth(bm.getHeight());
            bm.setWidth(Bridgesize);
        }
    }*/

}

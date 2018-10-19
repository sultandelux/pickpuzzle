package com.example.root.picpuz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.InputStream;


public class TestLoadImage extends AppCompatActivity {

    private static final int PICK_PICTURE = 0;
    private int gridWidth=3;
    private void loadPicture(Uri pUri) {
        //Toast.makeText(this, pUri.toString(), Toast.LENGTH_LONG).show();
        try {
            //gridWidth=2;
            InputStream is = getContentResolver().openInputStream(pUri);
            Bitmap img = BitmapFactory.decodeStream(is);//,null, options);
            //
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();

            final TestTaquinAdapter adapter = new TestTaquinAdapter(this, img, gridWidth, width, height);
            final GridView grid = (GridView) findViewById(R.id.magrille);

            grid.setVisibility(View.VISIBLE);
            grid.setAdapter(adapter);
            grid.setNumColumns(gridWidth);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i(""+view.getId(), "ads "+ i + " " + adapter.zp());
                    adapter.move(i, gridWidth);
                    grid.setAdapter(adapter);
                    if (adapter.gameover()){
                        grid.setEnabled(false);
                        System.out.println("GG");
                        }
                }
            });

            //grid.setVisibility(View.INVISIBLE);
            //options.setVisibility(View.VISIBLE);


            //Toast.makeText(this,"Bitmap size = " + grid.getAdapter().getCount(), Toast.LENGTH_LONG).show();
            //Bitmap.createBitmap(picture, cWidth * col, cHeight * row, cWidth, cHeight);


        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_load_image);
        final LinearLayout options = findViewById(R.id.options);
        final GridView grid1 = (GridView) findViewById(R.id.magrille);
        grid1.setVisibility(View.INVISIBLE);
        Button btn = findViewById(R.id.button3);

        options.setEnabled(true);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {   //grid.setVisibility(View.VISIBLE);
                //grid.setVisibility(View.VISIBLE);
                final RadioButton rb3 = findViewById(R.id.radioButton3);
                final RadioButton rb4 = findViewById(R.id.radioButton4);
                final RadioButton rb5 = findViewById(R.id.radioButton5);
                final RadioButton rb6 = findViewById(R.id.radioButton6);
                if (rb3.isChecked()){
                    gridWidth=3;
                }
                else if (rb4.isChecked()){
                    gridWidth=4;
                }
                else if (rb5.isChecked()){
                    gridWidth=5;
                }
                else if (rb6.isChecked()){
                    gridWidth=6;
                }
                options.setVisibility(View.INVISIBLE);
                Choosepicture();
            }
        });

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_main);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri pUri = data.getData();
        if (pUri!=null) {
            loadPicture(pUri);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public void Choosepicture(){
        Uri pUri = this.getIntent().getData();
        if (pUri == null) {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            this.startActivityForResult(i, PICK_PICTURE);
        } else {
            loadPicture(pUri);
        }
    }
}

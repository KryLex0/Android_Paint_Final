package com.example.mypaintv0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    PaintView paintView;
    public ProgressBar progressBar;
    public SeekBar seekbar_cursor;
    public TextView seekbar_value;
    public static int sizeCursor;
    private Activity2 activity2Obj;
    Bitmap mBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);



    }

    public int getPaintSizeCursor() {
        return paintView.getStrokeWidth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 222 && resultCode == RESULT_OK) {
            int xSize = (data.getIntExtra("taille", paintView.getStrokeWidth()));
            paintView.setStrokeWidth(xSize);
            int xColor = (data.getIntExtra("couleur", paintView.getColor()));
            paintView.setColor(xColor);
        }
        if(requestCode == 333 && resultCode == RESULT_OK){
            Log.i("Image:", "selectionne");
            Intent intent = this.getIntent();

            //Uri imageSelected = data.getParcelableExtra(Intent.EXTRA_STREAM);
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageSelected);
            try {
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                BitmapDrawable background = new BitmapDrawable(bitmap);
                //Drawable dr = new BitmapDrawable(bitmap);
                paintView.setBackground(background);
            } catch (IOException e) {
                e.printStackTrace();
            }


            /*
            Uri selectedImage = data.getData();
            Log.e("test", selectedImage.toString());

            PaintView img = findViewById(R.id.paintView);
            img.setBack(selectedImage);
*/

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                paintView.normal();
                return true;
                /*
            case R.id.emboss:       //pas fluide
                paintView.emboss();
                return true;
                */
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
            case R.id.option:
                openActivity2();
                return true;
            case R.id.credit:
                openActivity3();
                return true;
            case R.id.gomme:
                paintView.effacer();
                return true;

            case R.id.save:
                Log.i("size = ", String.valueOf(paintView.getmBitmap()));

                paintView.storeImage(paintView.getmBitmap());
                Toast.makeText(this, "Image sauvegarde dans " + paintView.getOutputMediaFile().toString(),
                        Toast.LENGTH_LONG).show();
                return true;


                /*
            case R.id.color_palet:

                return true;
*/


            case R.id.import_img:
                importImage();
                return true;
                /*
            case R.id.color_pot:
                //openActivity3();
                return true;
*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void importImage(){
        Intent myIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult((myIntent), 333);

    }

    public void openActivity2() {
        Intent intent = new Intent(getApplicationContext(), Activity2.class);
        intent.putExtra("tailleCurs", paintView.getStrokeWidth());
        intent.putExtra("couleurCurs", paintView.getColor());
        startActivityForResult((intent), 222);
    }

    public void openActivity3() {
        Intent intent = new Intent(getApplicationContext(), Activity3.class);
        //startActivityForResult((intent), 333);
        startActivity(intent);
    }
}
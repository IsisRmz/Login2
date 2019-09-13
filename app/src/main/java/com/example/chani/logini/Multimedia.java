package com.example.chani.logini;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Multimedia extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);
    }


    public void tomarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_TAKE_PHOTO:
                Date date = new Date();
                Bitmap picture = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.PNG, 0, arrayOutputStream);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"nombre"+date.getTime()+date.getHours()+date.getMinutes()+date.getSeconds()+".png");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(arrayOutputStream.toByteArray());
                    Toast.makeText(getApplicationContext(), "OKS", Toast.LENGTH_LONG).show();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    Log.d("CAMARAAA", e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("CAMARAAA", e.getMessage());
                    e.printStackTrace();
                }

        }
    }

    public void playMusic(View view) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
        startActivity(intent);
    }
}

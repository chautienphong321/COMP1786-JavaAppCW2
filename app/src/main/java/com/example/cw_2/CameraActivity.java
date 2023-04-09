package com.example.cw_2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1000;
    private ImageView photoView;
    private static final int MY_CAMERA_PERMISSION_CODE = 1001;

    private Bitmap photo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        setTitle(getIntent().getStringExtra("title"));

        photoView = findViewById(R.id.photoView);

        Button btnTakeAPhoto = findViewById(R.id.btnTakeAPhoto);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSavePhoto = findViewById(R.id.btnSavePhoto);

        btnCancel.setOnClickListener(v -> finish());
        btnTakeAPhoto.setOnClickListener(v -> takeAPhoto());
        btnSavePhoto.setOnClickListener(v -> savePhoto());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takeAPhoto(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            photoView.setImageBitmap(photo);
        }
    }

    private void savePhoto(){
        try{
            Date date = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("yyyyMMDDHHmmss");
            String filename = df.format(date);

            FileOutputStream stream = this.openFileOutput(filename, MODE_PRIVATE);
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

            stream.close();
            photo.recycle();

            Intent intent = new Intent();
            intent.putExtra("photo", filename);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
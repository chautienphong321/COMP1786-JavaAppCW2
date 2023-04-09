package com.example.cw_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected static final String fileName = "list_img.txt";

    private ArrayList<String> imgList;

    private ImageView imgView;
    private int currentIndex;
    private TextView imgIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.imageView);
        imgIndex = findViewById(R.id.imgIndex);

        Button btnNext = findViewById(R.id.btnNext);
        Button btnPrev = findViewById(R.id.btnPrev);

        btnNext.setOnClickListener(v -> nextImg());
        btnPrev.setOnClickListener(v -> prevImg());

        ImageButton btnIntentAdd = findViewById(R.id.btnIntentAdd);
        btnIntentAdd.setOnClickListener(v -> intentAdd());

        ImageButton btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(v -> cameraClicked());

        imgList = getImgList();
        currentIndex = 0;

        loadImg();
    }

    public ArrayList<String> getImgList(){
        ArrayList<String> arrImg = new ArrayList<>();
        arrImg.add("https://e.khoahoc.tv/photos/image/2016/06/24/kim-tu-thap-650.jpg");
        arrImg.add("https://toplist.vn/images/800px/thap-nghieng-pisa-489711.jpg");

        getImageListFromFile(arrImg);
        Toast.makeText(this, "Get images successfully !!!", Toast.LENGTH_SHORT).show();

        return arrImg;
    }

    public void loadImg(){
        if (imgList == null || imgList.size() == 0) {
            Toast.makeText(this, "No image available !!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(imgList.get(currentIndex).contains("https")){
            Picasso.get().load(imgList.get(currentIndex)).into(imgView);
        } else {
            String photoName = imgList.get(currentIndex);
            File file = new File(getFilesDir(), photoName);
            imgView.setImageURI(Uri.parse(file.getAbsolutePath()));
        }
        imgIndex.setText("Image "+ (currentIndex + 1));
    }

    private void nextImg() {
        currentIndex += 1;
        // Check if it is the last img
        if (imgList.size() == currentIndex){
            currentIndex = 0;
        }
        loadImg();
    }

    private void prevImg() {
        currentIndex -= 1;
        // Check if it is the first img
        if (currentIndex == -1){
            currentIndex = imgList.size() - 1;
        }
        loadImg();
    }

    private void getImageListFromFile(ArrayList<String> imageList) {
        try {
            InputStream inputStream = openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String url = "";
                while ((url = bufferedReader.readLine()) != null) {
                    imageList.add(url);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Image not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void intentAdd(){
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("title", "Add image");
        startActivityForResult(intent, 123);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            addImgToList(data.getStringExtra("link"));
        } else if(resultCode == Activity.RESULT_OK && requestCode == 456){
            String photo = data.getStringExtra("photo");
            savePhotoToFile(photo);
        }
        else {
            Toast.makeText(this, "Add failure !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addImgToList(String link){
        imgList.add(link);
        writeURLToFile(link);
        Toast.makeText(this, "Add successful !!!", Toast.LENGTH_SHORT).show();
    }

    protected void writeURLToFile(String url) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, MODE_APPEND));
            outputStreamWriter.write(url);
            outputStreamWriter.write(10); // write line - 10 == line break (ASCII)
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void cameraClicked(){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("title", "Take photo");
        startActivityForResult(intent, 456);
    }

    private void savePhotoToFile(String photo){
        imgList.add(photo);
        Toast.makeText(this, "Save successful !!!", Toast.LENGTH_SHORT).show();
    }
}
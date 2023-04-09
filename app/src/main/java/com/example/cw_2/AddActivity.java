package com.example.cw_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class AddActivity extends AppCompatActivity {

    private SearchView searchView;
    private ImageView searchImageView;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String title = getIntent().getStringExtra("title");
        setTitle(title);

        searchView = findViewById(R.id.searchView);
        searchImageView = findViewById(R.id.searchImageView);
        // Example image
        searchView.setQuery("https://sp-ao.shortpixel.ai/client/q_glossy,ret_img,w_598,h_918/https://hungphatsaigon.vn/wp-content/uploads/2022/07/10_hinh-nen-gau-cute.jpg", false);

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnFind = findViewById(R.id.btnFind);

        btnCancel.setOnClickListener(v -> finish());
        btnAdd.setOnClickListener(v -> addImgToList());
        btnFind.setOnClickListener(v -> findImgFromLink());
    }

    private void findImgFromLink(){
        query = searchView.getQuery().toString();
        if(query.length() == 0) {
            Toast.makeText(this, "You need to input URL !", Toast.LENGTH_SHORT).show();
        } else {
            Picasso.get().load(query).error(R.drawable.notfound).into(searchImageView);
            Toast.makeText(this, "Finding", Toast.LENGTH_SHORT).show();
        }
        searchView.clearFocus();
    }

    private void addImgToList(){
        query = searchView.getQuery().toString();
        Intent intent = new Intent();
        intent.putExtra("link", query);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
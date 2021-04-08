package com.example.cisnewsapp.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.cisnewsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class SpecificPostActivity extends AppCompatActivity {

    private TextView titleView;
    private TextView authorView;
    private TextView categoryView;
    private TextView dateView;
    private TextView infoView;
    private Button signOut;
    private Button goBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String title;
    private String author;
    private String category;
    private String date;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_post);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        titleView = findViewById(R.id.titleView);
        authorView = findViewById(R.id.authorView);
        categoryView = findViewById(R.id.categoryView);
        dateView = findViewById(R.id.dateView);
        infoView = findViewById(R.id.infoView);
        goBack = findViewById(R.id.goBack);
        signOut = findViewById(R.id.signOut);

        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        category = getIntent().getStringExtra("category");
        date = getIntent().getStringExtra("date");
        info = getIntent().getStringExtra("info");

        this.titleView.setText(title);
        this.authorView.setText("Author : " + author);
        this.categoryView.setText("Category : " + category);
        this.dateView.setText("Date : " + date);
        this.infoView.setText("Info : " + info);
    }
}
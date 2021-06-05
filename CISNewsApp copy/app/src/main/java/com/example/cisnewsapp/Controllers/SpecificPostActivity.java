package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SpecificPostActivity extends AppCompatActivity {

    private TextView titleView;
    private TextView authorView;
    private TextView categoryView;
    private TextView dateView;
    private TextView infoView;
    private Button signOut;
    private Button goBack;
    private Button seenPost;
    private Button starButton;
    private Button approveButton;
    private Button rejectButton;
    private Button submitReject;
    private EditText rejectEditText;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String title;
    private String author;
    private String category;
    private String date;
    private String info;
    private String id;
    private String approvalStatus;

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
        seenPost = findViewById(R.id.seenPost);
        starButton = findViewById(R.id.starButton);
        approveButton = findViewById(R.id.approveButton);
        rejectButton = findViewById(R.id.rejectButton);
        rejectEditText = findViewById(R.id.rejectEditText);
        submitReject = findViewById(R.id.submitRejection);

        approveButton.setVisibility(View.INVISIBLE);
        rejectButton.setVisibility(View.INVISIBLE);
        rejectEditText.setVisibility(View.INVISIBLE);
        submitReject.setVisibility(View.INVISIBLE);

        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        category = getIntent().getStringExtra("category");
        date = getIntent().getStringExtra("date");
        info = getIntent().getStringExtra("info");
        id = getIntent().getStringExtra("id");
        approvalStatus = getIntent().getStringExtra("approval");

        this.titleView.setText(title);
        this.authorView.setText("Author : " + author);
        this.categoryView.setText("Category : " + category);
        this.dateView.setText("Date : " + date);
        this.infoView.setText("Info : " + info);

        setUpButtons();
    }

    public void setUpButtons()
    {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    if (String.valueOf(user.getUserType()).equals("Admin") && approvalStatus.equals("awaiting")) {
                        approveButton.setVisibility(View.VISIBLE);
                        rejectButton.setVisibility(View.VISIBLE);
                        seenPost.setVisibility(View.INVISIBLE);
                        starButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    public void backToMain (View v) {
        Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
        startActivity(nextScreen);
    }

    public void seenPost(View v) {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    ArrayList<String> seenPosts = user.getSeenPosts();
                    seenPosts.add(id);
                    user.setSeenPosts(seenPosts);
                    firestore.collection("users").document(user.getUid()).set(user);

                    Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(nextScreen);
                }
            }
        });
    }

    public void starPost(View v) {
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);
                    ArrayList<String> starredPosts = user.getStarredPosts();
                    starredPosts.add(id);
                    user.setStarredPosts(starredPosts);
                    firestore.collection("users").document(user.getUid()).set(user);

                    Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(nextScreen);
                }
            }
        });
    }

    public void approve(View v) {
        firestore.collection("Posts").document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    Post post = ds.toObject(Post.class);
                    post.setApprovalStatus("approved");

                    Date date = post.getPostDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                    cal.set(Calendar.HOUR_OF_DAY, 7);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    date = cal.getTime();
                    post.setPostDate(date);

                    firestore.collection("Posts").document(title).set(post);
                    Intent intent = new Intent(getBaseContext(), ModActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void reject(View v) {
        rejectEditText.setVisibility(View.VISIBLE);
        submitReject.setVisibility(View.VISIBLE);
        firestore.collection("Posts").document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    Post post = ds.toObject(Post.class);
                    post.setApprovalStatus("rejected");

                    firestore.collection("Posts").document(title).set(post);

                }
            }
        });
    }
}
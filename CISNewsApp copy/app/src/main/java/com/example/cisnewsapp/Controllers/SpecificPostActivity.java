package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cisnewsapp.Models.AcademicsPost;
import com.example.cisnewsapp.Models.Admin;
import com.example.cisnewsapp.Models.CCAPost;
import com.example.cisnewsapp.Models.MiscPost;
import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.Models.ServicePost;
import com.example.cisnewsapp.Models.SportsPost;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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
    private TextView emailView;
    private Button signOut;
    private Button goBack;
    private Button seenPost;
    private Button starButton;
    private Button approveButton;
    private Button rejectButton;
    private Button submitReject;
    private EditText rejectEditText;
    private ImageView accessedImage;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    public StorageReference mStorageRef;
    public DatabaseReference mDatabaseRef;

    private String title;
    private String author;
    private String category;
    private String date;
    private String email;
    private String info;
    private String id;
    private String approvalStatus;
    private String picURL;
    private String recipient;


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
        emailView = findViewById(R.id.emailView);
        goBack = findViewById(R.id.goBack);
        signOut = findViewById(R.id.signOut);
        seenPost = findViewById(R.id.seenPost);
        starButton = findViewById(R.id.starButton);
        approveButton = findViewById(R.id.approveButton);
        rejectButton = findViewById(R.id.rejectButton);
        rejectEditText = findViewById(R.id.rejectEditText);
        submitReject = findViewById(R.id.submitRejection);
        accessedImage = findViewById(R.id.accessedImage);

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
        picURL = getIntent().getStringExtra("url");
        email = getIntent().getStringExtra("email");

        this.titleView.setText(title);
        this.authorView.setText("Author : " + author);
        this.categoryView.setText("Category : " + category);
        this.infoView.setText("Info : " + info);
        this.emailView.setText("Contact email: " + email);
        //this.dateView.setText("Date : " + date);
        dateView.setVisibility(View.INVISIBLE);

        setUpButtons();
        displayImage();
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

    public void displayImage()
    {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getImageURL().equals(picURL)) {
                        Glide.with(getApplicationContext())
                                .load(upload.getImageURL())
                                .into(accessedImage);
                        accessedImage.getLayoutParams().width = 250;
                        accessedImage.getLayoutParams().height = 250;
                        accessedImage.setAdjustViewBounds(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecificPostActivity.this, "bruh", Toast.LENGTH_SHORT).show();
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
        FirebaseUser mUser = mAuth.getCurrentUser();
        firestore.collection("Posts").document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Calendar cal = Calendar.getInstance();
                    DocumentSnapshot ds = task.getResult();
                    Post post = ds.toObject(Post.class);
                    if (post.getPostCategory().equals("CCA")) {
                        CCAPost ccaPost = ds.toObject(CCAPost.class);
                        ccaPost.setApprovalStatus("approved");
                        Date date = ccaPost.getPostDate();
                        cal.setTime(date);
                        cal.set(Calendar.HOUR_OF_DAY, 7);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                        date = cal.getTime();
                        ccaPost.setPostDate(date);
                        firestore.collection("Posts").document(title).set(ccaPost);
                    }
                    else if (post.getPostCategory().equals("Service")) {
                        ServicePost servicePost = ds.toObject(ServicePost.class);
                        Date date = servicePost.getPostDate();
                        cal.setTime(date);
                        cal.set(Calendar.HOUR_OF_DAY, 7);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                        date = cal.getTime();
                        servicePost.setPostDate(date);
                        servicePost.setApprovalStatus("approved");
                        firestore.collection("Posts").document(title).set(servicePost);
                    }
                    else if (post.getPostCategory().equals("Sports")) {
                        SportsPost sportsPost = ds.toObject(SportsPost.class);
                        Date date = sportsPost.getPostDate();
                        cal.setTime(date);
                        cal.set(Calendar.HOUR_OF_DAY, 7);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                        date = cal.getTime();
                        sportsPost.setPostDate(date);
                        sportsPost.setApprovalStatus("approved");
                        firestore.collection("Posts").document(title).set(sportsPost);
                    }
                    else if (post.getPostCategory().equals("Academics")) {
                        AcademicsPost academicsPost = ds.toObject(AcademicsPost.class);
                        Date date = academicsPost.getPostDate();
                        cal.setTime(date);
                        cal.set(Calendar.HOUR_OF_DAY, 7);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                        date = cal.getTime();
                        academicsPost.setPostDate(date);
                        academicsPost.setApprovalStatus("approved");
                        firestore.collection("Posts").document(title).set(academicsPost);
                    }
                }
            }
        });
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    Admin user = ds.toObject(Admin.class);
                    int approved = user.getApprovedPosts();
                    approved+=1;
                    user.setApprovedPosts(approved);
                    firestore.collection("users").document(user.getUid()).set(user);

                    sendEmailApproved();
                }
            }
        });
    }

    public void reject(View v) {
        FirebaseUser mUser = mAuth.getCurrentUser();
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
        firestore.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    Admin admin = ds.toObject(Admin.class);
                    int denied = admin.getDeniedPosts();
                    denied+=1;
                    System.out.println("new denied: " + denied);
                    admin.setDeniedPosts(denied);
                    System.out.println("stored denied: " + denied);
                    firestore.collection("users").document(admin.getUid()).set(admin);

                    //Intent nextScreen = new Intent(getBaseContext(), MainActivity.class);
                    //startActivity(nextScreen);
                }
            }
        });
    }

    public void sendEmailRejected(View v) {
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                        User user = ds.toObject(User.class);
                        if (user.getName().equals(author)) {
                            recipient = user.getEmail();
                            String[] recipientList = new String[1];
                            recipientList[0] = recipient;
                            String subject = "Your post to CIS News has been rejected";
                            String message = "Your post " + title + " has been rejected. Reason: " + rejectEditText.getText().toString();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_EMAIL, recipientList);
                            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            intent.putExtra(Intent.EXTRA_TEXT, message);
                            intent.setType("message/rfc822");
                            startActivity(Intent.createChooser(intent, "Choose an email client"));
                        }
                    }
                }
            }
        });
    }

    public void sendEmailApproved() {
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                        User user = ds.toObject(User.class);
                        if (user.getName().equals(author)) {
                            String recipient = user.getEmail();
                            String[] recipientList = new String[1];
                            recipientList[0] = recipient;
                            System.out.println(recipient);
                            String subject = "Your post to CIS News has been approved";
                            String message = "Your post " + title + " has been approved. It will be posted at 7am tomorrow. Log on to see it!";
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_EMAIL, recipientList);
                            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            intent.putExtra(Intent.EXTRA_TEXT, message);
                            intent.setType("message/rfc822");
                            startActivity(Intent.createChooser(intent, "Choose an email client"));
                        }
                    }
                }
            }
        });
    }
}
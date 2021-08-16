package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cisnewsapp.Models.CCAPost;
import com.example.cisnewsapp.Models.Post;
import com.example.cisnewsapp.Models.ServicePost;
import com.example.cisnewsapp.Models.SportsPost;
import com.example.cisnewsapp.Models.Student;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that retrieves data from the database in order to
 * display analytics including the number of posts of each
 * category within the app, each user type's posting activity,
 * and the percentage of posts accepted and rejected.
 *
 * Author: Joson Suen
 * Version: 1.0
 */
public class PostsDataAnalytics extends AppCompatActivity {

    public TextView ccaCount;
    public TextView academicsCount;
    public TextView serviceCount;
    public TextView miscCount;
    public TextView sportsCount;
    public TextView year7Posts;
    public TextView year8Posts;
    public TextView year9Posts;
    public TextView year10Posts;
    public TextView year11Posts;
    public TextView year12Posts;
    public TextView year13Posts;
    public TextView teacherPosts;
    public TextView adminPosts;
    public TextView popularSport;
    public TextView frequentServiceDay;
    public TextView frequentCCADay;
    public TextView acceptedPercentage;
    public TextView rejectedPercentage;

    public int year7PostCount;
    public int year8PostCount;
    public int year9PostCount;
    public int year10PostCount;
    public int year11PostCount;
    public int year12PostCount;
    public int year13PostCount;
    public int teacherPostCount;
    public int adminPostCount;

    public int sportsPostCount;
    public int ccaPostCount = 0;
    public int academicsPostCount = 0;
    public int servicePostCount = 0;
    public int miscPostCount = 0;

    public double accepted = 0;
    public double rejected = 0;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private Map<String, Integer> sports = new HashMap<String, Integer>();
    private Map<String, Integer> ccaDays = new HashMap<String, Integer>();
    private Map<String, Integer> serviceDays = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_data_analytics);

        year7Posts = findViewById(R.id.year7Posts);
        year8Posts = findViewById(R.id.year8Posts);
        year9Posts = findViewById(R.id.year9Posts);
        year10Posts = findViewById(R.id.year10Posts);
        year11Posts = findViewById(R.id.year11Posts);
        year12Posts = findViewById(R.id.year12Posts);
        year13Posts = findViewById(R.id.year13Posts);
        teacherPosts = findViewById(R.id.teacherPosts);
        adminPosts = findViewById(R.id.adminPosts);
        ccaCount = findViewById(R.id.ccaCount);
        academicsCount = findViewById(R.id.academicsCount);
        serviceCount = findViewById(R.id.serviceCount);
        miscCount = findViewById(R.id.miscCount);
        sportsCount = findViewById(R.id.sportsCount);
        popularSport = findViewById(R.id.popularSport);
        frequentServiceDay = findViewById(R.id.frequentServiceDay);
        frequentCCADay = findViewById(R.id.frequentCCADay);
        acceptedPercentage = findViewById(R.id.acceptedPercentage);
        rejectedPercentage = findViewById(R.id.rejectedPercentage);

        setUpYearViews();
        setUpCategoryViews();
    }
    /**
     * Method that sets up the TextViews that show how
     * many posts in total were created by each year group
     * of students (as well as by teachers and admins)
     *
     */
    public void setUpYearViews()
    {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                        User user = ds.toObject(User.class);
                        int postsNum = user.getCreatedPosts().size();
                        if (user.getUserType().equals("Student")) {
                            Student student = ds.toObject(Student.class);
                            switch (student.getYearGroup()) {
                                case "Year 7":
                                    year7PostCount+=postsNum;
                                case "Year 8":
                                    year8PostCount+=postsNum;
                                case "Year 9":
                                    year9PostCount+=postsNum;
                                case "Year 10":
                                    year10PostCount+=postsNum;
                                case "Year 11":
                                    year11PostCount+=postsNum;
                                case "Year 12":
                                    year12PostCount+=postsNum;
                                case "Year 13":
                                    year13PostCount+=postsNum;
                            }
                        }
                        if (user.getUserType().equals("Teacher")) {
                            teacherPostCount+=postsNum;
                        }
                        if (user.getUserType().equals("Admin")) {
                            adminPostCount+=postsNum;
                        }
                    }
                    year7Posts.setText("Year 7: " + year7PostCount);
                    year8Posts.setText("Year 8: " + year8PostCount);
                    year9Posts.setText("Year 9: " + year9PostCount);
                    year10Posts.setText("Year 10: " + year10PostCount);
                    year11Posts.setText("Year 11: " + year11PostCount);
                    year12Posts.setText("Year 12: " + year12PostCount);
                    year13Posts.setText("Year 13: " + year13PostCount);
                    teacherPosts.setText("Teachers: " + teacherPostCount);
                    adminPosts.setText("Admin: " + adminPostCount);
                }
            }
        });
    }
    /**
     * Method that sets up the TextViews that show how
     * many posts of each category were created in total.
     * Method also sets up TextViews showing miscellaneous
     * statistics such as the most popular day for CCAs,
     * most popular day for service, and most popular sport,
     * based on data retrieved from the database.
     *
     */
    public void setUpCategoryViews()
    {
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                        Post post = ds.toObject(Post.class);
                        if (post.getApprovalStatus().equals("approved")) {
                            accepted += 1;
                            if (post.getPostCategory().equals("Sports")) {
                                sportsPostCount += 1;
                                SportsPost sportsPost = ds.toObject(SportsPost.class);
                                String theSport = sportsPost.getSport().toLowerCase();
                                if (!sports.containsKey(theSport)) {
                                    sports.put(theSport, 1);
                                }
                                else {
                                    int value = sports.get(theSport);
                                    sports.put(sportsPost.getSport().toLowerCase(), value + 1);
                                }
                            }
                            else if (post.getPostCategory().equals("CCA")) {
                                ccaPostCount += 1;
                                CCAPost ccaPost = ds.toObject(CCAPost.class);
                                if (!ccaDays.containsKey(ccaPost.getDay())) {
                                    ccaDays.put(ccaPost.getDay(), 1);
                                }
                                else {
                                    int value = ccaDays.get(ccaPost.getDay());
                                    ccaDays.put(ccaPost.getDay(), value + 1);
                                }                            }
                            else if (post.getPostCategory().equals("Service")) {
                                servicePostCount += 1;
                                ServicePost servicePost = ds.toObject(ServicePost.class);
                                if (!serviceDays.containsKey(servicePost.getDay())) {
                                    serviceDays.put(servicePost.getDay(), 1);
                                }
                                else {
                                    int value = serviceDays.get(servicePost.getDay());
                                    serviceDays.put(servicePost.getDay(), value + 1);
                                }
                            }
                            else if (post.getPostCategory().equals("Academics")) {
                                academicsPostCount += 1;
                            }
                            else if (post.getPostCategory().equals("Miscellaneous")) {
                                miscPostCount += 1;
                            }
                        }
                        if (post.getApprovalStatus().equals("rejected")) {
                            rejected += 1;
                        }
                    }
                    sportsCount.setText("Sports: " + sportsPostCount);
                    academicsCount.setText("Academics: " + academicsPostCount);
                    ccaCount.setText("CCA: " + ccaPostCount);
                    serviceCount.setText("Service: " + servicePostCount);
                    miscCount.setText("Miscellaneous: " + miscPostCount);

                    Map.Entry<String, Integer> maximum = null;

                    for (Map.Entry<String, Integer> entry : sports.entrySet())
                    {
                        if (maximum == null || entry.getValue().compareTo(maximum.getValue()) > 0) {
                            maximum = entry;
                        }
                    }

                    Map.Entry<String, Integer> popularCCADay = null;

                    for (Map.Entry<String, Integer> entry : ccaDays.entrySet())
                    {
                        if (popularCCADay == null || entry.getValue().compareTo(popularCCADay.getValue()) > 0) {
                            popularCCADay = entry;
                        }
                    }

                    Map.Entry<String, Integer> popularServiceDay = null;

                    for (Map.Entry<String, Integer> entry : serviceDays.entrySet())
                    {
                        if (popularServiceDay == null || entry.getValue().compareTo(popularServiceDay.getValue()) > 0) {
                            popularServiceDay = entry;
                        }
                    }


                    popularSport.setText("The most popular sport is: " + maximum.getKey() + " with " + maximum.getValue() + " posts");
                    frequentCCADay.setText("Most frequent day for CCAs: " + popularCCADay.getKey() + " with " + popularCCADay.getValue() + " CCAs");
                    if (popularServiceDay.getValue().equals(1)) {
                        frequentServiceDay.setText("Most frequent day for Sports: " + popularServiceDay.getKey() + " with " + popularServiceDay.getValue() + " service");

                    }
                    else {
                        frequentServiceDay.setText("Most frequent day for Sports: " + popularServiceDay.getKey() + " with " + popularServiceDay.getValue() + " services");

                    }

                    System.out.println(accepted + "accepted");
                    System.out.println(rejected + "rejected");
                    if (accepted+rejected == 0) {
                        acceptedPercentage.setText("0");
                        rejectedPercentage.setText("0");
                    }
                    else {
                        double accept = Math.round(accepted / (accepted+rejected) * 100);
                        double reject = Math.round(rejected / (accepted+rejected) * 100);
                        acceptedPercentage.setText("Overall % of posts accepted: " + accept + "%");
                        rejectedPercentage.setText("Overall % of posts rejected: " + reject + "%");
                    }
                }
            }
        });
    }
}
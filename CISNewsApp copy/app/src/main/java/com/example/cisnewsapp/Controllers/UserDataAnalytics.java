package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cisnewsapp.Models.Student;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDataAnalytics extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    public int year7Count;
    public int year8Count;
    public int year9Count;
    public int year10Count;
    public int year11Count;
    public int year12Count;
    public int year13Count;
    public int teacherCount;
    public int adminCount;
    public int longestStreakEver;
    public int longestStreakCurrent;
    public String longestStreakEverPerson;
    public String longestStreakCurrentPerson;

    public TextView year7text;
    public TextView year8text;
    public TextView year9text;
    public TextView year10text;
    public TextView year11text;
    public TextView year12text;
    public TextView year13text;
    public TextView teachersText;
    public TextView adminsText;
    public TextView longestStreakEverText;
    public TextView longestStreakCurrentText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_analytics);

        year7text = findViewById(R.id.year7text);
        year8text = findViewById(R.id.year8text);
        year9text = findViewById(R.id.year9text);
        year10text = findViewById(R.id.year10text);
        year11text = findViewById(R.id.year11text);
        year12text = findViewById(R.id.year12text);
        year13text = findViewById(R.id.year13text);
        teachersText = findViewById(R.id.teachersText);
        adminsText = findViewById(R.id.adminsText);
        longestStreakCurrentText = findViewById(R.id.longestStreakCurrentText);
        longestStreakEverText = findViewById(R.id.longestStreakEverText);

        longestStreakEver = 0;
        longestStreakCurrent = 0;
        getAndShowData();
    }

    public void getAndShowData() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                        User user = ds.toObject(User.class);
                        if (user.getUserType().equals("Student")) {
                            Student student = ds.toObject(Student.class);
                            switch (student.getYearGroup()) {
                                case "Year 7":
                                    year7Count+=1;
                                case "Year 8":
                                    year8Count+=1;
                                case "Year 9":
                                    year9Count+=1;
                                case "Year 10":
                                    year10Count+=1;
                                case "Year 11":
                                    year11Count+=1;
                                case "Year 12":
                                    year12Count+=1;
                                case "Year 13":
                                    year13Count+=1;
                            }
                        }
                        if (user.getUserType().equals("Teacher")) {
                            teacherCount+=1;
                        }
                        if (user.getUserType().equals("Admin")) {
                            adminCount+=1;
                        }
                        if (user.getCurrentStreak() > longestStreakCurrent)
                        {
                            longestStreakCurrent = user.getCurrentStreak();
                            longestStreakCurrentPerson = user.getName();
                        }
                        if (user.getLongestStreak() > longestStreakEver)
                        {
                            longestStreakEver = user.getLongestStreak();
                            longestStreakEverPerson = user.getName();
                        }
                    }
                    year7text.setText("Year 7: " + year7Count);
                    year8text.setText("Year 8: " + year8Count);
                    year9text.setText("Year 9: " + year9Count);
                    year10text.setText("Year 10: " + year10Count);
                    year11text.setText("Year 11: " + year11Count);
                    year12text.setText("Year 12: " + year12Count);
                    year13text.setText("Year 13: " + year13Count);
                    teachersText.setText("Teachers " + teacherCount);
                    adminsText.setText("Admins: " + adminCount);
                    longestStreakCurrentText.setText("Longest current streak: " + longestStreakCurrent + " , held by " + longestStreakCurrentPerson);
                    longestStreakCurrentText.setText("Longest current streak: " + longestStreakCurrent + " , held by " + longestStreakCurrentPerson);
                }
            }
        });
    }
}
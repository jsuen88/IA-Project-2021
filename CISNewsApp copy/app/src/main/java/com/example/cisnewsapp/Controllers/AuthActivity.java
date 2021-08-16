package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cisnewsapp.Models.Admin;
import com.example.cisnewsapp.Models.Student;
import com.example.cisnewsapp.Models.Teacher;
import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.context.AttributeContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Class containing methods assisting in the process of signing up a new user/allowing them to log
 * in.
 *
 * Author: Joson Suen
 * Version: 1.0
 */
public class AuthActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private EditText emailField;
    private EditText passwordField;
    private EditText nameField;
    private EditText variableEdit;
    public ArrayList<String> posts = new ArrayList<>();
    public ArrayList<String> seenPosts = new ArrayList<>();
    public ArrayList<String> starredPosts = new ArrayList<>();
    private Spinner usersSpinner;
    public Context context;
    public Spinner yearsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Spinner yearSpinner = findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.year_groups, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter1);
        yearSpinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.editEmail);
        passwordField = findViewById(R.id.editPassword);
        nameField = findViewById(R.id.editName);
        variableEdit = findViewById(R.id.variableEdit);
        usersSpinner = findViewById(R.id.spinner);
        yearsSpinner = findViewById(R.id.yearSpinner);

        yearsSpinner.setVisibility(View.INVISIBLE);
        variableEdit.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    /**
     * Method allowing the user to sign in once they enter an email address and password matching
     * that of an existing user in the database. Method checks that email string contains .cis.edu.hk
     * or gmail.com to ensure that login is that of a CIS member.
     *
     * @param v: view displayed to the user
     */
    public void signIn (View v)
    {
        final String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();
        System.out.println(String.format("email: %s and password: %s",
                emailString, passwordString));

        if (!emailString.contains("@"))
        {
            Toast.makeText(AuthActivity.this, "Please enter a valid email address",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String[] parts = emailString.split("@");
        if (parts[1].contains(".cis.edu.hk"))
        {
            mAuth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGN IN", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                                Toast.makeText(AuthActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(AuthActivity.this, "Please enter an email of the " +
                    ".cis.edu.hk or gmail.com domain", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Method allowing the user to sign up once they enter a valid email and password. Method
     * checks that email string contains .cis.edu.hk
     * or gmail.com to ensure that login is that of a CIS member.
     *
     * @param v: view displayed to the user
     */
    public void signUp (View v)
    {
        final String nameString = nameField.getText().toString();
        final String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();

        if (passwordString.length() < 6)
        {
            Toast.makeText(AuthActivity.this, "Password must be at least 6 characters"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if (!emailString.contains("@"))
        {
            Toast.makeText(AuthActivity.this, "Please enter a valid email address",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String[] parts = emailString.split("@");
        if (usersSpinner.getSelectedItem().toString().equals("Enter your role:")) {
            Toast.makeText(AuthActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }
        if (parts[1].contains(".cis.edu.hk") || parts[1].contains("gmail.com"))
        {
            mAuth.createUserWithEmailAndPassword(emailString, passwordString).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Log.d("SIGN UP", "successfully signed up the user");
                                FirebaseUser mUser = mAuth.getCurrentUser();

                                String userUID = mUser.getUid();
                                if (usersSpinner.getSelectedItem().toString().equals("Admin")) {
                                    System.out.println("admin activated");
                                    if (!variableEdit.getText().toString().equals("adm1n")) {
                                        Toast.makeText(AuthActivity.this, "Please enter the correct admin code", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    User currentUser = new Admin(nameString, userUID,
                                            usersSpinner.getSelectedItem().toString(), emailString,
                                            0, posts, seenPosts, "All", starredPosts,
                                            Calendar.getInstance().getTime(), 1, 1, Calendar.getInstance().getTime(), 0, 0, 0);
                                    firestore.collection("users").document(userUID).set(currentUser);
                                }
                                else if (usersSpinner.getSelectedItem().equals("Student")) {
                                    if (yearsSpinner.getSelectedItem().equals("Select your year:")) {
                                        Toast.makeText(AuthActivity.this, "Please select your year group", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    User currentUser = new Student(nameString, userUID,
                                            usersSpinner.getSelectedItem().toString(), emailString, 0, posts, seenPosts, "All", starredPosts, Calendar.getInstance().getTime(), 1, 1, Calendar.getInstance().getTime(), yearsSpinner.getSelectedItem().toString());
                                    firestore.collection("users").document(userUID).set(currentUser);
                                }
                                else {
                                    if (variableEdit.getText().length() == 0) {
                                        Toast.makeText(AuthActivity.this, "Please enter your subject", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    User currentUser = new Teacher(nameString, userUID,
                                            usersSpinner.getSelectedItem().toString(), emailString, 0, posts, seenPosts, "All", starredPosts, Calendar.getInstance().getTime(), 1, 1, Calendar.getInstance().getTime(), variableEdit.getText().toString());
                                    firestore.collection("users").document(userUID).set(currentUser);
                                }
                                //Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                //intent.putExtra("role", usersSpinner.getSelectedItem().toString());
                                updateUI(mUser);
                            }
                            else
                            {
                                Log.w("SIGN UP", "createUserWithEmail:failure", task.getException());
                                updateUI(null);
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(AuthActivity.this, "Please enter an email of the " +
                    ".cis.edu.hk or gmail.com domain", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that redirects user to MainActivity upon successful login / signing up
     *
     * @param currentUser: current FirebaseUser
     */
    public void updateUI (FirebaseUser currentUser)
    {
        if (currentUser != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Method called when item is selected on the user roles spinner.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        if (text.equals("Student")) {
            yearsSpinner.setVisibility(View.VISIBLE);
            variableEdit.setVisibility(View.INVISIBLE);
        }
        else if (text.equals("Teacher")) {
            yearsSpinner.setVisibility(View.INVISIBLE);
            variableEdit.setVisibility(View.VISIBLE);
            variableEdit.setHint("Please enter your subject: ");
        }
        else if (text.equals("Admin")) {
            yearsSpinner.setVisibility(View.INVISIBLE);
            variableEdit.setVisibility(View.VISIBLE);
            variableEdit.setHint("Please enter the admin code: ");
        }
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method called when no item is selected on the user role spinner
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
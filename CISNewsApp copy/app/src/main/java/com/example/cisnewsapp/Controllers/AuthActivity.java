package com.example.cisnewsapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cisnewsapp.Models.User;
import com.example.cisnewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AuthActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private EditText emailField;
    private EditText passwordField;
    private EditText nameField;
    public ArrayList<String> posts = new ArrayList<>();
    public ArrayList<String> seenPosts = new ArrayList<>();
    private Spinner usersSpinner;

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

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.editEmail);
        passwordField = findViewById(R.id.editPassword);
        nameField = findViewById(R.id.editName);
        usersSpinner = findViewById(R.id.spinner);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

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
        if (parts[1].contains(".cis.edu.hk") || parts[1].contains("gmail.com"))
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

                                User currentUser = new User(nameString, userUID,
                                        usersSpinner.getSelectedItem().toString(), emailString, 0, posts, seenPosts, "All");
                                firestore.collection("users").document(userUID).set(currentUser);
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

    public void updateUI (FirebaseUser currentUser)
    {
        if (currentUser != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
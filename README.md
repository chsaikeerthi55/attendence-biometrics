# attendence-biometrics
package com.example.seyon;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    EditText txtEmail, txtFacultyID;
    Button btnLogin;
    private FirebaseAuth auth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);

        txtEmail = findViewById(R.id.txtEmail);
        txtFacultyID = findViewById(R.id.txtFacultyID);

        auth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString().trim();
                String password = txtFacultyID.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    txtFacultyID.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    txtFacultyID.setError("Password Must be >= 6 Characters");
                    return;
                }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            //Toast.makeText(MainActivity.this, "Error ! ", Toast.LENGTH_SHORT).show();
                           // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }


                });
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String txtEmail= user.getEmail();
                    String txtFacultyID= user.getEmail();


                    // Check if user's email is verified
                    boolean emailVerified = user.isEmailVerified();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getIdToken() instead.
                    String uid = user.getUid();
                }



            }

        });

        TextView register = (TextView) findViewById(R.id.lnkRegister);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }
    private void updateUI(FirebaseUser user) {
        user = auth.getCurrentUser();
        /*-------- Check if user is already logged in or not--------*/
        if (user != null) {
            /*------------ If user's email is verified then access login -----------*/
            if(user.isEmailVerified()){
                Toast.makeText(MainActivity.this, "Login Success.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Track.class));
            }
            else {
                Toast.makeText(MainActivity.this, "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}

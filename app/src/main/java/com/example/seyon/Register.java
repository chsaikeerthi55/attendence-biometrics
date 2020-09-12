package com.example.seyon;

//did nothing

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import com.google.android.gms.tasks.Task;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.hardware.fingerprint.FingerprintManager;
import android.widget.ImageView;
import android.widget.Toast;
import org.w3c.dom.Text;


public class Register extends AppCompatActivity {
    EditText txtName, txtEmail, txtPwd;
    Button btnLogin;
    private TextView textView;
    private ImageView imageView;
    private FingerprintManager fingerprintManager;
    private FingerprintManager.AuthenticationCallback authenticationCallback;
    private FirebaseAuth auth;

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        txtName = findViewById(R.id.txtName);
        txtPwd = findViewById(R.id.txtPwd);
        txtEmail = findViewById(R.id.txtEmail);
        btnLogin = findViewById(R.id.btnLogin);
        TextView login = (TextView) findViewById(R.id.lnkLogin);
        login.setMovementMethod(LinkMovementMethod.getInstance());


        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.image1);
        imageView.setImageResource(R.drawable.image1);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        authenticationCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                textView.setText("ERROR");
                imageView.setImageResource(R.drawable.image2);
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                textView.setText("HELP");
                imageView.setImageResource(R.drawable.image1);
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                textView.setText("SUCCESS");
                imageView.setImageResource(R.drawable.image3);
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                textView.setText("FAILED");
                imageView.setImageResource(R.drawable.image2);
                super.onAuthenticationFailed();
            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
        String email = txtEmail.getText().toString().trim();
        String password = txtPwd.getText().toString().trim();

        if ((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(password)) ) {
            Toast.makeText(getApplicationContext(), "Enter the details!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        if (!task.isSuccessful())
                        {
                            Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        }

                    }
                });

    }
    @RequiresApi(api = VERSION_CODES.M)
    public void scanButton (View view){
        fingerprintManager.authenticate(null, null, 0, authenticationCallback, null);
    }
}



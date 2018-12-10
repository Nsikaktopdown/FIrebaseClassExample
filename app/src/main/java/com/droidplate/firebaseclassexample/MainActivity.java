package com.droidplate.firebaseclassexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEdt, passwordEdt;
    private FirebaseAuth firebaseAuth;
    private Button registerButton, loginButton;
    String email, password;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pd = new ProgressDialog(this);
        pd.setCancelable(false);

        /**
         * firebase auth
         */
        firebaseAuth = FirebaseAuth.getInstance();

        emailEdt = findViewById(R.id.editTextEmail);
        passwordEdt = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);


    }


    /**
     * Register user with email and password
     *
     */
    private void signUpUser() {

        email = emailEdt.getText().toString();
        password = passwordEdt.getText().toString();

        if (!dataValidate(email, password)) {
            return;
        }
        pd.setMessage("Registering user..");
        pd.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                    pd.hide();
                } else {
                    Toast.makeText(MainActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                    pd.hide();
                }

            }
        });

    }

    /**
     * Login user with email and password
     *
     */
    private void loginUser() {

        email = emailEdt.getText().toString();
        password = passwordEdt.getText().toString();

        if (!dataValidate(email, password)) {
            return;
        }

        pd.setMessage("Signing in user..");
        pd.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                    pd.hide();
                } else {
                    Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    pd.hide();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Validate text fields
     * @param email
     * @param password
     * @return
     */
    private boolean dataValidate(String email, String password) {

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdt.setError("Enter a valid email address");
            return false;
        }

        if (password.isEmpty() || password.length() < 2) {
            passwordEdt.setError("Password not valid");
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.registerButton:
                signUpUser();
                break;
            case R.id.loginButton:
                loginUser();
                break;
        }
    }
}

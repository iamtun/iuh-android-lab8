package fit.android.lab8_fire_base;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class SignIn extends AppCompatActivity {
    private static final String TAG = "UserAuth";
    private FirebaseAuth mAuth;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnSignIn;
    private TextView tvMoveRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtEmail = findViewById(R.id.editTextEmailSignIn);
        txtPassword = findViewById(R.id.editTextPassWordSignIn);
        btnSignIn = findViewById(R.id.btnSignIn_SignIn);
        tvMoveRegister = findViewById(R.id.tvRegisterClick);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //event click button signin
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

        //event click textview register
        tvMoveRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRegisterActivity();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void startSignIn() {
        //letuan19431791.iuh@gmail.com - 22052001
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG,"signInWithEmail:success");
                            Intent intent = new Intent(getApplicationContext(), FaceScreen.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void moveRegisterActivity() {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }
}
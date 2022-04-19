package fit.android.lab8_fire_base.activity;

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

import fit.android.lab8_fire_base.R;

public class Register extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private TextView tvMoveSignIn;
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPass;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //start firebase
        mAuth = FirebaseAuth.getInstance();

        //items
        tvMoveSignIn = findViewById(R.id.tvSignInClick);
        txtName = findViewById(R.id.editTextYourName_Register);
        txtEmail = findViewById(R.id.editTextEmail_Register);
        txtPassword = findViewById(R.id.editTextPassword_Register);
        txtConfirmPass = findViewById(R.id.editTextConfirmPassword_Register);
        btnRegister = findViewById(R.id.btnRegister_Register);

        //event click sign in textview
        tvMoveSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveSignInActivity();
            }
        });

        //event click register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
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

    private void moveSignInActivity() {
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
    }

    //function execute create one account by data user input
    private void register() {
        String name = txtName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPass.getText().toString().trim();

        if(name.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(this, "Please, input full information", Toast.LENGTH_SHORT).show();
        }else {
            if(!confirmPassword.equals(password)) {
                Toast.makeText(this, "Confirm password must equals password", Toast.LENGTH_SHORT).show();
            }else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(Register.this, "Register success",
                                            Toast.LENGTH_SHORT).show();
                                    clearInput();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private void clearInput() {
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirmPass.setText("");
    }
}
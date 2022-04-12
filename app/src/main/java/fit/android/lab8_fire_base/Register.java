package fit.android.lab8_fire_base;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Register extends AppCompatActivity {
    private TextView tvMoveSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvMoveSignIn = findViewById(R.id.tvSignInClick);

        //event click textview signin
        tvMoveSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveSignInActivity();
            }
        });
    }

    private void moveSignInActivity() {
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
    }
}
package fit.android.lab8_fire_base.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import fit.android.lab8_fire_base.R;
import fit.android.lab8_fire_base.dao.UserDAO;
import fit.android.lab8_fire_base.database.AppDatabase;
import fit.android.lab8_fire_base.model.User;

public class FaceScreen extends AppCompatActivity {
    private Button btnFinish;
    private ImageButton btnHappy;
    private ImageButton btnUnHappy;
    private ImageButton btnNormal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_screen);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        //item
        btnFinish = findViewById(R.id.btnFinish);
        btnHappy = findViewById(R.id.btnHappy);
        btnUnHappy = findViewById(R.id.btnUnHappy);
        btnNormal = findViewById(R.id.btnNormal);

        //start database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "user-manager").allowMainThreadQueries().build();

        //DAO
        UserDAO dao = db.userDAO();
        User user = dao.findByEmail(email);

        Toast.makeText(FaceScreen.this, "HAPPY CLIKED = " +
                        user.getHappy() + " UNHAPPY CLICKED = " +
                        user.getUnhappy() + " NORMAL CLICKED = " +
                        user.getNormal(), Toast.LENGTH_SHORT).show();

        //saving status from database when clicked icon
        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user == null)
                {
                    dao.insert(new User(email, 1, 0, 0));
                }else {
                    user.setHappy(user.getHappy() + 1);
                    dao.update(user);

                }
                Toast.makeText(FaceScreen.this, ">>>>> HAPPY CLIKED => " + user.getHappy(), Toast.LENGTH_SHORT).show();
            }
        });

        btnUnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null) {
                    dao.insert(new User(email, 0, 0, 1));
                }
                else {
                    user.setUnhappy(user.getUnhappy() + 1);
                    dao.update(user);
                }

                Toast.makeText(FaceScreen.this, ">>>>> UNHAPPY CLIKED => " + user.getUnhappy(), Toast.LENGTH_SHORT).show();
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null) {
                    dao.insert(new User(email, 0, 1, 0));
                }
                else {
                    user.setNormal(user.getNormal() + 1);
                    dao.update(user);
                }

                Toast.makeText(FaceScreen.this, ">>>>> NORMAL CLIKED => " + user.getNormal(), Toast.LENGTH_SHORT).show();
            }
        });

        //event click finish button
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //log out user
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
package fit.android.lab8_fire_base.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.android.lab8_fire_base.R;
import fit.android.lab8_fire_base.dao.UserDAO;
import fit.android.lab8_fire_base.database.AppDatabase;
import fit.android.lab8_fire_base.model.User;

public class FaceScreen extends AppCompatActivity {
    private Button btnFinish;
    private ImageButton btnHappy;
    private ImageButton btnUnHappy;
    private ImageButton btnNormal;
    private DatabaseReference mDatabase;
    private UserDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_screen);

        //get intent from sign in
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        //item
        btnFinish = findViewById(R.id.btnFinish);
        btnHappy = findViewById(R.id.btnHappy);
        btnUnHappy = findViewById(R.id.btnUnHappy);
        btnNormal = findViewById(R.id.btnNormal);

        //start firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //start database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "user-manager").allowMainThreadQueries().build();

        //DAO
        dao = db.userDAO();

        //if init app then get data from firebase insert to sqlite
        if(dao.getAll().size() == 0) {
            getDataFromFirebaseToSQLite();
        }

        //saving status from database when clicked icon
        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = getUserByEmail(email);
                if(user == null)
                {
                    dao.insert(new User(email, 1, 0, 0));
                }
                else {
                    user.setHappy(user.getHappy() + 1);
                    dao.update(user);

                    //if code below out of block else then error because login first user = null
                    Toast.makeText(FaceScreen.this, ">>>>> HAPPY CLIKED => "
                            + user.getHappy(), Toast.LENGTH_SHORT).show();
                }

                //save data from sqlLite to FireBase
                saveDataFromClientToFireBase();
            }
        });

        btnUnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = getUserByEmail(email);
                if(user == null) {
                    dao.insert(new User(email, 0, 0, 1));
                }
                else {
                    user.setUnhappy(user.getUnhappy() + 1);
                    dao.update(user);

                    Toast.makeText(FaceScreen.this, ">>>>> UNHAPPY CLIKED => "
                            + user.getUnhappy(), Toast.LENGTH_SHORT).show();
                }

                //save data from sqlLite to FireBase
                saveDataFromClientToFireBase();
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = getUserByEmail(email);
                if(user == null) {
                    dao.insert(new User(email, 0, 1, 0));
                }
                else {
                    user.setNormal(user.getNormal() + 1);
                    dao.update(user);

                    Toast.makeText(FaceScreen.this, ">>>>> NORMAL CLIKED => "
                            + user.getNormal(), Toast.LENGTH_SHORT).show();
                }

                //save data from sqlLite to FireBase
                saveDataFromClientToFireBase();
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

    private void saveDataFromClientToFireBase() {
        List<User> users = dao.getAll();
        Map<String, User> mapUsers = new HashMap<>();

        for (User u : users) {
            mapUsers.put(u.getEmail().split("@")[0], u);
        }

        try {
            mDatabase.child("users").setValue(mapUsers);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User getUserByEmail(String email) {
        User user = dao.findByEmail(email);

        if(user != null) {
            Toast.makeText(FaceScreen.this, "HAPPY CLIKED = " +
                    user.getHappy() + " UNHAPPY CLICKED = " +
                    user.getUnhappy() + " NORMAL CLICKED = " +
                    user.getNormal(), Toast.LENGTH_SHORT).show();
        }

        return user;
    }

    private void getDataFromFirebaseToSQLite() {
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn : snapshot.getChildren()) {
                    User us = sn.getValue(User.class);
                    dao.insert(us);
                }

                System.out.println("======>GET DATA FROM FIRE SUCCESSFULLY!");
                System.out.println("======> NUMBER USER: " + dao.getAll().size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }
}
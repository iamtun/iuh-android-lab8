package fit.android.lab8_fire_base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "happy")
    private int happy;

    @ColumnInfo(name = "normal")
    private int normal;

    @ColumnInfo(name = "unhappy")
    private int unhappy;

    public User(@NonNull String email, int happy, int normal, int unhappy) {
        this.email = email;
        this.happy = happy;
        this.normal = normal;
        this.unhappy = unhappy;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHappy() {
        return happy;
    }

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getUnhappy() {
        return unhappy;
    }

    public void setUnhappy(int unhappy) {
        this.unhappy = unhappy;
    }
}

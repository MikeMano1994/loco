package android.duke290.com.loco.database;

import android.duke290.com.loco.Creation;
import android.duke290.com.loco.User;

import java.util.ArrayList;

public interface DatabaseFetchCallback {
    void onDatabaseResultReceived(ArrayList<Creation> creations);
    void onUserReceived(User user);
}

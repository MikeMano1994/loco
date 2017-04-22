package android.duke290.com.loco;

import android.content.Intent;
import android.duke290.com.loco.database.DatabaseFetch;
import android.duke290.com.loco.database.DatabaseFetchCallback;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements DatabaseFetchCallback{
    private static final String TAG = "ProfileActivity";

    private Button signOut;
    private TextView nameText;
    private ProgressBar progressBar;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private TextView post1;
    private TextView post2;
    private TextView post3;
    private TextView post4;


    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth mAuth;

    public User currentUser;
    public String uid;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_STORAGE = 1;
    static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    // variables used for Camera
    private Bitmap mBitmap;
    private Uri mUri;
    public String mCurrentPhotoPath;
    private File mFile;
    public ImageView mPicture;
    public TextView mAddPicture;

    private static final String USERS = "users";
    private static final String USERINFO = "userinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //mAddPicture = (TextView) findViewById(R.id.addpicture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        //get firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = mAuth.getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user mAuth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }

            }
        };

        signOut = (Button) findViewById(R.id.sign_out);
        nameText = (TextView) findViewById(R.id.name_text);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        photo3 = (ImageView) findViewById(R.id.photo3);
        post1 = (TextView) findViewById(R.id.post1);
        post2 = (TextView) findViewById(R.id.post2);
        post3 = (TextView) findViewById(R.id.post3);
        post4 = (TextView) findViewById(R.id.post4);

        showProgressBar();
        setCurrentUser(user);
        setUserCreation(user);
        if (progressBar != null) {
            hideProgressBar();
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    /**
     * set name TextView with username
     */
    private void setNameText(){
        nameText.setText(currentUser.name);
    }


    public void signOut() {
        mAuth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    private void setUserCreation(FirebaseUser user){
        uid = user.getUid();
        DatabaseFetch databasefetch = new DatabaseFetch(this);
        databasefetch.fetchByUser(uid);
    }

    /**
     * Retrieve user information from Firebase database and set current user of this activity
     * @param user
     */
    private void setCurrentUser(FirebaseUser user){
        uid = user.getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(USERS).child(uid).child(USERINFO);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                hideProgressBar();
                setNameText();
                Log.d(TAG, "Got user: "+currentUser + " username: " + currentUser.name);
                Log.d(TAG, "Got key: "+dataSnapshot.getKey());
                Log.d(TAG, "Got value: "+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onDatabaseResultReceived(ArrayList<String> messages, ArrayList<StorageReference> storagerefs) {
        Log.d(TAG, "Messages: " + messages.toString());
        Log.d(TAG, "Storagepaths: " + storagerefs.toString());
        populateView(messages, storagerefs);
    }

    private void populateView(ArrayList<String> messages, ArrayList<StorageReference> storagerefs){
        int messages_size = messages.size();
        int storagerefs_size = storagerefs.size();

        if(messages_size>=1){
            post1.setText(messages.get(messages_size - 1));
        }
        if(messages_size>=2){
            post2.setText(messages.get(messages_size - 2));
        }
        if(messages_size>=3){
            post3.setText(messages.get(messages_size - 3));
        }
        if(messages_size>=4){
            post4.setText(messages.get(messages_size - 4));
        }

        if(storagerefs_size>=1){
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(storagerefs.get(storagerefs_size - 1))
                    .override(75, 75)
                    .into(photo1);
        }
        if(storagerefs_size>=2){
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(storagerefs.get(storagerefs_size - 2))
                    .override(75, 75)
                    .into(photo2);
        }
        if(storagerefs_size>=3){
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(storagerefs.get(storagerefs_size - 3))
                    .override(75, 75)
                    .into(photo3);
        }
    }

/*    public void pictureClick(View myview){
        mAddPicture.setVisibility(View.GONE);
        mPicture = (ImageView) findViewById(R.id.picture);
        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            mPicture.setImageBitmap(mBitmap);
            try {
                checkPermission();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Saving the full-size image in Gallery so that it could be used by other apps
    // Creating a unique filename with timestamp
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "image path " + mCurrentPhotoPath);
        return image;
    }

    // Add picture to gallery
    private void galleryAddPic() throws IOException {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = createImageFile();
        Uri contentUri = Uri.fromFile(f);
        Log.d(TAG, "content uri: " + contentUri);
        Log.d(TAG, "photo uri before gallery: " + mCurrentPhotoPath);
        MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "", "");
        mediaScanIntent.setData(contentUri);
        Log.d(TAG, "here going to gallery");
        this.sendBroadcast(mediaScanIntent);
        MediaScannerConnection.scanFile(getApplicationContext(), new String[] { mCurrentPhotoPath }, null, new MediaScannerConnection.OnScanCompletedListener() {

            @Override
            public void onScanCompleted(String path, Uri uri) {
                // TODO Auto-generated method stub
                Log.d(TAG, "done scanning");

   //         }
        });
    }




    // Checking for permissions
    private void checkPermission() throws IOException {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                    WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(getApplicationContext(), "Storing the image in Gallery",Toast.LENGTH_SHORT).show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            //MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "" , "");
            galleryAddPic();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "" , "");
                    try {
                        galleryAddPic();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Couldn't store image in Gallery",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/


    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){ progressBar.setVisibility(View.GONE); }
}



package android.duke290.com.loco.photos;

import android.duke290.com.loco.R;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * Activity for showing full-size images when clicked
 */
public class PhotoFullSizeActivity extends AppCompatActivity {
    ImageView mImage;
    private FirebaseStorage mStorage;
    public ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slide);
        String path = getIntent().getStringExtra("path");
        int pos = getIntent().getIntExtra("position",0);
        mStorage = FirebaseStorage.getInstance();

        // Getting all images
        ArrayList<String> image_paths = getIntent().getStringArrayListExtra("imagepaths");

        // Getting all locations
        ArrayList<String> locations = getIntent().getStringArrayListExtra("locations");

        // Getting fetchtype
        String fetchtype = getIntent().getStringExtra("fetchtype");

        // Get Pager from xml
        mPager = (ViewPager) findViewById(R.id.pager);

        // Set Adapter for GridView
        mPager.setAdapter(new FullScreenImageAdapter(this, image_paths, locations, fetchtype));
        mPager.setCurrentItem(pos);


    }

}

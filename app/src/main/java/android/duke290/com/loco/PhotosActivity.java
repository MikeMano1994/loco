package android.duke290.com.loco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //Setting the toolbar for the activity
        Toolbar myToolbar = (Toolbar) findViewById(R.id.photos_toolbar);
        setSupportActionBar(myToolbar);

        GridView gridview = (GridView) findViewById(R.id.grid_view);
        ImageAdapter image_adp = new ImageAdapter(this, SharedLists.getInstance().getImageCreations());
        gridview.setAdapter(image_adp);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Show full-size image
            }
        });
    }
}

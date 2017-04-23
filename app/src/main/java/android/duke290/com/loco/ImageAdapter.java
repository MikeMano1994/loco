package android.duke290.com.loco;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Jihane on 4/22/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Creation> mImageCreations;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private final String TAG = "ImageAdapter";

    public ImageAdapter (Context c, ArrayList<Creation> imageCreations) {
        mContext = c;
        mImageCreations = imageCreations;
    }

    public Integer[] mThumbs =
            { R.drawable.testimage, R.drawable.testimage
            };

    @Override
    public int getCount () {
        return mImageCreations.size();
    }

    @Override
    public Object getItem (int position) {
        return mImageCreations.get(position);
    }


    @Override
    public long getItemId (int position) {
        return position;
    }


    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView called");
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView (mContext);
            imageView.setScaleType (ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams (new GridView.LayoutParams(220, 220));
        } else {
            imageView = (ImageView) convertView;
        }

        String storage_path = mImageCreations.get(position).extra_storage_path;

        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(mStorage.getReference().child(storage_path))
                .override(75, 75)
                .into(imageView);

        return imageView;
    }

}
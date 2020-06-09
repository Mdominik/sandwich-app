package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        try {
            populateUI(sandwich);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) throws MalformedURLException {

        // description field
        TextView description_tv = findViewById(R.id.description_tv);
        description_tv.setText(sandwich.getDescription());

        //also known field
        TextView also_known_tv = findViewById(R.id.also_known_tv);
        String[] alsoKnownArray = new String[sandwich.getAlsoKnownAs().size()];
        also_known_tv.setText(TextUtils.join(", ", sandwich.getAlsoKnownAs().toArray(alsoKnownArray)));

        // origin field
        TextView origin_tv = findViewById(R.id.origin_tv);
        origin_tv.setText(sandwich.getPlaceOfOrigin());

        // image field
        new DownloadImageTask((ImageView) findViewById(R.id.image_iv))
                .execute(sandwich.getImage());

        // name field

        // ingredients field
        TextView ingredients_tv = findViewById(R.id.ingredients_tv);
        String[] ingredientsArray = new String[sandwich.getIngredients().size()];
        ingredients_tv.setText(TextUtils.join(", ", sandwich.getIngredients().toArray(ingredientsArray)));


    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bm = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bm = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bm;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
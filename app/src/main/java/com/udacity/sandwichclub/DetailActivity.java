package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

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


        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();///
            return;
        }

        populateUI(sandwich);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    // This we i am to implement the code which will populate the layout from the JSON to the activity.
    private void populateUI(Sandwich sandwich) {
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        TextView alsoNameDisplay = findViewById(R.id.also_known_tv);
        TextView ingredientsDisplay = findViewById(R.id.ingredients_tv);
        TextView descriptionDisplay = findViewById(R.id.description_tv);
        TextView originDisplay = findViewById(R.id.origin_tv);

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
        List<String> nameList = sandwich.getAlsoKnownAs();
        for(int i=0 ; i< nameList.size() ; i++){
            alsoNameDisplay.append(nameList.get(i) + ". ");
        }

        List<String> ingredientsList = sandwich.getIngredients();
        for(int i=0 ; i< ingredientsList.size() ; i++){
            ingredientsDisplay.append(ingredientsList.get(i) + ". ");
        }

        originDisplay.append(sandwich.getPlaceOfOrigin());
        descriptionDisplay.append(sandwich.getDescription());
    }
}

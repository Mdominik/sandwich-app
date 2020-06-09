package com.udacity.sandwichclub.utils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        final String NAME_FIELD = "name";
        final String MAIN_NAME_FIELD = "mainName";
        final String ALSO_KNOWN_AS_FIELD ="alsoKnownAs";
        final String PLACE_OF_ORIGIN_FIELD = "placeOfOrigin";
        final String DESCRIPTION_FIELD = "description";
        final String IMAGE_FIELD = "image";
        final String INGREDIENTS_FIELD = "ingredients";

        try {

            JSONObject jsonObject = new JSONObject(json);
            String mainName = jsonObject.getJSONObject(NAME_FIELD).getString(MAIN_NAME_FIELD);
            JSONArray alsoKnownAs = jsonObject.getJSONObject(NAME_FIELD).getJSONArray(ALSO_KNOWN_AS_FIELD);
            String placeOfOrigin = jsonObject.getString(PLACE_OF_ORIGIN_FIELD);
            String description = jsonObject.getString(DESCRIPTION_FIELD);
            String imageURL = jsonObject.getString(IMAGE_FIELD);
            JSONArray ingredients = jsonObject.getJSONArray(INGREDIENTS_FIELD);
            List<String> alsoKnowsAsTab = new ArrayList<>();
            List<String> ingredientsTab = new ArrayList<>();
            for(int i=0; i<alsoKnownAs.length(); i++) {
                alsoKnowsAsTab.add(alsoKnownAs.getString(i));
            }

            for(int i=0; i<ingredients.length(); i++) {
                ingredientsTab.add(ingredients.getString(i));
            }
            return new Sandwich(mainName, alsoKnowsAsTab, placeOfOrigin, description, imageURL, ingredientsTab);

        } catch (JSONException e) {
            e.printStackTrace();
            return new Sandwich();
        }


    }
}

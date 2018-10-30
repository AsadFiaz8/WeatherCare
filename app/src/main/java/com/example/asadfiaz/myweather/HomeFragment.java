package com.example.asadfiaz.myweather;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    public static TextView txtpoints;
    public static TextView txtCityName;
    public static TextView txtWindSpeed;
    public static TextView txtDetail;
    ImageView tempIcon;
    public static String API_KEY = "&appid=af7a53d06b56d519d2f81cdc3048a554";
    public static String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public HomeFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtpoints = (TextView) view.findViewById(R.id.points);
        txtCityName = (TextView) view.findViewById(R.id.txtCityName);
        txtWindSpeed = (TextView) view.findViewById(R.id.txtWindSpeed);
        txtDetail = (TextView) view.findViewById(R.id.txtDetail);
        tempIcon = (ImageView) view.findViewById(R.id.tempIcon);

        Typeface pointFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Alegreya-Black.ttf");
        txtpoints.setTypeface(pointFont);

        Typeface fonttxtCityName = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation-Regular.ttf");
        txtpoints.setTypeface(fonttxtCityName);

        Typeface humidityfont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation-Regular.ttf");
        txtDetail.setTypeface(humidityfont);

        Typeface windFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation-Regular.ttf");
        txtWindSpeed.setTypeface(windFont);


        loadLocation();

        return view;
    }

    public void loadLocation() {


        String LATLNG_URL = "http://api.openweathermap.org/data/2.5/weather?lat=" + MainActivity.Latitude + "&lon=" + MainActivity.Longitude + API_KEY + "&units=metric";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, LATLNG_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String cityName = response.getString("name");

                    //main Json Object
                    JSONObject object = response.getJSONObject("main");
                    String temperature = object.getString("temp");

                    //Weather JsonArray rEquest
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject jsonObject = array.getJSONObject(0);
                    String Detail = jsonObject.getString("main");

                    //Wind Json Object
                    JSONObject object1 = response.getJSONObject("wind");
                    String windSpeed = object1.getString("speed");

                    if ((temperature.contains("."))) {
                        try {
                            float parseFloatTemp = Float.parseFloat(temperature);
                            int temp = (int) parseFloatTemp;
//                                    tempIcon.setImageResource(R.drawable.clouds);
                            if (temp >= 15 || temp <= 24) {
                                tempIcon.setImageResource(R.drawable.normal_temp);
                            } else if ((temp >= 25)) {
                                tempIcon.setImageResource(R.drawable.sunny);
                            } else if ((temp <= 10 || temp >= 14)) {
                                tempIcon.setImageResource(R.drawable.clouds);
                            }
                            txtpoints.setText(temp + "°" + "C\n");
                            txtCityName.setText(cityName + "\n");
                            txtDetail.setText(Detail);
                            txtWindSpeed.setText("Wind Speed: " + windSpeed + " " + "mph");

                        } catch (Exception e) {
                            txtpoints.setText(e.toString());
                        }
                    } else {
                        txtpoints.setText(temperature + "°" + "C\n");
                        txtCityName.setText(cityName + "\n");
                        txtDetail.setText(Detail);
                        txtWindSpeed.setText("Wind Speed: " + windSpeed + " " + "mph");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);


        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String jsonQuery = API_URL + query + API_KEY + "&units=metric";

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.GET, jsonQuery, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String cityName = response.getString("name");

                            JSONObject object = response.getJSONObject("main");
                            String temperature = object.getString("temp");


                            //Weather JsonArray rEquest
                            JSONArray array = response.getJSONArray("weather");
                            JSONObject jsonObject = array.getJSONObject(0);
                            String Detail = jsonObject.getString("main");


                            JSONObject object1 = response.getJSONObject("wind");
                            String windSpeed = object1.getString("speed");

                            if ((temperature.contains("."))) {
                                try {
                                    float parseFloatTemp = Float.parseFloat(temperature);
                                    int temp = (int) parseFloatTemp;
//                                    tempIcon.setImageResource(R.drawable.clouds);
                                    if (temp >= 15 || temp <= 24) {
                                        tempIcon.setImageResource(R.drawable.normal_temp);
                                    } else if ((temp >= 25)) {
                                        tempIcon.setImageResource(R.drawable.sunny);
                                    } else if ((temp <= 10 || temp >= 14)) {
                                        tempIcon.setImageResource(R.drawable.clouds);
                                    }
                                    txtpoints.setText(temp + "°" + "C\n");
                                    txtCityName.setText(cityName + "\n");
                                    txtDetail.setText(Detail);
                                    txtWindSpeed.setText("Wind Speed: " + windSpeed + " " + "mph");

                                } catch (Exception e) {
                                    txtpoints.setText(e.toString());
                                }
                            } else {
                                txtpoints.setText(temperature + "°" + "C\n");
                                txtCityName.setText(cityName + "\n");
                                txtDetail.setText(Detail);
                                txtWindSpeed.setText("Wind Speed: " + windSpeed + " " + "mph");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                );
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(request);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


}

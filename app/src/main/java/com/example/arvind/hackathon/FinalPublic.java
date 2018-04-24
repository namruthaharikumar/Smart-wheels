package com.example.arvind.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class FinalPublic extends AppCompatActivity {

    Button button;

    String location,placeText,stopText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_public);

        button = (Button) findViewById(R.id.button16);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        placeText = prefs.getString("placeKey", null);
        stopText = prefs.getString("stopKey", null);

        if(placeText.equals("Choose NearBy BusStop"))
        {
            //editText.setText(stopText);
            location = stopText;

        }
        else
        {
            //editText.setText(placeText);
            location = placeText;
        }


    }

    public void onSearch2(View view) {

        //finish();
        //startActivity(new Intent(FinalPublic.this, NavigationBar.class));

        try{

            if(location.equals(""))
            {
                //editText.setText(altText);
                //button.setEnabled(true);
                Toast.makeText(FinalPublic.this, "BusStops Not Found, Try Again Later",
                        Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(FinalPublic.this,NavigationBar.class));
            }
            else
            {

                List<Address> addressList = null ;

                if(location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    android.location.Address address = addressList.get(0);

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr="+String.valueOf(address.getLatitude())+","+String.valueOf(address.getLongitude())));
                    startActivity(intent);
                }

            }


        }
        catch (Exception e)
        {
            Toast.makeText(FinalPublic.this, "BusStops Not Found, Try Again Later",
                    Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(FinalPublic.this,NavigationBar.class));
        }



    }

}

package com.example.ivan.recorridodb4o;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.recorridodb4o.pojo.Position;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int CTEPLAY = 1;
    private FloatingActionButton fab;
    private LocationRequest locationRequest;
    private GoogleApiClient cliente;
    private TextView tv;
    private Database db;
    private DatePicker dp;
    private String today;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database();

        init();
    }

    public void iniciar(View v){
        Intent i = new Intent(this,ActivityMaps.class);
        i.putExtra("day", getDateFromDatePicker());
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getDateFromDatePicker(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");


        return sdf.format(calendar.getTime());
    }

    public void init() {
        tv = (TextView) findViewById(R.id.textView);
        dp = (DatePicker) findViewById(R.id.datePicker);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar(view);
            }
        });


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            cliente = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            cliente.connect();
            tv.setText("Conecta" + "\n");
        } else {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, this, CTEPLAY).show();
            } else {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }


        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CTEPLAY) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }
    }


    private Location ultimaLocalizacion;

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, locationRequest, this);
        /*ultimaLocalizacion= LocationServices.
                FusedLocationApi.getLastLocation(cliente);
        if(ultimaLocalizacion!= null) {

        }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        double la = location.getLatitude();
        double lo = location.getLongitude();

        tv.append("Latitud: " + la + " " + "Longitude: " + lo + "\n");
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        today = sdf.format(calendar.getTime());
        Position p = new Position(la,lo,today);
        Log.v("caca", p.getDay());

        db.create(this);

        db.addPosition(p);

        db.close();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

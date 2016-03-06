package com.example.ivan.recorridodb4o;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ivan.recorridodb4o.pojo.Position;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ivan on 3/6/2016.
 */
public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap gMap;
    private static final int CTEPLAY = 1;
    private LocationRequest locationRequest;
    private GoogleApiClient client;
    private Database db;
    private List<Position> positions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Calendar calendar = Calendar.getInstance();
        String day;
        day = getIntent().getExtras().getString("day");




        Log.v("day", day);

        db = new Database();

        db.create(this);

        positions = db.getPositions(day);

        db.close();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            client.connect();
            //tv.setText("Conecta" + "\n");
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng first = new LatLng(0.0,0.0);

        // Add a marker in Sydney and move the camera
        PolylineOptions po =  new PolylineOptions();
        int i= 0;
        for(Position p : positions){
            if(i==0){
                first = new LatLng(p.getLatitude(), p.getLongitude());
            }
            po.add(new LatLng(p.getLatitude(), p.getLongitude()));
        }

        gMap.addPolyline(po);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(first));
    }

    @Override
    public void onLocationChanged(Location location) {
        double la = location.getLatitude();
        double lo = location.getLongitude();
        //tv.append("Latitud: " + la + " " + "Longitude: " + lo + "\n");
        LatLng locat = new LatLng(la, lo);
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
        gMap.setMyLocationEnabled(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(locat));
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        //locationRequest.setInterval(10000);
        locationRequest.setSmallestDisplacement(1);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        /*ultimaLocalizacion= LocationServices.
                FusedLocationApi.getLastLocation(client);
        if(ultimaLocalizacion!= null) {

        }*/
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



}


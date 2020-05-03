package com.example.ggmap;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ConnectionReceiver;
import com.directionhelpers.FetchURL;
import com.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.http.AsynClient;
import com.http.mJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import cz.msebera.android.httpclient.Header;


public class MapFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener {
    public MapFragment(){

    }
    LatLng current_vehicle_location;

    // Socket io variable
    private Socket mSocket;

    private final String URL = "http://171.251.79.228"; // nhớ sửa chỗ này

    private int someStateValue;
    private final String SOME_VALUE_KEY = "someValueToSave";
    private GoogleMap mMap;
    private LatLng place1, place2;
    ImageButton getDirection, bt_showVehicle, my_locate;
    Button btnClear;
    private Polyline currentPolyline;
    private static final String TAG = MapFragment.class.getSimpleName();
    private CameraPosition mCameraPosition;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private PlacesClient mPlacesClient;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_maps,container,false);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            someStateValue = savedInstanceState.getInt(SOME_VALUE_KEY);
        }

        // Check login
        AsynClient.get("", new RequestParams(), new mJsonHttpResponseHandler(getContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                return;
            }
        });

        // Connect socket to server
        try {
            mSocket = IO.socket(URL);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        /**
         * Listen an event
         * @onServerSentGpsData: Listener, defined below (line: 193)
         * */
        mSocket.on("Server-sent-gps-data", onServerSentGpsData);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        getDirection = (ImageButton) view.findViewById(R.id.button_click);
        bt_showVehicle = (ImageButton) view.findViewById(R.id.bt_vehicle);
        my_locate = (ImageButton) view.findViewById(R.id.my_location);

        //16.074121, 108.149850: bach khoa

        place1 = null;
        place2 = new LatLng(16.074121, 108.149850);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ret = ConnectionReceiver.isConnected();
                if (ret == false) {
                    elert(1);
                } else {
                    if(getDeviceLocation()==null){
                        Toast.makeText(view.getContext(), "Wait a second.....", Toast.LENGTH_LONG).show();
                    }
                    else{
                        String url = getUrl(getDeviceLocation(), place2, "driving");
                        new FetchURL(MapFragment.this).execute(url, "driving");
                        Log.d(TAG, "click direction");
                    }
                }
            }
        });

        bt_showVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "show my vehicle.....", Toast.LENGTH_LONG).show();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place2, 13));
            }
        });

        my_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDeviceLocation()==null) Toast.makeText(view.getContext(), "Wait a second.....", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(view.getContext(), "show device location.....", Toast.LENGTH_LONG).show();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getDeviceLocation(), 13));
                    mMap.addMarker(new MarkerOptions().position(getDeviceLocation()).title("My location"));
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                my_locate.performClick();
            }
        }, 100);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTaskDone(Object... var1) {
        if(currentPolyline!=null) currentPolyline.remove(); // phai sua lai cai nay trong th check vi tri lien tuc
        currentPolyline = mMap.addPolyline((PolylineOptions) var1[0]);
        LatLng currentGeo = getDeviceLocation();
        Log.d(TAG, "onTaskDone");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom

                (new LatLng(
                                currentGeo.latitude/2 + place2.latitude/2, currentGeo.longitude/2 + place2.longitude/2),
                        11));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
        }
        outState.putInt(SOME_VALUE_KEY, someStateValue);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        //getVehicleLocation;
        mMap.addMarker(new MarkerOptions().position(place2).title("Bach Khoa"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place2, 14));
    }

    private LatLng getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            try{
                                place1 = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            }
                            catch (NullPointerException e){
                                elert(2);
                                return;
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        return place1;
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void elert(int info){
        if(info==1) {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setView(R.layout.dialog_wifi)
                    .show();
        }
        else if (info==2){
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setView(R.layout.dialog_location)
                    .show();
        }
    }

    private void ResetMarker(LatLng place01, LatLng place02){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(place01).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("My Location"));
        mMap.addMarker(new MarkerOptions().position(place02).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Vehicle"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place02, 13));
    }

    private Emitter.Listener onServerSentGpsData = new Emitter.Listener() {
        /**
         * Handle received data here
         * data which sent by server is stored in "args[0]"
         * data's type is JSON, so we must use JSONObject to store it
         * data's content is {
         *     "lat": ... a double value,
         *     "lng": ... a double value
         * }
         * */
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Double lat = 0.0;
                    Double lng = 0.0;
                    try {
                        lat = data.getDouble("lat");
                        lng = data.getDouble("lng");
                        current_vehicle_location = new LatLng(lat, lng);
                        ResetMarker(getDeviceLocation(),current_vehicle_location);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), lat + ", " + lng, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

}

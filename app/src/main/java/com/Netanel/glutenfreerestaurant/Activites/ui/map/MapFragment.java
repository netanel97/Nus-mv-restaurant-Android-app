package com.Netanel.glutenfreerestaurant.Activites.ui.map;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.Netanel.glutenfreerestaurant.Model.UserDB;
import com.Netanel.glutenfreerestaurant.MyUtils.Constants;
import com.Netanel.glutenfreerestaurant.R;
import com.Netanel.glutenfreerestaurant.databinding.MapFragmentBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapFragment extends Fragment {

    private MapFragmentBinding binding;
    private ArrayList<LatLng> locationArrayList = new ArrayList<>();
    private Handler handler;
    private int currentPosition = 0;
    private Runnable r;
    private final long Delay = 60000; //1 min


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = MapFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps));
        locationArrayList = getArguments().getParcelableArrayList(Constants.ALL_LOCATIONS);
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                currentPosition =  calcTimeOrder();
                supportMapFragment.getMapAsync(googleMap -> setMapLocation(googleMap));
                handler.postDelayed(this, Delay);
            }
        };handler.postDelayed(r, 0);

        return root;
    }

    /**
     * calculate the time from now minus the time that i order, and the result gives me the
     * current position that should be on the map
     */
    private int calcTimeOrder() {
        int currentOrder = getArguments().getInt(Constants.ORDER_NUMBER);
        return (int) (System.currentTimeMillis() - UserDB.getInstance().getAllOrders().get(currentOrder).getTimeStamp())/1000/60;
    }

    public void setMapLocation(GoogleMap googleMap) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(locationArrayList.get(currentPosition)).title("Driver"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationArrayList.get(currentPosition))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);


    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }
}

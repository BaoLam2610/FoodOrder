package com.example.foodorderapp.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentViewAllBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IViewMap;
import com.example.foodorderapp.model.Address;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.ViewMapPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.here.sdk.core.Anchor2D;
import com.here.sdk.core.GeoBox;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.Point2D;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.gestures.DoubleTapListener;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapview.MapCamera;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.CarOptions;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Waypoint;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewAllRestaurantFragment extends Fragment implements IViewMap, ICartDatabase {
    FragmentViewAllBinding binding;
    ViewMapPresenter presenter;
    BottomSheetBehavior bottomSheetBehavior;
    List<MapMarker> mapMarkers = new ArrayList<>();
    List<Waypoint> waypoints = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    double range;
    String userAddress;
    double tempX, tempY;
    Waypoint waypoint1;
    Waypoint waypoint2;
    RoutingEngine routingEngine;
    MapPolyline routePolyline;
    CartDatabasePresenter cartPresenter;

    public static ViewAllRestaurantFragment newInstance() {

        Bundle args = new Bundle();

        ViewAllRestaurantFragment fragment = new ViewAllRestaurantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_all, container, false);
        binding.layoutMap.mapView.onCreate(savedInstanceState);
        // action bar
        setHasOptionsMenu(true);
        cartPresenter = new CartDatabasePresenter(this, getContext());
        // get user location
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        binding.btnMyLocation.setOnClickListener(v -> {
            getUserLocation();
        });

        // routing - range - calculation
        try {
            routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            e.printStackTrace();
        }
        // show map
        presenter = new ViewMapPresenter(this, getContext());
        presenter.showAllRestaurant();

        // dialog
        bottomSheetBehavior = BottomSheetBehavior.from(binding.dialogViewAll.bottomDialog);

        binding.btnDirect.setOnClickListener(v -> {

            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            calculateRoute();

        });
        // touch listener
        setListenerLocation();
//        setDoubleTouch();

        // hide - back and next
        binding.layoutMap.btnNext.setVisibility(View.GONE);
        binding.layoutMap.btnBackPress.setVisibility(View.GONE);

        return binding.getRoot();

    }

    public void loadMapScene(double x, double y) {
        binding.layoutMap.mapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, mapError -> {
            if (mapError == null) {
                binding.layoutMap.mapView.getCamera().lookAt(new GeoCoordinates(x, y), 8000);
            } else {

            }
        });

    }

    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            addMarkerUser(location.getLatitude(), location.getLongitude());
                            waypoint1 = new Waypoint(new GeoCoordinates(location.getLatitude(), location.getLongitude()));

                        } catch (Exception e) {

                        }
                    }
                }
            });
        }
    }

    public void getCameraLocation(double x1, double y1, double x2, double y2) {
        GeoBox geoBox = new GeoBox(new GeoCoordinates(x1, y1), new GeoCoordinates(x2, y2));
        MapCamera.OrientationUpdate camera = new MapCamera.OrientationUpdate(0.0, 0.0);

        binding.layoutMap.mapView.getCamera().lookAt(geoBox, camera);
    }

    public void addMarkerUser(double x, double y) {
        loadMapScene(x, y);
        try {
            clearMap();
            MapImage mapImage = MapImageFactory.fromResource(getResources(), R.drawable.ic_current_location_32);
            Anchor2D anchor2D = new Anchor2D(0.5f, 1.0f);
            MapMarker mapMarker = new MapMarker(new GeoCoordinates(x, y), mapImage, anchor2D);


            mapMarkers.add(mapMarker);
            binding.layoutMap.mapView.getMapScene().addMapMarker(mapMarker);


            getLocation(x, y);

//            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            calculateRoute();
        } catch (Exception e) {

        }

    }

    public void addMarkerAddress(double x, double y) {
        MapImage mapImage = MapImageFactory.fromResource(getResources(), R.drawable.ic_restaurant_pin);
        Anchor2D anchor2D = new Anchor2D(0.5f, 1.0f);
        MapMarker mapMarker = new MapMarker(new GeoCoordinates(x, y), mapImage, anchor2D);
        binding.layoutMap.mapView.getMapScene().addMapMarker(mapMarker);

    }

    public void addPin(double x, double y, String str) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_restaurant_pin, null);
        TextView text = view.findViewById(R.id.tvResName);
        text.setText(str);
        binding.layoutMap.mapView.pinView(view, new GeoCoordinates(x, y));
    }

    public void clearMap() {
        for (MapMarker mapMarker : mapMarkers
        ) {
            binding.layoutMap.mapView.getMapScene().removeMapMarker(mapMarker);
        }

        if (routePolyline != null)
            binding.layoutMap.mapView.getMapScene().removeMapPolyline(routePolyline);
    }

    public void setLongTouch() {
        binding.layoutMap.mapView.getGestures().setDoubleTapListener(new DoubleTapListener() {
            @Override
            public void onDoubleTap(@NonNull Point2D point2D) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    public void setListenerLocation() {

        binding.layoutMap.mapView.getGestures().setTapListener(new TapListener() {
            @Override
            public void onTap(@NonNull Point2D point2D) {
                try {
                    clearMap();


                    MapImage mapImage = MapImageFactory.fromResource(getResources(), R.drawable.ic_current_location_32);
                    MapMarker mapMarker = new MapMarker(binding.layoutMap.mapView.viewToGeoCoordinates(point2D), mapImage);
                    mapMarkers.add(mapMarker);
                    binding.layoutMap.mapView.getMapScene().addMapMarker(mapMarker);

                    waypoint1 = new Waypoint(binding.layoutMap.mapView.viewToGeoCoordinates(point2D));


                    GeoCoordinates geoCoordinates = binding.layoutMap.mapView.viewToGeoCoordinates(point2D);
                    double latitude = geoCoordinates.latitude;
                    double longitude = geoCoordinates.longitude;
                    getLocation(latitude, longitude);

                    //if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    calculateRoute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getLocation(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        userAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
        binding.dialogViewAll.tvMyLocation.setText(userAddress);
    }

    public void calculateRoute() {
        waypoints.clear();
        if (waypoint1 != null)
            waypoints.add(waypoint1);
        waypoints.add(waypoint2);
        if (waypoints.size() == 2) {
            if (routePolyline != null)
                binding.layoutMap.mapView.getMapScene().removeMapPolyline(routePolyline);
            routingEngine.calculateRoute(waypoints, new CarOptions(), new CalculateRouteCallback() {
                @Override
                public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> list) {
                    if (routingError == null) {
                        Route route = list.get(0);
                        drawRoute(route);
                    }
                }
            });

            double x1 = waypoints.get(0).coordinates.latitude;
            double y1 = waypoints.get(0).coordinates.longitude;
            double x2 = waypoints.get(1).coordinates.latitude;
            double y2 = waypoints.get(1).coordinates.longitude;
            getCameraLocation(x1, y1, x2, y2);
        }
    }

    private void drawRoute(Route route) {
        GeoPolyline geoPolyline;
        try {
            geoPolyline = new GeoPolyline(route.getPolyline());
            routePolyline = new MapPolyline(geoPolyline, 20, com.here.sdk.core.Color.valueOf((short) 0x00, (short) 0x90, (short) 0x8A, (short) 0xA0));
        } catch (Exception e) {

        }
        binding.layoutMap.mapView.getMapScene().addMapPolyline(routePolyline);

//
        range = route.getLengthInMeters() / 1000.0;
        binding.dialogViewAll.tvRange.setText(String.format("%.1f km", range));

//

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.layoutMap.mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.layoutMap.mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.layoutMap.mapView.onDestroy();
    }

    @Override
    public void onShowRestaurantLocation(double latitude, double longitude, Restaurant restaurant) {

    }

    @Override
    public void onShowAllRestaurantLocation(List<Restaurant> restaurantList) {
        List<Address> addressList = new ArrayList<>();
        List<String> nameAddressList = new ArrayList<>();

        for (int i = 0; i < restaurantList.size(); i++) {
            addressList.add(restaurantList.get(i).getAddress());
        }
        loadMapScene(addressList.get(0).getX(), addressList.get(0).getY());
        for (int i = 0; i < addressList.size(); i++) {
            nameAddressList.add(restaurantList.get(i).getName());
            addMarkerAddress(addressList.get(i).getX(), addressList.get(i).getY());
            addPin(addressList.get(i).getX(), addressList.get(i).getY(), nameAddressList.get(i));
        }

        ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, nameAddressList);

        binding.dialogViewAll.spResAddress.setAdapter(addressAdapter);
        binding.dialogViewAll.spResAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (routePolyline != null)
                    binding.layoutMap.mapView.getMapScene().removeMapPolyline(routePolyline);

                Address address = addressList.get(position);
                binding.dialogViewAll.tvResAddress.setText(address.getAddress());
                loadMapScene(address.getX(), address.getY());
                waypoint2 = new Waypoint(new GeoCoordinates(address.getX(), address.getY()));

                if (waypoints.size() == 2)
                    calculateRoute();
                binding.dialogViewAll.btnNext.setOnClickListener(v -> {
                    Intent it = new Intent(getContext(), DetailActivity.class);
                    it.putExtra("quick_deliveries", "item");
                    Restaurant restaurant = restaurantList.get(position);
                    EventBus.getDefault().postSticky(restaurant); // dùng luôn sql
                    cartPresenter.saveRestaurantOnCart(restaurant);
                    Cart currentCart = new Cart(restaurant, 0, 0);
                    cartPresenter.saveCart(currentCart);
                    EventBus.getDefault().postSticky(currentCart);
                    getContext().startActivity(it);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onShowYourLocation(double latitude, double longitude) {

    }

    @Override
    public void onShowCart(Cart cart) {

    }
}

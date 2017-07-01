package de.android.testaufgabe;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainFragment extends Fragment implements View.OnClickListener {
    private Button btnFind, btnSkip;
    private ProgressBar progressBar;
    private TextView textView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> arrayList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LinearLayout linearLayout;
    private String text = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Set<String> set;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        arrayList = new ArrayList<>();
        loadPreferences();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        getActivity();
        btnFind = (Button) view.findViewById(R.id.btnFind);
        btnSkip = (Button) view.findViewById(R.id.btnSkip);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        textView = (TextView) view.findViewById(R.id.textView);
        listView = (ListView) view.findViewById(R.id.listView);

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayList);
        listView.setAdapter(adapter);

        btnFind.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFind:
                textView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

//                        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
//                        try {
//                            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                            Log.d("TEST", String.valueOf(addresses.size()));
//                            if (addresses.size() > 0) {
//                                text = addresses.get(0).getLocality();
//                            }else {
//                                text = location.getLatitude() + "/" + location.getLongitude();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        text = location.getLatitude() + "/" + location.getLongitude();

                        arrayList.add(text);
                        adapter.notifyDataSetChanged();
                        textView.setText(text);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        //unused
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        //unused
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        //unused
                    }
                };

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, ConstantManager.GPS_REQUEST_PERMISSION_CODE);
                    Snackbar.make(linearLayout, "Для корректной работы необходимо дать требуемые разрешения",
                            Snackbar.LENGTH_LONG)
                            .setAction("Разрешить", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openApplicationSettings();
                                }
                            }).show();
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Integer.MAX_VALUE, Integer.MAX_VALUE, locationListener);
                }
                break;
            case R.id.btnSkip:
                if (arrayList != null) {
                    arrayList.clear();
                }
                adapter.notifyDataSetChanged();
                savePreferences();
                break;
            default:
                break;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        savePreferences();
        super.onPause();
    }

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    private void savePreferences() {
        editor = prefs.edit();
        set = new HashSet<>();
        set.addAll(arrayList);
        editor.putStringSet(ConstantManager.KEY_OF_SET, set);
        editor.apply();
    }

    private void loadPreferences() {

        if (prefs != null) {
            set = prefs.getStringSet(ConstantManager.KEY_OF_SET, null);
            if (set != null && arrayList != null) {
                for (String s : set) {
                    arrayList.add(s);
                }
            }
        }
    }


}

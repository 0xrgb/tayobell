package com.sgen.tayobell;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class FirstFragment extends Fragment implements LocationListener{
    ListView busstoplistView;

    MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private String provider;
    private boolean statusOfGps;
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.tab1_view, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        busstoplistView= (ListView)view.findViewById(R.id.busstop_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

        // 아이템을 추가
        adapter.add("정류소1");
        adapter.add("정류소2");
        adapter.add("정류소3");

        busstoplistView.setAdapter(adapter);
        busstoplistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                //아이템 클릭 시 엑티비티 정류소 별 버스 리스트 화면으로 이동
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(),BusListActivity.class);
                getActivity().startActivity(i);
            }
        });



        //int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        statusOfGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Criteria criteria =  new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        Toast.makeText(getActivity().getApplicationContext(), "GPS 사용여부"+statusOfGps, Toast.LENGTH_LONG).show();

        if(statusOfGps) {   //gps가 이미 켜져있을 경우.
            locationManager.requestLocationUpdates(provider, 1, 1, FirstFragment.this); //기본 위치 값 설정
            setUpMapIfNeeded();
            setMyLocation(); //내위치 정하는 함수
        }else {   //gps가 꺼져있을 경우
            new AlertDialog.Builder(getActivity())
                    .setTitle("위치서비스 동의")
                    .setNeutralButton("이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        }
                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.cancel();
                        }
                    }).show();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//위치설정 엑티비티 종료 후
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, true);
                if(provider==null){//사용자가 위치설정동의 안했을때 종료
                   // finish();
                }else{//사용자가 위치설정 동의 했을때
                    locationManager.requestLocationUpdates(provider, 1L, 2F, FirstFragment.this);
                    Log.d("KTH","117 locationMaanger done");
                    setUpMapIfNeeded();
                    setMyLocation(); //내위치 정하는 함수
                }
                break;
        }
    }

    private void setMyLocation(){
        googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    Marker mMarker;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Toast.makeText(getActivity().getApplicationContext(),"현재 위도 : "+location.getLatitude()+", 경도: "+location.getLongitude(),Toast.LENGTH_LONG).show();
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.clear();  // 이전 marker 전부 지우기
            mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
            if(googleMap != null){
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }

    };


    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            // mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (googleMap != null) {
                setUpMap();
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"하하.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setUpMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getMyLocation();

    }

    boolean locationTag=true;


    @Override
    public void onLocationChanged(Location location) {
        if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            double lat =  location.getLatitude();
            double lng = location.getLongitude();

            Toast.makeText(getActivity().getApplicationContext(), "위도  : " + lat +  " 경도: "  + lng ,  Toast.LENGTH_SHORT).show();
            locationTag=false;
        }

    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         //getMenuInflater().inflate(R.menu.menu_main, menu);
         return true;
     }
 */
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
    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }*/

}
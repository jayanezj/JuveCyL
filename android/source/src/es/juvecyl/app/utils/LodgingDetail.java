
package es.juvecyl.app.utils;

import java.util.ArrayList;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import es.juvecyl.app.DownloadXML;
import es.juvecyl.app.Favs;
import es.juvecyl.app.R;
import es.juvecyl.app.navs.DetailsNav;
import es.juvecyl.app.navs.DetailsNavMaker;

@SuppressLint("InflateParams") public class LodgingDetail extends SherlockActivity {
    private float scale;
    private XMLDB db;
    private HashSet<String> favs;
    private boolean isImageFitToScreen;
    private Resources res;
    private Lodging target;
    private LinearLayout detailsContainer;
    private TextView title, detailsTarget;
    private ImageView image;
    private ListView navList;
    private DetailsNavMaker arrayAdapter;
    private Vibrator vibe;
    private DrawerLayout drawerLayout;
    private String targetProvince;
    // ESCUCHADOR DEL MENU NAV
    private ArrayList<DetailsNav> navdata = new ArrayList<DetailsNav>();
    private ActionBarDrawerToggle navToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.details);
        res = getResources();
        // /////////////////////////////////////////////////////////////////////
        // CARGAMOS LOS DATOS
        // /////////////////////////////////////////////////////////////////////
        db = LodgingSingleton.getInstance(getApplicationContext()).getDB();
        // /////////////////////////////////////////////////////////////////////
        // PARA QUE LOS PIXELS SEAN EN FUNCION DE LA DENSIDAD DE LA PANTALLA
        // /////////////////////////////////////////////////////////////////////
        scale = getResources().getDisplayMetrics().density;
        // /////////////////////////////////////////////////////////////////////
        // CARGAMOS EL ALOJAMIENTO
        // /////////////////////////////////////////////////////////////////////
        Bundle bundle = this.getIntent().getExtras();
        targetProvince = bundle.getString("lodging");
        LocateLodging(Integer.parseInt(targetProvince));
        navList = (ListView) findViewById(R.id.main_menu_drawer);
        // /////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL VIBRADOR
        // /////////////////////////////////////////////////////////////////////
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // /////////////////////////////////////////////////////////////////////
        // RECOGEMOS OBJETOS DEL LAYOUT
        // /////////////////////////////////////////////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.details_drawer_layout);
        detailsContainer = (LinearLayout) findViewById(R.id.details_container);
        title = (TextView) findViewById(R.id.details_title);
        // /////////////////////////////////////////////////////////////////////
        // LLAMADA PARA EL MENÚ SUPERIOR
        // /////////////////////////////////////////////////////////////////////
        topMenuMaker();
        // /////////////////////////////////////////////////////////////////////
        // LLAMADA PARA EL MENÚ LATERAL
        // /////////////////////////////////////////////////////////////////////
        navListMaker();
        // /////////////////////////////////////////////////////////////////////
        // PINTAMOS DATOS BÁSICOS – IMAGEN Y TÍTULO
        // /////////////////////////////////////////////////////////////////////
        isImageFitToScreen = false;
        new ImageDownloader(LodgingDetail.this).execute(target.getImage());
        title.setText(target.getTitle());
        title.setBackgroundColor(
                res.getColor(ProvinceSingleton.
                getInstance().
                getProvince(target.getProvince()).
                getColor())
               );
        printDetails("desc");
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navToggle.onConfigurationChanged(newConfig);
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // FUNCIONES DE CLASE
    //
    //
    // /////////////////////////////////////////////////////////////////////////
    private void LocateLodging(int id) {
        target = db.getLodgings().get(id);
    }

    private void printDetails(String parameter) {
        String output = "";
        if (parameter.equals("desc")) {
            output = target.getDesc();
        }
        else if (parameter.equals("contact")) {
            output = target.getContactData();
        }
        else if (parameter.equals("loc")) {
            output = target.getLoc();
        }
        
        detailsContainer.removeAllViews();
        LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        prm.setMargins(0, (int) (5 * scale + 0.5f), 0,
                (int) (5 * scale + 0.5f));
        detailsTarget = new TextView(getApplicationContext());
        detailsTarget.setLayoutParams(prm);
        detailsTarget.setText(output);
        detailsTarget.setPadding((int) (10 * scale + 0.5f),
                (int) (20 * scale + 0.5f),
                (int) (10 * scale + 0.5f),
                (int) (20 * scale + 0.5f));
        
        if (parameter.equals("loc")) {
            detailsContainer.addView(detailsTarget);
            View child1 = LayoutInflater.from(this).inflate(
                    R.layout.map, null);
            GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            LatLng sydney = new LatLng(-33.867, 151.206);

            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
            map.addMarker(new MarkerOptions()
            .title("Sydney")
            .snippet("The most populous city in Australia.")
            .position(sydney));
            detailsContainer.addView(child1);
        }
        else{
            detailsContainer.addView(detailsTarget);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // SECCIÓN BOTONES ANDROID
    //
    //
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            vibe.vibrate(60);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void updateBitmap(Bitmap image2) {
        image = (ImageView) findViewById(R.id.details_image);
        image.setImageBitmap(image2);

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe.vibrate(60);
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
                    image.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, (int) (100 * scale + 0.5f)));
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    isImageFitToScreen = true;
                    image.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
        });

    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // SECCIÓN MENÚ SUPERIOR
    //
    //
    // /////////////////////////////////////////////////////////////////////////

    private void topMenuMaker() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // /////////////////////////////////////////////////////////////////////////
    // AÑADIMOS ELEMENTOS AL MENU DEL ACTION BAR
    // /////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                vibe.vibrate(60);
                if (drawerLayout.isDrawerOpen(navList)) {
                    drawerLayout.closeDrawer(navList);
                } else {
                    drawerLayout.openDrawer(navList);
                }
                break;
            case R.id.favs:
                vibe.vibrate(60);
                startActivity(new Intent(this, Favs.class));
                break;
            case R.id.reload:
                vibe.vibrate(60);
                startActivity(new Intent(this, DownloadXML.class));
                finish();
                break;
        }
        return true;
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // SECCIÓN MENÚ LATERAL
    //
    //
    // /////////////////////////////////////////////////////////////////////////
    private void navListMaker() {
        navList = (ListView) findViewById(R.id.details_menu_drawer);
        // /////////////////////////////////////////////////////////////////////
        // CREANDO EL NAVEGADOR LATERAL
        // /////////////////////////////////////////////////////////////////////
        navdata = new DetailsNav(getApplicationContext()).getNav();
        arrayAdapter = new DetailsNavMaker(this, R.layout.main_list, navdata);
        navList.setAdapter(arrayAdapter);
        navList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int pos, long id) {
                vibe.vibrate(60);
                String selected = ((DetailsNav) a.getAdapter().getItem(pos))
                        .getTitle();
                if (selected.equals(res.getString(R.string.coding_back))) {
                    finish();
                }
                if (selected.equals(res.getString(R.string.coding_desc))){
                    printDetails("desc");
                    drawerLayout.closeDrawer(navList);
                }
                if (selected.equals(res.getString(R.string.coding_contact))){
                    printDetails("contact");
                    drawerLayout.closeDrawer(navList);
                }
                if (selected.equals(res.getString(R.string.coding_loc))){
                    printDetails("loc");
                    drawerLayout.closeDrawer(navList);
                }
                if (selected.equals(res.getString(R.string.coding_fav))) {
                    try {
                        favs = (HashSet<String>) getSharedPreferences(
                                "PREFERENCE", MODE_PRIVATE).getStringSet(
                                "favs", null);
                        if (favs.contains(targetProvince)) {
                            favs.remove(targetProvince);
                            drawerLayout.closeDrawer(navList);
                            Toast.makeText(
                                    getApplicationContext(),
                                    res.getString(R.string.coding_del_fav_txt),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            favs.add(targetProvince);
                            getSharedPreferences(
                                    "PREFERENCE", MODE_PRIVATE).
                                    edit().
                                    putStringSet("favs", favs).commit();
                            drawerLayout.closeDrawer(navList);
                            Toast.makeText(
                                    getApplicationContext(),
                                    res.getString(R.string.coding_new_fav_txt),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (NullPointerException e) {
                        favs = new HashSet<String>();
                        favs.add(targetProvince);
                        getSharedPreferences(
                                "PREFERENCE", MODE_PRIVATE).
                                edit().
                                putStringSet("favs", favs).commit();
                        drawerLayout.closeDrawer(navList);
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.coding_new_fav_txt,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // /////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL ESCUCHADOR DE NUESTRO MENU LATERAL
        // /////////////////////////////////////////////////////////////////////
        navToggle = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.drawable.ic_drawer,
                R.string.open_drawer,
                R.string.close_drawer
            ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(navToggle);
    }
}

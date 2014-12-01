
package es.juvecyl.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class LodgingDetail extends SherlockActivity {
    private float scale;
    private XMLDB db;
    private HashSet<String> favs;
    private boolean isImageFitToScreen;
    private Lodging target;
    private TextView title, desc, phones;
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
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        setContentView(R.layout.details);
        // //////////////////////////////////////////////////////////////////////////
        // CARGAMOS LOS DATOS
        // //////////////////////////////////////////////////////////////////////////
        //db = new XMLDB(getBaseContext());
        db = LodgingSingleton.getInstance(getApplicationContext()).getDB();
        // //////////////////////////////////////////////////////////////////////////
        // PARA QUE LOS PIXELS SEAN EN FUNCIÓN DE LA DENSIDAD DE LA PANTALLA
        // //////////////////////////////////////////////////////////////////////////
        scale = getResources().getDisplayMetrics().density;
        // //////////////////////////////////////////////////////////////////////////
        // CARGAMOS EL ALOJAMIENTO
        // //////////////////////////////////////////////////////////////////////////
        Bundle bundle = this.getIntent().getExtras();
        targetProvince = bundle.getString("lodging");
        LocateLodging(Integer.parseInt(targetProvince));
        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL VIBRADOR
        // //////////////////////////////////////////////////////////////////////////
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // //////////////////////////////////////////////////////////////////////////
        // NAVEGADOR LATERAL
        //
        // PRIMERO HACEMOS QUE EL TÍTULO SEA UN BOTÓN QUE PUEDA EXPANDIR
        // EL MENÚ LATERAL
        //
        // //////////////////////////////////////////////////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.details_drawer_layout);
        navList = (ListView) findViewById(R.id.details_menu_drawer);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL ESCUCHADOR DE NUESTRO MENÚ LATERAL
        // //////////////////////////////////////////////////////////////////////////
        navToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.open_drawer,
                R.string.close_drawer) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(navToggle);

        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS NUESTRO ARRAY DE NAVEGACIÓN PRINCIPAL Y COGEMOS
        // EL COLOR DE LA PROVINCIA
        // //////////////////////////////////////////////////////////////////////////
        navdata = new DetailsNav(1).getNav();
        if (targetProvince.equals("Búsqueda")) {
            Search();
        }

        // //////////////////////////////////////////////////////////////////////////
        // CREANDO EL NAVEGADOR LATERAL
        // //////////////////////////////////////////////////////////////////////////
        navdata = new DetailsNav(1).getNav();
        arrayAdapter = new DetailsNavMaker(this, R.layout.main_list, navdata);
        navList.setAdapter(arrayAdapter);
        navList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int pos, long id) {
                vibe.vibrate(60);
                String selected = ((DetailsNav) a.getAdapter().getItem(pos))
                        .getTitle();
                if (selected.equals("Atrás")) {
                    finish();
                }
                if (selected.equals("Favorito")){
                    try{
                    favs = (HashSet<String>) getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .getStringSet("favs", null);
                    if(favs.contains(targetProvince)){
                        Log.d("favoritos", targetProvince + " ya estaba");
                        favs.remove(targetProvince);
                        drawerLayout.closeDrawer(navList);
                        Toast.makeText(getApplicationContext(),
                                "Alojamiento eliminado", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else{
                        favs.add(targetProvince);
                        Log.d("favoritos", targetProvince + " añadido");
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putStringSet("favs", favs).commit();
                        drawerLayout.closeDrawer(navList);
                        Toast.makeText(getApplicationContext(),
                                "Alojamiento añadido", Toast.LENGTH_SHORT)
                                .show();
                    }
                    }
                    catch (NullPointerException e){
                       favs = new HashSet<String>();
                       favs.add(targetProvince);
                       Log.d("favoritos", targetProvince + " añadido en nueva lista");
                       getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                       .edit().putStringSet("favs", favs).commit();
                       drawerLayout.closeDrawer(navList);
                       Toast.makeText(getApplicationContext(),
                               "Alojamiento añadido", Toast.LENGTH_SHORT)
                               .show();
                    }
                }
                /*
                 * if (selected.equals(targetProvince)) { // NOTHING } else { targetProvince =
                 * selected; setTitle(targetProvince); ClearLayout(); if
                 * (!targetProvince.equals("Búsqueda")) { ProvinceResults(); }
                 * drawerLayout.closeDrawer(navList); }
                 */
            }
        });

        isImageFitToScreen = false;
        title = (TextView) findViewById(R.id.detailsTitle);
        desc = (TextView) findViewById(R.id.detailsDesc);
        phones = (TextView) findViewById(R.id.detailsPhones);
        title.setText(target.getTitle());
        desc.setText(target.getDesc());
        phones.setText("Contacto:\n" + target.getPhones());
        new ImageDownloader(LodgingDetail.this).execute(target.getImage());
    }

    // //////////////////////////////////////////////////////////////////////////
    // AÑADIMOS ELEMENTOS AL MENÚ DEL ACTION BAR
    // //////////////////////////////////////////////////////////////////////////
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
            case R.id.reload:
                vibe.vibrate(60);
                startActivity(new Intent(this, DownloadXML.class));
                finish();
        }

        return true;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void Search() {
        // TODO
    }

    // //////////////////////////////////////////////////////////////////////////
    // BÚSQUEDA DE ALOJAMIENTOS POR NOMBRE
    // //////////////////////////////////////////////////////////////////////////
    protected void LocateLodging(int id) {
        target = db.getLodgings().get(id);
    }

    public void updateBitmap(Bitmap image2) {
        image = (ImageView) findViewById(R.id.detailsImage);
        image.setImageBitmap(image2);

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

}

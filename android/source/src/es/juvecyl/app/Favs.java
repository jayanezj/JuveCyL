
package es.juvecyl.app;

import java.util.HashSet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Favs extends SherlockActivity {
    private float scale;
    private XMLDB db;
    private TextView[] tv;
    private Context actualcontext;
    private Vibrator vibe;
    private HashSet<String> favs;
    private Lodging province;
    // //////////////////////////////////////////////////////////////////////////
    // CAPAS DEL XML
    // //////////////////////////////////////////////////////////////////////////
    private RelativeLayout contentFrame;
    // //////////////////////////////////////////////////////////////////////////
    // CAPAS DE LA ACTIVIDAD
    // //////////////////////////////////////////////////////////////////////////
    private ScrollView scrollMain;
    private LinearLayout linearInsideScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.favs);
        // //////////////////////////////////////////////////////////////////////////
        // PARA QUE LOS PIXELS SEAN EN FUNCIÓN DE LA DENSIDAD DE LA PANTALLA
        // //////////////////////////////////////////////////////////////////////////
        scale = getResources().getDisplayMetrics().density;
        actualcontext = this;
        // //////////////////////////////////////////////////////////////////////////
        // RECOGEMOS DEL XML
        // //////////////////////////////////////////////////////////////////////////
        contentFrame = (RelativeLayout) findViewById(R.id.main_favs_frame);
        // //////////////////////////////////////////////////////////////////////////
        // CARGAMOS LOS DATOS
        // //////////////////////////////////////////////////////////////////////////
        db = LodgingSingleton.getInstance(getApplicationContext()).getDB();
        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL VIBRADOR
        // //////////////////////////////////////////////////////////////////////////
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS UN SCROLLVIEW PARA LOS RESULTADOS
        // //////////////////////////////////////////////////////////////////////////
        scrollMain = new ScrollView(actualcontext);
        scrollMain.setId(2989);
        RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        scrollParams.setMargins(0, (int) (10 * scale + 0.5f), 0, 0);
        scrollParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        scrollParams.addRule(RelativeLayout.BELOW, 1988);
        scrollMain.setLayoutParams(scrollParams);
        // //////////////////////////////////////////////////////////////////////////
        //
        // PRIMERO HACEMOS QUE EL TÍTULO SEA UN BOTÓN QUE PUEDA EXPANDIR
        // EL MENÚ LATERAL
        //
        // //////////////////////////////////////////////////////////////////////////

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // //////////////////////////////////////////////////////////////////////////
        // DECLARAMOS EL LINEAR LAYOUT INTERNO AL SCROLLVIEW
        // //////////////////////////////////////////////////////////////////////////
        linearInsideScroll = new LinearLayout(actualcontext);
        linearInsideScroll.setId(2990);
        LinearLayout.LayoutParams linearInsideParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearInsideParams.topMargin = (int) (40 * scale + 0.5f);
        linearInsideScroll.setOrientation(LinearLayout.VERTICAL);
        linearInsideScroll.setLayoutParams(linearInsideParams);
        ClearLayout();
        setFavs();
    }

    private void setFavs() {
        try {
            favs = (HashSet<String>) getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getStringSet("favs", null);
            tv = new TextView[favs.size()];
            int counter = 0;
            for (String target : favs) {
                province = db.getLodgings().get(Integer.parseInt(target));
                tv[counter] = new TextView(actualcontext);
                LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                prm.setMargins(0, (int) (5 * scale + 0.5f), 0,
                        (int) (5 * scale + 0.5f));
                tv[counter].setLayoutParams(prm);
                tv[counter].setText(province.getTitle());
                // tv[counter].setBackgroundColor(Color.parseColor(provinceColor));
                tv[counter].setTextColor(Color.BLACK);
                tv[counter].setPadding((int) (10 * scale + 0.5f),
                        (int) (20 * scale + 0.5f), (int) (10 * scale + 0.5f),
                        (int) (20 * scale + 0.5f));
                tv[counter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                tv[counter].setTag((Object) target);
                tv[counter].setTypeface(null, Typeface.BOLD);

                tv[counter].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        vibe.vibrate(60);
                        Intent intent = new Intent(Favs.this, LodgingDetail.class);
                        Bundle b = new Bundle();
                        b.putString("lodging", v.getTag().toString());
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

                linearInsideScroll.addView(tv[counter]);
                counter++;
            }
        } catch (NullPointerException e) {
            TextView tv = new TextView(actualcontext);
            tv = new TextView(actualcontext);
            LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            prm.setMargins(0, (int) (5 * scale + 0.5f), 0,
                    (int) (5 * scale + 0.5f));
            tv.setLayoutParams(prm);
            tv.setText("No tienes favoritos, puedes añadir nuevos favoritos" +
                    " desde los detalles de un alojamiento");
            tv.setTextColor(Color.BLACK);
            tv.setPadding((int) (10 * scale + 0.5f),
                    (int) (20 * scale + 0.5f), (int) (10 * scale + 0.5f),
                    (int) (20 * scale + 0.5f));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv.setTypeface(null, Typeface.BOLD);
            linearInsideScroll.addView(tv);
        }
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
                Intent intent = new Intent(Favs.this, Main.class);
                Bundle b = new Bundle();
                b.putString("province", "Búsqueda");
                intent.putExtras(b);
                startActivity(intent);
                finish();
                break;
            case R.id.favs:
                vibe.vibrate(60);
                Toast.makeText(getApplicationContext(),
                        "Ya estás en favoritos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reload:
                vibe.vibrate(60);
                startActivity(new Intent(this, DownloadXML.class));
                finish();
                break;
        }

        return true;
    }

    protected void ClearLayout() {
        contentFrame.removeAllViews();
        contentFrame.addView(scrollMain);
        scrollMain.removeAllViews();
        scrollMain.addView(linearInsideScroll);
        linearInsideScroll.removeAllViews();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            vibe.vibrate(60);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

package es.juvecyl.app;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Main extends SherlockActivity
	{
		private String targetProvince, provinceColor;
		private MainNavMaker arrayAdapter;
		private XMLDB db;
		private int counter = 0;
		private TextView[] tv;
		private Context actualcontext;
		private ArrayList<MainNav> navdata = new ArrayList<MainNav>();
		private Vibrator vibe;
		// //////////////////////////////////////////////////////////////////////////
		// CAPAS DEL XML
		// //////////////////////////////////////////////////////////////////////////
		private RelativeLayout contentFrame;
		private DrawerLayout drawerLayout;
		private ListView navList;
		// ESCUCHADOR DEL MENU NAV
		private ActionBarDrawerToggle navToggle;
		// //////////////////////////////////////////////////////////////////////////
		// CAPAS DE LA ACTIVIDAD
		// //////////////////////////////////////////////////////////////////////////
		private ScrollView scrollMain;
		private LinearLayout linearInsideScroll;
		private TextView provinceTextName;
		private EditText searchField;

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
				setContentView(R.layout.provinces_layout);

				Bundle bundle = this.getIntent().getExtras();
				targetProvince = bundle.getString("province");
				actualcontext = this;
				// //////////////////////////////////////////////////////////////////////////
				// RECOGEMOS DEL XML
				// //////////////////////////////////////////////////////////////////////////
				contentFrame = (RelativeLayout) findViewById(R.id.main_content_frame);
				drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
				navList = (ListView) findViewById(R.id.main_menu_drawer);
				// //////////////////////////////////////////////////////////////////////////
				// ESTABLECEMOS UN TÍTULO
				// //////////////////////////////////////////////////////////////////////////
				setTitle(targetProvince);
				// //////////////////////////////////////////////////////////////////////////
				// CARGAMOS LOS DATOS
				// //////////////////////////////////////////////////////////////////////////
				db = new XMLDB(getBaseContext(), true);
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
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);

				// //////////////////////////////////////////////////////////////////////////
				// DECLARAMOS EL ESCUCHADOR DE NUESTRO MENÚ LATERAL
				// //////////////////////////////////////////////////////////////////////////
				navToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.open_drawer, R.string.close_drawer)
					{
						public void onDrawerClosed(View view)
							{
								invalidateOptionsMenu();
							}

						public void onDrawerOpened(View view)
							{
								invalidateOptionsMenu();
							}
					};
				drawerLayout.setDrawerListener(navToggle);

				// //////////////////////////////////////////////////////////////////////////
				// DECLARAMOS NUESTRO ARRAY DE NAVEGACIÓN PRINCIPAL Y COGEMOS
				// EL COLOR DE LA PROVINCIA
				// //////////////////////////////////////////////////////////////////////////
				navdata = new MainNav(1).getNav();
				if (targetProvince.equals("Búsqueda")) {
					Search();
					provinceColor = navdata.get(0).getBgColor();
				} else {
					Province();
				}

				// //////////////////////////////////////////////////////////////////////////
				// DECLARAMOS UN SCROLLVIEW PARA LOS RESULTADOS
				// //////////////////////////////////////////////////////////////////////////
				scrollMain = new ScrollView(actualcontext);
				scrollMain.setId(1989);
				RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				scrollParams.setMargins(0, 10, 0, 0);
				scrollParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
				scrollParams.addRule(RelativeLayout.BELOW, 1988);
				scrollMain.setLayoutParams(scrollParams);
				// //////////////////////////////////////////////////////////////////////////
				// DECLARAMOS EL LINEAR LAYOUT INTERNO AL SCROLLVIEW
				// //////////////////////////////////////////////////////////////////////////
				linearInsideScroll = new LinearLayout(actualcontext);
				linearInsideScroll.setId(1990);
				LinearLayout.LayoutParams linearInsideParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				linearInsideParams.topMargin = 40;
				linearInsideScroll.setOrientation(LinearLayout.VERTICAL);
				linearInsideScroll.setLayoutParams(linearInsideParams);

				// //////////////////////////////////////////////////////////////////////////
				// CON TODAS LAS CAPAS DECLARADAS LAS AGREGAMOS A LA VISTA
				// //////////////////////////////////////////////////////////////////////////
				if (targetProvince.equals("Búsqueda")) {
					contentFrame.addView(searchField);
					contentFrame.addView(scrollMain);
					scrollMain.addView(linearInsideScroll);
				} else {
					contentFrame.addView(provinceTextName);
					ProvinceResults();
				}
				// //////////////////////////////////////////////////////////////////////////
				// CREANDO EL NAVEGADOR LATERAL
				// //////////////////////////////////////////////////////////////////////////
				navdata = new MainNav(1).getNav();
				arrayAdapter = new MainNavMaker(this, R.layout.main_list, navdata);
				navList.setAdapter(arrayAdapter);
				navList.setOnItemClickListener(new OnItemClickListener()
					{

						@Override
						public void onItemClick(AdapterView<?> a, View v, int pos, long id)
							{
								vibe.vibrate(60);
								String selected = ((MainNav) a.getAdapter().getItem(pos)).getTitle();
								if (selected.equals(targetProvince)) {
									// NOTHING
								} else {
									targetProvince = selected;
									setTitle(targetProvince);
									ClearLayout();
									if (!targetProvince.equals("Búsqueda")) {
										ProvinceResults();
									}
									drawerLayout.closeDrawer(navList);
								}

							}
					});
			}

		// //////////////////////////////////////////////////////////////////////////
		// AÑADIMOS ELEMENTOS AL MENÚ DEL ACTION BAR
		// //////////////////////////////////////////////////////////////////////////
		@Override
		public boolean onCreateOptionsMenu(Menu menu)
			{
				MenuInflater inflater = getSupportMenuInflater();
				inflater.inflate(R.menu.menu, menu);
				return true;
			}

		@SuppressLint("NewApi")
		@SuppressWarnings("deprecation")
		protected void Search()
			{
				// //////////////////////////////////////////////////////////////////////////
				// DECLARAMOS UN TEXTFIELD PARA BÚSQUEDAS
				// //////////////////////////////////////////////////////////////////////////
				searchField = new EditText(actualcontext);
				searchField.setId(1988);
				RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				editTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
				editTextParams.topMargin = 15;
				searchField.setPadding(25, 20, 0, 5);
				searchField.setLayoutParams(editTextParams);
				searchField.setGravity(Gravity.LEFT);
				searchField.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View arg0)
							{
								vibe.vibrate(60);
							}
					});
				// //////////////////////////////////////////////////////////////////////////
				// COMPROBAMOS LA VERSIÓN DE ANDROID PARA USAR LA PROPIEDAD DEPRECADA
				// O LA NUEVA SI EL TARGET ES COMO MÍNIMO DE JELLY BEAN
				// //////////////////////////////////////////////////////////////////////////
				int sdk = android.os.Build.VERSION.SDK_INT;
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					searchField.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_box));
				} else {
					searchField.setBackground(getResources().getDrawable(R.drawable.search_box));
				}
				// //////////////////////////////////////////////////////////////////////////
				// ESCUCHADOR PARA CUANDO ESCRIBAMOS EN LA CAJA DE TEXTO
				// //////////////////////////////////////////////////////////////////////////
				searchField.addTextChangedListener(new TextWatcher()
					{

						@Override
						public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
							{
								String searchString = searchField.getText().toString();
								// //////////////////////////////////////////////////////////////////////////
								// COMO MÍNIMO ESPERAMOS A 2 CARACTERES DE ENTRADA
								// //////////////////////////////////////////////////////////////////////////
								if (searchString.length() > 2) {
									Log.d("busca","busca");
									SearchResults(searchString);
								} else {
									scrollMain.removeAllViews();
									scrollMain.addView(linearInsideScroll);
									linearInsideScroll.removeAllViews();
								}
							}

						@Override
						public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
							{
								// NO SE USA
							}

						@Override
						public void afterTextChanged(Editable arg0)
							{
								// NO SE USA

							}
					});
			}

		// //////////////////////////////////////////////////////////////////////////
		// BÚSQUEDA DE ALOJAMIENTOS POR NOMBRE
		// //////////////////////////////////////////////////////////////////////////
		protected void SearchResults(String searchString)
			{
				//scrollMain.removeAllViews();
				//scrollMain.addView(linearInsideScroll);
				//linearInsideScroll.removeAllViews();
				counter = 0;
				// //////////////////////////////////////////////////////////////////////////
				// BUSCAMOS COINCIDENCIAS Y DESPUÉS INSTANCIAMOS TANTOS TEXTVIEW
				// COMO
				// COINCIDENCIAS HAYA
				// //////////////////////////////////////////////////////////////////////////
				for (int i = 0; i < db.getLodgings().size(); i++) {
					if (db.getLodgings().get(i).getTitle().toLowerCase().contains(searchString.toLowerCase())) {
						counter++;
						Log.d("Resultados",""+counter);
					}
				}
				tv = new TextView[counter];
				counter = 0;
				// //////////////////////////////////////////////////////////////////////////
				// METEMOS LAS COINCIDENCIAS EN SENDOS TEXTVIEW
				// //////////////////////////////////////////////////////////////////////////
				for (int i = 0; i < db.getLodgings().size(); i++) {
					if (db.getLodgings().get(i).getTitle().toLowerCase().contains(searchString.toLowerCase())) {
						tv[counter] = new TextView(actualcontext);
						LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						prm.setMargins(0, 10, 0, 10);
						tv[counter].setLayoutParams(prm);
						tv[counter].setText(db.getLodgings().get(i).getTitle());
						for (int j = 0; j < navdata.size(); j++) {
							if (navdata.get(j).getTitle().equals(db.getLodgings().get(i).getProvince())) {
								tv[counter].setBackgroundColor(Color.parseColor(navdata.get(j).getBgColor()));
								break;
							}
						}
						tv[counter].setTextColor(Color.WHITE);
						tv[counter].setPadding(10, 20, 10, 20);
						tv[counter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
						tv[counter].setTypeface(null, Typeface.BOLD);
						linearInsideScroll.addView(tv[counter]);
						counter++;
					}
				}
			}

		protected void Province()
			{
				for (int i = 0; i < navdata.size(); i++) {
					if (navdata.get(i).getTitle().equals(targetProvince)) {
						provinceColor = navdata.get(i).getBgColor();
						break;
					}
				}
				// //////////////////////////////////////////////////////////////////////////
				// DECLARAMOS UN TEXTVIEW
				// //////////////////////////////////////////////////////////////////////////
				provinceTextName = new TextView(actualcontext);
				provinceTextName.setId(1988);
				RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				provinceTextName.setLayoutParams(textViewParams);
				provinceTextName.setGravity(Gravity.CENTER);
				provinceTextName.setTextAppearance(actualcontext, android.R.style.TextAppearance_Large);
				provinceTextName.setText(targetProvince);
				provinceTextName.setTextSize(getResources().getDimension(R.dimen.small_font));
				provinceTextName.setTextColor(getResources().getColor(R.color.white));
				provinceTextName.setBackgroundColor(Color.parseColor(provinceColor));
			}

		protected void ProvinceResults()
			{
				for (int i = 0; i < navdata.size(); i++) {
					if (navdata.get(i).getTitle().equals(targetProvince)) {
						provinceColor = navdata.get(i).getBgColor();
						break;
					}
				}
				// //////////////////////////////////////////////////////////////////////////
				// MOSTRAR RESULTADOS DE LA PROVINCIA
				// //////////////////////////////////////////////////////////////////////////
				counter = 0;
				for (int i = 0; i < db.getLodgings().size(); i++) {
					if (db.getLodgings().get(i).getProvince().equals(targetProvince)) {
						counter++;
					}
				}
				tv = new TextView[counter];
				counter = 0;
				for (int i = 0; i < db.getLodgings().size(); i++) {
					if (db.getLodgings().get(i).getProvince().equals(targetProvince)) {
						tv[counter] = new TextView(actualcontext);
						LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						prm.setMargins(0, 10, 0, 10);
						tv[counter].setLayoutParams(prm);
						tv[counter].setText(db.getLodgings().get(i).getTitle());
						tv[counter].setBackgroundColor(Color.parseColor(provinceColor));
						tv[counter].setTextColor(Color.WHITE);
						tv[counter].setPadding(10, 20, 10, 20);
						tv[counter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
						tv[counter].setTypeface(null, Typeface.BOLD);
						linearInsideScroll.addView(tv[counter]);
						counter++;
					}
				}
			}

		protected void ClearLayout()
			{
				contentFrame.removeAllViews();
				if (targetProvince.equals("Búsqueda")) {
					Search();
					contentFrame.addView(searchField);
				} else {
					Province();
					contentFrame.addView(provinceTextName);
				}
				contentFrame.addView(scrollMain);
				scrollMain.removeAllViews();
				scrollMain.addView(linearInsideScroll);
				linearInsideScroll.removeAllViews();
			}

		public boolean onMenuItemSelected(int featureId, MenuItem item)
			{

				int itemId = item.getItemId();
				switch (itemId)
					{
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
		protected void onPostCreate(Bundle savedInstanceState)
			{
				super.onPostCreate(savedInstanceState);
				navToggle.syncState();
			}

		@Override
		public void onConfigurationChanged(Configuration newConfig)
			{
				super.onConfigurationChanged(newConfig);
				navToggle.onConfigurationChanged(newConfig);
			}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					moveTaskToBack(true);
					return true;
				}
				return super.onKeyDown(keyCode, event);
			}
	}

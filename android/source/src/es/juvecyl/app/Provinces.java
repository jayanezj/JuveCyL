package es.juvecyl.app;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Provinces extends SherlockActivity
	{
		private ListView navList;
		private TextView tv1;
		private String targetProvince, provinceColor;
		private MainNavMaker arrayAdapter;
		private XMLDB db;
		private int counter = 0;
		private TextView[] tv;
		private LinearLayout layout;
		private Context actualcontext;
		ArrayList<MainNav> navdata = new ArrayList<MainNav>();

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
				setContentView(R.layout.provinces_layout);
				Bundle bundle = this.getIntent().getExtras();
				targetProvince = bundle.getString("province");
				actualcontext=this;
				// //////////////////////////////////////////////////////////////////////////
				// INSTANCIAMOS NUESTRO ARRAY DE NAVEGACIÓN PRINCIPAL Y COGEMOS
				// EL COLOR DE LA PROVINCIA
				// //////////////////////////////////////////////////////////////////////////
				navdata = new MainNav(1).getNav();
				for (int i = 0; i < navdata.size(); i++) {
					if (navdata.get(i).getTitle().equals(targetProvince)) {
						provinceColor = navdata.get(i).getBgColor();
						break;
					}
				}
				// //////////////////////////////////////////////////////////////////////////
				// MOSTRAR RESULTADOS DE LA PROVINCIA
				// //////////////////////////////////////////////////////////////////////////
				layout = (LinearLayout) findViewById(R.id.provinces_L_Layout_Results);
				db = new XMLDB(getBaseContext(), true);
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
						LinearLayout.LayoutParams prm=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						prm.setMargins(0, 10, 0, 10);
						tv[counter].setLayoutParams(prm);
						tv[counter].setText(db.getLodgings().get(i).getTitle());
						tv[counter].setBackgroundColor(Color.parseColor(provinceColor));
						tv[counter].setTextColor(Color.WHITE);
						tv[counter].setPadding(10, 20, 10, 20);
						tv[counter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
						tv[counter].setTypeface(null, Typeface.BOLD);
						layout.addView(tv[counter]);
						counter++;
					}
				}
				// //////////////////////////////////////////////////////////////////////////
				// CREANDO EL NAVEGADOR LATERAL
				// //////////////////////////////////////////////////////////////////////////
				navList = (ListView) findViewById(R.id.main_left_drawer);
				navdata = new MainNav(1).getNav();
				arrayAdapter = new MainNavMaker(this, R.layout.main_list, navdata);
				navList.setAdapter(arrayAdapter);
				tv1 = (TextView) findViewById(R.id.provinces_tv1);
				tv1.setText(targetProvince);
				tv1.setBackgroundColor(Color.parseColor(provinceColor));
				navList.setOnItemClickListener(new OnItemClickListener()
					{

						@Override
						public void onItemClick(AdapterView<?> a, View v, int pos, long id)
							{
								// TODO Auto-generated method stub
								String selected = ((MainNav) a.getAdapter().getItem(pos)).getTitle();
								if (selected.equals(targetProvince)) {
									// NOTHING
								} else if (selected.equals("Búsqueda")) {
									Intent intent = new Intent(Provinces.this, Main.class);
									startActivity(intent);
									finish();
								} else {
									targetProvince=selected;
									layout.removeAllViews();
									for (int i = 0; i < navdata.size(); i++) {
										if (navdata.get(i).getTitle().equals(targetProvince)) {
											provinceColor = navdata.get(i).getBgColor();
											break;
										}
									}
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
											LinearLayout.LayoutParams prm=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
											prm.setMargins(0, 10, 0, 10);
											tv[counter].setLayoutParams(prm);
											tv[counter].setText(db.getLodgings().get(i).getTitle());
											tv[counter].setBackgroundColor(Color.parseColor(provinceColor));
											tv[counter].setTextColor(Color.WHITE);
											tv[counter].setPadding(10, 20, 10, 20);
											tv[counter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
											tv[counter].setTypeface(null, Typeface.BOLD);
											layout.addView(tv[counter]);
											counter++;
										}
									}
									tv1.setText(targetProvince);
									tv1.setBackgroundColor(Color.parseColor(provinceColor));
								}

							}
					});
			}

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
			{
				MenuInflater inflater = getSupportMenuInflater();
				inflater.inflate(R.menu.menu, menu);
				return true;
			}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
			{
				if (item.getItemId() == R.id.reload) {
					startActivity(new Intent(this, DownloadXML.class));
					finish();
				}
				return true;
			}

	}

package es.juvecyl.app;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
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
		private ListView navList;
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
				contentFrame = (RelativeLayout) findViewById(R.id.main_content_frame);
				setTitle(targetProvince);
				vibe=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
				// //////////////////////////////////////////////////////////////////////////
				// INSTANCIAMOS NUESTRO ARRAY DE NAVEGACIÓN PRINCIPAL Y COGEMOS
				// EL COLOR DE LA PROVINCIA
				// //////////////////////////////////////////////////////////////////////////
				navdata = new MainNav(1).getNav();
				for (int i = 0; i < navdata.size(); i++)
					{
						if (navdata.get(i).getTitle().equals(targetProvince))
							{
								provinceColor = navdata.get(i).getBgColor();
								break;
							}
					}
				if (targetProvince.equals("Búsqueda"))
					{
						Search();
					}
				else
					{
						Province();

					}

				// //////////////////////////////////////////////////////////////////////////
				// INSTANCIAMOS UN SCROLLVIEW
				// //////////////////////////////////////////////////////////////////////////
				scrollMain = new ScrollView(actualcontext);
				scrollMain.setId(1989);
				RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				scrollParams.setMargins(0, 10, 0, 0);
				scrollParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
				scrollParams.addRule(RelativeLayout.BELOW, 1988);
				scrollMain.setLayoutParams(scrollParams);
				// //////////////////////////////////////////////////////////////////////////
				// INSTANCIAMOS EL LINEAR LAYOUT INTERNO AL SCROLLVIEW
				// //////////////////////////////////////////////////////////////////////////
				linearInsideScroll = new LinearLayout(actualcontext);
				linearInsideScroll.setId(1990);
				LinearLayout.LayoutParams linearInsideParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				linearInsideParams.topMargin = 40;
				linearInsideScroll.setOrientation(LinearLayout.VERTICAL);
				linearInsideScroll.setLayoutParams(linearInsideParams);

				// //////////////////////////////////////////////////////////////////////////
				// CON TODAS LAS CAPAS INSTANCIADAS LAS AGREGAMOS A LA VISTA
				// //////////////////////////////////////////////////////////////////////////
				if (targetProvince.equals("Búsqueda"))
					{
						contentFrame.addView(searchField);
					}
				else
					{
						contentFrame.addView(provinceTextName);
					}
				contentFrame.addView(scrollMain);
				scrollMain.addView(linearInsideScroll);
				// //////////////////////////////////////////////////////////////////////////
				// MOSTRAR RESULTADOS DE LA PROVINCIA
				// //////////////////////////////////////////////////////////////////////////
				db = new XMLDB(getBaseContext(), true);
				counter = 0;
				for (int i = 0; i < db.getLodgings().size(); i++)
					{
						if (db.getLodgings().get(i).getProvince().equals(targetProvince))
							{
								counter++;
							}
					}
				Log.d("Resultados", counter + "");
				tv = new TextView[counter];
				counter = 0;
				for (int i = 0; i < db.getLodgings().size(); i++)
					{
						if (db.getLodgings().get(i).getProvince().equals(targetProvince))
							{
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
								Log.e("views", counter + "");
							}
					}
				// //////////////////////////////////////////////////////////////////////////
				// CREANDO EL NAVEGADOR LATERAL
				// //////////////////////////////////////////////////////////////////////////
				navList = (ListView) findViewById(R.id.main_menu_drawer);
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
								if (selected.equals(targetProvince))
									{
										// NOTHING
									}
								else
									{
										targetProvince = selected;
										setTitle(targetProvince);
										contentFrame.removeAllViews();
										if (targetProvince.equals("Búsqueda"))
											{
												Search();
												contentFrame.addView(searchField);
											}
										else
											{
												Province();
												contentFrame.addView(provinceTextName);
											}
										contentFrame.addView(scrollMain);
										scrollMain.removeAllViews();
										scrollMain.addView(linearInsideScroll);
										linearInsideScroll.removeAllViews();
										if (!targetProvince.equals("Búsqueda"))
											{

												for (int i = 0; i < navdata.size(); i++)
													{
														if (navdata.get(i).getTitle().equals(targetProvince))
															{
																provinceColor = navdata.get(i).getBgColor();
																break;
															}
													}
												counter = 0;
												for (int i = 0; i < db.getLodgings().size(); i++)
													{
														if (db.getLodgings().get(i).getProvince().equals(targetProvince))
															{
																counter++;
															}
													}

												tv = new TextView[counter];
												counter = 0;
												for (int i = 0; i < db.getLodgings().size(); i++)
													{
														if (db.getLodgings().get(i).getProvince().equals(targetProvince))
															{
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
												provinceTextName.setText(targetProvince);
												provinceTextName.setBackgroundColor(Color.parseColor(provinceColor));
											}
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
				if (item.getItemId() == R.id.reload)
					{
						vibe.vibrate(60);
						startActivity(new Intent(this, DownloadXML.class));
						finish();
					}
				return true;
			}

		protected void Search()
			{
				// //////////////////////////////////////////////////////////////////////////
				// INSTANCIAMOS UN TEXTFIELD
				// //////////////////////////////////////////////////////////////////////////
				searchField = new EditText(actualcontext);
				searchField.setId(1988);
				RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				searchField.setLayoutParams(editTextParams);
				searchField.setGravity(Gravity.CENTER);
			}

		protected void Province()
			{
				// //////////////////////////////////////////////////////////////////////////
				// INSTANCIAMOS UN TEXTVIEW
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

	}

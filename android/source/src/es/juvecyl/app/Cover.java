package es.juvecyl.app;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.ShowcaseViews;

public class Cover extends SherlockActivity implements ShowcaseView.OnShowcaseEventListener
	{
		private DrawerLayout drawerLayout;
		private ListView navList;
		private ActionBarDrawerToggle navToggle;
		private Button buttonPull;
		private ShowcaseView sv, sv2 = null;
		private Timer limit;
		private TimerTask tsk1;
		private ShowcaseViews mViews;
		private Vibrator vibe;
		private ShowcaseView.ConfigOptions mOptions = new ShowcaseView.ConfigOptions();

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				setContentView(R.layout.first_run_layout);

				ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
				co.hideOnClickOutside = true;
				
				// //////////////////////////////////////////////////////////////////////////
				// RECOGEMOS DEL XML
				// //////////////////////////////////////////////////////////////////////////
				drawerLayout = (DrawerLayout) findViewById(R.id.first_run_drawer_layout);
				navList = (ListView) findViewById(R.id.first_run_left_drawer);
				
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
				// DECLARAMOS EL VIBRADOR
				// //////////////////////////////////////////////////////////////////////////
				vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				
				sv = ShowcaseView.insertShowcaseView(R.id.img_first_pull, this, R.string.tutorial_title, R.string.tutorial_message, co);
				sv.setOnShowcaseEventListener(this);
				navList = (ListView) findViewById(R.id.first_run_left_drawer);
				buttonPull = (Button) findViewById(R.id.btn_first_pull);
				final String[] names = getResources().getStringArray(R.array.nav_options);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
				navList.setAdapter(adapter);
				tsk1 = new TimerTask()
					{
						public void run()
							{
								Cover.this.runOnUiThread(new Runnable()
									{

										@Override
										public void run()
											{
												sv.animateGesture(-70, 0, 250, 0);
												// limit.schedule(tsk1, 1000);

											}

									});
							}
					};
				limit = new Timer();
				limit.scheduleAtFixedRate(tsk1, 500, 6500);

			}

		@Override
		public void onShowcaseViewHide(ShowcaseView showcaseView)
			{
				buttonPull.setText(R.string.first_run_got_it);
				if (!sv.isActivated() && sv2 == null)
					{
						limit = new Timer();
						limit.schedule(new TimerTask()
							{

								@Override
								public void run()
									{
										Cover.this.runOnUiThread(new Runnable()
											{
												public void run()
													{
														mOptions.block = false;
														mOptions.hideOnClickOutside = true;
														mOptions.fadeInDuration = 10000;
														mViews = new ShowcaseViews(Cover.this, R.layout.first_run_layout, new ShowcaseViews.OnShowcaseAcknowledged()
															{
																@Override
																public void onShowCaseAcknowledged(ShowcaseView showcaseView)
																	{
																		buttonPull.setVisibility(View.VISIBLE);
																		buttonPull.setOnClickListener(new OnClickListener()
																			{

																				@Override
																				public void onClick(View arg0)
																					{
																						// TODO Auto-generated method stub
																						Intent intent = new Intent(Cover.this, Main.class);
																						Bundle b = new Bundle();
																						b.putString("province", "Búsqueda");
																						intent.putExtras(b);
																						startActivity(intent);
																						finish();
																					}
																			});
																	}
															});
														mViews.addView(new ShowcaseViews.ItemViewProperties(android.R.id.home, R.string.tutorial_title, R.string.tutorial_message2, 1.0f));
														mViews.show();
													}
											});

									}
							}, 2500);

					}
			}

		public void onShowcaseViewShow(ShowcaseView showcaseView)
			{
				buttonPull.setText(R.string.pull_first_run);
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
	}

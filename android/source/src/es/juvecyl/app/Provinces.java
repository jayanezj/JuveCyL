package es.juvecyl.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
		private String targetProvince;
		private AlertsAdapter arrayAdapter;
		ArrayList<MainNav> navdata = new ArrayList<MainNav>();

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				setContentView(R.layout.provinces_layout);
				Bundle bundle = this.getIntent().getExtras();
				targetProvince = bundle.getString("province");
				navList = (ListView) findViewById(R.id.main_left_drawer);
				navdata.add(new MainNav("Búsqueda", "#4a4a4a"));
				navdata.add(new MainNav("Ávila", "#5b75c8"));
				navdata.add(new MainNav("Burgos", "#aead4f"));
				navdata.add(new MainNav("León", "#5bc1c8"));
				navdata.add(new MainNav("Palencia", "#aeaf73"));
				navdata.add(new MainNav("Salamanca", "#ae4f65"));
				navdata.add(new MainNav("Segovia", "#6499bf"));
				navdata.add(new MainNav("Soria", "#c38960"));
				navdata.add(new MainNav("Valladolid", "#9b78aa"));
				navdata.add(new MainNav("Zamora", "#724fae"));
				arrayAdapter = new AlertsAdapter(this, R.layout.main_list, navdata);
				navList.setAdapter(arrayAdapter);
				tv1 = (TextView) findViewById(R.id.provinces_tv1);
				tv1.setText(targetProvince);
				for (int i = 0; i < navdata.size(); i++) {
					if (navdata.get(i).getTitle().equals(targetProvince)) {
						tv1.setBackgroundColor(Color.parseColor(navdata.get(i).getBgColor()));
						break;
					}
				}
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
									Intent intent = new Intent(Provinces.this, Provinces.class);
									Bundle b = new Bundle();
									b.putString("province", selected);
									intent.putExtras(b);
									startActivity(intent);
									finish();
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

		class AlertsAdapter extends ArrayAdapter<MainNav>
			{

				int resource;
				String response;
				Context context;

				// Initialize adapter
				public AlertsAdapter(Context context, int resource, List<MainNav> items)
					{
						super(context, resource, items);
						this.resource = resource;
					}

				public View getView(int position, View convertView, ViewGroup parent)
					{
						LinearLayout alertView;
						// Get the current alert object
						MainNav al = getItem(position);

						// Inflate the view
						if (convertView == null) {
							alertView = new LinearLayout(getContext());
							alertView.setBackgroundColor(Color.parseColor(al.getBgColor()));
							alertView.setPadding(0, 15, 0, 15);
							String inflater = Context.LAYOUT_INFLATER_SERVICE;
							LayoutInflater vi;
							vi = (LayoutInflater) getContext().getSystemService(inflater);
							vi.inflate(resource, alertView, true);
						} else {
							alertView = (LinearLayout) convertView;
						}
						// Get the text boxes from the listitem.xml file
						TextView alertText = (TextView) alertView.findViewById(R.id.TitleList);

						// Assign the appropriate data from our alert object
						// above
						alertText.setText(al.getTitle());
						alertText.setPadding(20, 0, 0, 0);
						return alertView;
					}
			}
	}

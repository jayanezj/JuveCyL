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

public class Main extends SherlockActivity
	{
		private ListView navList;
		private AlertsAdapter arrayAdapter;
		ArrayList<MainNav> navdata=new ArrayList<MainNav>();
		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				setContentView(R.layout.main_layout);
				//CREACIÓN DEL MENÚ LATERAL CON COLORES
				//RECOGEMOS NUESTRO MENÚ LATERAL
				navList = (ListView) findViewById(R.id.main_left_drawer);
				//CREAMOS LOS ELEMENTOS QUE COMPONDRÁN EL MENÚ
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
				//INSTANCIAMOS NUESTRA CLASE EXTENDIDA DE ARRAYADAPTER
				arrayAdapter = new AlertsAdapter(this, R.layout.main_list,navdata);
				//COLOCAMOS EL NAV BAR
				navList.setAdapter(arrayAdapter);
				//ESCUCHADOR PARA CLICK EN LA NAV BAR
				navList.setOnItemClickListener(new OnItemClickListener()
					{

						@Override
						public void onItemClick(AdapterView<?> a, View v, int pos, long id)
							{
								//RECOGEMOS QUÉ ELEMENTO HA SIDO CLICKADO
								String selected =((MainNav)a.getAdapter().getItem(pos)).getTitle();
								if (!selected.equals("Búsqueda")){
								//GUARDAMOS LA PROVINCIA DESTINO Y LANZAMOS LA ACTIVIDAD DE PROVINCIAS
								Intent intent = new Intent(Main.this, Provinces.class);
								Bundle b = new Bundle();
				                b.putString("province", selected);
				                intent.putExtras(b);
								startActivity(intent);
								finish();}
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
		//CLASE EXTENDIDA DE ARRAYADAPTER
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

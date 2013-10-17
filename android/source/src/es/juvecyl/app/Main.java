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
		private MainNavMaker arrayAdapter;
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
				navdata=new MainNav(1).getNav();
				//INSTANCIAMOS NUESTRA CLASE EXTENDIDA DE ARRAYADAPTER
				arrayAdapter = new MainNavMaker(this, R.layout.main_list,navdata);
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
	}

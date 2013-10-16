package es.juvecyl.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DownloadXML extends SherlockActivity {
	private boolean firstrun;
	private Background bgtask;
	private Button downloadButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_xml_layout);
		bgtask = new Background();
		bgtask.execute();
	}

	private class Background extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			OutputStreamWriter out = null;
			try {
				// TODO code application logic here
				// URL remoteXML = new URL("http://10.0.2.2/1284198756067.xml");
				URL remoteXML = new URL(
						"http://www.datosabiertos.jcyl.es/web/jcyl/risp/es/directorio/AlberguesJuveniles/1284198756067.xml");
				BufferedReader read = new BufferedReader(new InputStreamReader(
						remoteXML.openConnection().getInputStream()));
				out = new OutputStreamWriter(openFileOutput("db.xml",
						Context.MODE_PRIVATE));
				String line;
				while ((line = read.readLine()) != null) {
					out.write(line);
				}
				out.close();
			} catch (MalformedURLException ex) {
				Log.e("IOE", ex.toString());
			} catch (UnknownHostException ex) {
				return false;
			} catch (IOException ex) {
				Log.e("IOE", ex.toString());
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			//
		}

		@Override
		protected void onPreExecute() {
			Toast.makeText(DownloadXML.this,
					"Actualizando la base de datos...", Toast.LENGTH_SHORT)
					.show();
			TimerTask tsk1=new TimerTask(){public void run(){Log.e("Te pasas","Te pasas");bgtask.cancel(false);}};
			Timer limit=new Timer();
			limit.schedule(tsk1, 6000);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast.makeText(DownloadXML.this, "Base de datos actualizada",
						Toast.LENGTH_SHORT).show();
				firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
						.getBoolean("firstrun", true);
				if (firstrun) {
					Intent intent = new Intent(DownloadXML.this, Cover.class);
					startActivity(intent);
					finish();
					// Save the state
					getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
							.putBoolean("firstrun", false).commit();
				} else {

					Intent intent = new Intent(DownloadXML.this, Main.class);
					startActivity(intent);
					finish();
				}
			} else {
				Toast.makeText(DownloadXML.this, "Error en la actualización",
						Toast.LENGTH_SHORT).show();
				try {
					FileInputStream dataBase = getBaseContext().openFileInput(
							"db.xml");
					downloadButton = (Button) findViewById(R.id.downloadButton);
					downloadButton.setText("Usar datos locales");
					downloadButton.setVisibility(View.VISIBLE);
					downloadButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							firstrun = getSharedPreferences("PREFERENCE",
									MODE_PRIVATE).getBoolean("firstrun", true);
							if (firstrun) {
								Intent intent = new Intent(DownloadXML.this,
										Cover.class);
								startActivity(intent);
								finish();
								// Save the state
								getSharedPreferences("PREFERENCE", MODE_PRIVATE)
										.edit().putBoolean("firstrun", false)
										.commit();
							} else {

								Intent intent = new Intent(DownloadXML.this,
										Main.class);
								startActivity(intent);
								finish();
							}
						}
					});

				} catch (FileNotFoundException e) {
					downloadButton = (Button) findViewById(R.id.downloadButton);
					downloadButton.setText("Reintentar");
					downloadButton.setVisibility(View.VISIBLE);
					downloadButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							downloadButton.setVisibility(View.INVISIBLE);
							bgtask = new Background();
							bgtask.execute();
						}
					});
				}
			}

		}

		@Override
		protected void onCancelled() {
			Toast.makeText(DownloadXML.this, "Tarea cancelada!",
					Toast.LENGTH_SHORT).show();
			
			try {
				FileInputStream dataBase = getBaseContext().openFileInput(
						"db.xml");
				downloadButton = (Button) findViewById(R.id.downloadButton);
				downloadButton.setText("Usar datos locales");
				downloadButton.setVisibility(View.VISIBLE);
				downloadButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						firstrun = getSharedPreferences("PREFERENCE",
								MODE_PRIVATE).getBoolean("firstrun", true);
						if (firstrun) {
							Intent intent = new Intent(DownloadXML.this,
									Cover.class);
							startActivity(intent);
							finish();
							// Save the state
							getSharedPreferences("PREFERENCE", MODE_PRIVATE)
									.edit().putBoolean("firstrun", false)
									.commit();
						} else {

							Intent intent = new Intent(DownloadXML.this,
									Main.class);
							startActivity(intent);
							finish();
						}
					}
				});

			} catch (FileNotFoundException e) {
				downloadButton = (Button) findViewById(R.id.downloadButton);
				downloadButton.setText("Reintentar");
				downloadButton.setVisibility(View.VISIBLE);
				downloadButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						downloadButton.setVisibility(View.INVISIBLE);
						bgtask = new Background();
						bgtask.execute();
					}
				});
			}
		}
	}

}

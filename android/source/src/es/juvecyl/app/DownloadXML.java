
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
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadXML extends SherlockActivity
{
    private boolean firstrun;
    private Background bgtask;
    private Button downloadButton;
    private Timer limit;
    private ProgressBar pgb1;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.download_xml_layout);
        pgb1 = (ProgressBar) findViewById(R.id.download_progress_bar);
        bgtask = new Background();
        bgtask.execute();
    }

    /**
     * CLASE EXTENDIDA DE ASYNCTASK PARA EJECUTAR EN SEGUNDO PLANO LA DESCARGA DEL FICHERO XML DEL
     * PORTAL DE DATOS ABIERTOS
     * 
     * @author berik
     */
    private class Background extends AsyncTask<Void, Integer, Boolean>
    {

        // TAREA A REALIZAR
        // DEVUELVE TRUE SI SE REALIZA CORRECTAMENTE
        // FALSE SI FALLA
        @Override
        protected Boolean doInBackground(Void... arg0)
        {
            OutputStreamWriter out = null;
            try {
                // URL("http://10.0.2.2/1284198756067.xml");
                URL remoteXML = new URL(
                        "http://www.datosabiertos.jcyl.es/web/jcyl/risp/es/directorio/AlberguesJuveniles/1284198756067.xml");
                BufferedReader read = new BufferedReader(new InputStreamReader(remoteXML
                        .openConnection().getInputStream()));
                out = new OutputStreamWriter(openFileOutput("db.xml", Context.MODE_PRIVATE));
                String line;
                while ((line = read.readLine()) != null) {
                    out.write(line);
                }
                out.close();
            } catch (MalformedURLException ex) {
                Log.e("URL", ex.toString());
                return false;
            } catch (UnknownHostException ex) {
                Log.e("HOST", ex.toString());
                return false;
            } catch (IOException ex) {
                Log.e("IOE", ex.toString());
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //
        }

        @Override
        protected void onPreExecute()
        {
            // MENSAJE DE ACTUALIZACIÓN
            Toast.makeText(DownloadXML.this,
                    "Actualizando la base de datos...",
                    Toast.LENGTH_SHORT).show();
            pgb1.setVisibility(View.VISIBLE);
            // INSTANCIAMOS UN TIMER PARA VER SI TARDA DEMASIADO EN
            // DESCARGAR
            TimerTask tsk1 = new TimerTask()
            {
                public void run()
                {
                    Log.e("Exceed", "Han pasado 8 segundos y no se ha descargado");
                    bgtask.cancel(false);
                }
            };
            limit = new Timer();
            limit.schedule(tsk1, 8000);
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            // DESCARGA SATISFACTORIA
            if (result) {
                // DETENMOS EL TIMER
                limit.cancel();
                pgb1.setVisibility(View.INVISIBLE);
                // MENSAJE DE ACTUALIZADO
                Toast.makeText(DownloadXML.this, "Base de datos actualizada", Toast.LENGTH_SHORT)
                        .show();
                // COMPROBAMOS SI ES LA PRIMERA EJECUCIÓN DE LA APP
                // SI ES LA PRIMERA VEZ APARECERÁ UNA ACTIVIDAD DE
                // TUTORIAL
                firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun",
                        true);
                if (firstrun) {
                    LodgingSingleton.getInstance(getApplicationContext()).setDB();
                    Intent intent = new Intent(DownloadXML.this, Cover.class);
                    startActivity(intent);
                    finish();
                    // GUARDAMOS LA PREFERENCIA PARA QUE NO SE
                    // VUELVA A EJECUTAR LA ACTIVIDAD
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putBoolean("firstrun", false).commit();
                } else {
                    pgb1.setVisibility(View.INVISIBLE);
                    // MANDAMOS A LA ACTIVIDAD PRINCIPAL
                    LodgingSingleton.getInstance(getApplicationContext()).setDB();
                    Intent intent = new Intent(DownloadXML.this, Main.class);
                    Bundle b = new Bundle();
                    b.putString("province", "Búsqueda");
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            }
            // DESCARGA FALLIDA
            else {
                pgb1.setVisibility(View.INVISIBLE);
                // INFORMAMOS DEL FALLO DE LA DESCARGA
                Toast.makeText(DownloadXML.this, "Error en la actualización", Toast.LENGTH_SHORT)
                        .show();
                // COMPROBAMOS SI EL DISPOSITIVO TIENE UNA COPIA ANTIGUA DE LA BASE DE DATOS
                try {
                    @SuppressWarnings("unused")
                    FileInputStream dataBase = getBaseContext().openFileInput("db.xml");
                    downloadButton = (Button) findViewById(R.id.downloadButton);
                    downloadButton.setText("Usar datos locales");
                    downloadButton.setVisibility(View.VISIBLE);
                    downloadButton.setOnClickListener(new OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean(
                                    "firstrun", true);
                            if (firstrun) {
                                LodgingSingleton.getInstance(getApplicationContext()).setDB();
                                Intent intent = new Intent(DownloadXML.this, Cover.class);
                                startActivity(intent);
                                finish();
                                // Save the state
                                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                        .putBoolean("firstrun", false).commit();
                            } else {
                                LodgingSingleton.getInstance(getApplicationContext()).setDB();
                                Intent intent = new Intent(DownloadXML.this, Main.class);
                                Bundle b = new Bundle();
                                b.putString("province", "Búsqueda");
                                intent.putExtras(b);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
                // SI NO LA TIENE MOSTRAMOS UN BOTON DE REINTENTAR PARA QUE VUELVA A PROBAR LA
                // DESCARGA
                catch (FileNotFoundException e) {
                    downloadButton = (Button) findViewById(R.id.downloadButton);
                    downloadButton.setText("Reintentar");
                    downloadButton.setVisibility(View.VISIBLE);
                    downloadButton.setOnClickListener(new OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            pgb1.setVisibility(View.VISIBLE);
                            downloadButton.setVisibility(View.INVISIBLE);
                            bgtask = new Background();
                            bgtask.execute();
                        }
                    });
                }
            }

        }

        @Override
        protected void onCancelled()
        {
            Toast.makeText(DownloadXML.this, "Cancelando...", Toast.LENGTH_SHORT).show();

            try {
                @SuppressWarnings("unused")
                FileInputStream dataBase = getBaseContext().openFileInput("db.xml");
                downloadButton = (Button) findViewById(R.id.downloadButton);
                downloadButton.setText("Usar datos locales");
                downloadButton.setVisibility(View.VISIBLE);
                downloadButton.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean(
                                "firstrun", true);
                        if (firstrun) {
                            Intent intent = new Intent(DownloadXML.this, Cover.class);
                            startActivity(intent);
                            finish();
                            // Save the state
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putBoolean("firstrun", false).commit();
                        } else {
                            LodgingSingleton.getInstance(getApplicationContext()).setDB();
                            Intent intent = new Intent(DownloadXML.this, Main.class);
                            Bundle b = new Bundle();
                            b.putString("province", "Búsqueda");
                            intent.putExtras(b);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            } catch (FileNotFoundException e) {
                downloadButton = (Button) findViewById(R.id.downloadButton);
                downloadButton.setText("Reintentar");
                downloadButton.setVisibility(View.VISIBLE);
                downloadButton.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        downloadButton.setVisibility(View.INVISIBLE);
                        bgtask = new Background();
                        bgtask.execute();
                    }
                });
            }
        }
    }

}

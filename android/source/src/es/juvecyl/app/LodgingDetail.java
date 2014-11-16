package es.juvecyl.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class LodgingDetail extends SherlockActivity {
	private float scale;
	private XMLDB db;
	private boolean isImageFitToScreen;
	private Lodging target;
	private Context actualcontext;
	private TextView title, desc, phones;
	private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new XMLDB(getBaseContext());
		// //////////////////////////////////////////////////////////////////////////
		// PARA QUE LOS PIXELS SEAN EN FUNCIÓN DE LA DENSIDAD DE LA PANTALLA
		// //////////////////////////////////////////////////////////////////////////
		scale = getResources().getDisplayMetrics().density;
		Bundle bundle = this.getIntent().getExtras();
		String targetProvince = bundle.getString("lodging");
		LocateLodging(Integer.parseInt(targetProvince));
		actualcontext = this;
		isImageFitToScreen = false;
		setContentView(R.layout.details);
		title = (TextView) findViewById(R.id.detailsTitle);
		desc = (TextView) findViewById(R.id.detailsDesc);
		phones = (TextView) findViewById(R.id.detailsPhones);
		title.setText(target.getTitle());
		desc.setText(target.getDesc());
		phones.setText("Contacto:\n" + target.getPhones());
		// image.setImageURI(Uri.parse("http://www.datosabiertos.jcyl.es/web/jcyl/binarios/58/993/principalnavarredonda.jpg"));
		new ImageDownloader(LodgingDetail.this).execute(target.getImage());
	}

	// //////////////////////////////////////////////////////////////////////////
	// BÚSQUEDA DE ALOJAMIENTOS POR NOMBRE
	// //////////////////////////////////////////////////////////////////////////
	protected void LocateLodging(int id) {
		target = db.getLodgings().get(id);
	}

	public void updateBitmap(Bitmap image2) {
		image = (ImageView) findViewById(R.id.detailsImage);
		image.setImageBitmap(image2);

		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (100 * scale + 0.5f)));
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else{
                    isImageFitToScreen=true;
                    image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
			}
		});

	}

}

package es.juvecyl.app;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageDownloader extends AsyncTask<String, String, Bitmap> {

	   private LodgingDetail parentActivity;


	   public ImageDownloader(LodgingDetail parentActivity) {
	    super();
	    this.parentActivity = parentActivity;
	 }

	  protected Bitmap doInBackground(String... args) {
	    try {
	        Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
	        return bitmap;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	 }

	   protected void onPostExecute(Bitmap image) {
	    if(image != null){
	        parentActivity.updateBitmap(image);

	    }
	  }
	}
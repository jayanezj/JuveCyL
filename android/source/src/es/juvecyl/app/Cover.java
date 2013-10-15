package es.juvecyl.app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Cover extends Activity
	{

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.cover_layout);
			}

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
			{
				// Inflate the menu; this adds items to the action bar if it is
				// present.
				getMenuInflater().inflate(R.menu.cover, menu);
				return true;
			}

	}

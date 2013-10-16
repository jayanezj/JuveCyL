package es.juvecyl.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockActivity;

public class Cover extends SherlockActivity implements AnimationListener {
	private Animation anim;
	private ImageView arrowPull;
	private ListView navList;
	private Button btn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_run_layout);
		arrowPull = (ImageView) findViewById(R.id.img_first_arrow);
		navList = (ListView) findViewById(R.id.left_drawer);
		btn1 = (Button) findViewById(R.id.btn_first_ok);
		final String[] names = getResources().getStringArray(
				R.array.nav_options);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names);
		navList.setAdapter(adapter);
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_left_right);
		anim.setAnimationListener(this);
		arrowPull.startAnimation(anim);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Cover.this, Main.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		// arrowPull.setVisibility(View.INVISIBLE);
		// arrowPull.startAnimation(anim);
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		// arrowPull.setVisibility(View.VISIBLE);

	}

}

package es.juvecyl.app;

import android.content.Context;
import android.webkit.WebView;

public class GifView extends WebView
	{

		public GifView(Context context, String path)
			{
				super(context);
				loadUrl(path);
			}
	}
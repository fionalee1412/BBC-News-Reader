package com.bbcnewsreader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleActivity extends Activity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		WebView webView = (WebView)findViewById(R.id.articleWebView);
		//webView.loadUrl("http://www.google.com");
	}
}
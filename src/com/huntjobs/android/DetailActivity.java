package com.huntjobs.android;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class DetailActivity extends Activity
{
	 private WebView webView;
	 public static final String URL_SRC = "http://job.xidian.edu.cn";
	 private String content;
	 
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.detail);
		 webView = (WebView) this.findViewById(R.id.wv);
         content = SouthActivity.html7;
		 
				 StringBuffer sb2 = new StringBuffer();	
				 
				 sb2.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\">");
		    	 sb2.append("<head>");
		    	 sb2.append("<meta name=\"HandheldFriendly\" content=\"true\" /><meta name=\"viewport\" content=\"width=device-width, height=device-height, user-scalable=no\" />");
		    	 sb2.append("</head>");
		    	 sb2.append("<body style=\"background:#fff;color:#000;\">");
		    	 sb2.append(content);
		    	 sb2.append("</body>");
		    	 sb2.append("</html>");
				 webView.loadDataWithBaseURL(URL_SRC, sb2.toString(), "text/html", "UTF-8", null);	
	 }
}

package com.huntjobs.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class XJActivity extends Activity{

    private String liststr;
    private SimpleAdapter adapter;
    private ListView listView;
    private ListView listViewDetail;
    private List<News> listnews;
    private String url;
    private ProgressBar xh_ProgressBar;
    private ProgressDialog progressDialog = null;
    protected static final int GUI_STOP_NOTIFIER = 0x110; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobslist);
        NetworkHtml.HttpTest(XJActivity.this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        
        listView = (ListView)this.findViewById(R.id.list);

        liststr = HuntjobsActivity.htmlxj;
        if(liststr != null)
            updateListView();
        else
        	System.out.println("Error\n");
        
        xh_ProgressBar = (ProgressBar) findViewById(R.id.ProgressBar02);
        xh_ProgressBar.setIndeterminate(false); 
        listViewDetail = (ListView)this.findViewById(R.id.list);
        listViewDetail.setOnItemClickListener(new OnItemClickListener()
        {
        	@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
     		{
     			xh_ProgressBar.setVisibility(View.VISIBLE);
        		xh_ProgressBar.setMax(100);  
                xh_ProgressBar.setProgress(0);
                progressDialog = ProgressDialog.show(XJActivity.this, "请稍等...", "获取数据中...", true);
                url = "http://job.xjtu.edu.cn" + listnews.get(arg2).getCompanyurl();
                
                new Thread(new Runnable() 
                {  
                    @Override  
                    public void run() 
                    {  
                    	String html3 = NetworkHtml.getContent(url,"UTF-8");
                        if(html3 != null) 
                        {
                        	    SouthActivity.html7 = NetworkHtml.getXJDetail(html3);
                                Message m = new Message();   
                                m.what = XJActivity.GUI_STOP_NOTIFIER;  
                                XJActivity.this.myMessageHandler.sendMessage(m);  
                        }  
                     }  
                }).start();  
			}	
        });
    }
    
    private void updateListView()
    {
    	NewsParser np = new NewsParser();
    	np.parse(liststr);
    	listnews = np.getNewsList();

    	adapter = new SimpleAdapter(
    			XJActivity.this,
    			getMapList(listnews),
    			R.layout.listitem,
    			new String[] { "title","date","time"},
    			new int[] {R.id.text1, R.id.text3, R.id.text2});
    	try
    	{
    		listView.setAdapter(adapter);
    	}
    	catch(Exception e)
    	{
    		System.out.println("Error!");
    	}
    }
   
    
    private List<Map<String, Object>> getMapList(List<News> listnews)
    {
    	List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
    	for(News news : listnews)
    	{
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("title", news.getCompany());
    		map.put("date", news.getDate()+" "+news.getTime());
    		map.put("time", news.getAddress());
    		ret.add(map);
    	}
    	return ret;
    }
    Handler myMessageHandler = new Handler() {  
        @SuppressWarnings("static-access")
		@Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case XJActivity.GUI_STOP_NOTIFIER:  
            	progressDialog.dismiss();
                Intent intent = new Intent(XJActivity.this, DetailActivity.class);
                XJActivity.this.startActivity(intent);
                Thread.currentThread().interrupted(); 
                break;  
            }  
            super.handleMessage(msg);  
        }  
    };  

}

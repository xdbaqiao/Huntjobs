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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;


public class NorthActivity extends Activity {
	
    private String liststr;
    private SimpleAdapter adapter;
    private ListView listView;
    private ListView listViewDetail;
    private List<News> listnews;
    private String url;
    private ProgressBar xh_ProgressBar;
    private ProgressDialog progressDialog = null;
    protected static final int GUI_STOP_NOTIFIER = 0x109;
    public static final String DELIMITER_FIELD = "<>";
    public static final String FILENAME_NEWS_LIST = "newslist.txt";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobslist);
        NetworkHtml.HttpTest(NorthActivity.this);
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

        liststr = HuntjobsActivity.html55;
        if(liststr != null)
            updateListView();
        else
        	System.out.println("Error!\n");
        
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
                progressDialog = ProgressDialog.show(NorthActivity.this, "请稍等...", "获取数据中...", true);
                url = "http://job.xidian.edu.cn" + listnews.get(arg2).getCompanyurl();
                
                new Thread(new Runnable() 
                {  
                    @Override  
                    public void run() 
                    {  
                    	String html3 = NetworkHtml.getContent(url,"gb2312");
                        if(html3 != null) 
                        {
                            SouthActivity.html7 = NetworkHtml.getDetail(html3);
                            Message m = new Message();   
                            m.what = NorthActivity.GUI_STOP_NOTIFIER;  
                            NorthActivity.this.myMessageHandler.sendMessage(m);  
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
    			NorthActivity.this,
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
    		System.out.println("Error!!");
    	}
    }
   
    
    private List<Map<String, Object>> getMapList(List<News> listnews)
    {
    	List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
    	for(News news : listnews)
    	{
    		if(news.getCompany().replace("&#160;","").replace(" ","")!=null)
    		{
    		    Map<String, Object> map = new HashMap<String, Object>();
    		    map.put("title", news.getCompany());
    		    map.put("date", news.getDate()+" "+news.getTime());
    		    map.put("time", news.getAddress()+" ");
    		    ret.add(map);
    		}
    	}
    	return ret;
    }
    Handler myMessageHandler = new Handler() {  
        @SuppressWarnings("static-access")
		@Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case NorthActivity.GUI_STOP_NOTIFIER:  
            	progressDialog.dismiss();
            	Intent intent=new Intent(NorthActivity.this, DetailActivity.class);
                startActivity(intent);
                Thread.currentThread().interrupted(); 
                break;  
            }  
            super.handleMessage(msg);  
        }  
    }; 

}

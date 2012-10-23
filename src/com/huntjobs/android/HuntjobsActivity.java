package com.huntjobs.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;


public class HuntjobsActivity extends Activity 
{    
    private ProgressBar xh_ProgressBar;  
    protected static final int GUI_STOP_NOTIFIER = 0x108;   
    public static String html22;
    public static String html55;
    public static String htmlxj;
    public static String htmlnwpu;
    private ProgressDialog progressDialog = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
        xh_ProgressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
        xh_ProgressBar.setIndeterminate(false); 
        
        TextView tvsouth = (TextView)findViewById(R.id.buttonsouth);
        tvsouth.setOnClickListener(new OnClickListener()
        {      
        		@Override
            	public void onClick(View arg0)
            	{     
            		xh_ProgressBar.setVisibility(View.VISIBLE);
            		xh_ProgressBar.setMax(100);  
                    xh_ProgressBar.setProgress(0);
                    progressDialog = ProgressDialog.show(HuntjobsActivity.this, "请稍等...", "获取数据中...", true);
                    
                    new Thread(new Runnable() 
                    {  
                        @Override  
                        public void run() 
                        {  
                        	String htmltmp = NetworkHtml.getContent("http://job.xidian.edu.cn/html/narrangement/index.html","gb2312");
                            if(htmltmp != null) 
                            {
                            	    html22 = NetworkHtml.parserHtml(htmltmp);
                                    Message m = new Message();   
                                    m.what = HuntjobsActivity.GUI_STOP_NOTIFIER;  
                                    HuntjobsActivity.this.myMessageHandler.sendMessage(m);  
                            }  
                         }  
                    }).start();  
            	} 
        	
        });
        
        TextView tvnorth = (TextView)findViewById(R.id.buttonnorth);
        tvnorth.setOnClickListener(new OnClickListener()
        {    
        	@Override
        	public void onClick(View arg0)
        	{     
        		xh_ProgressBar.setVisibility(View.VISIBLE);
        		xh_ProgressBar.setMax(100);  
                xh_ProgressBar.setProgress(0);
                progressDialog = ProgressDialog.show(HuntjobsActivity.this, "请稍等...", "获取数据中...", true);
                
                new Thread(new Runnable() 
                {  
                    @Override  
                    public void run() 
                    {  
                    	String htmltmp = NetworkHtml.getContent("http://job.xidian.edu.cn/html/barrangement/index.html","gb2312");
                        if(htmltmp != null) 
                        {
                        	    html55 = NetworkHtml.parserHtml(htmltmp);
                                Message m = new Message();   
                                m.what = HuntjobsActivity.GUI_STOP_NOTIFIER;  
                                HuntjobsActivity.this.myMessageHandler2.sendMessage(m);  
                        }  
                     }  
                }).start();  
        	} 
    	
        });
        
        TextView tv = (TextView)findViewById(R.id.about);
        tv.setText(Html.fromHtml("关于Huntjobs"));
        tv.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View arg0)
        	{     
                Intent intent3=new Intent();          
                intent3.setClass(HuntjobsActivity.this, AboutActivity.class);     
                HuntjobsActivity.this.startActivity(intent3);  
        	}
        });
        
        TextView tvxajt = (TextView)findViewById(R.id.xajt);
        tvxajt.setOnClickListener(new OnClickListener()
        {      
        		@Override
            	public void onClick(View arg0)
            	{     
            		xh_ProgressBar.setVisibility(View.VISIBLE);
            		xh_ProgressBar.setMax(100);  
                    xh_ProgressBar.setProgress(0);
                    progressDialog = ProgressDialog.show(HuntjobsActivity.this, "请稍等...", "获取数据中...", true);
                    
                    new Thread(new Runnable() 
                    {  
                        @Override  
                        public void run() 
                        {   
                        	String html3 = NetworkHtml.getContent("http://job.xjtu.edu.cn/meeting","UTF-8");
                            String html4 = NetworkHtml.getContent("http://job.xjtu.edu.cn/listMeeting.do?week=1","UTF-8");
                            if(html3 != null) 
                            {
                            	 htmlxj = NetworkHtml.parserXJHtml(html3) + NetworkHtml.parserXJHtml(html4);
                                 Message m = new Message();   
                                 m.what = HuntjobsActivity.GUI_STOP_NOTIFIER;  
                                 HuntjobsActivity.this.myMessageHandlerxj.sendMessage(m); 
                            }
                        }
                    }).start();  
            	}
        }); 
        TextView tvnwpu = (TextView)findViewById(R.id.nwpu);
        tvnwpu.setOnClickListener(new OnClickListener()
        {      
        		@Override
            	public void onClick(View arg0)
            	{     
            		xh_ProgressBar.setVisibility(View.VISIBLE);
            		xh_ProgressBar.setMax(100);  
                    xh_ProgressBar.setProgress(0);
                    progressDialog = ProgressDialog.show(HuntjobsActivity.this, "请稍等...", "获取数据中...", true);
                    
                    new Thread(new Runnable() 
                    {  
                        @Override  
                        public void run() 
                        {   
                        	String dateurl = NetworkHtml.nwpuSystime(); 
                        	String html3 = NetworkHtml.getContent("http://job.nwpu.edu.cn/nwpujy/List/NewZPHList.aspx?date="+dateurl,"UTF-8");
                            if(html3 != null) 
                            {
                            	 htmlnwpu = NetworkHtml.parserNwpuHtml(html3);
                                 Message m = new Message();   
                                 m.what = HuntjobsActivity.GUI_STOP_NOTIFIER;  
                                 HuntjobsActivity.this.myMessageHandlernwpu.sendMessage(m); 
                            }
                        }
                    }).start();  
            	}
        }); 
    }
    
    
    Handler myMessageHandler = new Handler() {  
        @SuppressWarnings("static-access")
		@Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case HuntjobsActivity.GUI_STOP_NOTIFIER:  
            	progressDialog.dismiss();
                Intent intent=new Intent();          
                intent.setClass(HuntjobsActivity.this, SouthActivity.class);     
                HuntjobsActivity.this.startActivity(intent);
                Thread.currentThread().interrupted(); 
                break;  
            }  
            super.handleMessage(msg);  
        }  
    };  
    Handler myMessageHandler2 = new Handler() {  
        @SuppressWarnings("static-access")
		@Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case HuntjobsActivity.GUI_STOP_NOTIFIER:  
            	progressDialog.dismiss();
                Intent intent=new Intent();          
                intent.setClass(HuntjobsActivity.this, NorthActivity.class);     
                HuntjobsActivity.this.startActivity(intent);
                Thread.currentThread().interrupted(); 
                break;  
            }  
            super.handleMessage(msg);  
        }  
    };
    Handler myMessageHandlerxj = new Handler() {  
        @SuppressWarnings("static-access")
		@Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case HuntjobsActivity.GUI_STOP_NOTIFIER:  
            	progressDialog.dismiss();
                Intent intent=new Intent();          
                intent.setClass(HuntjobsActivity.this, XJActivity.class);     
                HuntjobsActivity.this.startActivity(intent);
                Thread.currentThread().interrupted(); 
                break;  
            }  
            super.handleMessage(msg);  
        }  
    }; 
    Handler myMessageHandlernwpu = new Handler() {  
        @SuppressWarnings("static-access")
		@Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case HuntjobsActivity.GUI_STOP_NOTIFIER:  
            	progressDialog.dismiss();
                Intent intent=new Intent();          
                intent.setClass(HuntjobsActivity.this, NwpuActivity.class);     
                HuntjobsActivity.this.startActivity(intent);
                Thread.currentThread().interrupted(); 
                break;  
            }  
            super.handleMessage(msg);  
        }  
    }; 
}
package com.huntjobs.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHtml
{

	public static String convertHtmlToText(String s) 
	{
		return s.replace("&nbsp;", " ").replace("&quot;", "\"").replace("&amp;", "&").replace("&middot;", "·");		
	}
	public static final String DELIMITER_FIELD = "<>";
	public static String splitInfo(String date1, String time1, String address1 ,String company1, String companyurl1, String bakup1)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(date1);
		sb.append(DELIMITER_FIELD);
		sb.append(time1);
		sb.append(DELIMITER_FIELD);
		sb.append(address1);
		sb.append(DELIMITER_FIELD);
		sb.append(company1);
		sb.append(DELIMITER_FIELD);
		sb.append(companyurl1);
		sb.append(DELIMITER_FIELD);
		sb.append(bakup1);
		return sb.toString();
	}
    public static String getContent(String preurl, String encoding)
    {
    	InputStream is = null;
    	
        try
        {
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpGet httpget = new HttpGet(preurl);
        	HttpResponse response = httpclient.execute(httpget);
        	HttpEntity entity = response.getEntity();
        	is = entity.getContent();
        }
        catch(Exception e)
        {
        	return "Fail to eatablish http connection";        	
        }
        try
        {        
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
            String ht = "";
            StringBuffer sb = new StringBuffer();
            while( (ht = br.readLine()) != null )
                sb.append(ht);
            is.close();
            return sb.toString();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            return "Error url:" + preurl;
        }
    }
    public static String parserHtml(String parseht)
    {
    	StringBuffer sb = new StringBuffer();
        Document doc = Jsoup.parse(parseht);
        Element content = doc.select("table[align=center]").last();
        if(content==null)
        	return null;
        Elements trlists = content.select("tr[style^=height]");
        for(Element trlist:trlists)
        { 
        	String date = trlist.select("td").first().text();
        	String datetmp = "20" + date.replace("年", "").replace("月","").replace("日","").substring(0, 6);
        	if(Integer.parseInt(datetmp)<Integer.parseInt(systime()))
        		continue;
        	String time = trlist.select("td[class=style1]").first().text();
        	String address = trlist.select("td[width=150px").text();
        	String company = trlist.select("td[width=250px]").text();
        	String companyurl = trlist.select("td[width=250px]").first().select("a").first().attr("href");
        	//String bakup = trlist.select("td[width=110px]").first().text();
        	String bakup = "1";
        	String str1 = splitInfo(date, time, address, company, companyurl, bakup);
        	sb.append("<<>>");
        	sb.append(str1);
        }  
        return convertHtmlToText(sb.toString());
    }
    public static String getDetail(String html)
    {
    	StringBuffer sb = new StringBuffer();
        Document doc = Jsoup.parse(html);
        //doc.select("table").remove();
        Element content = doc.select("div[class=content]").first();
        if(content.outerHtml().length()==34)
        	sb.append("没有可以显示的内容！");
        else
        	sb.append(content.outerHtml());
        return convertHtmlToText(sb.toString());	
    }
    public static String parserXJHtml(String parseht)
    {

    	StringBuffer sb = new StringBuffer();
        Document doc = Jsoup.parse(parseht);
        Element content = doc.select("div[class=r_list]").first();
        if(content==null)
        {
        	return null;
        }
        Elements trlists = content.select("table[class=week_fairs_tb]");
        for(Element trlist:trlists)
        { 
        	Elements a = trlist.select("tr");

        	for(Element everya:a)
        	{  
        	    String date = trlist.select("tr").first().text();
        	    int i=date.indexOf("(");
        	    if(i>0)
        	    {
        	    	String datetem = date.substring(0, i);
        	    	if(Integer.parseInt(datetem.replace(".", ""))<Integer.parseInt(systime()))
        	    		continue;
        	    }
        	    String time = everya.select("td[width=17%]").text();
        	    String address = everya.select("td[width=30%]").text();
        	    String company = everya.select("td[width=53%]").text().replace("·", "");
        	    if(company=="")continue;
        	    String companyurl = everya.select("td[width=53%]").select("a").attr("href");
        	    String bakup = "1";
        	    String str1 = splitInfo(date, time, address, company, companyurl, bakup);
        	    sb.append("<<>>");
        	    sb.append(str1);
        	 }
        }  

        return convertHtmlToText(sb.toString());
    }
    public static String getXJDetail(String html)
    {
    	StringBuffer sb = new StringBuffer();
        Document doc = Jsoup.parse(html);
        //doc.select("table").remove();
        doc.select("div[class=view_bottom_tools]").remove();
        doc.select("div[class=artInfo]").remove();
        Element content = doc.select("div[class=view_content]").first();
        if(content.outerHtml().length()==34)
        	sb.append("没有可以显示的内容！");
        else
        	sb.append(content.outerHtml());
        return convertHtmlToText(sb.toString());	
    }
    public static String parserNwpuHtml(String parseht)
    {

    	StringBuffer sb = new StringBuffer();
        Document doc = Jsoup.parse(parseht);
        Element content = doc.select("table[id=ctl00_ContentPlaceHolder3_tbZPHList]").first();
        if(content==null)
        {
        	return null;
        }
        Elements trlists = content.select("tr");
        for(Element trlist:trlists)
        { 
        	String[] a =trlist.select("td[align=left]").toString().split("<br />");
        	
        	for(String everya:a)
        	{  
        		String company = "";
        		String address = "";
        	    String date = trlist.select("td").first().text();
        	    int i=date.indexOf(" ");
        	    if(i>0)
        	    {
        	    	String datetem = date.substring(0, i);
        	    	if(Integer.parseInt(datetem.replace("-", ""))<Integer.parseInt(systime()))
        	    		continue;
        	    }
        	    date = "  " + date;
        	    String title = Jsoup.parse(everya).select("a").text();
            	if(title=="")
            		continue;
        	    int j=title.indexOf("_");
        	    if(j>0){
        	    	company = title.substring(0, j);
        	    	address = title.substring(j+1);
        	    }
        	    String time = Jsoup.parse(everya).text().replace(title, "");
        	    String companyurl = Jsoup.parse(everya).select("a").attr("href");
        	    String bakup = "1";
        	    String str1 = splitInfo(date, time, address, company, companyurl, bakup);
        	    sb.append("<<>>");
        	    sb.append(str1);
        	 }
        }  

        return convertHtmlToText(sb.toString());
    }
    public static String getNwpuDetail(String html)
    {
    	StringBuffer sb = new StringBuffer();
        Document doc = Jsoup.parse(html);
        //doc.select("table").remove();
        Element content = doc.select("td[id=mainFrame]").first();
        if(content.outerHtml().length()==34)
        	sb.append("没有可以显示的内容！");
        else
        	sb.append(content.outerHtml());
        return convertHtmlToText(sb.toString());	
    }
    
    public static String systime()
    {
    	String strmonth;
    	Calendar c = Calendar.getInstance();
    	int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        if(month<10)
        	strmonth = "0"+month;
        else
        	strmonth = String.valueOf(month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year) + strmonth +String.valueOf(day);
    }
    public static String nwpuSystime()
    {
    	String strmonth;
    	Calendar c = Calendar.getInstance();
    	int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        if(month<10)
        	strmonth = "0"+month;
        else
        	strmonth = String.valueOf(month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year) + "-" +strmonth +"-"+String.valueOf(day);
    }
    public static boolean isNetworkAvailable( Activity mActivity ) 
    {  
        Context context = mActivity.getApplicationContext(); 
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        if (connectivity == null) 
        {     
          return false; 
        } 
        else 
        {   
            NetworkInfo[] info = connectivity.getAllNetworkInfo();     
            if (info != null) 
            {         
                for (int i = 0; i < info.length; i++) 
                {            
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) 
                    {               
                        return true;  
                    }         
                }      
            }  
        }    
        return false; 
    } 
    public static void HttpTest(final Activity mActivity) 
    { 
      if( !isNetworkAvailable( mActivity) )
      { 
        AlertDialog.Builder builders = new AlertDialog.Builder(mActivity); 
        builders.setTitle("出错了！"); 
        builders.setMessage("网络连接错误！");
        builders.setPositiveButton("确定",  new DialogInterface.OnClickListener(){ 
          public void onClick(DialogInterface dialog, int which) 
          { 
            mActivity.finish(); 
          }        
        }); 
        builders.show(); 
      } 
    }
}

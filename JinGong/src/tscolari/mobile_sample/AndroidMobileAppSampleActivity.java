package tscolari.mobile_sample;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

//import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.methods.*;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class AndroidMobileAppSampleActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	//solve the problem of NetworkOnMainThreadException
    	String strVer=GetVersion();
    	strVer=strVer.substring(0,3).trim();
    	float fv=Float.valueOf(strVer);
    	if(fv>2.3)
    	{
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
    	.detectDiskReads()
    	.detectDiskWrites()
    	.detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
    	.penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
    	.build());
    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
    	.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
    	.penaltyLog() //打印logcat
    	.penaltyDeath()
    	.build()); 
    	}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        WebView mainWebView = (WebView) findViewById(R.id.mainWebView);
        String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        
        String dbDir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        String pages[] = new String[51];
        int i;
        final double longitude, latitude;
        pages[0]="http://www.beessoft.com/kd/pda/";
        pages[1]="http://www.jin-gong.com/index.php?do=intro&class_id=001";
        pages[2]="http://www.jin-gong.com/index.php?do=intro&class_id=001001";
        pages[3]="http://www.jin-gong.com/index.php?do=intro&class_id=001002";
        pages[4]="http://www.jin-gong.com/index.php?do=intro&class_id=001003";
        pages[5]="http://www.jin-gong.com/index.php?do=pic&class_id=001004";
        pages[6]="http://www.jin-gong.com/index.php?do=intro&class_id=001005";
        pages[7]="http://www.jin-gong.com/index.php?do=intro&class_id=001006";
        pages[8]="http://www.jin-gong.com/index.php?do=intro&class_id=001007";
        pages[9]="http://www.jin-gong.com/index.php?do=pic&class_id=002";
        pages[10]="http://www.jin-gong.com/index.php?do=pic&class_id=002&page=2";
        pages[11]="http://www.jin-gong.com/index.php?do=pic&class_id=002&page=3";
        pages[12]="http://www.jin-gong.com/index.php?do=pic&class_id=002&page=4";
        pages[13]="http://www.jin-gong.com/index.php?do=pic&class_id=002&page=5";
        pages[14]="http://www.jin-gong.com/index.php?do=pic&class_id=002001";
        pages[15]="http://www.jin-gong.com/index.php?do=pic&class_id=002002";
        pages[16]="http://www.jin-gong.com/index.php?do=pic&class_id=002003";
        pages[17]="http://www.jin-gong.com/index.php?do=pic&class_id=002004";
        pages[18]="http://www.jin-gong.com/index.php?do=pic&class_id=002005";
        pages[19]="http://www.jin-gong.com/index.php?do=pic&class_id=002006";
        pages[20]="http://www.jin-gong.com/index.php?do=pic&class_id=002007";
        pages[21]="http://www.jin-gong.com/index.php?do=pic&class_id=002008";
        pages[22]="http://www.jin-gong.com/index.php?do=news&class_id=003";
        pages[23]="http://www.jin-gong.com/index.php?do=news&class_id=003001";
        pages[24]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=2";
        pages[25]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=3";
        pages[26]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=4";
        pages[27]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=5";
        pages[28]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=6";
        pages[29]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=7";
        pages[30]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=8";
        pages[31]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=9";
        pages[32]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=10";
        pages[33]="http://www.jin-gong.com/index.php?do=news&class_id=003001&page=11";
        pages[34]="http://www.jin-gong.com/index.php?do=news&class_id=003002";
        pages[35]="http://www.jin-gong.com/index.php?do=news&class_id=003002&page=2";
        pages[36]="http://www.jin-gong.com/index.php?do=news&class_id=003002&page=3";
        pages[37]="http://www.jin-gong.com/index.php?do=news&class_id=003003";
        pages[38]="http://www.jin-gong.com/index.php?do=news&class_id=003003&page=2";
        pages[39]="http://www.jin-gong.com/index.php?do=home";
        pages[40]="http://www.jin-gong.com/index.php?do=intro&class_id=005";
        pages[41]="http://www.jin-gong.com/cp.php";
        pages[42]="http://www.jin-gong.com/index.php?do=pic&class_id=006";
        pages[43]="http://www.jin-gong.com/index.php?do=pic&class_id=006&page=2";
        pages[44]="http://www.jin-gong.com/index.php?do=rb&class_id=007";
        pages[45]="http://www.jin-gong.com/index.php?do=rb&class_id=007&page=2";
        pages[46]="http://www.jin-gong.com/index.php?do=rb&class_id=007&page=3";
        pages[47]="http://www.jin-gong.com/index.php?do=rb&class_id=007&page=4";
        pages[48]="http://www.jin-gong.com/index.php?do=rb&class_id=007&page=5";
        pages[49]="http://www.jin-gong.com/index.php?do=faq";
        pages[50]="http://www.jin-gong.com";
        
        /*final LocationListener locationListener = new LocationListener() {
        	    public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        	        // log it when the location changes
        	        if (location != null) {
        	            Log.i("SuperMap", "Location changed : Lat: "
        	              + location.getLatitude() + " Lng: "
        	              + location.getLongitude());
        	        }


        	    }

        	    public void onProviderDisabled(String provider) {
        	    // Provider被disable时触发此函数，比如GPS被关闭

        	    }

        	    public void onProviderEnabled(String provider) {
        	    //  Provider被enable时触发此函数，比如GPS被打开
        	    }

        	    public void onStatusChanged(String provider, int status, Bundle extras) {
        	    // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        	    }
        	};
        	
        	 final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
             locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener); 
             Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
             
         	 while(location  == null)  
             {  
         		  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, locationListener);  
         	 }
        */
        
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true); //enable use of local storage
        webSettings.setGeolocationDatabasePath("/data/data/<my-app>");
        // enable geolocationEnabled
        webSettings.setGeolocationEnabled(true);
        // enable JavaScriptCanOpenWindowsAutomatically, not sure if this is needed
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheMaxSize(8*1024*1024);
        webSettings.setAllowFileAccess(true);
        webSettings.setSaveFormData(true);
        //set appCache and database paths
        //webSettings.setAppCachePath(appCacheDir);
        webSettings.setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );
        webSettings.setDatabasePath(dbDir);      
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        ConnectivityManager cm;
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConnected=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        if(!isWifiConnected){
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }


        mainWebView.setWebViewClient(new MyCustomWebViewClient());       
        mainWebView.setWebChromeClient(new MyChromeWebViewClient());
        mainWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //mainWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null); //clear cache
        mainWebView.loadUrl(pages[0]);


        /*HttpGet request = new HttpGet(pages[50]);
        HttpClient httpclient =  new DefaultHttpClient();
        //httpclient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        try
        {
        	HttpResponse httpResponse = httpclient.execute(request);
        	int statusCode = httpResponse.getStatusLine().getStatusCode();
        	if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + httpResponse.getStatusLine());
              }

        	HttpEntity entity = httpResponse.getEntity();
        	//InputStream is = entity.getContent();   //store html file data
        	String html_content = EntityUtils.toString(entity);
        	String FILENAME = "html_file";
        	
        	FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

        	fos.write(html_content.getBytes());
        	fos.close();     	
        	
        }
        catch (ClientProtocolException e) {
            //System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
          }
        catch (IOException e) {
              //System.err.println("Fatal transport error: " + e.getMessage());
              e.printStackTrace();
            }
       finally {
              // Release the connection.
              //method.releaseConnection();
       } 
        
        try{
         FileInputStream is = openFileInput("html_file");
         String result = is.toString();
         notification(result);
        }
        catch (IOException e)
        {}*/
        
        AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(this, AlarmReceiver.class);
        in.setAction("Test");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, in, 0);
        Calendar c=Calendar.getInstance();
        
        c.set(Calendar.YEAR,2013);
        c.set(Calendar.MONTH,Calendar.JULY);//也可以填数字，0-11,一月为0
        c.set(Calendar.DAY_OF_MONTH, 5);
        c.set(Calendar.HOUR_OF_DAY, 8);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        //int day = c.get(Calendar.DAY_OF_WEEK);//星期几，1-7为星期天到星期六

        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000 * 60, pi); // Millisec * Second * Minute
        //notification(String.valueOf(day));
        
    }
       
    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedError(WebView view,int errorCode, String description, String failingUrl)
        {
        	String mes = "未连接到网络" ;
        	view.loadUrl("");
        	notification(mes);
        
        }
    }
    
    public void notification(String message)
    {
    	new AlertDialog.Builder(this) 
    	.setTitle("提示" ) 
    	.setMessage(message) 
    	.setPositiveButton("确定" , null ) 
    	.show(); 
    }
    	
    
    public static String GetVersion()
    {
        return android.os.Build.VERSION.RELEASE;
    }

    
    private class MyChromeWebViewClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {
            // Activities and WebViews measure progress with different scales.
            // The progress meter will automatically disappear when we reach 100%
        	AndroidMobileAppSampleActivity.this.setProgress(progress * 100);
        	
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //Log.d(LOG_TAG, message);
            // This shows the dialog box.  This can be commented out for dev
            AlertDialog.Builder alertBldr = new AlertDialog.Builder(AndroidMobileAppSampleActivity.this);
            alertBldr.setMessage(message);
            alertBldr.setTitle("Alert");
            alertBldr.show();
            result.confirm();
            return true;
        }

        @Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        
        @Override
        public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater)
        {
    		quotaUpdater.updateQuota(spaceNeeded * 2);
        }
    }
   
}//end of activity
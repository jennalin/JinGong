package tscolari.mobile_sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;




public class AlarmReceiver extends BroadcastReceiver 
{    

     @Override
     public void onReceive(Context context, Intent intent) 
     {   

    	 String httpUrl = "http://www.beessoft.com/kd/app/save_app_jwd?";
    	 List <BasicNameValuePair> params = new ArrayList <BasicNameValuePair>();

         HttpClient httpclient = new DefaultHttpClient();
         httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30*1000);
         httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000);
         httpclient.getParams().setLongParameter(ConnManagerPNames.TIMEOUT, 30*1000);
    	 final LocationListener locationListener = new LocationListener() {
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
     	
     	 final LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); 
          locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener); 
          Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
          
      	 while(location  == null)  
          {  
      		  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, locationListener);  
      	 }
      	 
    	double latitude = location.getLatitude();     //纬度   
    	double longitude = location.getLongitude(); //经度   
    	double altitude =  location.getAltitude();     //海拔 
    	String mes = "纬度："+latitude+"经度："+longitude;
   	    Toast.makeText(context, mes, Toast.LENGTH_SHORT).show();
    
    	
    	WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);   
    	WifiInfo info = wifi.getConnectionInfo();
    	String getMacAddr = info.getMacAddress();
    	
    	params.add(new BasicNameValuePair("jd", Double.toString(longitude)));
    	params.add(new BasicNameValuePair("wd", Double.toString(latitude)));
    	params.add(new BasicNameValuePair("cmaker", getMacAddr));
    	
        String test = httpUrl+"jd="+longitude+"&wd="+latitude+"&cmaker="+getMacAddr;
        HttpPost httppost = new HttpPost(test);
        
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        Intent i = new Intent(context, AndroidMobileAppSampleActivity.class);
        
        RequestParams parameters = new RequestParams();
        parameters.put("jd", Double.toString(longitude));
        parameters.put("wd", Double.toString(latitude));
        parameters.put("cmaker", getMacAddr);

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(test,parameters,new AsyncHttpResponseHandler()
        		{
        	@Override
        	public void onSuccess(String response)
        	{
        		System.out.println(response);

        	}
        		});


        
        //if((hour == 8 || hour == 10 || hour == 12 || hour == 14 || hour == 16 || hour == 18 || hour == 20 || hour == 22) && (minute == 0 || minute == 30))
        /*if(hour!=0)
        {
    	  try
    	  {
    		 httppost.setEntity(new UrlEncodedFormEntity(params)); 
    		 HttpResponse httpResponse=httpclient.execute(httppost);
    		 while(httpResponse == null)
    		 {
    			 httpResponse=httpclient.execute(httppost);
    		 }
    		 
    		 if(httpResponse.getStatusLine().getStatusCode() == 200)
    		 {
    			 String strResult = EntityUtils.toString(httpResponse.getEntity());   
    			 Toast.makeText(context, strResult, Toast.LENGTH_SHORT).show();
    		 }
    		 
    		 else
    		 {
    			 Toast.makeText(context, "Error Response:" + httpResponse.getStatusLine().toString(), Toast.LENGTH_SHORT).show();
    		 }

    	  }
    	  catch (ClientProtocolException e) {         
    		// TODO Auto-generated catch block 
    		  e.printStackTrace();
    	  } 
    	  catch (IOException e) {         
    		// TODO Auto-generated catch block
    		  e.printStackTrace();
    	  }
        
    	  //Toast.makeText(context, test, Toast.LENGTH_SHORT).show();
        }*/
    	
    	
    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	 
    	 //context.startActivity(i);
     }
         
 }

package tscolari.mobile_sample;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BootReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
		{
		  try {  
			 Thread.sleep(30 * 1000);  
		     } 
		  catch (InterruptedException ignore) {  
		   }

		  ConnectivityManager cm;
		  cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  boolean isWifiConnected=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
		  while(!isWifiConnected)
		  {
			
		  }
		
		  AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
          Intent in = new Intent(context, AlarmReceiver.class);
          in.setAction("Test");
          PendingIntent pi = PendingIntent.getBroadcast(context, 0, in, 0);
          Calendar c=Calendar.getInstance();
        
          c.set(Calendar.YEAR,2013);
          c.set(Calendar.MONTH,Calendar.JULY);//也可以填数字，0-11,一月为0
          c.set(Calendar.DAY_OF_MONTH, 5);
          c.set(Calendar.HOUR_OF_DAY, 8);
          c.set(Calendar.MINUTE, 0);
          c.set(Calendar.SECOND, 0);
          am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000 * 60, pi); // Millisec * Second * Minute
          
		  Intent myIntent = new Intent(context, AndroidMobileAppSampleActivity.class);
		  myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  //context.startActivity(myIntent);
	  }
	}

}

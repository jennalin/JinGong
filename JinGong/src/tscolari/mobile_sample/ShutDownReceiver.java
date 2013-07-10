package tscolari.mobile_sample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutDownReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive (Context context, Intent intent)
	{
		System.exit(0);
	}

}

package br.ufu.weightThrowing;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class ApplicationPush  extends Application {
   

    private static ApplicationPush instance = new ApplicationPush();

    public ApplicationPush() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
	 ParseInstallation installation;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Application ","PUSH WEIGHT");
		Parse.initialize(this, "E0VHO2Xj9IBLw8NqvxrPvf67WFPcmUHT0BPWrJGf", 
		        "8sMCGbJGVexbVk42F7MagqJr37VDbXfXk3I1St6i"); 
		 PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.subscribe(this, "", MainActivity.class);
        installation=ParseInstallation.getCurrentInstallation();
		/* session = new SessionManager(getApplicationContext());
	        HashMap<String, String> user = session.getUserDetails();
	        // sponsor
	    id = user.get(SessionManager.KEY_ID);
	    
    	 installation.put("CONTACTID", Integer.parseInt(id));
 	    installation.put("crm_id", id);
 	   installation.saveInBackground();
       Log.e("Application","Push, ya va para bienvenida");
*/
    }
    
}
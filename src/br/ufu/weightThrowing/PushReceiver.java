package br.ufu.weightThrowing;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PushReceiver  extends BroadcastReceiver {
	   
	  private static final String TAG = "PushReceiver";
	 
	  @Override
	    public void onReceive(Context context, Intent intent) {
	        try {
	            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
	        
	            Intent resultIntent = new Intent(context, MainActivity.class);
	            //resultIntent.putExtra(MainActivity.ID, json.getString("id")); //Aquí pasamos la información
	            resultIntent.putExtra(MainActivity.WEIGHT, json.getString("weight")); //Aquí pasamos la información
	            resultIntent.putExtra(MainActivity.MENSAJE, json.getString("mensaje")); //Aquí pasamos la información
	            resultIntent.putExtra(MainActivity.DESTINATARIO, json.getString("destinatario")); //Aquí pasamos la información
	            resultIntent.putExtra(MainActivity.REMITENTE, json.getString("remitente")); //Aquí pasamos la información

	            resultIntent.putExtra("API", json.getBoolean("API"));
	            resultIntent.putExtra("Return", json.getBoolean("Return"));
	            resultIntent.putExtra("MoreWeight", json.getBoolean("MoreWeight"));


	           /*Intent resultIntent = new Intent(Intent.ACTION_INSERT)
		        .setData(Events.CONTENT_URI)
		        
		        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, json.getString("startTime"))
		        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, json.getString("endTime"))
		        .putExtra(Events.TITLE,json.getString("type") )
		        .putExtra(Events.CALENDAR_ID, json.getString("event_id"))
		        .putExtra(Events.DESCRIPTION, json.getString("description"))
		        .putExtra(Events.EVENT_LOCATION, "BizManager")
		        .putExtra(Events.ORGANIZER, "BizManager Organizer")
		        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_FREE)
		        .putExtra(Events.EVENT_COLOR, "#127FFF")
		        .putExtra(Intent.EXTRA_EMAIL, json.getString("email"));
	          */
	           Log.e("put","intent push completo");
	            PendingIntent resultPendingIntent =
	                    PendingIntent.getActivity(
	                            context,
	                            0,
	                            resultIntent,
	                            PendingIntent.FLAG_UPDATE_CURRENT
	                    );
	            //Esto hace posible crear la notificación
	            NotificationCompat.Builder mBuilder =
	                    new NotificationCompat.Builder(context)
	                            .setSmallIcon(R.drawable.ic_launcher)
	                            .setContentTitle(json.getString("titulo"))
	                            .setContentText(json.getString("texto"))
                                .setContentIntent(resultPendingIntent) // <- Para cargar el intent
	           					.setAutoCancel(true); // <- Para poder cerrar la notificacion
	            NotificationManager mNotificationManager =
	                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	            mNotificationManager.notify(1, mBuilder.build());

	        } catch (JSONException e) {
	            Log.d(TAG, "JSONException: " + e.getMessage());
	        }
	    }
	}
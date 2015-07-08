package br.ufu.weightThrowing;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	
		public node p,q,r;
		double total=0;
		double minimum=0.5;
		String message="Mensaje ";
        Button iniciar;
        Button iniciarQ;
        Button EnviarMensajeP,EnviarMensajeQ,EnviarMensajeR;
        TextView txtID,txtWeight,txtdescripcion,txtr;
        public SendMessage Send=new SendMessage();
        Intent intent;
    	public final static String ID = "id";
    	public final static String WEIGHT = "weight";
    	public final static String MENSAJE = "mensaje";
    	public final static String DESTINATARIO = "destinatario";
    	public final static String REMITENTE = "remitente";

        public void addButtonListener()
        {
			EnviarMensajeP=(Button)findViewById(R.id.btnEnviarMensajeP);
            EnviarMensajeQ=(Button)findViewById(R.id.btnEnviarMensajeQ);
            EnviarMensajeR=(Button)findViewById(R.id.btnEnviarMensajeR);
            
    		iniciar=(Button)findViewById(R.id.btnIniciar);			
    		iniciarQ=(Button)findViewById(R.id.btnIniciarQ);
    		
            iniciar.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				inicializar("P",0);  
    				iniciar.setEnabled(false);
        	 		iniciarQ.setEnabled(false);
        	 		EnviarMensajeP.setVisibility(1);
        	 		EnviarMensajeP.setEnabled(true);
        	 		iniciar.setVisibility(View.GONE);
        	 		iniciarQ.setVisibility(View.GONE);
    				p.active=true;
    				p.setWeight(12);
    				total=12;
    		    	txtWeight.setText("Weight "+String.valueOf(p.weight));
    			}
    		});
			iniciarQ.setOnClickListener(new OnClickListener() {
			    			
			    			@Override
			    			public void onClick(View v) {
			        	 		iniciar.setEnabled(false);
			        	 		EnviarMensajeQ.setVisibility(1);
			        	 		iniciar.setVisibility(View.GONE);
			        	 		iniciarQ.setVisibility(View.GONE);
			    				inicializar("Q",1); 
			    			}
			    		});
            EnviarMensajeP.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {  	
    				EnviarMensajeP.setEnabled(false);
    		     		p.weight=p.weight/2;
    					if ((p.weight)<minimum)
    					{
    						if (!p.initiator)
    						{
    							txtdescripcion.setText("Node isn't initator, after it call more weight");
    							while(!moreweight()){}
    						}
    						else	
    						{
    							txtdescripcion.setText("Node is initator, after it add (+1) no total");
    						    total=total+1;
    						}
							txtdescripcion.setText("Node Weight is < minimum, after it node weight +1 ");
    						p.weight=p.weight+1;
    					}
    					p.active=false;
    					txtdescripcion.setText("Send Message to Q with weight "+p.weight);
    					txtdescripcion.setText(txtdescripcion.getText()+ "\r\n Node going to passive");
    					p.weight=rellenarTablaNodo("P",p.weight);
    					sendPush("1","Q", p.weight, "P", "New message from P", "Add weight here",false);
    					EnviarMensajeP.setEnabled(true);
    					//En primera instancia, el lider envia un mensaje con parte de su peso en el
    			}
    		});
            EnviarMensajeQ.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {  				 
    		     		q.weight=q.weight/2;
    					if (q.weight<minimum)
    					{
    						if (!q.initiator)
    						{
    							txtdescripcion.setText("Node isn't initator, after it call more weight");
    							while(!moreweight()){}
    							sendPushMoreWeight("P");
    						}
    						else	
    						{ txtdescripcion.setText("Node is initator, after it add (+1) no total");
    						total=total+1;}
							txtdescripcion.setText("Node Weight is < minimum, after it node weight +1 ");
    						q.weight=q.weight+1;
    					}
    					q.active=false;
    					txtdescripcion.setText("Send Message to R with weight "+q.weight);
    					txtdescripcion.setText(txtdescripcion.getText()+ "\r\n Node going to passive");
    					txtr.setText("Puedes retorar el peso o enviar otro mensaje");

    					q.weight=rellenarTablaNodo("Q",q.weight);
    					sendPush("0","P", q.weight, "Q", "New message from Q", "Add weight here",false);
    			}
    		});
            EnviarMensajeR.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {  				 
					r.weight=r.weight/2;
    					if (r.weight<minimum)
    					{
    						if (!r.initiator)
    						{   txtdescripcion.setText("Node isn't initator, after it call more weight");
    							while(!moreweight()){}
    							sendPushMoreWeight("P");

    						}
    						else	
    						{ txtdescripcion.setText("Node is initator, after it add (+1) no total");
    						total=total+1;}							
    						txtdescripcion.setText("Node Weight is < minimum, after it node weight +1 ");
    						r.weight=r.weight+1;
    					}
    					r.active=false;
    					r.weight=rellenarTablaNodo("R",r.weight);
    					txtdescripcion.setText("Send Message to R with weight "+q.weight);
    					txtdescripcion.setText(txtdescripcion.getText()+ "\r\n Node going to passive");
    					sendPush("0", "P",r.weight, "R", "New message from R", "Add weight here",false);
    			}
    		});
        	
        }
	public void enviarMensajeP_Q (String mensaje,double w,int destinatario)
	{	
		if(destinatario==0)
		{
			p.weight=rellenarTablaNodoR("P",w);
			p.active=true;
			//p.weight=p.weight+w;
			txtID.setText("ID: P");
			txtWeight.setText("Weight: "+p.weight);
			//EnviarMensajeP.setEnabled(true);
			//EnviarMensajeP.setVisibility(View.VISIBLE);
		}
		else if (destinatario==1)
		{
			//EnviarMensajeQ.setEnabled(true);
			//EnviarMensajeQ.setVisibility(View.VISIBLE);

			Log.e("entroo","por Q");
			q.active=true;
			q.weight=rellenarTablaNodoR("Q",w);
			//q.weight=q.weight+w;
			txtID.setText("ID: Q");
			txtWeight.setText("Weight: "+q.weight);
		}
		else if(destinatario==2)
			{
				r.active=true;
				r.weight=rellenarTablaNodoR("R",w);
				//r.weight=r.weight+w;
				txtID.setText("ID: R");

				txtWeight.setText("Weight: "+r.weight);
			}
		else
		Log.e("no paso","por ninguno");
	}
    public void nodopasivoP()
        {
        	p.active=false;
        	txtdescripcion.setText("Pasó a pasivo con weight P"+p.weight+ "y total "+total);
			if(p.initiator==false)
			{	
	    	 	sendPush("1","Q", p.weight, "P", "Nuevo Mensaje", "Return your weight",true);
				p.weight=0;
			}
			else if (total==p.weight)
			{ 
				callAnnounce();	
			}	
        }
    public void nodopasivoQ()
    {
    	q.active=false;
    	txtdescripcion.setText("Pasó a pasivo con weight Q "+q.weight+ "y total "+total);
		if(q.initiator==false)
		{
	    	txtdescripcion.setText("Como es pasivo, retorna su peso  "+q.weight+ " al iniciador ");
    	 	sendPush("0","P", q.weight, "Q", "Nuevo Mensaje", "Return your weight",true);
			q.weight=0;
			txtWeight.setText("Weight: 0");
			txtr.setText("Debes esperar una nueva corrida");
			EnviarMensajeQ.setVisibility(View.GONE);
		}
		else if (total==q.weight)
		{ 
			callAnnounce();	
		}		
    }
    public void nodopasivoR()
    {
    	r.active=false;
    	txtdescripcion.setText("Pasó a pasivo con weight R"+r.weight+ "y total "+total);
		if(r.initiator==false)
		{
    	 	sendPush("0","P", r.weight, "R", "Nuevo Mensaje", "Return your weight",true);
			r.weight=0;
		}
		else if (total==r.weight)
		{ 
			callAnnounce();	
		}	
    }
        
     public void returnweight(double w, String remitente)
     {
    	 	p.weight=p.weight+w;
    	 	txtID.setText("ID: P");
    	 	txtWeight.setText("Weight:"+p.weight);
    	 	
			txtdescripcion.setText("Call Return Weight");
			if (p.active==false && total==p.weight)
			{
			callAnnounce();
			}
     }
     public boolean moreweight()
     {
			total=total+1;
			return (true);
     }
     
     public void callAnnounce()
     {
    	 TextView x=(TextView)findViewById(R.id.txtDetail);
    	 x.setText("CALL ANNOUNCE");
    	
     }
     public void inicializar(String Node,int x)
        {   	  
           ParseInstallation installation;
            installation=ParseInstallation.getCurrentInstallation();
	    	installation.put("Node", Node);
	    	DatabaseHelper bd = new DatabaseHelper(this);
	    	
	        //weight=Double.valueOf(bd.weightnode(id).getString(0))+weight;
	    	if(x==2)
	    	{
		    	installation.put("Weight", r.weight);
		        bd.insertarNodo(1, "R", r.weight);
		        Cursor c=bd.weightnode("R");
		    	 
	    	    c.moveToFirst();
	    	    r.weight=Double.valueOf(c.getString(0));
		    	txtWeight.setText("WeightR "+String.valueOf(r.weight));
			    installation.put("Weight", r.weight);

	    	}
	    	else if (x==1)
		    {
	    		q.weight=0;
		        bd.insertarNodo(1, "Q", q.weight);
		        Cursor c=bd.weightnode("Q");
		    	 
	    	    c.moveToFirst();
	    	    q.weight=Double.valueOf(c.getString(0));
		    	txtWeight.setText("Weight "+String.valueOf(q.weight));
			    installation.put("Weight", q.weight);
	    	}else
	    	{
	    		
		        Cursor c=bd.weightnode("P");
		    	 
	    	    c.moveToFirst();
	    	    p.weight=Double.valueOf(c.getString(0));
	    	    if(p.weight==0)p.weight=12.0;
	    	    installation.put("Weight", p.weight);
		        bd.insertarNodo(1, "P", p.weight);
		    	txtWeight.setText("WeightP "+String.valueOf(p.weight));
		    	p.initiator=true;
	    	}
	    	txtID.setText("Nodo: "+Node);
			txtdescripcion.setText("Register with node "+Node);	    	
			installation.saveInBackground();

       }
     	public void construc()
     	{
     		
     		txtID=(TextView)findViewById(R.id.txtID);
     		txtr=(TextView)findViewById(R.id.txtr);

     		txtWeight=(TextView)findViewById(R.id.txtWeight);
     		txtdescripcion=(TextView)findViewById(R.id.txtDescripcion);
			txtdescripcion.setText("Login with P or Q");
            p=new node(); q=new node();r=new node();
            p.setId('P');q.setId('Q'); r.setId('R');
            q.setWeight(0); r.setWeight(0);      
     	}
     	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba2);
			try {
				 Parse.initialize(this, "T4ScqUWBWRBfrsXVfJ6Bzkn5Sl87WepRiVezxu18", "EhVjQKASv5oPc9nC64qa6zj1hhV2tu8NsgRa0ufl");
			     PushService.setDefaultPushCallback(this, MainActivity.class);
				  ParsePush.subscribeInBackground("", new SaveCallback() {
					  @Override
					  public void done(ParseException e) {
					    if (e == null) {
					      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
					    } else {
					      Log.e("com.parse.push", "failed to subscribe for push", e);
					    }
					  }
					});	  
			} catch (Exception e) {
				Log.e("error", "error");
			}       
        intent=getIntent();
        construc();
		if( intent.getBooleanExtra("API", false))
		{
			//Si entro por aqui, el mensaje fue recibido y entonces el nodo nuevo debe aumentar su peso 
			// pasar a activo y activar la opcion de enviar mensaje d nuevo
			txtdescripcion.setText("Arrived New Message: "+intent.getStringExtra(MENSAJE));
			int des=Integer.parseInt(intent.getStringExtra(DESTINATARIO));
			enviarMensajeP_Q(intent.getStringExtra(MENSAJE), Double.parseDouble(intent.getStringExtra(WEIGHT)),des);
			Log.e("En teoria ya deberia haber enviado todo","ese es el mensaje");
			/*if(iniciar.getVisibility()==View.VISIBLE)
			iniciar.setVisibility(View.GONE);
			if(iniciarQ.getVisibility()==View.VISIBLE)
			iniciarQ.setVisibility(View.GONE);
			EnviarMensajeQ.setVisibility(View.VISIBLE);
			*/
		}
		else if( intent.getBooleanExtra("Return", false))
		{
			//Si entro por aqui, el mensaje fue recibido y entonces el nodo nuevo debe aumentar su peso 
			// pasar a activo y activar la opcion de enviar mensaje d nuevo
			txtdescripcion.setText("2 Arrived New Message: "+intent.getStringExtra(MENSAJE));
			returnweight(Double.parseDouble(intent.getStringExtra(WEIGHT)), intent.getStringExtra(REMITENTE));
		}
		else if( intent.getBooleanExtra("MoreWeight", false))
		{
			//Si entro por aqui, el mensaje fue recibido y entonces el nodo nuevo debe aumentar su peso 
			// pasar a activo y activar la opcion de enviar mensaje d nuevo
			txtdescripcion.setText("Arrived New Message: MoreWeight");
			moreweight();
		}
		addButtonListener();
	}
    
    public void sendPush(String destinatario,String node, Double weight, String remitente, String titulo, String texto,boolean returnW)
    {
    	Log.e("Nodo", node);
    	// Send push notification to query
    	JSONObject data = new JSONObject();
		JSONObject data2 = new JSONObject();

    	try {   
    		data2.put("action","br.ufu.weightThrowing.PushReceiver.UPDATE_STATUS");
    		data2.put("mensaje", "Nuevo Mensaje");
    		data2.put("weight",weight);
    		data2.put("remitente", remitente);
    		data2.put("destinatario", destinatario);
    		data2.put("titulo",titulo);
    		data2.put("texto", texto);
    		data2.put("API", !returnW);
    		data2.put("Return", returnW);
    		data2.put("MoreWeight",false);
    		data.put("data",data2);                   
		} catch (JSONException e) {	
			e.printStackTrace();
		}
    	ParsePush push = new ParsePush();
    	push.setMessage("Mensaje");
    	push.setData(data2); // Set our Installation query
    	ParseQuery pushQuery = ParseInstallation.getQuery();
    	pushQuery.whereEqualTo("Node", node);
    	push.setQuery(pushQuery);
    	push.sendInBackground();
    }
    public void sendPushMoreWeight(String node)
    {
    	// Send push notification to query
    	JSONObject data = new JSONObject();
		JSONObject data2 = new JSONObject();

    	try {   
    		data2.put("action","br.ufu.weightThrowing.PushReceiver.UPDATE_STATUS");
    		data2.put("mensaje", "Nuevo Mensaje");
    		data2.put("weight","");
    		data2.put("remitente", "");
    		data2.put("titulo","");
    		data2.put("texto", "");
    		data2.put("API", false);
    		data2.put("Return", false);
    		data2.put("MoreWeight", true);

    		data.put("data",data2);                   
		} catch (JSONException e) {	
			e.printStackTrace();
		}
    	ParsePush push = new ParsePush();
    	push.setMessage("Mensaje");
    	push.setData(data2); // Set our Installation query
    	ParseQuery pushQuery = ParseInstallation.getQuery();
    	pushQuery.whereEqualTo("Node", node);
    	push.setQuery(pushQuery);
    	push.sendInBackground();
    }
    
public class SendMessage extends AsyncTask<String,Integer,Boolean> {

	boolean resul;
	int node=0;
	@Override
	protected void onPreExecute() {
		EnviarMensajeP.setEnabled(false);
		EnviarMensajeQ.setEnabled(false);

		EnviarMensajeR.setEnabled(false);
		super.onPreExecute();
		

	}
	@Override
	protected Boolean doInBackground(String... params) {

	publishProgress(30);
	HttpClient httpClient = new DefaultHttpClient();
	String url="http://www.proserviciosdeoccidente.com.co/pars.php";
	HttpPost push = new HttpPost(url);

	try
	{
		HttpResponse resp;
	     List <NameValuePair> push_params = new ArrayList<NameValuePair>();
	      push_params.add(new BasicNameValuePair("application_id",getResources().getString(R.string.application_id)));	
	      push_params.add(new BasicNameValuePair("rest_api_key",getResources().getString(R.string.rest_api_key)));	
	      push_params.add(new BasicNameValuePair("mensaje",params[0]));	
	      push_params.add(new BasicNameValuePair("weight",params[1]));
	      push_params.add(new BasicNameValuePair("destinatario",params[2]));	
	      push_params.add(new BasicNameValuePair("remitente",params[3]));	
	      push_params.add(new BasicNameValuePair("texto","Texto "));	
	      push_params.add(new BasicNameValuePair("titulo","Titulo"));

	      Log.e("push","ready");
	      push.setEntity(new UrlEncodedFormEntity(push_params));
			 resp = httpClient.execute(push);
		String push_r = EntityUtils.toString(resp.getEntity());	
		//rellenarTablaNodo(params[3],  Double.parseDouble(params[1]));
		resul = true;
	  	Log.e("push id, ",push_r);
	   
	  	publishProgress(40);	
	  	if(params[3]=="Q")
	  		node=1;
	  	else
	  		if(params[3]=="R")
	  		node=2;
	  	else
	  			node=0;
	}
	catch(Exception ex)
	{
		Log.e("ServicioRest","Error mira ", ex);
		resul = false;
	}
	return resul;
}
		@Override
		protected void onPostExecute(Boolean result) {
			  if (result)
			{    
				  if( node==0)
					 nodopasivoP();
				  else if(node==1)
					  nodopasivoQ();
				  else 
					  nodopasivoR();
				  Log.e("Envio de mensaje ", "Perfecto ");		
			}
			    EnviarMensajeP.setEnabled(true);;
				EnviarMensajeQ.setEnabled(true);
				EnviarMensajeR.setEnabled(true);

		}			   
}
private Double rellenarTablaNodo(String id,Double weight){
    DatabaseHelper bd = new DatabaseHelper(this);
    ParseInstallation installation;
    //weight=Double.valueOf(bd.weightnode(id).getString(0))+weight;
		
    Log.e("relleno usuario", "peso es"+weight);
    txtWeight.setText("Weight "+weight);
    bd.insertarNodo(1, id, weight);
    installation=ParseInstallation.getCurrentInstallation();
	 installation.put("Node", id);
	 installation.put("Weight", String.valueOf(weight));
	 installation.saveInBackground();
	 return(weight);
    	
}
private Double rellenarTablaNodoR(String id,Double weight){
    Log.e("Recibe"," peso "+weight);
    DatabaseHelper bd = new DatabaseHelper(this);

    Cursor c=bd.weightnode(id);
 
    c.moveToFirst();
    weight=weight+Double.valueOf(c.getString(0));
    //weight=Double.valueOf(bd.weightnode(id).getString(0))+weight;
	//weight=installation.getDouble("weight")+weight;
    Log.e("relleno usuario ", "peso es"+weight);
    bd.updateWeight(weight);  
    ParseInstallation installation;
    installation=ParseInstallation.getCurrentInstallation();		
	 installation.put("Node", id);
	 installation.put("Weight", String.valueOf(weight));
	 installation.saveInBackground();
	 return(weight);
    	
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            DatabaseHelper bd = new DatabaseHelper(this);
            FragmentManager fragmentManager = getFragmentManager();
            DialogoAlerta dialogo = new DialogoAlerta();
            dialogo.show(fragmentManager, "tagAlerta");
            bd.vaciarcontactos();
            
            return true;
        }
        if (id == R.id.action_enviar) {
        	//EnviarMensajeQ=(Button)findViewById(R.id.btnEnviarMensajeQ);
        	
			EnviarMensajeQ.setVisibility(View.VISIBLE);
			//iniciar=(Button)findViewById(R.id.btnIniciar);
//			iniciarQ=(Button)findViewById(R.id.btnIniciarQ);

			iniciar.setVisibility(View.GONE);
			iniciarQ.setVisibility(View.GONE);
            return true;
        }
        if (id == R.id.action_return) {
        	FragmentManager fragmentManager = getFragmentManager();
            DialogoAlerta dialogo = new DialogoAlerta();
            dialogo.show(fragmentManager, "tag2");
            nodopasivoQ();
            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}



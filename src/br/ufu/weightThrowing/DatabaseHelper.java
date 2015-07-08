package br.ufu.weightThrowing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	protected static String TableNode = "Algoritmos";
	protected static String Info = "nodos";
	private String SQLCreateNode = "CREATE TABLE IF NOT EXISTS " + Info +  " (llave INTEGER PRIMARY KEY,id INTEGER, nodo VARCHAR(2), weight VARCHAR(20)) ";

	private static String name = "Nodes";
	private static int version = 1;
	private static CursorFactory cursorFactory = null;

	//private String sql = "CREATE TABLE  " + Info +  "  (id INT INTEGER PRIMARY KEY, bussines VARCHAR(1000), bussines_name VARCHAR(1000),salutation VARCHAR(1000), name VARCHAR(1000), lastname VARCHAR(1000), fullname VARCHAR(1000), position VARCHAR(1000), email VARCHAR(1000), phone VARCHAR(1000), mobile VARCHAR(1000),birthdayVARCHAR(1000))";	

	
	public DatabaseHelper(Context context){
		 super(context,name,cursorFactory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("Base de datos","va a crearla");
		db.execSQL(SQLCreateNode);

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public void insertarNodo(int idUsuario, String nodo, Double weight){
		 SQLiteDatabase db = getWritableDatabase();
		onCreate(db);
		Log.e("database instertar usuario",""+idUsuario);
		 if(db!=null){
		  db.execSQL("INSERT INTO "+Info+"  (id,nodo, weight) " +
		  " VALUES(" +"1,'" + nodo + "', '" + weight +"' )  ");
		  db.close();   
		 }
		}

	public Cursor leerNodos(){
		 SQLiteDatabase db = getReadableDatabase();
		  
		 return db.rawQuery("SELECT id, nodo, weight FROM "+ Info , null);  
		}
	
	
	public void updateWeight(Double weight)
	{
		 SQLiteDatabase db = getReadableDatabase();

		db.execSQL("UPDATE "+Info +" SET weight='"+weight+"' WHERE id=1 ");
	 
	}
	
	public void ejecutarsql(String sql) 
	{
		 SQLiteDatabase db = getWritableDatabase();
			onCreate(db);
			 if(db!=null){
			  db.execSQL("INSERT INTO "+Info+sql+" WHERE id='1'");
			  db.close();   
			 }		
	}
	public Cursor datoscontacto(){
		 SQLiteDatabase db = getReadableDatabase();
		 return db.rawQuery("SELECT id,nodo,weight FROM "+ Info+" WHERE id = 1", null);  
		}
	public Cursor weightnode(String nodo){
		nodo="'"+nodo+"'";
		 SQLiteDatabase db = getReadableDatabase();
		 return db.rawQuery("SELECT weight FROM "+ Info+" WHERE id = 1", null);  
		}
	public Cursor leerContactos(String nodo){
		 SQLiteDatabase db = getReadableDatabase();
		  Log.e("leer peso de ",nodo);
		 return db.rawQuery("SELECT weight FROM "+ Info+"  WHERE nodo ="+nodo, null);  
		}


	public void vaciarcontactos()
	{
		 SQLiteDatabase db = getWritableDatabase();
			onCreate(db);
			 if(db!=null){
			
		 db.execSQL("DROP TABLE IF EXISTS '"+Info+"' ");
		 Log.e("se supone q la vacio","jaja");
		  db.close();   

			 }
	}

		
	
}

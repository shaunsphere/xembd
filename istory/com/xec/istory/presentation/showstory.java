
package com.xec.istory.presentation;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.xec.istory.R;


public class showstory extends Activity  {
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showstory);
		final TextView iStoryContent = (TextView)findViewById(R.id.truyencontent);
		
		Intent intent = getIntent();
		String getstorypath = intent.getExtras().getString("getstorypath");
		
		int p = getstorypath.lastIndexOf("=")+1;
		String id_str = getstorypath.substring(p);
		
		System.out.println("Recevied getstorypath=: "+getstorypath + " id = "+ id_str );
				

		File f = new File("/mnt/sdcard/truyen/"+id_str);
		Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
		Resources res = getResources();
		BitmapDrawable icon = new BitmapDrawable(res,bmp);
		getActionBar().setIcon(icon);
		iStoryContent.setText("Loading story content...");
		
		LongOperation LoadContentTask = new LongOperation();
		LoadContentTask.execute(getstorypath);
	}
	
	//////////////////////////
	
	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		  protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(params[0]);
	
			String result="";
			String iSTitle="";
			InputStream is=null;
	    try{  
			  HttpResponse response = httpclient.execute(httppost);
		      HttpEntity entity = response.getEntity();
		      if (entity != null) {
		      is = entity.getContent();
				try{
				      //  BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
					
				      StringBuilder sb = new StringBuilder();
				      String line = null;
				      while ((line = reader.readLine()) != null) {
			              sb.append(Html.fromHtml(line) + "\n");
			             // System.out.println(line);
			            //  System.out.println("pppp: "+Html.fromHtml(line));
			              
				      }
				      is.close();
				      result=sb.toString();
				}catch(Exception e){
				      Log.e("log_tag", "Error converting result "+e.toString());
				}
	
				//parse json data
				try{
					JSONObject jsonresult = new JSONObject(result);
					JSONArray JSfiles = jsonresult.getJSONArray("liststory");
			        JSONObject json_data = JSfiles.getJSONObject(0);
			        result=json_data.getString("storycontent");
			        iSTitle = json_data.getString("storyname");
			        //ChangeTitleonLoading(json_data.getString("storyname"));-->Wrong because inside of long run
			          
			            //System.out.println("storyname= " + jsonresult.getString("storyname") + " : " +jsonresult.getString("storycontent") );
			      
				}
				catch(JSONException e){
				      Log.e("log_tag", "Error parsing data "+e.toString());
				}
		      }
		}catch(Exception e){
		      Log.e("log_tag", "Error in http connection "+e.toString());
		}
		    
		  //  int p = result.split("\\s").length;
		    //int p = html.length();
	    result = iSTitle + "\u2665"+result;
		    return  result;
		   
		  }      

        @Override
        protected void onPostExecute(String result) {
        	String CombinTitCon[]= result.split("\u2665");
            TextView iStoryContent = (TextView) findViewById(R.id.truyencontent);
            iStoryContent.setText(CombinTitCon[1]); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            ChangeTitleonLoading(CombinTitCon[0]);
    		
        }
        
        protected void ChangeTitleonLoading(String iSTitle) {
         	setTitle(iSTitle);
    		setTitleColor(Color.BLUE);
    		//getActionBar().setIcon(R.drawable.ixcat);
    		
        }

        @Override
        protected void onPreExecute() {
        	// iStoryContent.setText("PRE--Executed");
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.barshowstory, menu);
	    return super.onCreateOptionsMenu(menu);//true; 
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
        case R.id.pre:
        	setTitle("pre");
            break;
        case R.id.next:
        	setTitle("nx");
            break;
        default:
        	//setTitle("dddd");
        	         
        }
        return super.onOptionsItemSelected(item);
	}
	
}



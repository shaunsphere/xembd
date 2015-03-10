package com.xec.istory.presentation;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.xec.istory.R;

@SuppressLint("NewApi")
public class DynListIS extends Activity implements OnItemClickListener {

public static String sserverip;
public static String storyname;
public static String truyenfolder;

//private TextView txtStatus;

ListView listv;
//GridView listv;
List<ViewItem> rowItems;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.xec.istory.R.layout.dynlist);

		Bundle extras = getIntent().getExtras(); 
		sserverip = extras.getString("serverip");
		truyenfolder = extras.getString("truyenfolder");
		
		if (truyenfolder.contains("V")) {
			setTitle(R.string.listvn);
			getActionBar().setIcon(R.drawable.vietnamic);
			
		}else{
			setTitle(R.string.listen);
			getActionBar().setIcon(R.drawable.englishic);
			
		}
		
		String[] AllStringwors=extras.getStringArray("AllStringwors");
		int numofapps = AllStringwors.length; 

	        rowItems = new ArrayList<ViewItem>();
	        for (int i = 0; i < numofapps; i++) {
	        	String rowi[] = AllStringwors[i].split( "\u2665" , 4); //u00e8 //u2202
	        	ViewItem item = new ViewItem(rowi[0].trim(),rowi[1].trim(),rowi[2].trim(),rowi[3].trim());
	        	//ViewItem item = new ViewItem(rowi[0],rowi[1],rowi[2],rowi[3]);
	        //System.out.println("Show Story: " + rowi[0] + "@@@"+rowi[1] + "@@@"+rowi[2] + "@@@"+rowi[3]);
	            rowItems.add(item);
	        }
	        listv = (ListView) findViewById(R.id.appList);
	        CustomListViewAdapter adapter = new CustomListViewAdapter(this, AllStringwors);
	        listv.setAdapter(adapter);
	        listv.setOnItemClickListener(this);

	     final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
		 final Drawable wallpaperDrawable = wallpaperManager.getFastDrawable();
		 getWindow().setBackgroundDrawable(wallpaperDrawable);
	        // for Search
		 handleIntent(getIntent());
 	}
 
		 /////////////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.searchview_in_menu, menu);
		//Log.d("oncreateOptionMenu: ",""+menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
		//searchView.setBackgroundColor(Color.DKGRAY);
	    return super.onCreateOptionsMenu(menu);//true; 
	}
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      // Do work using string
	      System.out.println("Search For: "+query);
	    }
	}

	    /////////////////////////////////////////////////////////////////

    @Override
    public void onDestroy()
    {
        listv.setAdapter(null);
        super.onDestroy();
    }
	//////////////////////////////////////////////////
	
//    public void ShowStory() {
//		Intent ShowStoryIntent = new Intent(this, showstory.class);
//		ShowStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//     	startActivity(ShowStoryIntent);	
//	   
//	}
    

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		//storyname=rowItems.get(position).getmd5();
//		String abspath=sserverip + truyenfolder+"/"+storyname+"/"+storyname+".txt";
		
		String stroyid=rowItems.get(position).getmd5();
		String getstorypath=sserverip + "browser.php?path="+stroyid;
	     System.out.println("PASS getstorypath=: "+getstorypath);
		Intent ShowStoryIntent = new Intent(this, showstory.class);
		//ShowStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ShowStoryIntent.putExtra("getstorypath", getstorypath);
		startActivity(ShowStoryIntent);
  		
	}

}


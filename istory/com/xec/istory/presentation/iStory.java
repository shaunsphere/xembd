package com.xec.istory.presentation;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xec.istory.R;
import com.xec.istory.asynprogress.AsyncTaskManager;
import com.xec.istory.asynprogress.OnTaskCompleteListener;
import com.xec.istory.asynprogress.Task;

public class iStory extends Activity implements OnTaskCompleteListener {

    private AsyncTaskManager mAsyncTaskManager;
	
	public static final String[] descriptions = new String[] { };
	public static final String[] ImLinks = new String[] { };
	public EditText EdtDomain;
    private TextView  txtshow;
	
	//public String serverIP = "http://vancouver.ieee.ca/CASS/media/truyen/";//"aroixec.co.nf";
	//public String serverIP = "http://192.168.0.174/mappiandroid/truyen/";
   // public String serverIP = "http://192.168.0.52/truyen/";
    public String serverIP;// = "http://aroixec.co.nf/truyen/";
  
	Context context;
	ImageView imageVN,imageEN;
	
	public int currentimageindex=0;
//	Timer timer;
//	TimerTask task;
	ImageView slidingimage;
	
	private int[] IMAGE_IDS = {
			R.drawable.katie0,R.drawable.katie1,R.drawable.katie2,R.drawable.katie3,R.drawable.katie4,R.drawable.katie5,
			R.drawable.katie6,R.drawable.katie7,R.drawable.katie8,R.drawable.katie9,R.drawable.katie10,R.drawable.katie11,
			R.drawable.katie12,R.drawable.katie13,R.drawable.katie14,R.drawable.katie15,R.drawable.katie16,R.drawable.katie17,
			R.drawable.katie18,R.drawable.katie19,R.drawable.katie20,R.drawable.katie21,R.drawable.katie22,R.drawable.katie23,
			R.drawable.katie24,R.drawable.katie25,R.drawable.katie26,R.drawable.katie27,R.drawable.katie28,R.drawable.katie29,
			R.drawable.katie30,R.drawable.katie31,R.drawable.katie32,R.drawable.katie33,R.drawable.katie34,R.drawable.katie35,
			R.drawable.katie36,R.drawable.katie37,R.drawable.katie38,R.drawable.katie39,R.drawable.katie40,R.drawable.katie41,
			R.drawable.katie42,R.drawable.katie43,R.drawable.katie45,R.drawable.katie46,R.drawable.katie47,R.drawable.katie48,
			R.drawable.katie49,R.drawable.katie50,R.drawable.katie51,R.drawable.katie52,R.drawable.katie53,R.drawable.katie54
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.xec.istory.R.layout.mainstory);
		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if (pref!=null){
			serverIP = pref.getString("serverid", "");
			  
			}
		
		EdtDomain = (EditText)findViewById(R.id.editTextDomain);
		EdtDomain.setText(serverIP, EditText.BufferType.EDITABLE);
		
		txtshow = (TextView)findViewById(com.xec.istory.R.id.warntxt);
		txtshow.setTextColor(Color.rgb(0,162,250)); // rgb( red , green , blue ) ;
		txtshow.setTextSize(16); 
		
		// Create manager and set this activity as context and listener
		mAsyncTaskManager = new AsyncTaskManager(this, this);
		// Handle task that can be retained before
		mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());
		
        final Handler mHandler = new Handler();
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
            	AnimateandSlideShow();
            }
        };
		
        int delay = 100; // delay for 1 sec.
        int period = 3000; // repeat every 4 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
        public void run() {
        	 mHandler.post(mUpdateResults);
        }
        }, delay, period);
		
	    
	}
	////////////////////////////////////////////////////////

    @Override
    public Object onRetainNonConfigurationInstance() {
	// Delegate task retain to manager
	return mAsyncTaskManager.retainTask();
    }

    @Override
    public void onTaskComplete(Task task) {
	if (task.isCancelled()) {
	    // Report about cancel
	    Toast.makeText(this, com.xec.istory.R.string.task_cancelled, Toast.LENGTH_LONG).show();
	} else {
	    // Get result
	    Boolean result = null;
	    try {
		result = task.get();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    // Report about result
	    //Toast.makeText(this,getString(R.string.task_completed, (result != null) ? result.toString() : null),Toast.LENGTH_LONG).show();
	}
	
    }
    //////////////////////////////////////////////////
public boolean wifistatus(){
	boolean wifistatus = false;
			ConnectivityManager connMgr = (ConnectivityManager) 
			getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			   if (networkInfo != null && networkInfo.isConnected()) {
				   wifistatus = true;
				   return wifistatus;
			   }
			return wifistatus;
	
}
public void PressedBut (View v) {
		Button clickedButton = (Button) v;
		String truyenfolder = "VN";
		
	    if (wifistatus()) {
    		context = getBaseContext();
			   switch (clickedButton.getId())
			   {
			      case com.xec.istory.R.id.viet_but:
//						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//						String sid = EdtDomain.getText().toString();
//						updatePref(pref,sid);
//					    mAsyncTaskManager.setupTask(new Task(context, serverIP,truyenfolder, getResources())); 
			          break;
			      case com.xec.istory.R.id.en_but:
			    	  truyenfolder = "EN";
			    	  
			          break;
			      case com.xec.istory.R.id.exit_but:
			          finish();
			          android.os.Process.killProcess(android.os.Process.myPid());
			   }
			   SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String sid = EdtDomain.getText().toString();
				updatePref(pref,sid);
			    mAsyncTaskManager.setupTask(new Task(context, serverIP,truyenfolder, getResources())); 
			   

    } else {
        // display error
    	txtshow.setText(com.xec.istory.R.string.nonetwork);
    	txtshow.setTextColor(0xAAFF0000);
    }
		

	}

	private void AnimateandSlideShow() {
		slidingimage = (ImageView)findViewById(R.id.imageVN);
		slidingimage.setImageResource(IMAGE_IDS[currentimageindex%IMAGE_IDS.length]);
		currentimageindex++;
		Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		slidingimage.startAnimation(rotateimage);
		}
	public void updatePref(SharedPreferences pref,String sid) {
		Editor editor = pref.edit();
		editor.putString("serverid", "" + sid);
		editor.commit();
	}

}


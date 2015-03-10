package com.xec.istory.asynprogress;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import com.xec.istory.presentation.DynListIS;
import com.xec.istory.presentation.JSONoutString;

public final class Task extends AsyncTask<Void, String, Boolean> {
	String[] AllStringrowstask;
    protected final Resources mResources;
    Context mContext;
    
    private Boolean mResult;
    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    private String serverip,storyfolder;
   
    /* UI Thread */
    public Task(Context context, String serverip, String storyfolder,Resources resources) {
	// Keep reference to resources
	mResources = resources;
	this.serverip = serverip;
	this.storyfolder = storyfolder;
	mContext = context;
	// Initialise initial pre-execute message
	
	mProgressMessage = resources.getString(R.string.unknownName,serverip);

    }

    /* UI Thread */
    public void setProgressTracker(IProgressTracker progressTracker) {
	// Attach to progress tracker
	mProgressTracker = progressTracker;
	// Initialise progress tracker with current task state
	if (mProgressTracker != null) {
	    mProgressTracker.onProgress(mProgressMessage);
	    if (mResult != null) {
		mProgressTracker.onComplete();
	    }
	}
    }

    /* UI Thread */
    @Override
    protected void onCancelled() {
	// Detach from progress tracker
	mProgressTracker = null;
    }
    
    /* UI Thread */
    @Override
    protected void onProgressUpdate(String... values) {
	// Update progress message 
	mProgressMessage = values[0];
	// And send it to progress tracker
	if (mProgressTracker != null) {
	    mProgressTracker.onProgress(mProgressMessage);
	}
    }

    /* UI Thread */
    @Override
    protected void onPostExecute(Boolean result) {
	// Update result
	mResult = result;
	// And send it to progress tracker
	if (mProgressTracker != null) {
	    mProgressTracker.onComplete();
	}
	
	// Detach from progress tracker
	//mProgressTracker = null;
		if(AllStringrowstask.length >1){
	//		System.out.println(AllStringrowstask[1]);
		
			Intent ListAppShowintent = new Intent(mContext, DynListIS.class);
			Bundle b=new Bundle();
			b.putStringArray("AllStringwors", AllStringrowstask);
			ListAppShowintent.putExtras(b);
	     	ListAppShowintent.putExtra("serverip",serverip);
	     	ListAppShowintent.putExtra("truyenfolder",storyfolder);
	     	ListAppShowintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     	mContext.startActivity(ListAppShowintent);	
		}
    }

    /* Separate Thread */
    @Override
    protected Boolean doInBackground(Void... arg0) {
	// Working in separate thread

	    // Check if task is cancelled
	    if (isCancelled()) {
		// This return causes onPostExecute call on UI thread
		return false;
	    }

	    try {
		// This call causes onProgressUpdate call on UI thread
			JSONoutString jsout = new JSONoutString();
			
			String useridlink = serverip + "browser.php?path=" + storyfolder;
			ArrayList<String> fetchList = jsout.JsGetStatus(useridlink);
//			int numofapps = fetchList.size();
			int numofapps = (int) Math.ceil(fetchList.size() /4); 
			AllStringrowstask= new String[numofapps];
			if (numofapps > 0){
				
				String[] storyid = new String[numofapps];
				String[] storyname = new String[numofapps];
				String[] storyten = new String[numofapps];
				String[] stroyiconlink = new String[numofapps];
				String[] stroycontent = new String[numofapps];
				//System.out.println(fetchList);
		        for (int i = 0; i < numofapps; i++) {
		        	storyid[i]=fetchList.get(4*i);
		        	storyname[i]=fetchList.get(4*i+1);
		        	storyten[i]=fetchList.get(4*i+2);
		        	stroycontent[i]=fetchList.get(4*i+3);
		        	
		        	//stroyiconlink[i]= serverip  +"storicons/" + storyname[i].replaceAll("\\s","").toLowerCase() +".png";
		        	
		        	stroyiconlink[i]= serverip  +"storicons/" + storyid[i] +".png";
		        	
		        	if (storyten[i].length()>2){
		        		AllStringrowstask[i] =  storyten[i]+ "\u2665" + stroycontent[i] + "\u2665" + stroyiconlink[i]+ "\u2665" + storyid[i];
		        	}
		        	else{
		        		AllStringrowstask[i] =  storyname[i]+ "\u2665" + stroycontent[i] + "\u2665" + stroyiconlink[i]+ "\u2665" + storyid[i];
		        	}
		            
		            publishProgress(mResources.getString(com.xec.istory.R.string.task_loading, i, storyname[i]));
//		            System.out.println("i= " +i +storyname[i] + ": " + stroycontent[i] +"\n");
		        }
			}else{
				  publishProgress("list empty");
		    	  Thread.sleep(1000);
			}
	    } catch (InterruptedException e) {
		e.printStackTrace();
		// This return causes onPostExecute call on UI thread
		return false;
	    }
	
	// This return causes onPostExecute call on UI thread
	return true;
    }

}

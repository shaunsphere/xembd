package com.xec.istory.presentation;
import android.app.Application;

public class GlobalVar  extends Application {

	  private static String storydisplay;
	  private static int numofpig;
//	  private static String title;

	  public String getStory(){
	    return storydisplay;
	  }
	  public void setStory(String s){
		  storydisplay = s;
	  }
	  
	  public static int getnumpix(){
		    return numofpig;
		  }
		  public void setnumpix(int n){
			  numofpig = n;
		  }
	 	  
	}
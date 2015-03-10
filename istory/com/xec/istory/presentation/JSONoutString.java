package com.xec.istory.presentation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

public class JSONoutString {
	
	public static ArrayList<String> JsGetStatus(String urlink) {
		
		ArrayList<String> allout = new ArrayList<String>();
		if (isHttpUrlAvailable(urlink)){

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(urlink);

		String result="";
		InputStream is=null;
			try{
		      httppost.setEntity(new UrlEncodedFormEntity(postParameters,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
			    e.printStackTrace();
			    Log.e("log_tag", "Error UnsupportedEncodingException "+e.toString());
			}  
			try{  
				  HttpResponse response = httpclient.execute(httppost);
			      HttpEntity entity = response.getEntity();
			      if (entity != null) {
			      is = entity.getContent();
					try{
					      //  BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
						BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
						//BufferedReader reader = new BufferedReader(new InputStreamReader(is,"HTML-ENTITIES"),8);
						
					      StringBuilder sb = new StringBuilder();
					      String line = null;
					      while ((line = reader.readLine()) != null) {
				              sb.append(Html.fromHtml(line) + "\n");
				             // System.out.println(line);
				              System.out.println("HEHE: "+Html.fromHtml(line));
				              
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
						// System.out.println("status= " + status);
						for(int i=0;i<JSfiles.length();i++){
				            JSONObject json_data = JSfiles.getJSONObject(i);
				            allout.add(json_data.getString("storyid"));
				            allout.add(json_data.getString("storyname"));
				            allout.add(json_data.getString("storyten"));
				            allout.add(json_data.getString("storycontent"));
				           // String Contentis = new String(json_data.getString("storycontent").getBytes("UTF-8"), 8);
				            //String Contentis=URLDecoder.decode(json_data.getString("storycontent"), "UTF-8");
				            //String Contentis=URLDecoder.decode(json_data.getString("storycontent"), "HTML-ENTITIES");
				           // allout.add(Contentis);
				            //System.out.println("storyname= " + jsonresult.getString("storyname") + " : " +jsonresult.getString("storycontent") );
				        }
							//For iscontent catch case in special case
							//System.out.println("contenttreuyn= " + jsonresult.getString("storycontent"));
						//	GlobalVar gstorycontent = new GlobalVar();
        					//gstorycontent.setStory(jsonresult.getString("storycontent"));
					}
					catch(JSONException e){
					      Log.e("log_tag", "Error parsing data "+e.toString());
					}
			      }
			}catch(Exception e){
			      Log.e("log_tag", "Error in http connection "+e.toString());
			      allout.add(e.toString());
			}

			return allout;
		}
		else{
			allout.add("Connect Timeout, Please Check Host Service");
			return allout;
		}
		
		}
	
//////////////////////////////////////////////////////////////

public static void GetcontentthenGput(String linktxt) {
	GlobalVar gstorycontent = new GlobalVar();
	try {
	    // Create a URL for the desired page
	    URL url = new URL(URLEncoder.encode(linktxt, "UTF-8"));

	    // Read all the text returned by the server
	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	    String str;
	    while ((str = in.readLine()) != null) {
	        // str is one line of text; readLine() strips the newline character(s)
	    }
	    in.close();
	  //  System.out.println("str= " + str);
	    gstorycontent.setStory(str);
	} catch (MalformedURLException e) {
	} catch (IOException e) {
	}


	}
						
			
////////////////////////////////////////////////////////////
	 public static boolean isHttpUrlAvailable(String urlString) {
		    HttpURLConnection connection = null;
		    try {
		      // might as well test for the url we need to access
		      URL url = new URL(urlString);

		      connection = (HttpURLConnection) url.openConnection();
		      connection.setConnectTimeout(3000);
		  
		      connection.connect();
		      boolean success = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
		  
		      return success;
		    } catch (IOException e) {
		  
		      return false;
		    } finally {
		      if (connection != null) {
		        connection.disconnect();
		      }
		    }
		  }
	////////////////////////////////////////////////////////////////
	}

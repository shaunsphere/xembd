package com.xec.istory.domain;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.util.Log;
 
public class Frothenticate {
  private String username, password;
  private String errorMessage;
  private Socket socket = new Socket();
  private  DataOutputStream dataOutputStream = null;
  private String fromServer,app;
  public String serverIP;
  
  
  public Frothenticate(String username, String password, String app, String sIP){
    this.username = username;
    this.password = password;
    this.app = app;
    this.serverIP = sIP;
  }
  
  public String FrothenticateStatus (){
	    String Frothenvalid = "false";
	    errorMessage = "";
	    try{
	      if (!socket.isConnected() || socket.isClosed()){
	        socket = new Socket();
	        System.out.println("Connecting with: " + serverIP);
	        SocketAddress sockaddr = new InetSocketAddress(serverIP,9995); 
	        socket.setKeepAlive(true);
	        socket.connect(sockaddr, 10000);
	        dataOutputStream = new DataOutputStream(socket.getOutputStream());
	        new DataInputStream(socket.getInputStream());
	        byte[] msgUser = (username + "," + password +"," + app + "$").getBytes();
	        dataOutputStream.write(msgUser,0,msgUser.length);
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        fromServer = in.readLine();
	        fromServer = fromServer.replace("$","");
	        System.out.println("From Server: " + fromServer);
	        String resp[];
	        resp = fromServer.split(",");
    	        
	        if (resp[0].contains("Yes")){
	        	Frothenvalid = "Ready";
	        }
	        
	        if (!resp[1].equals("")){
	        	
	           //Usually a maintenance / Wrong password message if the response replies with "No".
	        }
	        errorMessage = resp[1];
	        socket.close();
	      }
	    }catch(Exception e){
	      System.out.println("Login Error: " + e.getMessage());
	      errorMessage = e.getMessage();
	    }

	    return Frothenvalid;
	    
	 
  }
  
  public String getFromServer(){
    return fromServer;
  }
  
  public String getErrorMessage(){
    return errorMessage;
  }
  
}

package com.instance.comparator.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

 
 
public class TPRESTCalls {
	public static Map<Integer,String> Instance = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer,String> FirstInstance = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer,String> SecondInstance = new ConcurrentHashMap<Integer, String>();
	public static String[] hostList1;
	public static String[] hostList2;
	public static String[] hostList = new String[1000];
	public static String urlName,urlName1,urlName2, username, password;
	public static int count =0,len1=0,len2=0,key1=0,key2=1,diffLen=0;
	
	public static void Data(String UName, String Password, String Hostname,
			String UName2, String Password2, String Hostname2) throws InterruptedException {
		//Cleaning variables for second call to avoid overwriting or tampering with new data
		Instance.clear();
		FirstInstance.clear();
		SecondInstance.clear();
		hostList1 = new String[1000];
		hostList2 = new String[1000];
		len1=0;
		len2=0;
		urlName1="";
		urlName2 = "";
		urlName="";
		password="";
		username="";
		count=0;
		
		long startTime = System.nanoTime();
		 urlName = Hostname;
				username = UName;
				password = Password;
				urlName1 = urlName;
		// Calling 1st Instance
		Processing(urlName, username, password);
		FirstInstance.putAll(Instance);
		len1=count;
		int i=0;
		for(Map.Entry<Integer,String> k:FirstInstance.entrySet())
       {
           hostList1[i]=  k.getValue();      
		      i++;     
	   }
		// Cleaning variables for second instance
		Instance.clear();
		hostList =new String[1000];
		count = 0;
		
		urlName = Hostname2;
		username = UName2;
		password = Password2;
		urlName2 = Hostname2;
		System.out.println("Username:"+username);
		System.out.println("Password:"+password);
		System.out.println("url:"+urlName);
		Processing(urlName, username, password);
		SecondInstance.putAll(Instance);
		len2=count;
		i=0;
		//Storing name of workflows
		for(Map.Entry<Integer,String> k:SecondInstance.entrySet())
       {
           hostList2[i]=  k.getValue();      
		      i++;     
	   }
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Execution Time" +totalTime);
	}
	
	public static void Processing(String urlName, String username, String password) throws InterruptedException {

		try {
			
			URL url = new URL(urlName+"/svc/tradingpartners/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			conn.setRequestProperty("Authorization", basicAuth);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			String title;
			int j=1;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output;
			System.out.println("Output from Server .... \n");
			System.out.println("*********************************************************************");;
			while ((output = br.readLine()) != null) {
				if(output.contains("_id"))
					count++;
				if(output.contains("_title"))
				{
					output.trim();
					title = output.substring(15, output.length()-2);
					int i = (title.length()+4);
					Instance.put(j,title);
					System.out.print("*" + count + ". " + title);
					while(i < 68)
					{	System.out.print(" ");
					i++;
					}
					System.out.println("*");
					j++;
				}
			}			
			System.out.println("***********************************************************************");;
			System.out.println("Total number of workflows are: " + count);

			br.close();
			conn.disconnect();
		}
		
		catch(Exception e) {
		e.printStackTrace();
		}
	
		
		int j=0;
		for(Map.Entry<Integer, String> entry :Instance.entrySet()){
			hostList[j] = urlName + entry.getValue() ;
		j++;
		}
	
	}
}
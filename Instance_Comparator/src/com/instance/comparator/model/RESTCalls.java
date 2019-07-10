package com.instance.comparator.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RESTCalls {
	private static final int MYTHREADS = 30;
	public static Map<Integer, String> Instance = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> FirstInstance = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> SecondInstance = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> Program = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> Time = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> Program1 = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> Time1 = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> Program2 = new ConcurrentHashMap<Integer, String>();
	public static Map<Integer, String> Time2 = new ConcurrentHashMap<Integer, String>();
	public static String[] hostList1;
	public static String[] hostList2;
	public static String[] codeDiff;
	public static String[] hostList = new String[1000];
	public static String urlName, urlName1, urlName2, username, password;
	public static int count = 0, len1 = 0, len2 = 0, key1 = 0, key2 = 1, diffLen = 0;

	public static void Data(String UName, String Password, String Hostname, String UName2, String Password2,
			String Hostname2) throws InterruptedException {

		// Cleaning variables for second call to avoid overwriting or tampering with new
		// data
		Instance.clear();
		Program.clear();
		Program1.clear();
		Program2.clear();
		Time.clear();
		FirstInstance.clear();
		SecondInstance.clear();
		Time1.clear();
		Time2.clear();
		hostList1 = new String[1000];
		hostList2 = new String[1000];
		len1 = 0;
		len2 = 0;
		urlName1 = "";
		urlName2 = "";
		urlName = "";
		password = "";
		username = "";
		diffLen = 0;
		long startTime = System.nanoTime();
		urlName = Hostname;
		username = UName;
		password = Password;
		urlName1 = urlName;
		// Calling 1st Instance
		Processing(urlName, username, password);

		Program1.putAll(Program);
		Time1.putAll(Time);
		FirstInstance.putAll(Instance);
		len1 = count;
		int i = 0;
		for (Map.Entry<Integer, String> k : FirstInstance.entrySet()) {
			hostList1[i] = k.getValue();
			i++;
		}
		// Cleaning variables for second instance
		Instance.clear();
		Program.clear();
		Time.clear();
		hostList = new String[1000];
		count = 0;

		urlName = Hostname2;
		username = UName2;
		password = Password2;
		urlName2 = Hostname2;
		System.out.println("Username:" + username);
		System.out.println("Password:" + password);
		System.out.println("url:" + urlName);
		Processing(urlName, username, password);
		Program2.putAll(Program);
		Time2.putAll(Time);
		SecondInstance.putAll(Instance);
		i = 0;
		// Storing name of workflows
		for (Map.Entry<Integer, String> k : SecondInstance.entrySet()) {
			hostList2[i] = k.getValue();
			i++;
		}
		hostList = new String[1000];
		len2 = count;
		count = 0;
		char a;
		int begin = 0, end = 0;
		// fetching server's name
		for (int m = 0; m < urlName1.length(); m++) {
			a = urlName1.charAt(m);
			if (a == '/' && urlName1.charAt(m + 1) == '/') {
				begin = m + 2;
				m++;
				continue;
			}
			if (a == '/' && urlName1.charAt(m + 1) != '/') {
				end = m;
				break;
			}

		}
		System.out.println("This is begin and end" + begin + " " + end);
		urlName1 = urlName1.substring(begin, end);
		urlName2 = urlName2.substring(begin, end);
		// Cleaning Programming Code
		cleanCode();

		// Comparing instances
		compareInstances();

		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Execution Time" + totalTime);
	}

	public static void Processing(String urlName, String username, String password) throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);

		try {

			URL url = new URL(urlName + "/svc/workflows/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			conn.setRequestProperty("Authorization", basicAuth);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			String title;
			int j = 1;
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			System.out.println("Output from Server .... \n");
			System.out.println("*********************************************************************");
			;
			while ((output = br.readLine()) != null) {
				if (output.contains("_id"))
					count++;
				if (output.contains("_title")) {
					output.trim();
					title = output.substring(24, output.length() - 3);
					int i = (title.length() + 4);
					Instance.put(j, title);
					System.out.print("*" + count + ". " + title);
					while (i < 68) {
						System.out.print(" ");
						i++;
					}
					System.out.println("*");
					j++;
				}
			}
			System.out.println("***********************************************************************");
			;
			System.out.println("Total number of workflows are: " + count);

			br.close();
			conn.disconnect();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		int j = 0;
		for (Map.Entry<Integer, String> entry : Instance.entrySet()) {
			hostList[j] = urlName + "/svc/workflows/" + entry.getValue();
			j++;
		}

		for (int i = 0; i < count; i++) {
			String url = hostList[i];
			Runnable worker = new MyRunnable(url);
			executor.execute(worker);
		}
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.SECONDS);
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}
		System.out.println("\nFinished all threads");

	}

	public static class MyRunnable implements Runnable {
		private final String url;

		MyRunnable(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			try {
				URL siteURL = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) siteURL.openConnection();
				String userpass = username + ":" + password;
				String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
				conn.setRequestProperty("Authorization", basicAuth);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				conn.connect();
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}
				String output;
				BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				while ((output = br1.readLine()) != null) {

					if (output.contains("\"businessProcess\":")) {
						key1++;
						Program.put(key1, output.substring(22, output.length() - 2));
					}

					if (output.contains("\"timestamp\":")) {
						Time.put(key2, output.substring(16, 26));// * is used to segregate time from url
						key2++;
						break;
					}
				}
				br1.close();
				conn.disconnect();
			} catch (Exception e) {

				e.printStackTrace();

			}
		}
	}

	// Cleaning Program Code
	public static void cleanCode() {
		Program.clear();
		System.out.println("$$$$$$ Program Size" + Program.size());
		int i = 1;
		String temp = "";
		for (Map.Entry<Integer, String> entry : Program1.entrySet()) {
			temp = entry.getValue().replaceAll("(\\\\n+|\\\\t+| {2,}+|\\\\n+\\\\t+|\\t+|\\n+|\\r+)", "");
			Program.put(i, temp);
			i++;
		}

		Program1.clear();
		Program1.putAll(Program);
		Program.clear();
		temp = null;
		i = 1;
		for (Map.Entry<Integer, String> entry : Program2.entrySet()) {
			temp = entry.getValue().replaceAll("(\\\\n+|\\\\t+| {2,}+|\\\\n+\\\\t+|\\t+|\\n+|\\r+)", "");
			Program.put(i, temp);
			i++;
		}

		Program2.clear();
		Program2.putAll(Program);
		Program.clear();
		System.out.println("First Code");

		for (Map.Entry<Integer, String> entry : Program1.entrySet()) {
			System.out.println(entry.getValue());
		}
		System.out.println("Second Code");
		for (Map.Entry<Integer, String> entry : Program2.entrySet()) {
			{
				System.out.println(entry.getValue());
			}
		}

	}

	// Comparing Instances
	public static void compareInstances() {
		String s1 = "", s2 = "", t1 = "", t2 = "", temp = "";
		int i = 0, j = 1, k = 0;
		codeDiff = new String[1000];
		Arrays.fill(codeDiff, null);
		Map<Integer, String> Program3 = new ConcurrentHashMap<Integer, String>();
		Map<Integer, String> Time3 = new ConcurrentHashMap<Integer, String>();
		Boolean compare = FirstInstance.equals(SecondInstance);
		Boolean difference;
		if (compare == true) {
			difference = Program1.equals(Program2);
			if (difference)
				difference = Time1.equals(Time2);
			if (difference)
				System.out.println("Instances are same");
		} else {
			/* Fetching uncommon programs */
			Iterator<Integer> itr1 = Program1.keySet().iterator();
			while (itr1.hasNext()) {
				Iterator<Integer> itr2 = Program2.keySet().iterator();
				k = 0;
				char a;
				int beg = 0, end = 0, c = 0;
				Integer key = itr1.next();
				s1 = Program1.get(key).toString();
				t1 = Time1.get(key);
				  Pattern pattern = Pattern.compile("(process name.*)");
				  Matcher matcher = pattern.matcher(s1);
				  System.out.println("Matches"+matcher.matches());
				  while (matcher.find()) {
				        temp=matcher.group();
				    }
				  System.out.println("matched code:"+temp);
				for (k = 0; k < temp.length(); k++) {
					a = temp.charAt(k);
					if (c != 2)
						if (a == '\\' && (temp.charAt(k + 1)) == '\"') {
							beg = k + 2;
							c = 2;
							continue;
						}
					if (a == '\\' && (temp.charAt(k + 1)) == '\"' && c == 2) {
						end = k;
						break;
					}
				
				}
				temp = temp.substring(beg,end);
				while (itr2.hasNext()) {
					Integer key1 = itr2.next();
					s2 = Program2.get(key1).toString();
					t2 = Time2.get(key1);
					if (s2.contains(temp)) {
						if (!(s1.equals(s2))) {
							System.out.println(j + ": " + temp);
							Program.put(j, s1);
							Program3.put(j, s2);
							codeDiff[j - 1] = temp;
							diffLen++;
							j++;
							break;
						}
					}
				}
				i++;
			}
		}
		Program1.clear();
		Program1.putAll(Program);
		Program2.clear();
		Program2.putAll(Program3);
		Program3.clear();
		Time1.clear();
		Time1.putAll(Time);
		Time2.clear();
		Time2.putAll(Time3);
		Time3.clear();
		System.out.println("Program difference");
		for (Map.Entry<Integer, String> entry : Program1.entrySet()) {
			System.out.println(entry.getValue());
		}
		for (Map.Entry<Integer, String> entry : Program2.entrySet()) {
			System.out.println(entry.getValue());
		}
	}
}

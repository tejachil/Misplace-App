package edu.vt.teja.ece4564.misplace;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.widget.TextView;

public class MessageTask extends AsyncTask<String, Void, String> {
	private String tagID_;
	private String message_;
	private String time_;
	private double latitude_, longitude_;
	private ProgressDialog progDialog;
	private Context context_;
	private TextView textview_;
	
	public MessageTask(Context context) {
		context_ = context;
    }
	
	public void setMessage(String id, String message, String time, double lat, double longit){
		tagID_ = id;
		message_ = message;
		time_ = time;
		latitude_ = lat;
		longitude_ = longit;
	}
	
	public void setQueryText(TextView tv){
		textview_ = tv;
	}

    @Override
    protected void onPreExecute() {
    	progDialog = new ProgressDialog(context_);
    	progDialog.setTitle("Adding new tag...");
    	progDialog.setMessage("Please wait.");
    	progDialog.setCancelable(false);
    	progDialog.setIndeterminate(true);
    	progDialog.show();

    }

    @Override
    protected String doInBackground(String... params){
    	String ret = "";
    	try {
    		if(params[0] == "add"){
		    	String android_id = Secure.getString(context_.getContentResolver(), Secure.ANDROID_ID);
		        String url = Global.HOST_URL + "tag?action=add&devid=" + android_id + "&username=" + Global.USER_.username_ + "&tagid=" + tagID_ +
		        		"&latitude=" + Long.toString((long) (latitude_*1000)) + "&longitude=" + Long.toString((long) (longitude_*1000)) + "&message=" + message_ + "&time=" + time_;
		        HttpURLConnection connection = (HttpURLConnection)(new URL(url).openConnection());
				connection.setRequestMethod("GET");
		        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.114 Safari/537.36");
		        connection.connect();
		        
		        /*TagMessage.Tag newMessage = TagMessage.Tag.newBuilder()
		        		.setLatitude(latitude_)
		        		.setLongitude(longitude_)
		        		.setTime(time_)
		        		.setMessage(message_)
		        		.build();
		
		        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		        //outputStream.writeBytes();
		        newMessage.writeTo(outputStream);
		        outputStream.flush();
		        outputStream.close();*/
		
		        String pageHTML = getHTML(connection);
		        pageHTML = pageHTML + "";
		        
		        connection.disconnect();
		        ret = "add";
    		}
		    else{
		    	String android_id = Secure.getString(context_.getContentResolver(), Secure.ANDROID_ID);
		        String url = Global.HOST_URL + "tag";
		        HttpURLConnection connection = (HttpURLConnection)(new URL(url).openConnection());
				connection.setRequestMethod("GET");
		        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.114 Safari/537.36");
		        connection.connect();
		        
		        /*TagMessage.Tag newMessage = TagMessage.Tag.newBuilder()
		        		.setLatitude(latitude_)
		        		.setLongitude(longitude_)
		        		.setTime(time_)
		        		.setMessage(message_)
		        		.build();
		
		        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		        //outputStream.writeBytes();
		        newMessage.writeTo(outputStream);
		        outputStream.flush();
		        outputStream.close();*/
		
		        String pageHTML = getHTML(connection);
		        ret = pageHTML;
		        
		        connection.disconnect();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
    	progDialog.dismiss();
    	if(result != "add"){
    		textview_.setText(result);
    	}
    }
    
    public String getHTML(HttpURLConnection connection) throws IOException {
        BufferedReader inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder htmlStringBuilder = new StringBuilder();
        String htmlBuffer;
        while((htmlBuffer = inReader.readLine()) != null){
            htmlStringBuilder.append(htmlBuffer);
        }

        return htmlStringBuilder.toString();
    }
	
}

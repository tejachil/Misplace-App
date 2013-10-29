package edu.vt.teja.ece4564.misplace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import android.content.Context;
import android.provider.Settings.Secure;
import java.net.URL;

/**
 * Created by teja on 10/27/13.
 */
public class User {
    public String username_, password_;
    private Context context_;

    public User(String user, String pass, Context cont){
        username_ = user;
        password_ = pass;
        context_ = cont;
    }

    public boolean login() throws IOException {
        String url = Global.HOST_URL + "user?action=login&username=" + username_ + "&password=" + password_;
        HttpURLConnection connection = (HttpURLConnection)(new URL(url).openConnection());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.114 Safari/537.36");
        connection.connect();
        boolean ret = getHTML(connection).contains("LOGIN SUCCESS!");
        connection.disconnect();
        return ret;
    }

    public void addUser() throws IOException {
        String android_id = Secure.getString(context_.getContentResolver(), Secure.ANDROID_ID);
        String url = Global.HOST_URL + "user?action=add&username=" + username_ + "&password=" + password_ + "&devid=" + android_id;
        HttpURLConnection connection = (HttpURLConnection)(new URL(url).openConnection());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.114 Safari/537.36");
        connection.connect();
        String pageHtml = getHTML(connection);
        pageHtml = pageHtml + "";

        connection.disconnect();
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

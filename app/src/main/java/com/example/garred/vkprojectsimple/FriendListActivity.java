package com.example.garred.vkprojectsimple;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by garred on 18.03.17.
 */

public class FriendListActivity extends AppCompatActivity {
    static String METHOD_NAME = "friends.get";
    private String USER_ID;
    private String ACCESS_TOKEN;
    private String VERSION = "5,68";
    private ArrayList<String> arrayList;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private Uri.Builder builder;
    private String requestString;
    RequestFriend requestFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Bundle data_param = getIntent().getBundleExtra("params");
        ACCESS_TOKEN = data_param.getString("token");
        USER_ID = data_param.getString("uid");
        listView = (ListView) findViewById(R.id.friendsList);
        builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.vk.com")
                .appendPath("method")
                .appendPath(METHOD_NAME)
                .appendQueryParameter("user_ids",USER_ID)
                .appendQueryParameter("access_token",ACCESS_TOKEN)
                .appendQueryParameter("fields", "nickname," +
                                                "bdate," +
                                                "city," +
                                                "online")
                .appendQueryParameter("version",VERSION);
        builder.build();
        requestString = new String();
        requestFriend = new RequestFriend();
        requestFriend.execute(builder.toString());
        try {
            requestString = requestFriend.get();
            JSONObject jsonObject = new JSONObject(requestString);
            JSONArray friendsJson = jsonObject.getJSONArray("response");
            int a = 5;
            arrayList = friendsArrayBuild(friendsJson);
            arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(arrayAdapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> friendsArrayBuild (JSONArray friendsArrayJSON) {
        ArrayList<String> newArray = new ArrayList<>();
        for (int i=0;i<friendsArrayJSON.length();i++) {
            try {
                JSONObject friend = friendsArrayJSON.getJSONObject(i);
                newArray.add(friend.getString("first_name")
                        +" "
                        +friend.getString("last_name")
                        +"\n"
                        +friend.getString("bdate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newArray;
    }
    }
class RequestFriend extends AsyncTask<String,Void,String> {
    URL urlToRequest;

    @Override
    protected String doInBackground(String... url) {
        StringBuffer response = new StringBuffer();
        try {
            urlToRequest = new URL(url[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) urlToRequest.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}

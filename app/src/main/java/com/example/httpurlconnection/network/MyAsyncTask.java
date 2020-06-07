package com.example.httpurlconnection.network;

import android.os.AsyncTask;

import com.example.httpurlconnection.callback.OnResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAsyncTask extends AsyncTask<String, Void, String> {


    private OnResponse response;
    private String result = null;
    private List<String> list;

    public MyAsyncTask(OnResponse response) {
        this.response = response;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            URLConnection connection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            result = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        response.onResponse(regexChecker("<(h\\d)\\b[^>]*>(.*?)</\\1>", result));
        response.onResponse(regexChecker("<(p)\\b[^>]*>(.*?)</\\1>", result));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private List<String> regexChecker(String regex, String target) {
        list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        while (matcher.find()) {
            if (matcher.group().length() != 0)
                list.add(matcher.group().trim());
        }
        return list;
    }
}


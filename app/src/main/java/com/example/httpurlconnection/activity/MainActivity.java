package com.example.httpurlconnection.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.httpurlconnection.network.MyAsyncTask;
import com.example.httpurlconnection.R;
import com.example.httpurlconnection.callback.OnResponse;
import com.example.httpurlconnection.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView contentView;
    private List<String> netStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentView  = findViewById(R.id.content_view);
        netStrings = new ArrayList<>();
        load();

    }

    private void load(){
        MyAsyncTask task = new MyAsyncTask(new OnResponse() {
            @Override
            public void onResponse(List<String> response) {
                for(int i=0; i<response.size(); i++){
                  netStrings.addAll(regexChecker(">.+<", response.get(i)));
                }
                for(String text : netStrings){
                    String val = text.replaceAll("[><,.]", "").replaceAll("\\s", "\n");
                contentView.append(val);
                }
                Toast.makeText(MainActivity.this, "response", Toast.LENGTH_SHORT).show();
            }
        });
        task.execute(Constants.BASE_URL);
    }

    private List<String> regexChecker(String regex, String target) {
         List<String> netStringList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        while (matcher.find()) {
            if (matcher.group().length() != 0)
                netStringList.add(matcher.group().trim());
        }
        return netStringList;
    }
}


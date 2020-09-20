package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private String[] mountainName={"Matterhorn","Mont Blanc","Denali"};
    private String[] locationName={"Alps","Alps","Alaska"};
    private int[] mountainHeight={4478,4808,6190};

    private ArrayList<String> listData=new ArrayList<>(Arrays.asList(mountainName));
    private ArrayList<Mountain> mountainArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

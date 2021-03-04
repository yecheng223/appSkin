package com.cy.skin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cy.mylibrary.SkinManager;

import java.io.File;

public class MainActivity extends AppCompatActivity  {

    private Button change_skin,reset,skip;
    private String skinPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        skinPath=getExternalFilesDir("mySkin").getAbsolutePath()+ File.separator+"myskinplugin-debug.apk";
        change_skin=findViewById(R.id.change_skin);
        reset=findViewById(R.id.reset);
        change_skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().loadSkin(skinPath);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().loadSkin("");
            }
        });
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
    }


}

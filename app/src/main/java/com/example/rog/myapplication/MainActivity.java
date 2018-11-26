package com.example.rog.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button rsa,ZZ,SDe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rsa= (Button)findViewById(R.id.RSA);
        ZZ =(Button)findViewById(R.id.max);
        SDe =(Button)findViewById(R.id.LZW);


    }
    public void IrZig(View view){
        Intent intent = new Intent(this, ZigZag.class);
        startActivity(intent);
    }
    public void IrSDES(View view){
        Intent intent = new Intent(this, LZW.class);
        startActivity(intent);
    }
    public void IrRSA(View view){
        Intent intent = new Intent(this, RSA.class);
        startActivity(intent);
    }
}

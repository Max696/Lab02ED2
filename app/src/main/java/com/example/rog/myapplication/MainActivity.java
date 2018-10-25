package com.example.rog.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button rsa,ZigZag,LZW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rsa= (Button)findViewById(R.id.RSA);
        rsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana = new Intent(MainActivity.this,RSA.class);
                startActivity(ventana);
            }
        });
        LZW= (Button)findViewById(R.id.LZW);
        ZigZag = (Button) findViewById(R.id.Zig_Zag);
    }
    public void sendMessage(View view) {
        // Do something in response to button
    }
}

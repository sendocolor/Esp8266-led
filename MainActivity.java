package com.example.espcontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final String espIP = "http://192.168.4.1"; // ESP8266 IP
    private TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResponse = findViewById(R.id.txtResponse);

        Button btnOn = findViewById(R.id.btnOn);
        Button btnOff = findViewById(R.id.btnOff);

        btnOn.setOnClickListener(view -> sendCommand("/on"));
        btnOff.setOnClickListener(view -> sendCommand("/off"));
    }

    private void sendCommand(String path) {
        new Thread(() -> {
            try {
                URL url = new URL(espIP + path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                final String response = new String(buffer);
                runOnUiThread(() -> txtResponse.setText(response));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> txtResponse.setText("Error: " + e.getMessage()));
            }
        }).start();
    }
}
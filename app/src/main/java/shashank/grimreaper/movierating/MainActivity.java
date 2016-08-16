package shashank.grimreaper.movierating;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.URL;

import shashank.grimreaper.movierating.R;

public class MainActivity extends AppCompatActivity {

    EditText et1;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = (EditText)findViewById(R.id.etname);
        b1 = (Button)findViewById(R.id.bsubmit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Details.class);
                String temp = et1.getText().toString();
                i.putExtra("name",temp);
                startActivity(i);
            }
        });
    }
}

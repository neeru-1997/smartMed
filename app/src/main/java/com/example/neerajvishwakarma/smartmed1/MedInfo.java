package com.example.neerajvishwakarma.smartmed1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MedInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_info);

        TextView result = (TextView) findViewById(R.id.medname);
        result.setText(getIntent().getStringExtra("name"));

    }
}

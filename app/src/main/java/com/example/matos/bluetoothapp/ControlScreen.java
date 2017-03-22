package com.example.matos.bluetoothapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ControlScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);

        for(int i=0; i<2; i++){
            LinearLayout l = (LinearLayout) findViewById(R.id.ll);
            Button btn = new Button(this);
            btn.setOnClickListener(getOnClickDoSomething(btn));
            btn.setText("btn "+i);
            l.addView(btn);

        }


    }

    View.OnClickListener getOnClickDoSomething(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                button.setText("text now set.. ");
            }
        };
    }
}

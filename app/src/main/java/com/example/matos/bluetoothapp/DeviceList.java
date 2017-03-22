package com.example.matos.bluetoothapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.Set;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class DeviceList extends AppCompatActivity {

    public ListView list;
    public ArrayList<String> names;
    public ArrayList<String> adresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);



        //list = (ListView) findViewById(R.id.Listview);
        final Button Connect = (Button) findViewById(R.id.Connect);

        Connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
             startBluetooth();
            }
        });


    }


    public void startBluetooth(){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            System.out.println("Device does not support Bluetooth");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        if(mBluetoothAdapter.isEnabled()){
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            names = new ArrayList<>();
            adresses = new ArrayList<>();


            for(BluetoothDevice bt : pairedDevices) {
                names.add(bt.getName());
                adresses.add(bt.getAddress());
            }

            int count = 0;
            for(String name : names){
                LinearLayout l = (LinearLayout) findViewById(R.id.list);
                Button btn = new Button(this);

                btn.setOnClickListener(getOnClickDoSomething(btn));
                btn.setText(""+name);
                btn.setId(count);

                l.addView(btn);

                count++;

            }

        }

    }

    View.OnClickListener getOnClickDoSomething(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                //button.setText("text now set.. ");
                int id = button.getId();
                System.out.println(names.get(id) + " has adress: " + adresses.get(id));

                boolean succes = false;

                if(succes){

                    Intent ControlScreen = new Intent(DeviceList.this, ControlScreen.class);
                    DeviceList.this.startActivity(ControlScreen);


                }else{
                    Toast.makeText(DeviceList.this, "Connection failed", Toast.LENGTH_LONG).show();
                }

            }
        };
    }




    private class ConnectBT extends AsyncTask<Void, Void, Void>{

        private boolean ConnectSuccess = true;

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }



}

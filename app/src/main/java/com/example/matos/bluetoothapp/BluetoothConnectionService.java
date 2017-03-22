package com.example.matos.bluetoothapp;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by schmi on 22-03-2017.
 */

public class BluetoothConnectionService {

    private static final String TAG = "BluetoothConnectionServ";
    private static final String appName = "BluetoothApp";
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce25c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    public BluetoothConnectionService(Context context){
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName,MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread IOException: " + e.getMessage());
            }

            mmServerSocket = tmp;

        }

        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try{
                Log.d(TAG, "run: RFCOM server socket start.....");
                socket = mmServerSocket.accept();
                Log.d(TAG, "run: RFCOM server socket accepted connection");
            }catch(IOException e){
                Log.e(TAG, "AcceptThread IOException: " + e.getMessage());
            }

            if(socket != null){
                connected(socket, mmDevice);
            }

            Log.i(TAG, "End mAcceptThread");

        }

        public void cancel(){
            Log.d(TAG, "cancel: Cencelling AcceptThread.");

            try{
                mmServerSocket.close();
            }catch(IOException e){
                Log.e(TAG, "cencel: Close of AcceptThread ServerSocket failed: " + e.getMessage());
            }

        }
    }

}

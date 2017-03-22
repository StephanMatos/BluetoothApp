package com.example.matos.bluetoothapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;


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


    private class ConnectThread extends Thread{

        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG, "ConnectThread: started.");

            mmDevice = device;
            deviceUUID = uuid;

        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread");

            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRFCOMMSocket using UUID");
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRFCOMMSocket " + e.getMessage());
                e.printStackTrace();
            }

            mmSocket = tmp;

            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                Log.d(TAG,"run: ConnectThread connected.");
            } catch (IOException e) {
                try{
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket");
                }catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID " + MY_UUID_INSECURE);
            }

            connected(mmSocket, mmDevice);

        }

        public void cancel(){

            try{
                Log.d(TAG, "cancel: Closing client socket.");
                mmSocket.close();
            }catch(IOException e){
                Log.e(TAG, "cencel: Close of mmSocket in ConnectThread failed: " + e.getMessage());
            }
        }
    }

    public synchronized void start(){
        Log.d(TAG, "Start");

        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mInsecureAcceptThread == null){
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "StartClient: started.");

        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth", "Please Wait...", true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();

    }

}

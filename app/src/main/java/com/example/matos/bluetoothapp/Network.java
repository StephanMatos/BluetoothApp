package com.example.matos.bluetoothapp;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Matos on 22-03-2017.
 */

public class Network {


    private static Network instance = null;
    private static BluetoothServerSocket bServerSocket = null;
    private static BluetoothSocket bSocket = null;
    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;

    public static Network getInstance(){
        if(instance == null){
            instance = new Network();
        }
        return(instance);

    }




}

package com.emredmrbs99.smartpark;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:21:13:00:93:8D";
    Handler bluetoothIn;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;
    private RelativeLayout backGround;
    private ImageButton place1;
    private ImageButton place2;
    private ImageButton place3;
    private String readMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();




        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.checkBTState();

        backGround= (RelativeLayout) findViewById(R.id.place_background);
        place1= (ImageButton) findViewById(R.id.place1);
        place1.setOnClickListener(this);
        place2= (ImageButton) findViewById(R.id.place2);
        place2.setOnClickListener(this);
        place3= (ImageButton) findViewById(R.id.place3);
        place3.setOnClickListener(this);


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    Log.e("meh",readMessage);
                    //Toast.makeText(MainActivity.this, ""+readMessage, Toast.LENGTH_SHORT).show();
                    if (!readMessage.isEmpty())
                    switch (String.valueOf(readMessage.trim())){
                        //sensör 1 için
                        case "2"://boş
                            place1.setBackgroundResource(R.drawable.bos);
                            break;
                        case "3"://dolu
                            place1.setBackgroundResource(R.drawable.araba);
                            //Toast.makeText(MainActivity.this, "geldi", Toast.LENGTH_SHORT).show();
                            break;
                        //sensör 2 için
                        case "6":
                            place2.setBackgroundResource(R.drawable.bos);

                            break;
                        case "7":
                            Log.e("meh","44444444444444444444444444444444444444");
                            place2.setBackgroundResource(R.drawable.araba);
                            break;
                        //sensör 3 için
                        case "4":
                            place3.setBackgroundResource(R.drawable.bos);
                            break;
                        case "5":
                            place3.setBackgroundResource(R.drawable.araba);
                            break;
                        default:
                            break;

                    }

                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string

                        int dataLength = dataInPrint.length();                          //get length of data received


                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                        {
                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(6, 10);            //same again...
                            String sensor2 = recDataString.substring(11, 15);
                            String sensor3 = recDataString.substring(16, 20);


                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }


        };

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.place1:
                parkalani1();
                break;
            case R.id.place2:
                parkalani2();
                break;
            case R.id.place3:
                parkalani3();
                break;
            default:

        }

    }

    private void parkalani3() {
        backGround.setBackgroundResource(R.drawable.park);
        backGround.setBackgroundResource(R.drawable.park3);

    }

    private void parkalani2() {
        backGround.setBackgroundResource(R.drawable.park);
        backGround.setBackgroundResource(R.drawable.park2);
    }


    private void parkalani1() {
        backGround.setBackgroundResource(R.drawable.park);
        backGround.setBackgroundResource(R.drawable.park1);
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {//////////////
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                Method e = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) e.invoke(device, new Object[]{MY_UUID});
            } catch (Exception var3) {
                Log.e("bluetooth1", "Could not create Insecure RFComm Connection", var3);
            }
        }

        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }


    public void onResume() {////////////////////////////
        super.onResume();
        Log.d("bluetooth1", "...onResume - try connect...");
        BluetoothDevice device = this.btAdapter.getRemoteDevice(address);

        try {
            this.btSocket = this.createBluetoothSocket(device);
        } catch (IOException var7) {
            this.errorExit("Fatal Error", "In onResume() and socket create failed: " + var7.getMessage() + ".");
        }

        this.btAdapter.cancelDiscovery();
        Log.d("bluetooth1", "...Connecting...");

        try {
            this.btSocket.connect();
            Log.d("bluetooth1", "...Connection ok...");
        } catch (IOException var6) {
            try {
                this.btSocket.close();
            } catch (IOException var5) {
                this.errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + var5.getMessage() + ".");
            }
        }

        Log.d("bluetooth1", "...Create Socket...");

        try {
            this.outStream = this.btSocket.getOutputStream();
        } catch (IOException var4) {
            this.errorExit("Fatal Error", "In onResume() and output stream creation failed:" + var4.getMessage() + ".");
        }

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

    }

    public void onPause() {/////////////////////
        super.onPause();
        Log.d("bluetooth1", "...In onPause()...");
        if (this.outStream != null) {
            try {
                this.outStream.flush();
            } catch (IOException var3) {
                this.errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + var3.getMessage() + ".");
            }
        }

        try {
            this.btSocket.close();
        } catch (IOException var2) {
            this.errorExit("Fatal Error", "In onPause() and failed to close socket." + var2.getMessage() + ".");
        }

    }

    private void checkBTState() {///////////////////////////
        try {
            if (this.btAdapter == null) {
                this.errorExit("Fatal Error", "Bluetooth not support");
            } else if (this.btAdapter.isEnabled()) {
                Log.d("bluetooth1", "...Bluetooth ON...");
                Toast.makeText(MainActivity.this, "Bluetooth'a bağlanıldı", Toast.LENGTH_SHORT).show();
            } else {
                Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.startActivityForResult(enableBtIntent, 1);
                Toast.makeText(MainActivity.this, "Bluetooth'u açma izni verin", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }

    }

    private void errorExit(String title, String message) {/////////////////////////
        Toast.makeText(this.getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void sendData(String message) {

        byte[] msgBuffer = message.getBytes();
        Log.d("bluetooth1", "...Send data: " + message + "...");

        try {
            this.outStream.write(msgBuffer);
            outStream.flush();

        } catch (IOException var5) {
            String msg = "In onResume() and an exception occurred during write: " + var5.getMessage();
            if (address.equals("00:00:00:00:00:00")) {
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            }

            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
            this.errorExit("Fatal Error", msg);
        }

    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    readMessage = new String(buffer, 0, bytes);
                   // Log.d("MEHMET",""+readMessage);

                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }



        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }

    @Override
    public void onBackPressed() {
        backGround.setBackgroundResource(R.drawable.park);
    }
}


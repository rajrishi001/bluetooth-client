package com.example.rishiraaz.bluetooth2;

import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup ;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.IntentFilter;
import android.view.View.OnClickListener;

import android.os.Handler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
//import java.util.logging.Handler;
import java.lang.Object;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
import android.os.Message;

public class bluetooth2 extends AppCompatActivity {
    private static final String Tag = "bluetooth";
    BluetoothAdapter mBluetoothAdapter;
    int Bluetooth_REQUEST = 1;
    UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private  BluetoothDevice mmDevice,mdevice;
    private String MY_MAC_ADDR="40:88:05:28:5C:7E";
   // private int mstate;
    //private final int state_connected=4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth2);
        Button btonoff = (Button) findViewById(R.id.btonoff);
        final TextView rssi = (TextView) findViewById(R.id.rssi);
        //registerReceiver(receiver, new IntentFilter(mmDevice.ACTION_FOUND));

        btonoff.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                           if (mBluetoothAdapter == null) {
                                               Toast.makeText(getBaseContext(), "No Bluetooth found", Toast.LENGTH_LONG).show();
                                           } else {
                                               if (!mBluetoothAdapter.isEnabled()) {
                                                   Intent enablebtintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                                   startActivityForResult(enablebtintent, Bluetooth_REQUEST);
                                               }
                                           }

                                           /*Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                                           discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 400);
                                           startActivity(discoverableIntent);*/
                                           //       Toast.makeText(getApplicationContext(), "Now your device is discoverable by others", Toast.LENGTH_LONG).show();
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

               if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                       String deviceHardwareAddress = device.getAddress(); // MAC address
                        Log.v("device1", "Device name: " + deviceName + " device mac: " + deviceHardwareAddress);
                        if (MY_MAC_ADDR.equals(device.getAddress())) {
                            mdevice = device;
                            break;
                        }
                  }
               }
//                for(int i=0; i<2; i++){
//                   Log.v("BLuetoot","In");
//                    mBluetoothAdapter.startDiscovery();
//                    SystemClock.sleep(13000);}
                /*class bluetoothcheck extends Thread{
                    public void run()
                    {
                        while(true)
                        {
//                            rssi.getText().clear();
                            mBluetoothAdapter.startDiscovery();
                            SystemClock.sleep(3000);
                        }

                    }
                };

                bluetoothcheck btl = new bluetoothcheck();
                btl.start();*/
                                          // mBluetoothAdapter.startDiscovery();
                                           //mstate=1;
                                           ConnectThread bt1=new ConnectThread(mdevice);
                                           Log.v("raj", "wow");
                                                   bt1.start();
                                                   Log.v("raj", "wow5");
                                       }
                                   }

        );

    }

    public void onActivityResult(int rq_code, int rs_code, Intent data) {
        if (rq_code == Bluetooth_REQUEST) {
            if (rs_code == RESULT_OK) {
                Toast.makeText(getBaseContext(), "Ble is on", Toast.LENGTH_LONG).show();

            } else if (rs_code == RESULT_CANCELED) {
                Toast.makeText(getBaseContext(), "Ble can't be on", Toast.LENGTH_LONG).show();
            }
        }

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bluetooth2, menu);
        return true;
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("AAAAAAAAA","Function called");
            String action = intent.getAction();

            if(mmDevice.ACTION_FOUND.equals(action)) {
                int rssi1 = intent.getShortExtra(mmDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String name = intent.getStringExtra(mmDevice.EXTRA_NAME);

                TextView rssi = (TextView) findViewById(R.id.rssi);
                    Log.v(name, "Input stream was ");

                    ///mdevice=mmDevice;
                   // ConnectThread bt2=new ConnectThread(mdevice);
                  // bt2.start();

                rssi.setText(rssi.getText() + name + " => " + rssi1 + "dBm\n");}

        }
    };*/

    //private  BluetoothDevice mmDevice;
    //UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");

    //ConnectThread bt2=new ConnectThread(mdevice);
    // bt2.start();
    /*private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
// because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("asus", uuid);
            } catch (IOException e) {
                Log.e("raj", "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }


        public void run() {
            socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e("raj", "Socket's accept() method failed", e);
                    break;
                }
                if (socket != null) {
                    // A connection was accepted. Perform work associated with.
                    // the connection in a separate thread.
                    // manageMyConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e("raj", "Could not close the connect socket", e);
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("raj", "Could not close the connect socket", e);
            }

        }
    }*/
    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final BluetoothDevice mmDevice1;
 public ConnectThread(BluetoothDevice device) {
// Use a temporary object that is later assigned to mmSocket
 // because mmSocket is final.

 BluetoothSocket tmp = null;
 mmDevice1 = device;

 try {
 // Get a BluetoothSocket to connect with the given BluetoothDevice.
// MY_UUID is the app's UUID string, also used in the server code.
 tmp = device.createRfcommSocketToServiceRecord(uuid);
     Log.v("raj","wowq1");
 } catch (IOException e) {
 Log.e("raj", "Socket's create() method failed", e);
 }
 socket = tmp;
}

 public void run() {
 // Cancel discovery because it otherwise slows down the connection.
         mBluetoothAdapter.cancelDiscovery();
         Log.v("raj", "wow8");

         try {
             Log.v("raj", "wow9");
             // Connect to the remote device through the socket. This call blocks
             // until it succeeds or throws an exception.
             socket.connect();

             Log.v("raj", "wow0");


         } catch (IOException connectException) {
             // Unable to connect; close the socket and return.
             try {
                 socket.close();

             } catch (IOException closeException) {
                 Log.e("raj", "Could not close the client socket", closeException);
             }

           return;
         }


 // The connection attempt succeeded. Perform work associated with
 // the connection in a separate thread.
// manageMyConnectedSocket(mmSocket);
     Handler handler=new Handler(Looper.getMainLooper());
     handler.post(new Runnable() {
         @Override
         public void run() {
             Toast.makeText(getBaseContext(), "Ble connected", Toast.LENGTH_LONG).show();
         }
     });
    ConnectedThread1 raw=new ConnectedThread1(socket);
     raw.start();
     Log.v("raj", "Input stream was disconnected");
 }

 // Closes the client socket and causes the thread to finish.
         public void cancel() {
 try {
socket.close();
 } catch (IOException e) {
 Log.e("raj", "Could not close the client socket", e);
 }
 }
    }



    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MessageConstants.MESSAGE_READ:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String readMessage = new String(readBuffer, 0, msg.arg1);
                    //arrayAdapterMsg.add(btDevice.getName() + ": " + readMessage);
                    //aryLstMsg.add(btDevice.getName() + ": " + readMessage);
                    Toast.makeText(getBaseContext(), readMessage, Toast.LENGTH_LONG).show();
                    Log.v("raj", readMessage);
                    //arrayAdapterMsg.notifyDataSetChanged();

                    break;

                case MessageConstants.MESSAGE_WRITE:
                    byte[] writeBuffer = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuffer);
                    //arrayAdapterMsg.add("Me: " + "test");
                    //aryLstMsg.add("Me: " + writeMessage);
                    Toast.makeText(getBaseContext(), writeMessage, Toast.LENGTH_LONG).show();
                    //arrayAdapterMsg.notifyDataSetChanged();

                    break;
            }
        }
    };
    private class ConnectedThread1 extends Thread {
        //private Handler mHandler;
        private final InputStream mmInStream;
        //private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread1(BluetoothSocket socket) {
//mmSocket = socket;
            InputStream tmpIn = null;
//OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
// member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e("raj", "Error occurred when creating input stream", e);
            }
 /*try {
 tmpOut = socket.getOutputStream();
 } catch (IOException e) {
 Log.e(TAG, "Error occurred when creating output stream", e);
 }*/

          mmInStream = tmpIn;
// mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = mHandler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1, mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d("raj", "Input stream was disconnected", e);
                    break;
                }
            }
        }

    }

    private class ConnectedThread2 extends Thread {
        //private Handler mHandler;
        //private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread2(BluetoothSocket socket) {
//mmSocket = socket;
            //InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
// member streams are final.
            /*try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e("raj", "Error occurred when creating input stream", e);
            }*/
           try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("raj", "Error occurred when creating output stream", e);
            }

            //mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                // Share the sent message with the UI activity.
                Message writtenMsg = mHandler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, bytes);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e("raj", "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg = mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast", "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                mHandler.sendMessage(writeErrorMsg);
            }
        }

    }
}

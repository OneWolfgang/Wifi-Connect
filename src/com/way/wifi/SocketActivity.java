package com.way.wifi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
  
public class SocketActivity extends Activity implements Runnable {  
    private TextView tv_msg = null;  
    private EditText ed_msg = null;  
    private Button btn_send = null;  
	//private Button btn_login = null;  
    //private static final String HOST = "192.168.1.223";  
    private static final int PORT = 9999;
    private Socket socket = null;  
    private BufferedReader in = null;  
    private PrintWriter out = null;  
    private String content = "";  
    private String IPAddress;
  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.socket_activity);
        Intent intent = getIntent();
        IPAddress = intent.getStringExtra("targetIP");
        tv_msg = (TextView) findViewById(R.id.TextView);  
        ed_msg = (EditText) findViewById(R.id.EditText01);  
        //btn_login = (Button) findViewById(R.id.Button01);  
        btn_send = (Button) findViewById(R.id.Button02);  

        try {  
        	System.out.println("socket 49");	//fail
            socket = new Socket(IPAddress, PORT);
            System.out.println("socket 51");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(  
                    socket.getOutputStream())), true);  
        } catch (IOException ex) {  
            ex.printStackTrace();  
            ShowDialog("login exception" + ex.getMessage());
        }
        
        btn_send.setOnClickListener(new Button.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                String msg = ed_msg.getText().toString();  
                if (socket.isConnected()) {  
                    if (!socket.isOutputShutdown()) {  
                        out.println(msg);  
                    }  
                }  
            }  
        });  
        new Thread(SocketActivity.this).start();  
    }  
  
    public void ShowDialog(String msg) {  
        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)  
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                        // TODO Auto-generated method stub  
  
                    }  
                }).show();  
    }  
  
    public void run() {  
        try {  
            while (true) {  
                if (socket.isConnected()) {  
                    if (!socket.isInputShutdown()) {  
                        if ((content = in.readLine()) != null) {  
                            content += "\n";  
                            mHandler.sendMessage(mHandler.obtainMessage());  
                        } else {  
  
                        }  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public Handler mHandler = new Handler() {  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            tv_msg.setText(tv_msg.getText().toString() + content);  
        }  
    };  
}  
package com.way.wifi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ClientSocketActivity extends Activity{
	private static int PORT = 5000;
	private String IPAddress;
	private Socket socket = null;
	private EditText ed_msg = null;
    private Button btn_send = null;
    private TextView tv_msg = null;
    String send = null;
    int flag = 0;
    BufferedReader br;
    PrintWriter out;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientsocket_activity);
		Intent intent = getIntent();
		IPAddress = intent.getStringExtra("targetIP");
		tv_msg = (TextView) findViewById(R.id.TextView01);
		ed_msg = (EditText) findViewById(R.id.EditText01);
		btn_send = (Button) findViewById(R.id.Button02);
		
		btn_send.setOnClickListener(new Button.OnClickListener() {  
			  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
            	send = ed_msg.getText().toString();
            	System.out.println(send);
                flag = 1; 
            }  
        });
		
		new Thread(new ClientOut()).start();
	}
	
	class ClientOut implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				socket = new Socket(IPAddress, PORT);
				out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				new Thread(new ClientIn()).start();
				out.println("from client\n");
				out.flush();
				
				while(true){
					//System.out.println("client lin 68");
					if(flag == 1){
						out.println(send);
						out.flush();
						flag = 0;
					}
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	class ClientIn implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				
				while (true) {
                    String strContent;
                    if ((strContent = br.readLine()) != null ) {
                        Message message = new Message();
                        message.obj = strContent;
                        myhandler.sendMessage(message);
                    }
                }
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
    
	Handler myhandler=new Handler(){
    	public void handleMessage(Message msg){
    		System.out.println("---->"+msg.obj);
    		tv_msg.setText(msg.obj.toString());
    		}
    	};
    
}
package com.way.wifi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ServerSocketActivity extends Activity{
	private static int PORT = 5000;
	private ServerSocket server = null;
	private EditText ed_msg = null;
    private Button btn_send = null;
	private TextView tv_msg = null;
	private PrintWriter out = null;
	private BufferedReader br = null;
	Socket client;
	String send = null;
	int flag = 0;
	String filename;
	
	/*private List<Socket> mList = new ArrayList<Socket>();
	private ExecutorService mExecutorService = null; //thread pool  */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serversocket_activity);
		tv_msg = (TextView) findViewById(R.id.TextView);
		tv_msg.setText("I'm server");
		ed_msg = (EditText) findViewById(R.id.EditText);
		btn_send = (Button) findViewById(R.id.Button);
		
		btn_send.setOnClickListener(new Button.OnClickListener() {  
			  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
            	send = ed_msg.getText().toString();
            	ed_msg.setText("content");
            	System.out.println(send);
                flag = 1; 
            }  
        });
		
		new Thread(new ServerIn()).start();
    }
	

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
    	menu.add(0,1,1,R.string.sendfile);
    	menu.add(0,2,2,R.string.sendsound);
    	menu.add(0,3,3,R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}
    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case 1:
			final EditText input = new EditText(this);
			AlertDialog.Builder alertbBuilder=new AlertDialog.Builder(this);
			alertbBuilder.setTitle(R.string.sendfile).setIcon(android.R.drawable.ic_dialog_info).setView(input).
			setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					filename = input.getText().toString();
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
						File sendfile = new File(Environment.getExternalStorageDirectory(), filename);
						if(sendfile.exists()){
	                        sendFile(sendfile);
						}else{
	                        Toast.makeText(ServerSocketActivity.this, R.string.filenotexsit, 1).show();
	                    }
	                }else{
	                    Toast.makeText(ServerSocketActivity.this, R.string.sdcarderror, 1).show();
	                }
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
			    public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}).create().show();
			break;
		case 2:
			Toast.makeText(ServerSocketActivity.this, R.string.functiondeveloping, 1).show();
			break;
		case 3:
			try{
				client.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	void sendFile(final File sendfile){
		
	}


	class ServerIn implements Runnable {

    	
        @Override
        public void run() {

            try {
                server = new ServerSocket(PORT);
                client = server.accept();
                Message test = new Message();
                test.obj = client.getInetAddress().toString();
                myhandler.sendMessage(test);
                System.out.println(test.obj);
                
                //br = new BufferedReader(new InputStreamReader(s.getInputStream() , "utf-8"));
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(client.getOutputStream())),true);
                new Thread(new ServerOut()).start();
                
                while (true) {
                    String strContent;
                    if ((strContent = br.readLine()) != null ) {
                        Message message = new Message();
                        message.obj = strContent;
                        myhandler.sendMessage(message);
                    }
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    class ServerOut implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				out.println("from client\n");
				out.flush();
				
				while(true){
					if(flag == 1){
						out.println(send);
						out.flush();
						flag = 0;
					}
				}
			}catch(Exception e){
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
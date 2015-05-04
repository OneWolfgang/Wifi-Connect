package com.way.wifi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EmptyActivity extends Activity {

	private TextView myTextView0 = null;
	private TextView myTextView1 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty_activity);
		myTextView0 = (TextView)findViewById(R.id.textView0);
		myTextView1 = (TextView)findViewById(R.id.textView1);
		//myTextView.setText(R.string.no_connection);
		
		Intent intent = getIntent();
		String str = intent.getStringExtra("flag");
		int flag = Integer.parseInt(str);
		
		if(flag == 0){
			myTextView0.setText(flag+"");
			myTextView1.setText("asdf");
		}
		if(flag == 1) {
			String IPAddr = intent.getStringExtra("targetIP");
			myTextView0.setText(IPAddr);
			myTextView1.setText("1234");
		}
		
	}

}
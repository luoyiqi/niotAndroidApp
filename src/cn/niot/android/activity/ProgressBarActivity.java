package cn.niot.android.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.niot.android.service.ProcessResultReceiver;
import cn.niot.android.service.SendHttpRequestService;
import cn.niot.android.utility.ConstantUtil;

public class ProgressBarActivity extends Activity {
	
	
	private ProcessResultReceiver processResultReceiver = null;


	private TextView showInfoBar=null;
	private ProgressBar bar=null;
	
	Handler handler = null;
	TimerTask task = null;
	Timer timer = null;  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		System.out.println("ProgressBarActivity onCreate!");
		
		setContentView(R.layout.activity_progress);
		
		//ProgressBarAsyncTask asyncTask=new ProgressBarAsyncTask(showInfoBar, bar, ProgressBarActivity.this, context2)
		showInfoBar=(TextView)findViewById(R.id.showInfoBar);
		bar=(ProgressBar)findViewById(R.id.bar);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("ProgressBarActivity onResume!");
		//获取从上一个activity传来的intent（其中封存着要查询的编码），将该intent
		//发送给SendHttpRequestService，以启动向后台服务器的查询。
		Intent codeToSend = getIntent();
		codeToSend.setClass(ProgressBarActivity.this,SendHttpRequestService.class);
		startService(codeToSend);
		
		
		// 动态注册broadcastReceiver用来接收从SendHttpRequestService发来的数据
		IntentFilter intentFilterForResult = new IntentFilter();
		intentFilterForResult.addAction(ConstantUtil.ACTION_PROCESS_HTTPRESULT);
		processResultReceiver = new ProcessResultReceiver();
		registerReceiver(processResultReceiver, intentFilterForResult);
		
		
		//在定时器到达规定时间时，规定的handler来处理；
	    handler = new Handler(){
	        public void handleMessage(Message msg) {  
	            switch (msg.arg1) {      
	            case 1:      
	            	//首先判断是否结果已经被返回
	            	if(!processResultReceiver.isIfResultReceived())
	            	{
		            	System.out.println("time out");
		            	Toast.makeText(ProgressBarActivity.this, "服务器响应超时，请您重试", Toast.LENGTH_SHORT).show();
		            	
		            	//注销监听器
		            	unregisterReceiver(processResultReceiver);
		            	//返回主界面
		            	startActivity(new Intent(ProgressBarActivity.this,MainActivity.class));
	            	}
	            	else
	            	{
	            		unregisterReceiver(processResultReceiver);
	            	}
	            	break;      
	            }      
	            super.handleMessage(msg);  
	        }
	    };  
		
	    //让定时器在设置的时间到达时，发送message给handler处理
	    task = new TimerTask(){
	        public void run() {  
	            Message message = new Message();      
	            message.arg1 = 1;      
	            handler.sendMessage(message);    
	        }  
	    };  
		
	    timer = new Timer();  
		timer.schedule(task,ConstantUtil.DEFAULT_WAITING_TIME);
	}
	

	//此函数用来定义当用户点击“返回”按钮时，将返回到上一级页面，同时注销掉broadcastReceiver
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			System.out.println("KEYCODE_BACK pressed");
	        if(keyCode==KeyEvent.KEYCODE_BACK)  
	        {  
	        	System.out.println("KEYCODE_BACK pressed");
	            //do whatever you want the 'Back' button to do  
	            //as an example the 'Back' button is set to start a new Activity named 'NewActivity'  
	        	timer.cancel();
	        	unregisterReceiver(processResultReceiver);
	        	return super.onKeyDown(keyCode, event);
	        } 
	        else
			{
	        	return super.onKeyDown(keyCode, event);
			}
		}
	
	

}

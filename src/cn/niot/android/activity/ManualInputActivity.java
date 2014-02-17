package cn.niot.android.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.niot.android.main.R;
import cn.niot.android.service.SendHttpRequestService;
import cn.niot.android.utility.ConstantUtil;

@SuppressLint("NewApi")
public class ManualInputActivity extends Activity {
	
	
	private EditText edtTxtManualInput = null;
	private Button btnRequestCodeInfo = null;
	private ProcessResultReceiver processResultReceiver = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_input);
		//显示action bar上最左侧的回退按钮
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    edtTxtManualInput = (EditText) findViewById(R.id.editTextManualInput);
	    btnRequestCodeInfo = (Button) findViewById(R.id.buttonRequestCodeInfo);
	    
	    btnRequestCodeInfo.setOnClickListener(new btnRequestCodeInfoClickListener());


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//为“选项菜单”添加两个子项，一个为“设置”，另一个为“退出”
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		//当点击“设置”时，去设置页
		if(item.getItemId() == R.id.action_settings){
			Intent intentToSettingActivity= new Intent();
			intentToSettingActivity.setClass(this, SettingActivity.class);
			startActivity(intentToSettingActivity);
		}
		
		//当点击“退出”时，退出app  !!!尚有问题
		if(item.getItemId() == R.id.action_exit){

			System.exit(0);
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);

	}
	
	
	//启动一个新的activity用来画饼图
	protected void drawPieChart(String data)
	{
		Intent intentDrawPie = new Intent();
		intentDrawPie.setClass(ManualInputActivity.this, DrawActivity.class);
		intentDrawPie.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentDrawPie.putExtra("codeData", data);
		startActivity(intentDrawPie);
	}
	
	
	class btnRequestCodeInfoClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//获得用户输入的编码
			String requestCode = edtTxtManualInput.getText().toString();
			System.out.println(requestCode);
			
			//将该编码发送给SendHttpRequestService，以启动向后台服务器的查询。
			Intent codeToSend = new Intent();
			codeToSend.putExtra("requestCode", requestCode);
			codeToSend.setClass(ManualInputActivity.this, SendHttpRequestService.class);
			startService(codeToSend);
			
			//动态注册broadcastReceiver用来接收从SendHttpRequestService发来的数据
			IntentFilter intentFilterForResult = new IntentFilter();
			intentFilterForResult.addAction(ConstantUtil.ACTION_PROCESS_HTTPRESULT);
			processResultReceiver = new ProcessResultReceiver();
			registerReceiver(processResultReceiver, intentFilterForResult);
			
			
		}
		
	}
	
	//接收SendHttpRequestService发回的查询结果
	class ProcessResultReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("=============>");
			//从SendHttpRequestService中获得服务器传回的数据，前4位为null，因此需要从第5位开始取值。
			String result = intent.getExtras().getString("result").substring(4);
			System.out.println(result);
		
			
			//首先要判断result中status的值，当status大于2的时候才需要画饼图。
			//当status=0时，说明用户输入的编码不属于后台数据库中的任何编码类型
			//当status=1时，说明用户输入的编码与后台数据库中的一种编码规则吻合
			//当status="error"时，说明有错误出现
			
			//如果status>1，则调用drawPieChart呈现饼图;
			drawPieChart("test");
			
			//注销
			unregisterReceiver(processResultReceiver);
		}
		
	}
	

}

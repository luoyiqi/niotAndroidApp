package cn.niot.android.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.niot.android.activity.R;
import cn.niot.android.service.ProcessResultReceiver;
import cn.niot.android.service.SendHttpRequestService;
import cn.niot.android.utility.ConstantUtil;

@SuppressLint("NewApi")
public class ManualInputActivity extends Activity {

	
	private EditText edtTxtManualInput = null;
	private Button btnRequestCodeInfo = null;
	private ProcessResultReceiver processResultReceiver = null;
	private TextView showInfo = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_input);
		// 显示action bar上最左侧的回退按钮
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		edtTxtManualInput = (EditText) findViewById(R.id.editTextManualInput);
		btnRequestCodeInfo = (Button) findViewById(R.id.buttonRequestCodeInfo);
		showInfo = (TextView) findViewById(R.id.showInfo);// 显示提示信息
		btnRequestCodeInfo.setOnClickListener(new btnRequestCodeInfoClickListener());

	}

	

	class btnRequestCodeInfoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// 获得用户输入的编码
			String requestCode = edtTxtManualInput.getText().toString();
			
			//？？？添加判断requestCodes是否为空的语句，如果为空，则toast信息提示用户输入编码，
			//？？？如果不为空，则启动查询

			// 将该编码发送给SendHttpRequestService，以启动向后台服务器的查询。
			Intent codeToSend = new Intent();
			codeToSend.putExtra("requestCode", requestCode);
			

			//zt注释掉，将发送httprequest查询的操作挪到ProgressBarActivity去进行
//			codeToSend.setClass(ManualInputActivity.this,
//			SendHttpRequestService.class);
//			startService(codeToSend);

			// 动态注册broadcastReceiver用来接收从SendHttpRequestService发来的数据
//			IntentFilter intentFilterForResult = new IntentFilter();
//			intentFilterForResult
//					.addAction(ConstantUtil.ACTION_PROCESS_HTTPRESULT);
//			processResultReceiver = new ProcessResultReceiver();
//			registerReceiver(processResultReceiver, intentFilterForResult);
			
			//zt添加，启动ProgressBarActivity进入等待状态；
			codeToSend.setClass(ManualInputActivity.this,ProgressBarActivity.class);
			startActivity(codeToSend);
			finish();//这个也必须加上，否则会在点击MainActivity这个界面中的退出键会直接退到这里
		}
	}
}

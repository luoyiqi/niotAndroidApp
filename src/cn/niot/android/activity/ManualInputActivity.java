package cn.niot.android.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

	//private String ipString = null;
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
		btnRequestCodeInfo
				.setOnClickListener(new btnRequestCodeInfoClickListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 为“选项菜单”添加两个子项，一个为“设置”，另一个为“退出”
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// 当点击“设置”时，去设置页
		if (item.getItemId() == R.id.action_settings) {
			Intent intentToSettingActivity = new Intent();
			intentToSettingActivity.setClass(this, SettingActivity.class);
			startActivity(intentToSettingActivity);
			// startActivityForResult(intentToSettingActivity, 1);
		}

		// 当点击“退出”时，退出app !!!尚有问题
		if (item.getItemId() == R.id.action_exit) {

			System.exit(0);
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);

	}

	class btnRequestCodeInfoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// 获得用户输入的编码
			String requestCode = edtTxtManualInput.getText().toString();

			// 将该编码发送给SendHttpRequestService，以启动向后台服务器的查询。
			Intent codeToSend = new Intent();
			codeToSend.putExtra("requestCode", requestCode);
//			if (ipString == null) {
//				codeToSend.putExtra("ipString", getIntent().getExtras()
//						.getString("ipString"));
//			} else {
//				codeToSend.putExtra("ipString", ipString);
//			}
			codeToSend.setClass(ManualInputActivity.this,
					SendHttpRequestService.class);
			startService(codeToSend);

			// 动态注册broadcastReceiver用来接收从SendHttpRequestService发来的数据
			IntentFilter intentFilterForResult = new IntentFilter();
			intentFilterForResult
					.addAction(ConstantUtil.ACTION_PROCESS_HTTPRESULT);
			processResultReceiver = new ProcessResultReceiver();
			registerReceiver(processResultReceiver, intentFilterForResult);
			// 注销
			//unregisterReceiver(processResultReceiver);
		}

	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		Bundle bundle = data.getExtras();
//		ipString = bundle.getString("ipString");
//	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		showInfo.setText("");
	}

}

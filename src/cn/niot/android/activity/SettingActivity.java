package cn.niot.android.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.niot.android.activity.R;
import cn.niot.android.utility.ConstantUtil;

@SuppressLint("NewApi")
public class SettingActivity extends Activity {
	private EditText editTextServerAddress = null;
	private Button buttonUpdateServerAddress = null;
	private TextView textViewCurrentServerAddress = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// 显示action bar上最左侧的回退按钮
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		editTextServerAddress = (EditText) findViewById(R.id.editTextServerAddress);
		buttonUpdateServerAddress = (Button) findViewById(R.id.buttonUpdateServerAddress);
		textViewCurrentServerAddress = (TextView) findViewById(R.id.textViewCurrentServerAddress);
		
		if(ConstantUtil.ipStr.equals(""))
		{
			//用户还没有设置服务器地址时，用默认的服务器地址
			textViewCurrentServerAddress.setText("用户还没有设置过服务器地址，使用默认的地址："+ConstantUtil.DEFAULT_SERVER_ADDRESS);
		}
		else
		{
			textViewCurrentServerAddress.setText("当前服务器地址为："+ConstantUtil.ipStr);
		}
		
		// 点击修改按钮
		buttonUpdateServerAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ipString = editTextServerAddress.getText().toString();
				if (ipString == null || ipString.equals("")) {
					Toast.makeText(SettingActivity.this, "请输入ip地址",
							Toast.LENGTH_LONG).show();
				} else {
					//Intent intentIP = new Intent();
					//intentIP.putExtra("ipString", ipString);
					ConstantUtil.ipStr = ipString;
					System.out.println(ConstantUtil.ipStr);
					Toast.makeText(SettingActivity.this, "服务器地址更新成功",Toast.LENGTH_SHORT).show();
					textViewCurrentServerAddress.setText("当前服务器地址为："+ConstantUtil.ipStr);
					//SettingActivity.this.setResult(RESULT_OK,intentIP);
					//SettingActivity.this.finish();
				}
			}
		});
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

		// 当点击“设置”时，留在当前页
		if (item.getItemId() == R.id.action_settings) {
			// Intent intentToSettingActivity= new Intent();
			// intentToSettingActivity.setClass(SettingActivity.this,
			// SettingActivity.class);
			// startActivity(intentToSettingActivity);
		}

		// 当点击“退出”时，退出app !!!尚有问题
		if (item.getItemId() == R.id.action_exit) {

			System.exit(0);
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);

	}

}

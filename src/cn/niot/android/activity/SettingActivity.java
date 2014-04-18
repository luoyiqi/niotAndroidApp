package cn.niot.android.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.niot.android.dbhelper.DatabaseHelper;
import cn.niot.android.dbhelper.DataFactory;
import cn.niot.android.service.SendHttpRequestService;
import cn.niot.android.utility.ConstantUtil;

@SuppressLint("NewApi")
public class SettingActivity extends Activity {
	private EditText editTextServerAddress = null;
	private Button buttonUpdateServerAddress = null;
	private TextView textViewCurrentServerAddress = null;
	private Button ipdelete=null;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// 显示action bar上最左侧的回退按钮
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		/**
		 * 下面的ipdelete是用来删除数据库的,如果不需要刻意删除
		 */
//		ipdelete=(Button)findViewById(R.id.ipdelete);
//		ipdelete.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				DatabaseHelper databaseHelper = new DatabaseHelper(SettingActivity.this, "ipdb", null, 1);
//				SQLiteDatabase dbForWrite = databaseHelper.getWritableDatabase();
//				dbForWrite.delete("IP_CONFIGER",null,null);
//				databaseHelper.deleteDatabase(SettingActivity.this);
//				Toast.makeText(SettingActivity.this, "删除数据库成功",
//                         Toast.LENGTH_LONG).show();
//			}
//		});
//		
//	
		
		editTextServerAddress = (EditText) findViewById(R.id.editTextServerAddress);
		buttonUpdateServerAddress = (Button) findViewById(R.id.buttonUpdateServerAddress);
		textViewCurrentServerAddress = (TextView) findViewById(R.id.textViewCurrentServerAddress);
		DataFactory data=new DataFactory(SettingActivity.this);
		String ipStr=data.getIpFromDatabase("ip_string",ConstantUtil.IP_TABALE,SettingActivity.this);
		if(ipStr.equals("10.0.0.2"))
		{
			//用户还没有设置服务器地址时，用默认的服务器地址
			textViewCurrentServerAddress.setText("用户还没有设置过服务器地址，使用默认的地址："+ipStr);
		}
		else{
			textViewCurrentServerAddress.setText("用户已设置的服务器地址"+ipStr);
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
					//ConstantUtil.ipStr = ipString;
//					DatabaseHelper databaseHelper = new DatabaseHelper(SettingActivity.this, "ipdb", null, 1);
//					SQLiteDatabase dbForWrite = databaseHelper.getWritableDatabase();
//					ContentValues dataNewUpdate = new ContentValues();
//					dataNewUpdate.put("ip_string",ipString);
//					dbForWrite.update("IP_CONFIGER", dataNewUpdate, null, null);
					DataFactory data=new DataFactory(SettingActivity.this);
					data.updateData("ip_string", ipString,SettingActivity.this);
					Toast.makeText(SettingActivity.this, "服务器地址更新成功",Toast.LENGTH_SHORT).show();
					textViewCurrentServerAddress.setText("当前服务器地址为："+ipString);
					
				}
			}
		});
	}

	
}

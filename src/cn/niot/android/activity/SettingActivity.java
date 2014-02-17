package cn.niot.android.activity;

import cn.niot.android.main.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

@SuppressLint("NewApi")
public class SettingActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//显示action bar上最左侧的回退按钮
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);


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

		//当点击“设置”时，留在当前页
		if(item.getItemId() == R.id.action_settings){
//			Intent intentToSettingActivity= new Intent();
//			intentToSettingActivity.setClass(SettingActivity.this, SettingActivity.class);
//			startActivity(intentToSettingActivity);
		}
		
		//当点击“退出”时，退出app  !!!尚有问题
		if(item.getItemId() == R.id.action_exit){

			System.exit(0);
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);

	}
	

}

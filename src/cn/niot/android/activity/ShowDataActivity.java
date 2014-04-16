package cn.niot.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import cn.niot.android.activity.R;

@SuppressLint("NewApi")
public class ShowDataActivity extends Activity {
	private ImageView showInfoImag = null;
	private TextView showInfo = null;
	private TextView showInfo1 = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showdata);
		
		// 显示action bar上最左侧的回退按钮
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		showInfo1 = (TextView) findViewById(R.id.showInfo1);
		showInfo = (TextView) findViewById(R.id.showInfo);// 显示详细信息的文本框
		showInfoImag = (ImageView) findViewById(R.id.showInfoImage);// 显示查询结果为一种时的100%图片
		Intent revDataIntent = getIntent();
		String codeData = revDataIntent.getExtras().getString("statusToLow");// 获得扫描或者是手动输入的编码
		try {
			JSONObject obj = new JSONObject(codeData);

			int status = obj.getInt("status");

			if (status == 0) {
				
				showInfo.setText("您输入的编码不属于后台数据库中的任何编码类型");
			} else if (status == 1) {
				JSONObject extraJson = new JSONObject(
						obj.getString("extraData"));

				String codename = obj.getString("data");// 得到编码名字
				JSONObject extrainfo = extraJson.getJSONObject(codename);
				String fullname = extrainfo.getString("fullName");
				String codenum = extrainfo.getString("codeNum");
				showInfoImag.setBackgroundResource(R.drawable.showimage);
				showInfo1.setText("属于" + codename);
				showInfo.setText(fullname + "\n" + codenum);
			} else {
				showInfo.setText("编码查询出现错误");
			}

			onDestroy();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("发生json异常");
			e.printStackTrace();
		}
	}
	
	// 此函数用来定义当用户点击“返回”按钮时，将返回到主页面
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		System.out.println("ShowDataActivity KEYCODE_BACK pressed");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// do whatever you want the 'Back' button to do
			// as an example the 'Back' button is set to start a new Activity
			// named 'NewActivity'
			startActivity(new Intent(ShowDataActivity.this, MainActivity.class));
          finish();//这个也必须加上，否则会在点击MainActivity这个界面中的退出键会直接退到这里
		} else {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}


}

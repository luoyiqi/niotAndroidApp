package cn.niot.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.niot.android.service.ProgressBarAsyncTask;

public class ProgressBarActivity extends Activity {
private TextView showInfoBar=null;
private ProgressBar bar=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		showInfoBar=(TextView)findViewById(R.id.showInfoBar);
		bar=(ProgressBar)findViewById(R.id.bar);
		setContentView(R.layout.activity_progress);
		//ProgressBarAsyncTask asyncTask=new ProgressBarAsyncTask(showInfoBar, bar, ProgressBarActivity.this, context2)
	}

	

}

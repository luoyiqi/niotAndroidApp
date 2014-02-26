package cn.niot.android.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, String> {
	private TextView showInfo = null;
	private ProgressBar bar = null;
	private Context context1 = null;
	private Context context2 = null;

	public ProgressBarAsyncTask(TextView showInfo, ProgressBar bar,
			Context context1, Context context2) {
		super();
		this.showInfo = showInfo;
		this.bar = bar;
		this.context1 = context1;
		this.context2 = context2;
	}

	// 异步操作处理，该方法不运行在UI线程，不能对UI界面中的控件进行设置和修改
	@Override
	protected String doInBackground(Integer... arg0) {
		// TODO Auto-generated method stub
		//这里主要就是操作数据然后更新progressbar的操作
		
		return null;
	}

	// 异步执行结束
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//可以睡眠1秒
		Intent intent = new Intent();
		intent.putExtra("result", result);
		intent.setClass(context1,Context.class);
		context1.startActivity(intent);//跳转到处理界面

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		showInfo.setText("请稍后正在查询");
	}

	//使得滚动条显示进度
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		int value=values[0];
		bar.setProgress(value);
	}

}

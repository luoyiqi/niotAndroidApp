package cn.niot.android.service;

import org.json.JSONException;
import org.json.JSONObject;

import cn.niot.android.activity.DrawActivity;
import cn.niot.android.activity.ShowDataActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProcessResultReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//从SendHttpRequestService中获得服务器传回的数据，前4位为null，因此需要从第5位开始取值。
		String result = intent.getExtras().getString("result").substring(4);
		//取得从服务端获得的标准类型
		try {
			JSONObject obj = new JSONObject(result);
			int status=obj.getInt("status");
			 if(status>1){
				drawPieChart(context,result);
			}else{

				showData(context, result);
			}
	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//启动一个新的activity用来画饼图
	protected void drawPieChart(Context context ,String data)
	{
		Intent intentDrawPie = new Intent();
		intentDrawPie.setClass(context, DrawActivity.class);
		intentDrawPie.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentDrawPie.putExtra("codeData", data);
		context.startActivity(intentDrawPie);
	}
    protected void showData(Context context,String data){
    	Intent repintent=new Intent();
    	repintent.putExtra("statusToLow", data);
    	repintent.setClass(context,ShowDataActivity.class);
	    context.startActivity(repintent);
    }
}

package cn.niot.android.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.IntentService;
import android.content.Intent;
import cn.niot.android.dbhelper.DataFactory;
import cn.niot.android.utility.ConstantUtil;

public class SendHttpRequestService extends IntentService {


	private String defaultIP= ConstantUtil.DEFAULT_SERVER_ADDRESS;
	public SendHttpRequestService() {
		super("SendHttpRequestService");
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//String ipString=ConstantUtil.ipStr;
		String code = intent.getExtras().getString("requestCode");
		DataFactory data=new DataFactory(SendHttpRequestService.this);
		String ipString=data.getIpFromDatabase("ip_string",ConstantUtil.IP_TABALE,SendHttpRequestService.this);
		if(ipString!=null&&!ipString.equals("")&&!ipString.equals(defaultIP)){
			defaultIP=ipString;
			System.out.println("defaultIP==="+defaultIP);
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://"+defaultIP+":8080/niot/respCode.action");
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
        //nameValuePairs.add(new BasicNameValuePair("code", "00485123456789"));
	    nameValuePairs.add(new BasicNameValuePair("code", code));
	    System.out.println("输入的或扫描的编码为：\t"+code);
        try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Execute HTTP Post Request
        try {
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity responseEntity =  response.getEntity();
			InputStream responseInputStream = responseEntity.getContent();
			InputStreamReader responseReader = new InputStreamReader(responseInputStream);
			BufferedReader responseReaderBuffer = new BufferedReader(responseReader);
			String result = null;
			String newLine = null;
			int n = 0;
			while ((newLine = responseReaderBuffer.readLine())!=null)
			{
				result = result+newLine;
				n++;
			}
			Intent intentResult = new Intent();
			intentResult.setAction(ConstantUtil.ACTION_PROCESS_HTTPRESULT);
			intentResult.putExtra("result", result);
			sendBroadcast(intentResult);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}

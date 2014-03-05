package cn.niot.android.activity;
/**
 * 李慧霞2014.1.22
 */

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import cn.niot.android.camera.CameraManager;
import cn.niot.android.decoding.CaptureActivityHandler;
import cn.niot.android.decoding.InactivityTimer;
import cn.niot.android.service.ProcessResultReceiver;
import cn.niot.android.service.SendHttpRequestService;
import cn.niot.android.utility.ConstantUtil;
import cn.niot.android.view.ViewfinderView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import cn.niot.android.activity.R;
/**
 * Initial the camera
 * 
 *
 */
public class MipcaActivityCapture extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;//相机预览模式
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ProcessResultReceiver processResultReceiver = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		//点击返回按钮
		Button mButtonBack = (Button) findViewById(R.id.button_back);
		mButtonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MipcaActivityCapture.this.finish();

			}
		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);// 启动计时器
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		
		/*以SurfaceView作为相机Preview之用*/
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		
		/*绑定SurfaceView,取得SurfaceHolder对象*/
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		
		//若相机没有在预览模式，则打开相机（要是加一个非的话会在打开扫描时只显示黑框）
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * lihuixia 2014.1.14 处理扫描结果，获取条码或者二维码的编码，
	 * 也就是条码或者二维码中存储的数据通过resultIntent获得并通过Activity的setResult
	 * 方法传回给调用这个activity的方法，但是是怎样识别是这个activity的返回结果呢，就是通过RESULT_OK这个常量识别的，这个在
	 * Activity中是定义的常量值为-1，在主Activity中的onActivityResult方法进行显示结果并进行处理
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("requestCode", resultString);// 显示条码和二维码的扫描结果，以这种键值对的形式存储
			
			resultIntent.putExtras(bundle);
			//zt注释掉
			//resultIntent.setClass(MipcaActivityCapture.this, SendHttpRequestService.class);
			//startService(resultIntent);
			
			//zt添加，启动ProgressBarActivity进入等待状态；
			resultIntent.setClass(MipcaActivityCapture.this,ProgressBarActivity.class);
			startActivity(resultIntent);
			finish();//这个也必须加上，否则会在点击MainActivity这个界面中的退出键会直接退到这里
			//以下内容也由zt注释掉
			//动态注册broadcastReceiver用来接收从SendHttpRequestService发来的数据
//			IntentFilter intentFilterForResult = new IntentFilter();
//			intentFilterForResult.addAction(ConstantUtil.ACTION_PROCESS_HTTPRESULT);
//			processResultReceiver = new ProcessResultReceiver();
//			registerReceiver(processResultReceiver, intentFilterForResult);
//			//注销
//			unregisterReceiver(processResultReceiver);
			//this.setResult(RESULT_OK, resultIntent);
		}
		//MipcaActivityCapture.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	// 播放声音
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;
	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}
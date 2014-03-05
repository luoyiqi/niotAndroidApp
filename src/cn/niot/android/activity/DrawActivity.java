package cn.niot.android.activity;

/**
 * 李慧霞2014年1月22日
 * 从json中读取数据画饼图，然后可以先行饼图中扇形的点击（这里的点击比较简单，只是简单的toast显示）
 */
import java.text.DecimalFormat;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DrawActivity extends Activity {
	private GraphicalView mchartview;
	private DefaultRenderer renderer;
	private CategorySeries categorySeries = new CategorySeries("Vehicles Chart");
	private JSONObject obj = null;
	private JSONArray arr = null;
	private JSONObject extraJson;
	private int[] colors;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);

		// 显示action bar上最左侧的回退按钮
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String codeData = getIntent().getExtras().getString("codeData");
		// substring
		colors = new int[] { Color.RED, Color.rgb(255, 165, 0), Color.YELLOW,
				Color.GREEN, Color.rgb(0, 255, 255), Color.BLUE,
				Color.rgb(128, 0, 128) };// 设置扇形的颜色
		try {

			obj = new JSONObject(codeData);
			renderer = new DefaultRenderer();
			String data = obj.getString("data");
			arr = new JSONArray(data);
			for (int i = 0; i < obj.getInt("status"); i++) {
				// JSONObject extraJson = obj.getJSONObject("extraData");
				/*
				 * 这里这个之所以这样写是因为，从服务器哪里得到的数据将每一部分都封装成了字符串，所以没有使用 双斜线注释的那部分
				 */

				extraJson = new JSONObject(obj.getString("extraData"));
				String codename = arr.getJSONObject(i).getString("codeName");// 得到编码名字
				JSONObject extrainfo = extraJson.getJSONObject(codename);
				String fullname = extrainfo.getString("fullName");
				Double probility = arr.getJSONObject(i)
						.getDouble("probability");// 得到编码所占的比例
				categorySeries.add(fullname, probility);

				SimpleSeriesRenderer r = new SimpleSeriesRenderer();
				// 这里这个是NumberFormat的子类，可以格式数据，我设定让扇形中数据显示为00.0%
				DecimalFormat numberformat = new DecimalFormat("00.0%");
				r.setChartValuesFormat(numberformat);// 设置百分比
				// 设置颜色，这里只有7种颜色值，循环设置
				r.setColor(colors[(i % 7)]);
				r.setShowLegendItem(true);
				renderer.addSeriesRenderer(r);
			}
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.chart);
			layout.setBackgroundColor(Color.WHITE);
			mchartview = ChartFactory.getPieChartView(this, categorySeries,
					renderer);
			setPieView(renderer);
			// 这个是点击每个扇形触发的事件，使得每个扇形被点击时显示的Toast背景色和扇形的同样以及简单处理
			mchartview.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						SeriesSelection seriesSelection = mchartview
								.getCurrentSeriesAndPoint();
						// 当没有点击扇形时的操作
						if (seriesSelection != null) {// 当点击扇形时的操作
							for (int i = 0; i < categorySeries.getItemCount(); i++) {
								renderer.getSeriesRendererAt(i).setHighlighted(
										i == seriesSelection.getPointIndex());
							}
							mchartview.repaint();
							Double probility = arr.getJSONObject(
									seriesSelection.getPointIndex()).getDouble(
									"probability");// 得到编码所占的比例
							DecimalFormat showformat = new DecimalFormat(
									"00.0%");
							JSONObject extrainfo = extraJson.getJSONObject(arr
									.getJSONObject(
											seriesSelection.getPointIndex())
									.getString("codeName"));
							String codenum = extrainfo.getString("codeNum");
							String fullname = extrainfo.getString("fullName");
							Toast toast = Toast.makeText(DrawActivity.this,
									fullname + "\n" + "\t\t\t\t" + codenum
											+ "\n" + "\t\t\t\t比例：\t"
											+ showformat.format(probility),
									Toast.LENGTH_LONG);
							View view = toast.getView();
							view.setBackgroundColor(colors[seriesSelection
									.getPointIndex() % 7]);// 同步设置toast的颜色使得和扇形的颜色一样
							toast.setView(view);
							toast.show();

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("内部try catch json处理异常");
						e.printStackTrace();
					}
				}

			});
			layout.addView(mchartview);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("json处理异常");
		}

	}

	// 关于饼状图的一些设置我都放到了这个函数里
	public void setPieView(DefaultRenderer renderer) {

		renderer.setChartTitle("Collison Ratio");
		renderer.setChartTitleTextSize(40);
		renderer.setDisplayValues(true);// 是否显示数据
		renderer.setClickEnabled(true);// 使得图形可点击
		renderer.setLabelsColor(Color.rgb(89, 89, 89));// 设置标签的颜色
		renderer.setLabelsTextSize(20);
		renderer.setLegendTextSize(20);
		renderer.setBackgroundColor(Color.WHITE);

	}

	// 此函数用来定义当用户点击“返回”按钮时，将返回到主页面
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		System.out.println("KEYCODE_BACK pressed");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("KEYCODE_BACK pressed");
			// do whatever you want the 'Back' button to do
			// as an example the 'Back' button is set to start a new Activity
			// named 'NewActivity'

			startActivity(new Intent(DrawActivity.this, MainActivity.class));
			finish();//这个也必须加上，否则会在点击MainActivity这个界面中的退出键会直接退到这里

		} else {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

}
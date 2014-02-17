package cn.niot.android.activity;
/**
 * 李慧霞2014年1月22日
 * 从json中读取数据画饼图，然后可以先行饼图中扇形的点击（这里的点击比较简单，只是简单的toast显示）
 */
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.niot.android.getdata.JSONgetdata;
import cn.niot.android.main.R;

public class DrawActivity extends Activity {
	private GraphicalView mchartview;
	private DefaultRenderer renderer;
	private CategorySeries categorySeries = new CategorySeries("Vehicles Chart");
	private JSONgetdata jget = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		
	
		String codeData = getIntent().getExtras().getString("codeData");
		System.out.println(codeData);
		
		//substring
		
		int[] colors = new int[] { Color.RED, Color.GREEN, Color.BLUE };// 设置扇形的颜色
		jget = new JSONgetdata();// 获得处理json的类
		try {
			JSONObject obj = new JSONObject(jget.getJson());
			//JSONObject obj = new JSONObject(codeData);
			renderer = new DefaultRenderer();
			JSONArray arr = obj.getJSONArray("data");
			
			System.out.println("data===========>"+arr.toString());
			for (int i = 0; i < obj.getInt("status"); i++) {
				String codename = arr.getJSONObject(i).getString("codeName");// 得到编码名字
				Double probility = arr.getJSONObject(i)
						.getDouble("probability");// 得到编码所占的比例
				categorySeries.add(codename, probility);
				SimpleSeriesRenderer r = new SimpleSeriesRenderer();
				r.setColor(colors[(i % 3)]);
				renderer.addSeriesRenderer(r);
			}
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.chart);
			mchartview = ChartFactory.getPieChartView(this, categorySeries,
					renderer);
			renderer.setClickEnabled(true);// 使得图形可点击
			renderer.setLabelsTextSize(10);
			renderer.setLabelsColor(Color.BLUE);// 设置标签的颜色
			renderer.setLegendTextSize(20);//设置图例字体的大小
			renderer.setBackgroundColor(Color.GRAY);

			// 这个是点击每个扇形触发的事件，以及简单处理
			mchartview.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SeriesSelection seriesSelection = mchartview
							.getCurrentSeriesAndPoint();

					// 当没有点击扇形时的操作
					if (seriesSelection == null) {

						Toast.makeText(DrawActivity.this,
								"No chart element selected", Toast.LENGTH_SHORT)
								.show();
					} else {// 当点击扇形时的操作
						for (int i = 0; i < categorySeries.getItemCount(); i++) {
							renderer.getSeriesRendererAt(i).setHighlighted(
									i == seriesSelection.getPointIndex());
						}
						mchartview.repaint();
						Toast.makeText(
								DrawActivity.this,
								"Chart data point index "
										+ seriesSelection.getPointIndex()
										+ " selected" + " point value="
										+ seriesSelection.getValue(),
								Toast.LENGTH_SHORT).show();

					}
				}
			});
			layout.addView(mchartview);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("json处理异常");
		}

	}

}
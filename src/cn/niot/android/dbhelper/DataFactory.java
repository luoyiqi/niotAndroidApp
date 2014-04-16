package cn.niot.android.dbhelper;
/**
 * 该类可以对数据库进行操作
 */
import cn.niot.android.activity.SettingActivity;
import cn.niot.android.utility.ConstantUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataFactory {
	private Context context;
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase dbForWrite;

	public DataFactory(Context context) {
		this.context = context;
	}

	public DataFactory() {

	}

	public void initDatabase() {
		databaseHelper = new DatabaseHelper(context, ConstantUtil.DATABASENAME,
				null, 1);

		dbForWrite = databaseHelper.getWritableDatabase();
	}

	// 查询数据库得到数据
	public String getIpFromDatabase(String column, String table, Context context) {
		// DatabaseHelper databaseHelper = new DatabaseHelper(context,
		// ConstantUtil.DATABASENAME, null, 1);
		//
		// SQLiteDatabase dbForWrite = databaseHelper.getWritableDatabase();
		initDatabase();
		String ipString = null;
		Cursor myCursor = dbForWrite.query(ConstantUtil.IP_TABALE,
				new String[] { column }, null, null, null, null, null, null);
		while (myCursor.moveToNext()) {
			ipString = myCursor.getString(myCursor.getColumnIndex(column));
			System.out.println("ipString=============>" + ipString);
		}
		databaseHelper.close();
		return ipString;
	}

	// 更新数据库
	public void updateData(String column, String ipStr, Context context) {
		// DatabaseHelper databaseHelper = new DatabaseHelper(context,
		// ConstantUtil.DATABASENAME, null, 1);
		//
		// SQLiteDatabase dbForWrite = databaseHelper.getWritableDatabase();
		initDatabase();
		ContentValues dataNewUpdate = new ContentValues();
		dataNewUpdate.put(column, ipStr);
		dbForWrite.update(ConstantUtil.IP_TABALE, dataNewUpdate, null, null);
		databaseHelper.close();
	}
}

package cn.niot.android.dbhelper;

import cn.niot.android.service.SendHttpRequestService;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("SQLiteDatabase onCreate");

		// 创建表TABLE_CHARGE
		db.execSQL("create table IP_CONFIGER(id INTEGER  PRIMARY KEY autoincrement"
				+ ", " + "ip_string varchar(255))");
		// 初始化时插入一条新数据
		ContentValues dataNewUpdate = new ContentValues();
		dataNewUpdate.put("ip_string", "10.0.0.2");
		db.insert("IP_CONFIGER", null, dataNewUpdate);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	// 数据库删除
	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase("ipdb");
	}

}

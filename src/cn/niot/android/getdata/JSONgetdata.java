package cn.niot.android.getdata;

public class JSONgetdata {
	String json = "{"
			+ "status:2,"
			+ "data:[{codeName:\"cpc\",probability:0.12},{codeName:\"eCode\",probability:0.88}],"
			+ "extraData:{cpc:{codeNum:\"GB/T 23833-2009_8\",fullName:\"商品条码资产编码与条码表示VIII\"},"
			+ "eCode:{codeNum:\"GB/T 23833-2009_7\",fullName:\"商品条码资产编码与条码表示VII\"}}"
			+ "}";// 用于处理的json对象，这里是一个字符串数组

	public JSONgetdata() {

	}

	public JSONgetdata(String json) {
		super();
		this.json = json;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}

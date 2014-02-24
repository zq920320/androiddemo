package com.zq.zqtest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;

public class GexinSdkHttpPost {

	public static final String SERVICEURL = "http://sdk.open.api.igexin.com/service";
	public static final int CONNECTION_TIMEOUT_INT = 8000;
	public static final int READ_TIMEOUT_INT = 5000;

	public static void httpPost(Map<String, Object> map) {

		String param = JSONObject.toJSONString(map);

		if (param != null) {

			URL url = null;

			try {
				url = new URL(SERVICEURL);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoInput(true); // 璁剧疆杈撳叆娴侀噰鐢ㄥ瓧鑺傛祦
				urlConn.setDoOutput(true); // 璁剧疆杈撳嚭娴侀噰鐢ㄥ瓧鑺傛祦
				urlConn.setRequestMethod("POST");
				urlConn.setUseCaches(false); // 璁剧疆缂撳瓨
				urlConn.setRequestProperty("Charset", "utf-8");
				urlConn.setConnectTimeout(CONNECTION_TIMEOUT_INT);
				urlConn.setReadTimeout(READ_TIMEOUT_INT);

				urlConn.connect(); // 杩炴帴鏃㈠線鏈嶅姟绔彂閫佹秷鎭�

				DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
				dop.write(param.getBytes("utf-8")); // 鍙戦�鍙傛暟
				dop.flush(); // 鍙戦�锛屾竻绌虹紦瀛�
				dop.close(); // 鍏抽棴

				// 涓嬮潰寮�鍋氭帴鏀跺伐浣�
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				String result = ""; // 鑾峰彇鏈嶅姟鍣ㄨ繑鍥炴暟鎹�
				String readLine = null;
				while ((readLine = bufferReader.readLine()) != null) {
					result += readLine;
				}
				bufferReader.close();
				urlConn.disconnect();

				System.out.println("result锛�" + result);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("param is null");
		}
	}

}

package com.zq.zqtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.Consts;

public class touchuan extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GexinSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(Consts.CMD_ACTION)) {

		case Consts.GET_MSG_DATA:
			// ��ȡ͸������
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");

			if (payload != null) {
				String data = new String(payload);

				Log.d("GexinSdkDemo", "Got Payload:" + data);
				if (MainActivity.tLogView != null)
					MainActivity.tLogView.append(data + "\n");

			}
			break;
		case Consts.GET_CLIENTID:
			// ��ȡClientID(CID)
			// ������Ӧ����Ҫ��CID�ϴ��������������������ҽ���ǰ�û��ʺź�CID���й������Ա��պ�ͨ���û��ʺŲ���CID������Ϣ����
			String cid = bundle.getString("clientid");
			if (MainActivity.tView != null)
				MainActivity.tView.setText(cid);
			break;

		case Consts.BIND_CELL_STATUS:
			String cell = bundle.getString("cell");

			Log.d("GexinSdkDemo", "BIND_CELL_STATUS:" + cell);
			if (MainActivity.tLogView != null)
				MainActivity.tLogView.append("BIND_CELL_STATUS:" + cell + "\n");
			break;
		case Consts.THIRDPART_FEEDBACK:
			// sendMessage�ӿڵ��û�ִ
			String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			Log.d("GexinSdkDemo", "appid:" + appid);
			Log.d("GexinSdkDemo", "taskid:" + taskid);
			Log.d("GexinSdkDemo", "actionid:" + actionid);
			Log.d("GexinSdkDemo", "result:" + result);
			Log.d("GexinSdkDemo", "timestamp:" + timestamp);
			break;
		default:
			break;
		}
	}
}

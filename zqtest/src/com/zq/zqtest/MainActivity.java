package com.zq.zqtest;

/*
 * ����SDK��ʾ����
 * 
 * SDK Ver: 1.1.14.2
 */
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.Consts;
import com.igexin.sdk.aidl.GexinSdkNetstat;
import com.igexin.sdk.aidl.Tag;
import com.igexin.slavesdk.MessageManager;

public class MainActivity extends Activity implements OnClickListener {

	/**
	 * ������Ӧ��Master Secret���޸�Ϊ��ȷ��ֵ
	 */
	private static final String MASTERSECRET = "a02a76119b20d4e31620d7597a3b4f35";

	// �˵�
	private static final int ADDTAG = 100;
	private static final int NETSTAT = 101;
	private static final int VERSION = 102;
	private static final int PHONEADDRESS = 103;
	private static final int SILENTTIME = 104;
	private static final int EXIT = 106;

	// UI�ؼ�
	private Button btn_clear = null;
	private Button btn_service = null;
	private Button btn_enablelog = null;
	private Button btn_bindcell = null;
	private TextView tAppkeyView = null;
	private TextView tAppSecretView = null;
	private TextView tAppIdView = null;
	private TextView tMasterSecretView = null;
	public static TextView tView = null;
	public static TextView tLogView = null;
	private Button btn_pmsg = null;
	private Button btn_psmsg = null;
	private Button btn_send_msg = null;

	/**
	 * SDK�����Ƿ�����
	 */
	private boolean tIsRunning = true;
	private Context mContext = null;
	private SimpleDateFormat formatter = null;
	private Date curDate = null;

	// SDK���������Զ���Manifest�ļ��ж�ȡ�������������޸����б��������޸�AndroidManifest.xml�ļ�����Ӧ��meta-data��Ϣ��
	// �޸ķ�ʽ�μ�����SDK�ĵ�
	private String appkey = "";
	private String appsecret = "";
	private String appid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// UI��ʼ��
		setContentView(R.layout.activity_main);
		mContext = this;
		tIsRunning = true;
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);
		btn_service = (Button) findViewById(R.id.btn_service);
		btn_service.setOnClickListener(this);
		btn_enablelog = (Button) findViewById(R.id.btn_enablelog);
		btn_enablelog.setOnClickListener(this);
		btn_bindcell = (Button) findViewById(R.id.btn_bindcell);
		btn_bindcell.setOnClickListener(this);
		tView = (TextView) findViewById(R.id.tvclientid);
		tAppkeyView = (TextView) findViewById(R.id.tvappkey);
		tAppSecretView = (TextView) findViewById(R.id.tvappsecret);
		tMasterSecretView = (TextView) findViewById(R.id.tvmastersecret);
		tAppIdView = (TextView) findViewById(R.id.tvappid);
		tLogView = (EditText) findViewById(R.id.tvlog);
		tLogView.setInputType(InputType.TYPE_NULL);
		tLogView.setSingleLine(false);
		tLogView.setHorizontallyScrolling(false);
		btn_pmsg = (Button) findViewById(R.id.btn_pmsg);
		btn_pmsg.setOnClickListener(this);
		btn_psmsg = (Button) findViewById(R.id.btn_psmsg);
		btn_psmsg.setOnClickListener(this);

		btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
		btn_send_msg.setOnClickListener(this);

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// ��AndroidManifest.xml��meta-data�ж�ȡSDK������Ϣ
		String packageName = getApplicationContext().getPackageName();
		ApplicationInfo appInfo;
		try {
			appInfo = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			if (appInfo.metaData != null) {

				appid = appInfo.metaData.getString("appid");
				appsecret = appInfo.metaData.getString("appsecret");
				appkey = (appInfo.metaData.get("appkey") != null) ? appInfo.metaData.get("appkey").toString() : null;
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tAppkeyView.setText("AppKey=" + appkey);
		tAppSecretView.setText("AppSecret=" + appsecret);
		tMasterSecretView.setText("MasterSecret=" + MASTERSECRET);
		tAppIdView.setText("AppID=" + appid);

		// SDK��ʼ������������������ʱ����Ҫ����SDK��ʼ������
		Log.d("GexinSdkDemo", "initializing sdk...");
		
		MessageManager.getInstance().initialize(this.getApplicationContext());
	}

	@Override
	public void onDestroy() {
		Log.d("GexinSdkDemo", "onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onStop() {
		Log.d("GexinSdkDemo", "onStop()");
		super.onStop();
	}

	public void onClick(View v) {

		if (v == btn_send_msg) {

			final View view = new EditText(this);
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("�����ϴ�����").setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					TextView text = (TextView) view;

					byte[] uploadData = text.getText().toString().getBytes();

					// ����������������Ϣ����
					boolean result = MessageManager.getInstance().sendMessage(MainActivity.this, String.valueOf(System.currentTimeMillis()),
							uploadData);

					Toast.makeText(mContext, "�ϴ����:" + result, Toast.LENGTH_SHORT).show();
					Log.d("GexinSdkDemo", "�ϴ�����:" + text.getText().toString());

					dialog.dismiss();
				}
			}).setView(view);
			alertBuilder.create().show();

		} else if (v == btn_clear) {
			// ������־���������־��SDK�������汾SDK��֧�ָýӿڣ�
			MessageManager.getInstance().setLogEnable(this.getApplicationContext(), true);

			// clear log box
			tLogView.setText("");

		} else if (v == btn_service) {
			if (tIsRunning) {

				// ��ǰΪ����״̬��ֹͣSDK����
				Log.d("GexinSdkDemo", "stopping sdk...");
				MessageManager.getInstance().stopService(this.getApplicationContext());

				// UI����
				tView.setText(getResources().getString(R.string.no_clientid));
				btn_service.setText(getResources().getString(R.string.start));

				tIsRunning = false;
			} else {
				// ��ǰδ����״̬������SDK����
				Log.d("GexinSdkDemo", "reinitializing sdk...");

				// ���³�ʼ��sdk
				MessageManager.getInstance().initialize(this.getApplicationContext());

				// UI����
				btn_service.setText(getResources().getString(R.string.stop));
				tIsRunning = true;
			}
		} else if (v == btn_enablelog) {
			// ������־����
			MessageManager.getInstance().setLogEnable(this.getApplicationContext(), true);

			Toast.makeText(this, "������־�Ѵ�", Toast.LENGTH_SHORT).show();
			Log.d("GexinSdkDemo", "������־�Ѵ�");
		} else if (v == btn_bindcell) {
			// ���Ժ���󶨽ӿ�
			MessageManager.getInstance().bindPhoneAddress(this.getApplicationContext());
			Toast.makeText(this, "����������ѷ��ͣ����Ժ�...", Toast.LENGTH_SHORT).show();

		} else if (v == btn_pmsg) {

			if (isNetworkConnected()) {

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("action", "pushmessage"); // pushmessageΪ�ӿ�����ע��ȫ��Сд
				/*---���´��������趨�ӿ���Ӧ����---*/
				param.put("appkey", appkey);
				param.put("appid", appid);
				// ע��͸�����ݺ�����������֤�ӿڵ����Ƿ�ɹ����ٶ���дΪhello girl~
				param.put("data", "����һ��͸��������Ϣ");

				curDate = new Date(System.currentTimeMillis());
				param.put("time", formatter.format(curDate)); // ��ǰ����ʱ�䣬��ѡ
				param.put("clientid", tView.getText().toString()); // ����ȡ��ClientID
				param.put("expire", 3600); // ��Ϣ��ʱʱ�䣬��λΪ�룬��ѡ

				// ����Signֵ�����ڼ�Ȩ
				param.put("sign", makeSign(MASTERSECRET, param));

				GexinSdkHttpPost.httpPost(param);

			} else {

				Toast toast = Toast.makeText(this, "�Բ��𣬵�ǰ���粻����!", Toast.LENGTH_SHORT);

				toast.show();
			}

		} else if (v == btn_psmsg) {

			if (isNetworkConnected()) {

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("action", "pushSpecifyMessage"); // pushSpecifyMessageΪ�ӿ�����ע���Сд
				/*---���´��������趨�ӿ���Ӧ����---*/
				param.put("appkey", appkey);
				param.put("type", 2); // �������ͣ� 2Ϊ��Ϣ
				param.put("pushTitle", "֪ͨ������"); // pushTitle����д����Ӧ������

				// ������Ϣ���ͣ���TransmissionMsg��LinkMsg��NotifyMsg���֣��˴���LinkMsg����
				param.put("pushType", "LinkMsg");

				param.put("offline", true); // �Ƿ����������Ϣ

				param.put("offlineTime", 72); // ��Ϣ���߱���ʱ��
				param.put("priority", 1); // �����������ȼ�

				List<String> cidList = new ArrayList<String>();
				cidList.add(tView.getText().toString()); // ����ȡ��ClientID
				param.put("tokenMD5List", cidList);

				// ����Signֵ�����ڼ�Ȩ����ҪMasterSecret���������д
				param.put("sign", makeSign(MASTERSECRET, param));

				// LinkMsg��Ϣʵ��
				Map<String, Object> linkMsg = new HashMap<String, Object>();
				linkMsg.put("linkMsgIcon", "push.png"); // ��Ϣ��֪ͨ����ͼ��
				linkMsg.put("linkMsgTitle", "֪ͨ������"); // ������Ϣ�ı���
				linkMsg.put("linkMsgContent", "����һ��������Ϣ��"); // ������Ϣ������
				linkMsg.put("linkMsgUrl", "http://www.igexin.com/"); // ���֪ͨ��ת��Ŀ����ҳ
				param.put("msg", linkMsg);

				GexinSdkHttpPost.httpPost(param);

			} else {
				Toast toast = Toast.makeText(this, "�Բ��𣬵�ǰ���粻����!", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ADDTAG: {
			// ����addTag�ӿ�
			final View view = new EditText(this);
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("����Tag").setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					TextView tagText = (TextView) view;

					String[] tags = tagText.getText().toString().split(",");
					Tag[] tagParam = new Tag[tags.length];
					for (int i = 0; i < tags.length; i++) {
						Tag t = new Tag();
						t.setName(tags[i]);
						tagParam[i] = t;
					}

					Toast.makeText(mContext, "����tag:" + tagText.getText().toString(), Toast.LENGTH_SHORT).show();
					Log.d("GexinSdkDemo", "����tag:" + tagText.getText().toString());

					int i = MessageManager.getInstance().setTag(mContext, tagParam);
					String text = "ERROR";

					switch (i) {
					case Consts.SETTAG_SUCCESS:
						text = "���ñ�ǩ�ɹ�";
						break;
					case Consts.SETTAG_ERROR_COUNT:
						text = "���ñ�ǩʧ�ܣ�tag��������";
						break;
					case Consts.SETTAG_ERROR_FREQUENCY:
						text = "���ñ�ǩʧ�ܣ�Ƶ�ʹ���";
						break;
					case Consts.SETTAG_ERROR_REPEAT:
						text = "���ñ�ǩʧ�ܣ���ǩ�ظ�";
						break;
					case Consts.SETTAG_ERROR_UNBIND:
						text = "���ñ�ǩʧ�ܣ�aidl����δ��";
						break;
					case Consts.SETTAG_ERROR_EXCEPTION:
					default:
						text = "���ñ�ǩʧ�ܣ�setTag�쳣";
						break;
					}

					Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
					Log.d("GexinSdkDemo", text);

					dialog.dismiss();
				}
			}).setView(view);
			alertBuilder.create().show();

			break;
		}
		case NETSTAT: {
			// ����getNetstat ��������ͳ�ƽӿ�
			GexinSdkNetstat netstat = new GexinSdkNetstat();
			MessageManager.getInstance().getNetstat(this, netstat);
			long inbound = GexinSdkNetstat.inboundBytes;
			long outbound = GexinSdkNetstat.outboundBytes;
			Toast.makeText(this, "�ܼ�����Ϊ��" + String.valueOf(inbound + outbound) + "Bytes", Toast.LENGTH_SHORT).show();
			break;
		}
		case VERSION: {
			// ����getVersion��ȡ�汾�Žӿ�
			String version = MessageManager.getInstance().getVersion(this);
			Toast.makeText(this, "��ǰsdk�汾Ϊ��" + version, Toast.LENGTH_SHORT).show();
			break;
		}
		case PHONEADDRESS: {
			// ����getPhoneAddress��ȡ�ֻ��Žӿ�
			String address = MessageManager.getInstance().getPhoneAddress(this);
			Toast.makeText(this, "��ǰ�ֻ���Ϊ��" + address, Toast.LENGTH_SHORT).show();
			break;
		}
		case SILENTTIME: {
			// ����setSilentTime���þ�Ĭʱ��ӿ�
			final View view = LayoutInflater.from(this).inflate(R.layout.silent_setting, null);
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("���þ�Ĭʱ���").setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					TextView beginText = (TextView) view.findViewById(R.id.beginText);
					TextView durationText = (TextView) view.findViewById(R.id.durationText);

					String begin = beginText.getText().toString();
					String duration = durationText.getText().toString();

					if (begin.equals("") || duration.equals("")) {

						Toast.makeText(mContext, "���þ�Ĭʱ���ʧ�ܣ������뾲Ĭʱ�䣡", Toast.LENGTH_SHORT).show();
						Log.d("GexinSdkDemo", "���þ�Ĭʱ���ʧ�ܣ������뾲Ĭʱ�䣡");

					} else {

						int beginHour = Integer.valueOf(begin);
						int durationHour = Integer.valueOf(duration);

						boolean result = MessageManager.getInstance().setSilentTime(mContext, beginHour, durationHour);

						if (result) {
							Toast.makeText(mContext, "���þ�Ĭʱ��� begin:" + beginHour + " duration:" + durationHour, Toast.LENGTH_SHORT).show();
							Log.d("GexinSdkDemo", "���þ�Ĭʱ��� begin:" + beginHour + " duration:" + durationHour);
						} else {
							Toast.makeText(mContext, "���þ�Ĭʱ���ʧ�ܣ�ȡֵ����Χ begin:" + beginHour + " duration:" + durationHour, Toast.LENGTH_SHORT).show();
							Log.d("GexinSdkDemo", "���þ�Ĭʱ���ʧ�ܣ�ȡֵ����Χ begin:" + beginHour + " duration:" + durationHour);
						}
					}

					dialog.dismiss();
				}
			}).setView(view);
			alertBuilder.create().show();
			break;
		}
		case EXIT: {
			// ����
			finish();
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ADDTAG, 0, "���Tag");
		menu.add(0, NETSTAT, 1, "����ͳ��");
		menu.add(0, VERSION, 2, "��ǰ�汾");
		menu.add(0, PHONEADDRESS, 3, "��ѯ�ֻ���");
		menu.add(0, SILENTTIME, 4, "���þ�Ĭʱ��");
		menu.add(0, EXIT, 5, "�˳�");

		return super.onCreateOptionsMenu(menu);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// ���ؼ���С������
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);

			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public boolean isNetworkConnected() {

		// �ж������Ƿ�����
		ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * ����Sign����
	 */
	public static String makeSign(String masterSecret, Map<String, Object> params) throws IllegalArgumentException {
		if (masterSecret == null || params == null) {
			throw new IllegalArgumentException("masterSecret and params can not be null.");
		}

		if (!(params instanceof SortedMap)) {
			params = new TreeMap<String, Object>(params);
		}

		StringBuilder input = new StringBuilder(masterSecret);
		for (Entry<String, Object> entry : params.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String || value instanceof Integer || value instanceof Long) {
				input.append(entry.getKey());
				input.append(entry.getValue());
			}
		}

		return getMD5Str(input.toString());
	}

	/**
	 * MD5����
	 */
	public static String getMD5Str(String sourceStr) {
		byte[] source = sourceStr.getBytes();
		String s = null;
		char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		java.security.MessageDigest md = null;
		try {
			md = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// #debug
			e.printStackTrace();
		}
		if (md == null)
			return null;
		md.update(source);
		byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
		// ���ֽڱ�ʾ���� 16 ���ֽ�
		char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
		// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
		int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
		for (int i = 0; i < 16; i++) {
			// �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
			// ת���� 16 �����ַ���ת��
			byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��,
			// >>> Ϊ�߼����ƣ�������λһ������
			str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
		}
		s = new String(str); // ����Ľ��ת��Ϊ�ַ���
		return s;
	}

}

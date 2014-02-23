androiddemo
===========
#配置环境

##安装androidsdk[下载地址]
* 解压之后启动android sdk Manager
  * 下载想要用的android虚拟机版本

##创建android application
* file->new->Project->android->android application Project
* 填写相应的项目名称,保证package name 通个推申请应用的应用标识相同

##添加[个推]SDK到项目中

###根据个推提供的官方文档即可
* 将工具包中“个推Android平台SDK接入/客户端/需导入的资源”目录下的GetuiSdk-xxx.jar、GetuiExt- xxx.jar以及armeabi文件夹复制到工程根目录下的libs文件夹中
（若没有libs目录，则选中工程右键“New”->Folder->命名为“libs”）

* 右键单击工程，选择Build Path中的Configure Build Path...，选中Libraries，并通过Add Jars...导入工程libs目录下的GetuiSdk-xxx.jar、GetuiExt-xxx.jar文件

* 在您应用程序主Activity里导入MessageManager如下所示：
  
  `import com.igexin.slavesdk.MessageManager;`

* 然后在您应用程序启动初始化阶段，初始化SDK：

  `MessageManager.getInstance().initialize(this.getApplicationContext());`
* 将 app_download_notification.xml、notification.xml、increment_popup_dialog.xml 和 notification_inc.xml（在 SDK/需导入的资源文件夹下）复制到工程res/layout/文件夹中。
* 在AndroidManifest.xml 里添加SDK所需的服务声明和权限声明：

  `<!-- 个推SDK配置开始 -->
		 <!-- 配置的第三方参数属性 -->
		 <meta-data android:name="appid" android:value="b03c5cfef65ed30108f0a3fd82c3f6b4" />
		 <meta-data android:name="appsecret" android:value="LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX" />
		 <meta-data android:name="appkey" android:value="110000" />
		 <meta-data android:name="groupid" android:value="" />
		
	 	<activity android:name="com.igexin.sdk.SdkActivity"
             android:process=":pushservice"
			          android:theme="@android:style/Theme.Translucent.NoTitleBar"
         			 android:taskAffinity="android.task.GexinSdkActivityTask"
         			 android:excludeFromRecents="true"
         			 android:exported="false">
			 <intent-filter>
			  <action android:name="com.igexin.action.popupact.com.igexin.demo" />
				<!--
					com.igexin.action.popupact.第三方的包名
				-->
				 <category android:name="android.intent.category.DEFAULT" />
			 </intent-filter>
		 </activity>

   <!-- 配置弹框activity -->
   <activity android:name="com.igexin.getuiext.activity.GetuiExtActivity"  
             android:process=":pushservice"
        	    android:configChanges="orientation|keyboard|keyboardHidden"
             android:excludeFromRecents="true"
             android:taskAffinity="android.task.myServicetask"
             android:theme="@android:style/Theme.Translucent.NoTitleBar"
             android:exported="false" />
		
		  <service android:label="NotifyCenter" 
             android:name="com.igexin.sdk.SdkMainService"
             android:process=":pushservice"
             android:exported="false">
		  </service>
		 <service android:label="NotifyCenterAIDL"
            android:name="com.igexin.sdk.coordinator.SdkMsgService"
            android:process=":pushservice"
            android:exported="true">
		 </service>
		
		 <!-- 个推download模块配置-->
		 <service android:name="sdk.download.DownloadService"
			         android:process=":pushservice" />
		  <receiver
			     android:exported="false" android:name="sdk.download.DownloadReceiver"> 
			  <intent-filter>
				  <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
			  </intent-filter>
		  </receiver>
		 <provider android:authorities="sdk.download.com.igexin.demo" 
          			android:process=":pushservice" 
          			android:name="sdk.download.DownloadProvider"/>
			<!-- android:authorities="sdk.download.第三方包名" -->

   <!-- GetuiExt模块配置 -->
   <service android:name="com.igexin.getuiext.service.GetuiExtService" 
   	android:exported="false"
   	android:process=":pushservice" />
   <receiver android:name="com.igexin.getuiext.service.PayloadReceiver"
   android:exported="false" >
   		<intent-filter>
   			<!-- 这个com.igexin.sdk.action.7fjUl2Z3LH6xYy7NQK4ni4固定，不能修改  -->
   			<action android:name="com.igexin.sdk.action.7fjUl2Z3LH6xYy7NQK4ni4" />
   			<!-- android:name="com.igexin.sdk.action.第三方的appId" -->			
   			<action android:name="com.igexin.sdk.action.b03c5cfef65ed30108f0a3fd82c3f6b4" />
   		</intent-filter>
   </receiver>
   <service android:name="com.igexin.download.DownloadService"
   	android:exported="false" 
   		android:process=":pushservice" />
   	<provider android:authorities="increment.download.com.igexin.demo"
   		android:process=":pushservice"
   		android:exported="false" 
   		android:name="com.igexin.download.DownloadProvider" />
   <receiver
   		android:exported="false" android:name="com.igexin.download.DownloadReceiver"> 
   		<intent-filter>
   			<action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
   		</intent-filter>
   	</receiver>
   
   <receiver android:name="com.igexin.sdk.SdkReceiver">
   			<intent-filter>
   				<action android:name="android.intent.action.BOOT_COMPLETED" />
   				<action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
   				<action android:name="android.intent.action.USER_PRESENT" />
   			</intent-filter>
   		</receiver>
   		<receiver android:name="com.igexin.sdk.coordinator.NotificationCenterAIDLReceiver">
   			<intent-filter>
   				<action android:name="com.igexin.sdk.action.refreshls" />
   			</intent-filter>
   		</receiver>`
* 在Application标签外加入需要的权限：

  `<uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.WAKE_LOCK" />
  <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.VIBRATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
  <uses-permission android:name="getui.permission.GetuiService" />
  	
  <!-- 可选权限  -->
  <uses-permission android:name="android.permission.CALL_PHONE" />
  
  <!-- 自定义权限 -->
  <permission
      android:name="getui.permission.GetuiService"
      android:protectionLevel="normal" >
  </permission>`
* 运行您的工程，然后点击[个推开放平台] -> 应用管理 -> （对应应用）接入引导 -> ④ 测试SDK 的三个按钮，查看手机能否收到。

[下载地址]:http://developer.android.com/sdk/index.html
[个推]:http://www.igetui.com/
[个推开放平台]:http://dev.igetui.com

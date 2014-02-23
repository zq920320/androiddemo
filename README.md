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
  ``

[下载地址]:http://developer.android.com/sdk/index.html
[个推]:http://www.igetui.com/

# 树莓派命令行下登陆校园网


笔者是某理工大学的学生，我们学校的校园网是无密码登陆，但是需要网络认证，以下贴出我的登陆方法，可能只适用一部分人，仅供参考。

![登陆界面](https://cdn-std.dprcdn.net/files/acc_615569/VuKCWV)

因为我比较穷，没钱给树莓派配屏幕，只好想办法再命令行界面下进行登录认证，其实所谓的登录认证，也就是在本地向服务器POST一段数据，这段数据包含着你的登陆账户和密码。什么是POST呢，这是HTTP协议中的一个方法，具体详见 [维基百科-超文本传输协议](https://zh.wikipedia.org/zh-hans/%E8%B6%85%E6%96%87%E6%9C%AC%E4%BC%A0%E8%BE%93%E5%8D%8F%E8%AE%AE) 。我们首先来看它会向服务器POST什么数据，我使用 [Fiddler](https://www.telerik.com/fiddler) 进行数据的抓包，使用简易教程见 [Fiddler抓包简易教程](https://www.jianshu.com/p/9e05a2522758) 。你也可以使用浏览器的F12功能进行调试，只不过没有Fiddler方便。

![请求](https://cdn-std.dprcdn.net/files/acc_615569/oR1xgm)

我们可以看到发起了这么多的请求，其实只有一个是登陆时的POST请求，其他的是登陆成功后对其他资源的请求。我们可以看见第三条中的URL里面有一个**success**字样，这是登陆成功后返回的jsp页面。我们查看它的上一条请求在右侧窗口中可以看到请求的样式，我们不看Fiddler对请求和响应的格式分析结果，只看它们的原始字符串，单击**Raw**可以看到如下界面。

![这里写图片描述](https://cdn-std.dprcdn.net/files/acc_615569/JJFLif)

其中右上方的的字符串是POST请求的全部内容，右下方是登陆成功后的响应。我为什么不一个一个分析它的请求头响应头什么的？因为我接下来不会用HTTP协议向服务器发送请求，而是用TCP/IP协议发送请求。TCP/IP协议详见 [维基百科-TCP/IP](https://zh.wikipedia.org/zh-hans/TCP/IP%E5%8D%8F%E8%AE%AE%E6%97%8F)。
因为该请求是HTTP请求，所以服务器端口是80 。而服务器网址为 **auth.dlut.edu.cn** ，所以很容易发起一个TCP请求，然后获取它的响应。我们将Filder上面的请求字符串全部复制下来，然后通过TCP协议发送给服务器，然后等待响应字符串，简单粗暴。代码如下：

![代码](https://cdn-std.dprcdn.net/files/acc_615569/ItDvDy)

获得的相应如下：

![响应](https://cdn-std.dprcdn.net/files/acc_615569/J6ITT9)

可见已经成功了。

切到源代码文件目录，打开cmd，输入如下代码：

```
javac -encoding utf8 -d . .\Main.java 
```

该目录中会产生一个名为**http**的目录，将该目录移动到树莓派中去，目录里面是编译好的class字节码。
树莓派的raspbian系统自带jdk，所以直接运行如下代码即可.

```
java http.Main
```

但是在这之前需要在树莓派的配置文件中添加相应的wifi。如下代码：

```
sudo nano /etc/wpa_supplicant/wpa_supplicant.conf
```

在其中添加：

```
network={
        ssid="wifi名称"
        key_mgmt=NONE
}
```

重启树莓派，或者：

```
sudo iwconfig wlan0 essid wifi名称
```

即可连接校园网的wifi，然后执行之前的指令即可登陆成功。

代码地址：[github:crazyStrome](https://github.com/crazyStrome/RaspberryPiLoginWifi)
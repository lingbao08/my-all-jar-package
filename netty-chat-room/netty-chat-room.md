#netty 聊天室说明


##使用

启动下面的类：
 - `com.lingbao.nettyroom.start.ServerNetty` 服务端类
 - `com.lingbao.nettyroom.start.ClientNetty` 聊天室1
 - `com.lingbao.nettyroom.ClientNetty2` 聊天室2


然后就可以在ClientNetty和ClientNetty2的cmd窗口进行通话了。

-------------

`com.lingbao.nettyroom.pipeline`这个包定义了各个处理器，即要进行的操作。分为client和server。

`com.lingbao.nettyroom.pkg`这个包定义了编解码的一些信息。

`com.lingbao.nettyroom.pkg.cmd`这个包定义了用户可以进行的操作，也称为指令。


---------------

大体路径：
所有的请求经过command，根据指定packet分发到对应的server端的handler中去，然后server端的handler处理完后会给client端的handler中去。






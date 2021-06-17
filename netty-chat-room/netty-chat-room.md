#netty 聊天室说明


##使用

启动下面的类：
 - `com.lingbao.nettyroom.ServerNetty` 服务端类
 - `com.lingbao.nettyroom.ClientNetty` 聊天室1
 - `com.lingbao.nettyroom.ClientNetty2` 聊天室2


然后就可以在ClientNetty和ClientNetty2的cmd窗口进行通话了。

-------------

`com.lingbao.nettyroom.pipeline`这个包定义了各个处理器，即要进行的操作。分为client和server。

`com.lingbao.nettyroom.pkg`这个包定义了编解码的一些信息。

`com.lingbao.nettyroom.pkg.cmd`这个包定义了用户可以进行的操作，也称为指令。


---------------

最后，注释有点少，需要重新读一遍，然后补充注释和readme文档。







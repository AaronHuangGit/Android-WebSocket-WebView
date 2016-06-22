-由于android4.4版本以前webkit不支持websocket，所以需要用java建立websocket连接后，根据相对应的事件回调来调用js函数来实现android4.4版本以前webview对websocket
的支持。

-Websocket各平台支持情况：

http://caniuse.com/#feat=websockets

-github上与android java websocket相关的开源库，经调试有的不能支持最新版本websocket握手协议：

https://github.com/anismiles/websocket-android-phonegap
 
https://github.com/koush/AndroidAsync

https://github.com/koush/android-websockets

https://github.com/Gottox/socket.io-java-client

https://github.com/crossbario/autobahn-android

-websocket相关知识文章：

http://www.infoq.com/cn/news/2013/12/websocket-and-java

该项目使用 https://github.com/TooTallNate/Java-WebSocket 开源项目来实现java websocket连接，可选用Websocket Draft10、Draft17（websocket 13）、Draft75、Draft76版本草案协议


  

# windows-unix-synchronizer
Automatically synchronize file between your Window directory and Unix directory by FTP

平时还是喜欢在Windows上面干活，但是执行程序的时候还是想在Linux下面执行，比如写了一段代码，想放到Linux上面跑，这个时候不得不把手动拷贝文件过去
因此简单写了段代码，自动同步文件
eeeeehh，现在勉勉强强能跑起来，暂时满足我现在需求了，bug的话应该还是会有哦，边用边改进吧。要用的童鞋小心一点了，同步会直接删掉目标机器的源文件再传送过去的。
使用方式:下载代码，修改Main.java里面的参数之后直接运行Main类即可

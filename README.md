# IRMusicPlayer音乐播放器

- compileSdkVersion 29
- buildToolsVersion "29.0.3"
- minSdkVersion 27
- targetSdkVersion 29

## 使用到的开源项目

- 界面控件
    - CircleImageView

- 数据库管理
    - Room

## 涉及到的Android技术

- 异步交互
    - 系统广播（Broadcast）
    - Binder
    - 异步任务（AsyncTAsk）
    - Handler

- 自定义控件
    - 自定义ImageView
    - 自定义标题栏
    - 自定义StatusBar
    - 沉浸式

- 基本控件
    - SwipeRefreshLayout
    - RecyclerView
    - Fragment
    - PopupWindow

- Jni编程
    - 高斯模糊（后采用RenderScript的方式进行图像处理）

## 改进方向

1. ~~取消下滑刷新，改为ContentObserver观察媒体音乐库的变化，onChange()时，刷新List~~
2. 刷新时需要取消音乐播放
3. 历史记录需要用数据库记录，而不是arrayList
4. 添加歌单信息，而不是只有一个列表
5. 添加启动优化，最好是可以在打开时播放一个动画
6. 添加音效控制器的功能，需要数据持久存储 + 恢复默认设置
7. 歌词显示和滚动
8. 网络部分 ... ...
    
## IRMusic功能大全

1. 播放错误自动切歌
2. 支持手动刷新和自动刷新本地音乐库
3. 支持听歌历史记录查询
4. 界面模糊背景
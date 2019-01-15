## WanAndroid

基于WanAndroid api开发的技术类文章阅读App.

### 技术点

* **Retrofit+LiveData封装网络请求，支持https**
* 使用**SwipeRefreshLayout+BaseQuickAdapter**实现列表的下拉刷新和上拉加载
* 使用**FlowLayout**动态添加各种样式标签
* 使用**DrawerLayout+BottomNavigationView+Fragment**实现侧边栏加底部导航的App整体结构
* 使用**TabLayout+ViewPager+Fragment**实现顶部导航布局
* **双RecyclerView滑动联动**，效果见导航界面
* 使用自定义MultiStatusView实现多状态布局
* 使用**Glide**加载图片
* 使用**Theme.AppCompat.DayNight主题实现夜间模式**，切换几乎无抖动
* **适配到9.0**
* 使用Facebook的[ShimmerFrameLayout](https://github.com/facebook/shimmer-android)布局实现闪光文字
* 使用动画实现**卡片翻转效果**，见登录注册界面
* **bugly日志上报和版本更新**

其中大部分组件都是**support.design**包下的，非常方便使用。

#### TODO

* 公众号模块
* 优化代码结构

### 效果图

![](https://github.com/arvinljw/WanAndroid/blob/master/images/1.png)

![](https://github.com/arvinljw/WanAndroid/blob/master/images/2.png)

![](https://github.com/arvinljw/WanAndroid/blob/master/images/3.png)

![](https://github.com/arvinljw/WanAndroid/blob/master/images/4.png)

![](https://github.com/arvinljw/WanAndroid/blob/master/images/5.png)

[apk下载](https://github.com/arvinljw/WanAndroid/blob/master/WanAndroid.apk)

***注意：以后APK不再跟随修改代码更新，需要最新apk，可自行下载编译运行即可；目前最高版本是v1.0.6，最新提交是19/1/7，没有支持9.0，最新代码已支持***


**扫描二维码下载**

![](https://github.com/arvinljw/WanAndroid/blob/master/images/img_apk_download.png)

### 致谢

感谢WanAndroid提供的数据来源，也要感谢[iceCola7的WanAndroid项目](https://github.com/iceCola7/WanAndroid)功能上有一定的参考。

感谢用到的所有开源库提供的作者。

### License

```
Copyright 2018 arvinljw 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
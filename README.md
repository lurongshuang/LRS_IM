# LRS_IM
  1.#基于融云SDK的及时通讯、用户数据存储使用后端云SDK。
  <br> 
  ##如果想要换成自己的key：
  <br> 
  ###1.需要将  
  ```java
  Bmob.initialize(this, "1ad4db4503319e703ac4127b78827df5");
  ```
  换成自己的 后端云KEy  创建表与bean 下的 User、Group、GroupId_with_UserId实体类同步。
  <br> 
  ###2. 替换成为自己的融云key
  ```java
  RongIM.init(this, "8luwapkv84hyl"); 
  <meta-data
    android:name="RONG_CLOUD_APP_KEY"
    android:value="8luwapkv84hyl" />
  ```
  <br> 
  ###3.替换自己的高德地图key 
  <br> 
  ```java
  <meta-data
        android:name="com.amap.api.v2.apikey"
        android:value="7e7a14f0a15685e911530d3ba9f204fa" />
 ```
<br> 
<div>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/1.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/2.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/3.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/4.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/5.jpg" width="200"/>
</div>

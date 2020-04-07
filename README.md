# LRS_IM
  # 基于融云SDK的及时通讯、用户数据存储使用后端云SDK。
  
  没有后台接口，请求都在移动端发起：获取token、创建群组等。
  
  支持单聊、群聊、创建群组、发送文字、图片、位置、小视频、表情、音视频等。
  
  ## 如果想要换成自己的key：
  
  ### 1.替换自己的 后端云KEy  创建表与bean 下的 User、Group、GroupId_with_UserId实体类同步。
  ```java
  Bmob.initialize(this, "1ad4db4503319e703ac4127b78827df5");
  ```
  
  ### 2. 替换成为自己的融云key
  ```java
  RongIM.init(this, "8luwapkv84hyl"); 
  <meta-data
    android:name="RONG_CLOUD_APP_KEY"
    android:value="8luwapkv84hyl" />
  ```
  
  ### 3.替换自己的高德地图key 
  
  ```java
  <meta-data
        android:name="com.amap.api.v2.apikey"
        android:value="7e7a14f0a15685e911530d3ba9f204fa" />
 ```
 ### 测试apk > https://www.pgyer.com/BOfn
 
<div>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/1.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/2.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/3.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/4.jpg" width="200"/>
<img src="https://github.com/lurongshuang/LRS_IM/blob/master/image/5.jpg" width="200"/>
</div>

#
####  源码
[ijk-so编译源码](https://github.com/153437803/ijkplayer-k0.8.8-ff4.0)

#
####  版本
```
exoplayer => r2.18.0
lib_mediaplayer_core_exoplayer_r2.18.0
```
```
ijkplayer轻量版本 => k0.8.8 => ff4.0--ijk0.8.8--20210426--001
lib_mediaplayer_core_ijkplayer_k0.8.8_ff4.0_lite
```
```
ijkplayer全量版本 => 支持rmvb、avi => k0.8.8 => ff4.0--ijk0.8.8--20210426--001
lib_mediaplayer_core_ijkplayer_k0.8.8_ff4.0_full
```

#
#### 更新
```
----------------------------------------

2022-06-02

1.解决快进bug

----------------------------------------

2022-02-19

1.更新ffmpeg4.0版本, ijk软解

----------------------------------------

2022-02-11

1. 新增ijkplayer硬解

----------------------------------------

2021-12-19

1. 解决android6.0 crash [指定版本=>'com.google.guava:guava:30.1-android']

----------------------------------------

2021-12-15

1. 更新exoplayer2.16.1

----------------------------------------

2021-11-23

1. 优化ui
2. 删除冗余模块
3. 添加rtsp支持

---------------------------------------

2021-09-29

1. 更新exoplayer-2.15.0
2. 重构lib_mediaplayer_ui模块
```

#
#### 记录
```
1. android 6.0 crash[指定版本=>'com.google.guava:guava:30.1-android']

2. ijk-ff4.0--ijk0.8.8--20210426--001 => 编译错误
   需要修改 ffmpeg 编译配置：config/module-lite.sh
   #export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --disable-ffserver"
   #export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --disable-vda"
   export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --disable-bsf=eac3_core"
   编译ffmpeg4不过时，正如debugly 所说，需要注释掉2行-disable-ffserver和--disable-vda，然后添加一行把eac3_cored配置disable掉--disable-bsf=eac3_core，然后clean掉，重新编译ffmpeg就好了

3. compile-ijk.sh 不生成ijkplayer.so、ijksdk.so
   android/ijkplayer/xx/src/main/jin/Android.mk => 末尾新增 => include ../../../../../../ijkmedia/*.mk
   
4. ffmpeg 开启neon
   do-comfile-ffmpeg.sh 修改 FF_ASSEMBLER_SUB_DIRS="arm neon"
```

#
#### ffmpeg
```
模块：
libavutil：核心工具库，该模块是最基本的模块之一，其它这么多模块会依赖此模块做一些音视频处理操作。
libavformat： 文件格式和协议库，该模块是最重要的模块之一，封装了Protocol层、Demuxer层、muxer层，使用协议和格式对于开发者是透明的。
libavcodec: 编解码库，该模块也是最重要模块之一，封装了Codec层，但是有一些Codec是具备自己的License的，FFmpeg是不会默认添加，例如libx264,FDK-AAC, lame等库，但FFmpeg就像一个平台一样，可以将其它的第三方的Codec以插件的方式添加进来，然后为开发者提供统一的接口。
libswrsample：音频重采样库，可以对数字音频进行声道数、数据格式、采样率等多种基本信息的转换。
libswscale：视频压缩和格式转换库，可以进行视频分辨率修改、YUV格式数据与RGB格式数据互换。
libavdevice：输入输出设备库，编译ffplay就需要确保该模块是打开的，时时也需要libSDL预先编译，因为该设备播放声音和播放视频使用的都是libSDL库。
libavfilter:音视频滤镜库，该模块包含了音频特效和视频特效的处理，在使用FFmpeg的API进行编解码的过程中，直接使用该模块为音视频数据做特效物理非常方便同时也非常高效的一种方式。
libpostproc:音视频后期处理库，当使用libavfilter的时候需要打开该模块开关，因为Filter中会使用该库中的一些基础函数。

编译：
1. 安装相关工具
   apt-get update
   apt-get install git
   apt-get install yasm
2. 配置系统环境变量
   /etc/profile
   export ANDROID_NDK=/home/kalu/Android/android-ndk-r14b
   export PATH=$ANDROID_NDK:$PATH
   export ANDROID_SDK=/home/kalu/Android/Sdk
   export PATH=${PATH}:$ANDROID_SDK/tools:$ANDROID_SDK/platform-tools
3. 设置的环境变量生效[ndk-build -v、adb version]
   source /etc/profile
4. 下载ijkplayer-android源码
   git clone https://github.com/Bilibili/ijkplayer.git ijkplayer-android
5. 下载ijkffmpeg、libyuv、libsoundtouch、openssl
   cd ijkplayer-android
   git checkout -B latest k0.8.8
   ./init-android.sh
   ./init-android-openssl.sh
6.  编译openssl
   cd android/contrib
   ./compile-openssl.sh clean
   ./compile-openssl.sh all
7. 编译ffmpeg
   cd ../..
   cd config
   rm module.sh
   ln -s module-lite.sh module.sh
   cd ..
   cd android/contrib
   ./compile-ffmpeg.sh clean
   ./compile-ffmpeg.sh all
8. 编译ijlayer
   cd ..
   ./compile-ijk.sh all
```

#
####  exoplayer
```
jar：
files('libs/androidx-annotation-1.2.0.jar')
files('libs/commons-io-2.5.jar')
files('libs/guava-30.1-android.jar')
files('libs/failureaccess-1.0.1.jar')
files('libs/checker-qual-2.5.0.jar')
files('libs/checker-compat-qual-2.5.0.jar')
files('libs/j2objc-annotations-1.3.jar')
files('libs/error_prone_annotations-2.10.0.jar')
files('libs/annotations-3.0.1.jar')
```
![image](https://github.com/153437803/module_mediaplayer/blob/master/image/exoplayer.png )

#
####  计划
```
- [✓] 支持TV
- [✓] 支持exoplayer硬解
- [✓] 支持ijkplayer软解
- [✓] 编译更新ffmpeg4.0
```

#
#### 资料
```
https://github.com/google/ExoPlayer
https://mvnrepository.com/search?q=exoplayer
https://github.com/bilibili/ijkplayer
https://github.com/bilibili/FFmpeg/tags
```

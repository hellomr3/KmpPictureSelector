# KMPPictureSelector

这是一款适用于Kotlin MultiPlatform的图片选择库，复用平台开源库能力。支持从相册中获取图片、视频、压缩、裁剪图片等功能，是一个开源的图片选择框架 ，适用于安卓5.0+系统，IOS 12.0+系统

## 平台能力支持

### Android

- [x] 拍摄照片
- [x] 拍摄视频
- [x] 图片选择
- [x] 图片选择-裁剪
- [x] 图片选择-压缩

## IOS

- [x] 拍摄照片
- [ ] 拍摄视频 （计划中）
- [x] 图片选择
- [x] 图片选择-裁剪
- [x] 图片选择-压缩

## 基于

Android平台基于[https://github.com/LuckSiege/PictureSelector](https://github.com/LuckSiege/PictureSelector)

IOS平台基于[https://github.com/banchichen/TZImagePickerController](https://github.com/banchichen/TZImagePickerController)

## 怎么使用？

### 依赖

```auto
// setting.gradle.kts
repositories {
  maven { url = uri("https://central.sonatype.com") }
}
// build.gradle.kts
val commonMain by getting {
    dependencies {
      // 基础能力
      implementation("io.github.hellomr3:KmpPictureSelector:0.0.1")
      // compose
      implementation("io.github.hellomr3:KmpPictureSelectorCompose:0.0.1")
    }
} 
```

### IOS

添加相机和相册权限

```auto
<key>NSCameraUsageDescription</key>
<string>需要使用相机</string>
<key>NSPhotoLibraryUsageDescription</key>
<string>需要使用相册</string>
```

### 使用原生相机

```auto
// compose
@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val pictureSelector = rememberPictureSelect()
    Scaffold(modifier = Modifier.statusBarsPadding()) {
        var pictureMedia by remember { mutableStateOf<Media?>(null) }
        //
        Column {
            Button(onClick = {
                scope.launch {
                    pictureMedia =
                        pictureSelector.takePhoto(
                            params = PictureSelectParams(
                                maxImageNum = 1,
                                isCrop = true
                            )
                        )
                            .firstOrNull()
                            ?.getOrNull()?.firstOrNull()
                }
            }) {
                Text("拍摄照片", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Text("图片路径:${pictureMedia?.path}")
            AsyncImage(
                model = pictureMedia?.preview?.toByteArray(),
                contentDescription = "Image",
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            )
        }
    }
}
```

### 选择图片

```auto
@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val pictureSelector = rememberPictureSelect()
    Scaffold(modifier = Modifier.statusBarsPadding()) {
        var pictureMedia by remember { mutableStateOf<Media?>(null) }
        Column {
            Button(onClick = {
                scope.launch {
                    pictureMedia =
                        pictureSelector.selectPhoto(
                            params = PictureSelectParams(
                                maxImageNum = 1,
                                maxVideoNum = 1,
                                isCrop = true
                            )
                        ).firstOrNull()
                            ?.getOrNull()?.firstOrNull()
                }
            }) {
                Text("选择图库", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Text("图片路径:${pictureMedia?.path}")
            AsyncImage(
                model = pictureMedia?.preview?.toByteArray(),
                contentDescription = "Image",
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            )
        }
    }
}
```

## Sample

### Android

使用Android Studio 运行samples/composeApp/androidApp下的安卓应用

### IOS

```auto
// 找到对应IOS目录
cd samples/iosApp
// 安装依赖
pod install
// 使用Android Studio或Xcode运行
```

更多内容请查看项目中samples对应部分代码

## License

```auto
Copyright 2024 hellomr3
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

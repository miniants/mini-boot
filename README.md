# mini-boot
## 一、发版

### 1.在修订版本并提交

### 2.在根目录执行myPublishToMavenCentral就自动发布到中央仓库了。
> https://central.sonatype.com/publishing 可以查看发布的组件。
> 需要人工确认一下正式发布。10-30分钟左右正式发布完成，中央仓库就可以检索到了。

## 二、发版配置
### 1.安装gpg（如果是win）
#### 官方下载：https://gpg4win.org
#### 直接安装 gpg4win-x.x.x.exe,一路默认
### 2.生成签名私钥和公钥，并上传到公开的验证服务器
#### 生成密钥Central 对正式发布仍要求 PGP 签名：
> gpg --full-generate-key
> 
> gpg --list-secret-keys --keyid-format=long

#### 导出秘钥为asii或者文件,你的KEY_ID为上面list 的sec 后8位就行
>gpg --export-secret-keys --armor <你的KEY_ID>
>
>gpg --export-secret-keys --armor <你的KEY_ID> > private.asc

#### 保存私钥文件或者通过InMemory的方式存到gradle.properties文件中
```properties
signingInMemoryKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n\
\n\
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDo2QHp3wZ6qkEs\n\
hWzqJY7Xq7j7M8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7\n\
...（更多行）...\n\
K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7K8Z7\n\
-----END PGP PRIVATE KEY BLOCK-----
signingInMemoryKeyPassword=n1985223
```
> 特别要注意：
> 1. properties文件中key字符串必须\n\连接下一行。
> 2. 私钥必须有一个空行引导行
> 3. 如果私有有密码，需要设置：signingInMemoryKeyPassword=n1985223
> 4. properties可以放到项目根目录或者gradle用户主目录的properties文件中。后者安全。前者可以私有仓库公用。


#### 将秘钥上传到公开的验证服务器：
> gpg --keyserver keyserver.ubuntu.com --send-keys 6F4EB4CA3FF8A68A

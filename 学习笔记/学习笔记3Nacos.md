# Nacos注册中心的安装

是阿里巴巴的产品，目前已经是springcloud的组件了

### 下载安装包

进入[github](https://github.com/alibaba/nacos)下载，进入右侧releases：

<img src="学习笔记3Nacos.assets/image-20240410142033909.png" alt="image-20240410142033909" style="zoom:50%;" />



选择tags：

<img src="学习笔记3Nacos.assets/image-20240410142245038.png" alt="image-20240410142245038" style="zoom:50%;" />

选择版本（教程选的1.4.1）：

![image-20240410142438242](学习笔记3Nacos.assets/image-20240410142438242.png)

### 端口检查

解压到非中文路径，先查看端口8848是否被占用

```bash
netstat -ano | findstr "8848"
```

如果被占用，需要进入nacos的conf目录，修改配置文件application.properties中的端口。

<img src="学习笔记3Nacos.assets/image-20240410142718245.png" alt="image-20240410142718245" style="zoom:50%;" />

### 启动nacos

终端进入bin文件夹，输入命令

```bash
.\startup.cmd -m standalone
```

效果：

<img src="学习笔记3Nacos.assets/image-20240410142840157.png" alt="image-20240410142840157" style="zoom:50%;" />

### 进入控制台

ctrl键访问上图中，打印图案右侧的`Console: http://10.210.215.165:8848/nacos/index.html` 

浏览器被启动：

<img src="学习笔记3Nacos.assets/image-20240410143100100.png" alt="image-20240410143100100" style="zoom:50%;" />

输入用户名和密码，默认都是`nacos` ，点击提交后，进入控制台：

<img src="学习笔记3Nacos.assets/image-20240410143228718.png" alt="image-20240410143228718" style="zoom:50%;" />



# nacos服务注册与服务发现

### 依赖的添加和修改

父工程里需要添加nacos相关的依赖，因为是对子工程的管理，所以需要放在`dependencyManagement` 标签内的`dependencies` 内：

```
<!-- nacos -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.5.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

子工程里需要用nacos替代eureka，即把eureka的依赖注释掉，添加nacos的依赖：

```xml
        <!--eureka客户端依赖-->
<!--        <dependency>-->
<!--            <groupId>org.springframework.cloud</groupId>-->
<!--            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>-->
<!--        </dependency>-->
        <!-- nacos客户端依赖包 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```

### 修改配置文件

原本配置文件里是对eureka的配置，现在需要换成nacos，所以需要先把对eureka的配置注释掉：
```xml
#eureka:
#  client:
#    service-url:
#      defaultZone: http://127.0.0.1:12345/eureka
```

然后添加eureka的配置，它属于spring的配置，所以加在`spring:` 下面：

```xml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848
```

如下图所示，可以看到端口默认也是8848，所以这里其实可配可不配，但最好还是配上，便于将来修改

<img src="学习笔记3Nacos.assets/image-20240410145005148.png" alt="image-20240410145005148" style="zoom:50%;" />

备注：每个子工程（这个案例中是order和user，eureka不要写，因为我们已经不用它了）都要完成**依赖修改**和**配置文件**的修改

### 项目，启动！

先按照前面的步骤，启动nacos。

再在idea中启动服务，eureka此时不用再启动了：

<img src="学习笔记3Nacos.assets/image-20240410145826709.png" alt="image-20240410145826709" style="zoom:50%;" />

打开nacos的控制台，可以看到目前启动的三个服务：

![image-20240410145931859](学习笔记3Nacos.assets/image-20240410145931859.png)

可以点开详情看到集群具体包含的所有实例，这里点击user-service的详情看看：

<img src="学习笔记3Nacos.assets/image-20240410150259484.png" alt="image-20240410150259484" style="zoom:50%;" />

测试访问`http://localhost:8080/order/102` ，正确输出了包含user信息的结果：

<img src="学习笔记3Nacos.assets/image-20240410150029243.png" alt="image-20240410150029243" style="zoom:50%;" />

可以看到日志，UserApplication2服务实例被调用了：
<img src="学习笔记3Nacos.assets/image-20240410150123453.png" alt="image-20240410150123453" style="zoom:50%;" />

也就是说，服务发现部分和eureka还是一样的，不需要修改，服务注册只需要添加依赖和application配置一下端口号就行了
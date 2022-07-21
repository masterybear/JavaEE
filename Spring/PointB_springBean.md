## DI

依赖注入：Dependency Injection

容器动态的将`应用程序所需要的资源`注入到组件之中。

## JNDI

Java Naming and Directory Interface：Java命名和目录接口

# 装配 Spring Bean

> 依赖注入的工作由Spring容器完成
> 应用程序所需要的资源被称为Bean，容器装配完成Bean以后，才能进行依赖注入。

3种依赖注入的方式：

- XML
- Java
- 自动

# XML

通过XML文件的方式装配Bean

ClassPathXmlApplicationContext

---

## 构造器注入

有参构造方法与无参构造方法

```java
public User() {
    System.out.println("------------init-------------");
}

public User(String username, String email, Integer id) {
    this.username = username;
    this.email = email;
    this.id = id;
}
```

在Spring配置文件中配置Bean

```xml
<bean class="org.example.springDemo.User" id="user1">
    <constructor-arg index="0" value="张三"/>
    <constructor-arg index="1" value="zhangsan@email.com"/>
    <constructor-arg index="2" value="1"/>
</bean>

<bean class="org.example.springDemo.User" id="user2"/>
```

> user1为有参构造方法
> user2为无参构造方法

## 使用 setter 注入

name的属性是容器根据set方法自动生成的名字，本质上是调用set方法去修改对象的属性，而不是直接设置对象的属性。

```xml
<bean class="org.example.springDemo.User" id="user3">
    <property name="username" value="李四"/>
    <property name="email" value="lisi@email.com"/>
    <property name="id" value="2"/>
</bean>
```

### main方法

```java
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
User user1 = (User) context.getBean("user1");
User user2 = (User) context.getBean("user2");
User user3 = (User) context.getBean("user3");
System.out.println("user1 = " + user1);
System.out.println("--------------------------------------------");
System.out.println("user2 = " + user2);
System.out.println("--------------------------------------------");
System.out.println("user3 = " + user3);
```

## 接口注入

略

## 工厂注入

为什么要使用工厂注入？

```java
OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
```

像OkHttp这种的对象，创建过程未使用构造器，更没有set方法。

两种工厂注入的方式

静态工厂注入

```java
public class OkHttpStaticFactory {
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getInstance() {
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder().build();
        }
        return okHttpClient;
    }
}
```

```xml
<bean class="org.example.OkHttpStaticFactory" factory-method="getInstance" id="okHttpClient"/>
```

动态工厂注入

```java
public class OkHttpFactory {
    private OkHttpClient okHttpClient;

    public OkHttpClient getInstance() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().build();
        }
        return okHttpClient;
    }
}
```

```xml
<bean class="org.example.OkHttpFactory" id="okHttpFactory"/>
<bean class="okhttp3.OkHttpClient" factory-bean="okHttpFactory" factory-method="getInstance" id="okHttpClient"/>
```

```java
ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
OkHttpClient okHttpClient = (OkHttpClient) ctx.getBean("okHttpClient");
```

---

## 复杂属性注入

### 对象注入

```xml
<bean class="org.example.springDemo.User" id="user3">
    <property name="username" value="王五"/>
    <property name="email" value="wangwu@email.com"/>
    <property name="id" value="3"/>
    <property name="cat" ref="cat"/>
</bean>

<bean class="org.example.springDemo.Cat" id="cat">
    <property name="age" value="3"/>
    <property name="name" value="六一"/>
</bean>
```

```java
public class Cat {
    private String name;
    private Integer age;
   
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Integer getAge() {return age;}
    public void setAge(Integer age) {this.age = age;}
}
```

### 数组注入

list节点和array节点是可以相互替换的。

```xml
<bean class="org.example.springDemo.User" id="user4">
    <property name="cats">
        <array>
            <ref bean="cat1"/>
            <ref bean="cat2"/>
            <bean class="org.example.springDemo.Cat" id="cat3">
                <property name="name" value="八一"/>
                <property name="age" value="2"/>
            </bean>
        </array>
    </property>

    <property name="hobby">
        <list>
            <value>足球</value>
            <value>乒乓球</value>
            <value>羽毛球</value>
        </list>
    </property>
</bean>
```

```java
public class User {
    private Cat[] cats;
    private List<String> hobby;

    public Cat[] getCats() {return cats;}
    public void setCats(Cat[] cats) {this.cats = cats;}
    public List<String> getHobby() {return hobby;}
    public void setHobby(List<String> hobby) {this.hobby = hobby;}
}
```

### Map 注入

### Properties 注入

```xml
<property name="details">
    <map>
        <entry key="gender" value="男"/>
        <entry key="age" value="30"/>
    </map>
</property>

<property name="info">
    <props>
        <prop key="phone">124356789</prop>
    </props>
</property>
```

```java
public class User {
    private Map<String, Object> details;
    private Properties info;
    
    public Properties getInfo() {return info;}
    public void setInfo(Properties info) {this.info = info;}
    public Map<String, Object> getDetails() {return details;}
    public void setDetails(Map<String, Object> details) {this.details = details;}
}
```

# Java

通过Java注解的方式装配Bean

AnnotationConfigApplicationContext

---

## @Configuration

> **作用：指定当前类是一个配置类**

示例：

```java
//功能类
public class SayHello {
    public String sayHello(String name) {
        return "Hello_" + name;
    }
}
```

```java
//配置类
@Configuration
public class JavaConfig {
    @Bean
    SayHello sayHello() {
        return new SayHello();
    }
}
```

```java
//测试类
public class JavaConfigTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
        SayHello sayHello = ctx.getBean("sayHello", SayHello.class);
        System.out.println(sayHello.sayHello("张三"));
    }
}
```

```java
运行结果：Hello_张三
```

# 自动扫描注入

---

- 在`Service`层上，添加注解时，使用`@Service`
- 在`Dao`层，添加注解时，使用`@Repository`
- 在`Controller`层，添加注解时，使用`@Controller`
- 其他组件上添加注解时，使用`@Component`

## @ComponentScan

> **作用：用于通过注解指定spring在创建容器时要扫描的包**

**标记类**

```java
@Service
public class UserService {
    public List<String> AllUsers() {
        ArrayList<String> users = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            users.add("user_" + i);
        }
        return users;
    }
}
```

**配置类**

```java
@Configuration
@ComponentScan
//@ComponentScan(basePackages = "org.example.service",useDefaultFilters = false,includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Service.class)})
//@ComponentScan(basePackages = "org.example.service",useDefaultFilters = false)
public class JavaConfig {
	...
}
```

**测试类**

```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
UserService userService = ctx.getBean(UserService.class);
List<String> allUsers = userService.getAllUsers();
System.out.println("allUsers: " + allUsers);
```

运行结果

```java
user_[user_0, user_1, user_2, user_3, user_4, user_5, user_6, user_7, user_8, user_9]
```

另外再看一下XML自动扫描的注入方式

配置文件

```xml
<context:component-scan base-package="org.example.service" use-default-filters="false">
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Service"/>
</context:component-scan>
```

测试类

```java
ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
m5(ctx);
private static void m5(ClassPathXmlApplicationContext ctx) {
        UserService userService = ctx.getBean(UserService.class);
        List<String> allUsers = userService.getAllUsers();
        System.out.println("user_" + allUsers);
    }
```

---

**对象注入**

对象注入，即将程序所需要的对象动态的完成注入。被调用的程序需要其他类的对象，所以在该程序需要此对象时完成对象注入。

## @Autowired

> **作用：自动按照类型注入，从spring容器中去寻找满足的目标对象并且注入。**

## @Resource

> **作用：直接按照bean的id注入。**

示例：

标记类

```java
@Repository
public class UserDao {
    public String hello() {
        return "hello";
    }
}
```

进行对象注入的类

```java
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public List<String> getAllUsers() {

        String hello = userDao.hello();
        System.out.println("hello = " + hello);

        ArrayList<String> users = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            users.add("user_" + i);
        }
        return users;
    }
}
```

配置类

```java
@Configuration
@ComponentScan
//@ComponentScan(basePackages = "org.example",useDefaultFilters = false,includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Service.class),@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Repository.class)})
//@ComponentScan(basePackages = "org.example.service",useDefaultFilters = false)
public class JavaConfig {
    ...
}
```

## @Conditional

此注释将提供一个条件用以判断调度。

在依赖注入时，会根据此注解所提供的条件进行合理的注入。

示例：

```java
public class JavaConfig {
    @Bean("cmd")
    @Conditional(WindowsCondition.class)
    ShowCmd winCmd() {
        return new WindowsShowCmd();
    }

    @Bean("cmd")
    @Conditional(LinuxCondition.class)
    ShowCmd linuxCmd() {
        return new LinuxShowCmd();
    }
}
```

# Spring IoC概念

Spring指的是Spring Framework，即Spring框架。

## Spring IoC 概述

控制反转—— IoC：Inversion of Control

控制：指的是控制对象创建的能力

控制反转，即将创建对象的能力交由程序本身，使其本身能动态的创建自己所需要的对象，而无需人为控制。

> IoC 定义：控制反转是一种通过描述（XML或注释）并通过第三方去产生或获取特定对象的方法。

以下是示例程序：

```java
public class User {
    private String username;
    private String email;
    private Integer id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
```

主动创建：

```java
public class Main {
    public static void main(String[] args) {
        User user01 = new User();
        System.out.println("user ="+user01);
    }
}
```

示例中，我们仍需人为的创建程序所需的对象，此时没有使用控制反转。

控制反转：

```java
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = (User) context.getBean("user");
        System.out.println("user = " + user);
    }
}
```

配置文件（ applicationContext.xml ）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean class="org.example.springDemo.User" id="user"/>
</beans>
```

如此，程序就可以动态的控制对象的创建、而不在需要人为的控制了。

## Spring IoC 容器

设计主要基于两个接口：`BeanFactory`和`ApplicationContext`。

BeanFactory是IoC的最底层设计；ApplicationContext也是BeanFactory的子接口。

有关源码：

```java
public interface BeanFactory {
    String FACTORY_BEAN_PREFIX = "&";

    Object getBean(String var1) throws BeansException;

    <T> T getBean(String var1, Class<T> var2) throws BeansException;

    Object getBean(String var1, Object... var2) throws BeansException;

    <T> T getBean(Class<T> var1) throws BeansException;

    <T> T getBean(Class<T> var1, Object... var2) throws BeansException;

    <T> ObjectProvider<T> getBeanProvider(Class<T> var1);

    <T> ObjectProvider<T> getBeanProvider(ResolvableType var1);

    boolean containsBean(String var1);

    boolean isSingleton(String var1) throws NoSuchBeanDefinitionException;

    boolean isPrototype(String var1) throws NoSuchBeanDefinitionException;

    boolean isTypeMatch(String var1, ResolvableType var2) throws NoSuchBeanDefinitionException;

    boolean isTypeMatch(String var1, Class<?> var2) throws NoSuchBeanDefinitionException;

    @Nullable
    Class<?> getType(String var1) throws NoSuchBeanDefinitionException;

    @Nullable
    Class<?> getType(String var1, boolean var2) throws NoSuchBeanDefinitionException;

    String[] getAliases(String var1);
}
```
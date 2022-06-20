# 一、动态代理

---

动态代理技术有很多种，其中spring中常用的为以下两种。

动态代理技术，指的是在程序的运行过程中动态的生成代理，代理思想的主要意义是生成一个占位去代替真实对象与调用者进行交互，这种关系更像是中间人，从始至终用户都接触不到真实对象，但工作却仍由真实对象进行。

## 1、JDK动态代理

必须借助一个接口才能产生代理对象。所以需要定义接口和实现类才能开始编写代理类。

代理类需要实现（java.lang.reflect.InvocationHandler）接口。

> Invocation Handler：调用处理程序

### 实例

```java
public class JdkProxyExample implements InvocationHandler {
    private Object target = null;		//创建真实对象

    public Object bind(Object target) {
        this.target = target;
        //创建代理对象，需要三个数据（参数）：真实对象的类加载器|代理对象的目标位置|实现代理逻辑的类
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), 
                                      target.getClass().getInterfaces(),
                                      this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("进入代理逻辑方法");
        System.out.println("进行调度真实对象之前的服务");
        Object obj = method.invoke(target, args);
        System.out.println("进行调度真实对象之后的服务");
        return obj;
    }
}
```

1. 创建绑定方法（将真实对象与代理对象进行绑定，并返回代理对象）

2. 实现代理逻辑


### 测试类

```java
public void testProxy() {
        JdkProxyExample jdk = new JdkProxyExample();		//创建代理类对象
    
    	//创建代理对象，接下来会由代理对象去调用方法，但实际上还是由真实对象完成
        HelloWorld proxy = (HelloWorld) jdk.bind(new HelloWorldImpl());
        proxy.sayHelloWorld();
    }
```



## 2、CGLIB动态代理

无需借助接口便可完成代理功能，适用于不使用接口的时候使用。

代理类需要实现第三方（net.sf.cglib.proxy.MethodInterceptor）接口。

> Method Interceptor：方法拦截器
>
> enhancer：增强者
>
> intercept：拦截

### 实例

```java
public class CglibProxyExample implements MethodInterceptor {

    public Object getProxy(Class cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);				// 设置超类      真实对象所处的类
        enhancer.setCallback(this);					// 设置代理类    实现代理方法的类
        return enhancer.create();					// 返回代理对象
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("调用真实对象前");
        Object result = methodProxy.invokeSuper(proxy, args);
        System.out.println("调用真实对象后");
        return result;
    }
}
```

1. 返回代理对象的方法

2. 实现代理逻辑方法


### 测试类

```java
public void testProxy() {
        CglibProxyExample cpe = new CglibProxyExample();
        ReflectServiceImpl obj = (ReflectServiceImpl) cpe.getProxy(ReflectServiceImpl.class);
        obj.sayHello("张三");
    }
```



## 3、拦截器

拦截器接口用于方便开发者编写代理程序，有了拦截器接口，开发者将不必得知底层实现，便可通过实现接口的方式完成代理程序的编写。

### 实例

1、拦截器接口

```java
public interface Interceptor {
    public boolean before(Object proxy, Object target, Method method,Object[]args);
    public void around(Object proxy, Object target, Method method,Object[]args);
    public void after(Object proxy, Object target, Method method,Object[]args);
}
```

2、接口实现

```java
public class MyInterceptor implements Interceptor{
    @Override
    public boolean before(Object proxy, Object target, Method method, Object[] args) {
        System.err.println("反射方法前逻辑");
        return false;
    }

    @Override
    public void around(Object proxy, Object target, Method method, Object[] args) {
        System.err.println("取代了真实对象的方法");
    }

    @Override
    public void after(Object proxy, Object target, Method method, Object[] args) {
        System.err.println("反射方法后逻辑");
    }
}
```

3、代理类

```java
public class InterceptorJdkProxy implements InvocationHandler{
    private Object target = null;
    private String interceptorClass = null;

    public InterceptorJdkProxy(Object target,String interceptorClass) {
        this.target = target;
        this.interceptorClass = interceptorClass;
    }

    public static Object bind(Object target, String interceptorClass) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InterceptorJdkProxy(target, interceptorClass));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (interceptorClass == null){
            return method.invoke(target, args);
        }
        Object result = null;
        Interceptor interceptor = (Interceptor) Class.forName(interceptorClass).newInstance();
        if (interceptor.before(proxy,target,method,args)){
            result = method.invoke(target, args);
        }else {
            interceptor.around(proxy,target,method,args);
        }
        interceptor.after(proxy,target,method,args);
        return result;
    }
}
```

### 测试类

```java
public class TestInterceptor {
    @Test
    public void MyInterceptor() {
        HelloWorld proxy = (HelloWorld) InterceptorJdkProxy.bind(new HelloWorldImpl(), "org.example.interceptor.MyInterceptor");
        proxy.sayHelloWorld();
    }
```

## 4、责任链模式

> Chain of Responsibility Pattern：责任链模式

即一个对象在一条链上经过了多个拦截器的程序设计模式。

### 实例

定义多个拦截器

```java
//***********************************拦截器1********************************************
public class Interceptor1 implements Interceptor {

    @Override
    public boolean before(Object proxy, Object target, Method method, Object[] args) {
        System.out.println("【拦截器1】的before方法");
        return true;
    }

    @Override
    public void around(Object proxy, Object target, Method method, Object[] args) {

    }

    @Override
    public void after(Object proxy, Object target, Method method, Object[] args) {
        System.out.println("【拦截器1】的after方法");
    }
}
//***********************************拦截器2********************************************
public class Interceptor2 implements Interceptor{
    @Override
    public boolean before(Object proxy, Object target, Method method, Object[] args) {
        System.out.println("【拦截器2】的before方法");
        return true;
    }

    @Override
    public void around(Object proxy, Object target, Method method, Object[] args) {

    }

    @Override
    public void after(Object proxy, Object target, Method method, Object[] args) {
        System.out.println("【拦截器2】的after方法");
    }
}
//***********************************拦截器3********************************************
public class Interceptor3 implements Interceptor{
    @Override
    public boolean before(Object proxy, Object target, Method method, Object[] args) {
        System.out.println("【拦截器3】的before方法");
        return true;
    }

    @Override
    public void around(Object proxy, Object target, Method method, Object[] args) {

    }

    @Override
    public void after(Object proxy, Object target, Method method, Object[] args) {
        System.out.println("【拦截器3】的after方法");
    }
}
```

### 测试类

```java
@Test
    public void moreInterceptor() {
        HelloWorld proxy1 = (HelloWorld) InterceptorJdkProxy.bind(new HelloWorldImpl(), "org.example.interceptor.Interceptor1");
        HelloWorld proxy2 = (HelloWorld) InterceptorJdkProxy.bind(proxy1, "org.example.interceptor.Interceptor2");
        HelloWorld proxy3 = (HelloWorld) InterceptorJdkProxy.bind(proxy2, "org.example.interceptor.Interceptor3");
        proxy3.sayHelloWorld();
    }
```

# 二、观察者模式

---

> Observer：观察者
>
> 观察、订阅、监听

观察者模式又称发布订阅模式，是对象的行为模式，具有一对多的特性，**被订阅对象**的状态会影响**已订阅对象**的状态。

> 类似于<u>*自变量*</u>与<u>*因变量*</u>的关系（y = x²），**观察者**会随着**被观察者**的变化而变化。
>
> 被观察者需要继承（java.util.Observable）类，观察者需要实现（java.util.Observer）接口

## 实例

1、定义被观察者

```java
public class ProductList extends Observable {
    private List<String> productList = null;
    private static ProductList instance;	//定义唯一实例

    private ProductList(){}					//构造方法私有，相当于在其他类中禁用了new ProductList()

    public static ProductList getInstance() {
        if (instance == null) {
            instance = new ProductList();
            instance.productList = new ArrayList<String>();
        }
        return instance;					//只能通过调用此方法的方式返回对象
    }

    public void addProductListObserver(Observer observer){
        this.addObserver(observer);				//订阅功能，添加新的观察者
    }

    public void addProduct(String newProduct) {
        productList.add(newProduct);
        System.out.println("产品列表新增了产品：" + newProduct);
        this.setChanged();					   //发生变化
        this.notifyObservers(newProduct);		//通知观察者
    }
```

2、定义观察者

```java
//***********************************京东********************************************
public class JingDongObserver implements Observer {
    @Override
    public void update(Observable o, Object product) {
        String newProduct = (String) product;
        System.err.println("发送新产品【"+newProduct+"】同步到京东商城");
    }
}
//***********************************淘宝********************************************
public class TaoBaoObserver implements Observer {
    @Override
    public void update(Observable o, Object product) {
        String newProduct = (String) product;
        System.err.println("发送新产品【"+newProduct+"】同步到淘宝商城");
    }
}
```

## 测试类

```java
 @Test
    public void testObserver() {
        ProductList observable = ProductList.getInstance();
        TaoBaoObserver taoBaoObserver = new TaoBaoObserver();
        JingDongObserver jingDongObserver = new JingDongObserver();
        
        observable.addObserver(jingDongObserver);	//注册观察者
        observable.addObserver(taoBaoObserver);		//注册观察者
        observable.addProduct("新增产品 1");
    }
```

# 三、工厂模式和抽象工厂模式

---

工厂模式：将程序中的“对象创建语句”进行整合工作。即：将“创建对象”的工作交给**工厂类**来完成。

抽象工厂模式：当工厂过多，就需要提高工厂调用的便利性，使用抽象工厂可以让调用者在不必操心具体逻辑的情况下更加便捷的调用其所需的工厂来创建相应的对象。

# 四、建造者模式

---

> builder：建造者

建造者模式是对象的创建模式，此模式使得创建对象的过程变得清晰便利。

## Question

如何创建一个“拥有多个对象的特质”的对象？

> 例如一张套票是多种票型的集合体，那我们该如何创建一个套票对象？

此对象创建的难度在于既要完成多个功能逻辑的实现，也要完成各个功能的整合。

## Answer

Builder设计模式就是为了解决这样的问题，将此问题拆分成两块

> 功能逻辑的实现
>
> 功能的整合

功能逻辑的实现将会在配置类（Helper）中完成，功能的整合交给构建类（Builder）来完成。

## Generally

通俗的说：我们的目的是创建一幅巨作，我们将这幅巨作视为一个大拼图，

![成品图](images\成品.png "成品")

那么我们先使用Helper对象创建出需要的每一块小拼图，然后堆放在那里；

![散乱的拼图](images\散乱的拼图.png "拼图")

再使用Builder对象，将这些无序的拼图按照次序组合为成品。

![拼图](images\拼图.png "拼图")

## 实例

### 配置类

```java
//配置类
public class TicketHelper {

    public void buildAdult(String info) {
        System.err.println("构建成人票逻辑："+info);
    }

    public void buildChildrenForSeat(String info) {
        System.err.println("构建有座儿童票逻辑："+info);
    }

    public void buildChildrenNoSeat(String info) {
        System.err.println("构建无座儿童票逻辑：" +info);
    }

    public void buildElderly(String info) {
        System.err.println("构建老人票逻辑："+info);
    }

    public void buildSoldier(String info) {
        System.err.println("构建军人票逻辑："+info);
    }
}
```

### 构建类

```java
//构建类
public class TicketBuilder {
    public static Object builder(TicketHelper helper){
        System.out.println("通过TicketHelper构建套票信息");
        return null;
    }
}
```

### 实现类

```java
//整合创建 Object ticket
public void testPrint() {
    TicketHelper helper = new TicketHelper();		//使用Helper对象进行逻辑配置
    helper.buildAdult("成人票");
    helper.buildChildrenForSeat("有座儿童票");
    helper.buildChildrenNoSeat("无座儿童票");
    helper.buildElderly("老人票");
    helper.buildSoldier("军人票");
    Object ticket = TicketBuilder.builder(helper);	//使用Builder对象进行套票整合
}
```

#Servlet
#
##OverView（概述）
__Servlet：由Java语言编写，运行在Web服务器端的应用程序__  

* __体系结构__  

![Servlet](pngs\ServletArchitecture.png "Servlet体系结构")  

* __服务器与数据库的中间层（交界地）__ 

> 采集网页提供的前端动作  
> 呈现持久层提供的后端数据  

__资源传输：__

>使用HTTP协议来完成资源的传输
>使用URI（统一资源标识符）标识资源，来完成资源的请求  

>>URI包括URN（统一资源名）和URL（统一资源定位符）  
>>URN象征一个数据的身份，URL提供查找该事物的方法。  
>>抽象的说：URN相当于一个人的名字，URL相当于这个人住址  
##浅谈Servlet
与客户端对接的服务器主要完成的是对HTTP的处理工作  

* 传统的工作结构是静态的：即客户端发送请求，服务器返回资源
* 实现的功能也仅仅只是展示信息
* 但如今，程序的功能不再仅仅是展示，还包括用户对页面更加频繁的操作。为了保证用户的体验，必须尽量保证程序的响应速度，因此程序在现在需要更高的性能。  
* 相比于管理系统，现在的互联网系统的程序复杂度和功能性都得到了显著的提升，而且仍然要维持程序的可扩展性，所以必须要考虑的程序的耦合性；所以必须要将新的动态功能与HTTP服务程序分离开，以完成解耦工作，由此诞生了Servlet。

Servlet容器中包含：JSP（_Java Server Page_）与Servlet应用程序  

##Servlet's Feature
* 功能强大
* 可移植
* 性能高效
* 安全性高
* 可扩展  

##Servlet Interface
Servlet Interface：javax.servlet.Servlet 接口，其中定义了五个抽象方法  

 __void init(ServletConfig config)__
> 初始化Servlet程序

 __ServletConfig getServletConfig()__
> 获取Servlet配置信息  
> 返回ServletConfig对象

 __String getServletInfo()__
> 返回Servlet的信息

 __void service(ServletRequest request,ServletResponse response)__
> 负责响应用户的请求  
>  
> param1：request（请求）表示客户端请求的对象  
> param2：response（响应）用于响应客户端请求的对象  

 __void destroy()__

> destroy（销毁）方法，负责释放Servlet对象占用的资源。

##Servlet interface's Default implementation Class

###abstract GenericServlet
###HttpServlet（常用）  
>HttpServlet是GenericServlet的子类，它继承了父类的全部方法并新增了实现HTTP请求的方法。  、

##常用方法如下：
__protected void doGet(HttpServletRequest req，HttpServletResponse resp)__  
> 用于处理GET类型的HTTP请求的方法

__protected void doPost(HttpServletRequest req，HttpServletResponse resp)__  
> 用于处理POST类型的HTTP请求的方法

__protected void doPut(HttpServletRequest req，HttpServletResponse resp)__
> 用于处理PUT类型的HTTP请求的方法
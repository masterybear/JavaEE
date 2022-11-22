# 二、SpringMVC 入门

简介：Spring Web MVC 是一种基于 Java 的实现了 Web MVC 设计模式的轻量级 Web 框架。
核心：使用了**`请求—响应模型`**。

## 核心组件

1. **`DispatcherServlet`**：中央调度器

   负责安排各组件的工作。

2. **`HandlerMapping`**：处理器映射器

   完成资源与索引符之间的映射关系，通过他能得到索引符对应的资源处理器。

3. **`Handler`**：处理器

   即控制器，负责业务的处理。

4. **`HandlerAdapter`**：处理器适配器

   负责处理器的运行。

5. **`ViewResolver`**：视图解析器

   将处理器的运行结果转化为视图。将视图转交给调度器，便完成了整个业务逻辑的闭环。

## 案例

1. 编辑业务逻辑，即控制器。

   ```java
   public class MyController implements Controller {
   
       /**
        * @param request:这个是前端请求
        * @param response：这个是后端响应
        * @return：返回 ModelAndView——数据模型和视图
        * @throws Exception
        */
       @Override
       public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
           ModelAndView mv = new ModelAndView("hello");
           mv.addObject("name", "student");
           return mv;
       }
   }
   ```

2. 前端页面

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>hello</title>
   </head>
   <body>
   hello ${name}!
   </body>
   </html>
   ```

3. 配置 Spring MVC

   ```xml
   <bean class="org.example.springmvc01.controller.MyController" id="/hello"/>
   <!--处理器映射器：将前端请求和后端接口进行关联-->
   <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping" id="handlerMapping">
       <property name="beanName" value="/hello"/>
   </bean>
   <!--处理器适配器-->
   <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter" id="handlerAdapter"/>
   <!--视图解析器-->
   <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="viewResolver">
       <property name="prefix" value="/jsp/"/>
       <property name="suffix" value=".jsp"/>
   </bean>
   ```

4. 配置 web ，即 Servlet 和 映射。

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
       <servlet>
           <servlet-name>springmvc</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:spring-servlet.xml</param-value>
           </init-param>
           <!--设置启动时机-->
           <load-on-startup>1</load-on-startup>
       </servlet>
       
       <servlet-mapping>
           <servlet-name>springmvc</servlet-name>
           <url-pattern>/</url-pattern>
       </servlet-mapping>
    
   </web-app>
   ```

## 组合案例

Spring + Spring MVC

1. Service

   ```java
   @Service
   public class MyService {
       public String hello(String name) {
           return "hello" + name;
       }
   }
   ```

2. Spring 配置文件

   ```xml
       <context:component-scan base-package="org.example.springmvc01" use-default-filters="true">
           <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
       </context:component-scan>
   ```

3. 修改 Spring MVC 配置文件

   ```xml
       <!--<bean class="org.example.springmvc01.controller.MyController" id="/hello"/>-->
       <context:component-scan base-package="org.example.springmvc01" use-default-filters="false">
           <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
       </context:component-scan>
       <!--处理器映射器：将前端请求和后端接口进行关联-->
       <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping" id="handlerMapping">
           <property name="beanName" value="/hello"/>
       </bean>
       <!--处理器适配器-->
       <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter" id="handlerAdapter"/>
       <!--视图解析器-->
       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="viewResolver">
           <property name="prefix" value="/jsp/"/>
           <property name="suffix" value=".jsp"/>
       </bean>
   ```

4. 新的业务逻辑

   ```java
   @org.springframework.stereotype.Controller("/hello")
   public class MyController implements Controller {
   
       final
       MyService helloService;
   
       @Autowired
       public MyController(MyService helloService) {
           this.helloService = helloService;
       }
   
       /**
        * @param request:这个是前端请求
        * @param response：这个是后端响应
        * @throws Exception
        * @return：返回 ModelAndView——数据模型和视图
        */
       @Override
       public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
           ModelAndView mv = new ModelAndView("hello");
           mv.addObject("name", "student");
           System.out.println("helloService.hello(\"zhangSan\") = " + helloService.hello("zhangSan"));
           return mv;
       }
   ```

5. 修改 Web 配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
       
       <context-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath:applicationContext.xml</param-value>
       </context-param>
       
       <listener>
           <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
       </listener>
   
       <servlet>
           <servlet-name>springmvc</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:spring-servlet.xml</param-value>
           </init-param>
           <!--设置启动时机-->
           <load-on-startup>1</load-on-startup>
       </servlet>
       
       <servlet-mapping>
           <servlet-name>springmvc</servlet-name>
           <url-pattern>/</url-pattern>
       </servlet-mapping>
    
   </web-app>
   ```

## 容器问题

Spring 容器 和 Spring MVC 容器，二者的关系是父子关系。
子容器可以访问父容器，但父容器不能访问子容器。
如果在 Service 类里配置 Controller 资源，那么将会报错，因为父容器无法访问子容器。
可以在 Controller  类里配置 Service 资源，因为子容器可以访问父容器。

## 处理器映射器

1. **`BeanNameUrlHandlerMapping`**

   将请求的 `url` 与配置的 `BeanName` 进行匹配，从而找到资源实例。
   也就是说：索引与 Bean 名共用相同 id。

   ```xml
   <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping" id="handlerMapping">
       <property name="beanName" value="/hello"/>
   </bean>
   ```

   

2. **`SimpleUrlHandlerMapping`**

   将请求的 `url` 与配置的 `BeanName` 进行映射，通过键找到对应的资源实例。
   也就是说：索引与 Bean 名不共用相同 id。

   ```xml
   <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping" id="handlerMapping">
       <property name="mappings">
           <props>
               <prop key="/hello">myController</prop>
               <prop key="/hello2">myController2</prop>
           </props>
       </property>
   </bean>
   ```

## 处理器适配器

1. **`SimpleControllerHandlerAdapter`**

   所有实现了 `org.springframework.web.servlet.mvc.Controller` 接口的 Bean 通过此适配器进行适配、执行。

   ```java
   @Override
   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
       ModelAndView mv = new ModelAndView("hello");
       mv.addObject("name", "student");
       return mv;
   }
   ```

   ```xml
   <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter" id="handlerAdapter"/>
   ```

2. **`HttpRequestHandlerAdapter`**

   http 请求处理器适配器，所有实现了 `org.springframework.web.HttpRequestHandler` 接口的 Bean 通过此适配器进行适配、执行。

   ```java
   @Override
   public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           response.getWriter().write("hello student3!");
   }
   ```

   ```xml
   <bean class="org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter" id="handlerAdapter"/>
   ```

## 进阶案例

1. 编辑业务逻辑，即控制器。

   ```java
   @Controller
   public class MyController {
       @RequestMapping("/hello")
       public ModelAndView hello() {
           ModelAndView mv = new ModelAndView("hello");
           mv.addObject("name", "zhangSan");
           return mv;
       }
   }
   ```

2.  Spring MVC 配置文件

   ```xml
       <context:component-scan base-package="org.example.springmvc02" use-default-filters="false">
           <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
       </context:component-scan>
   
       <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"
             id="handlerMapping"/>
   
       <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"
             id="handlerAdapter"/>
   
       <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="viewResolver">
           <property name="prefix" value="/jsp/"/>
           <property name="suffix" value=".jsp"/>
       </bean>
   ```

   或是用以下方式代替处理器映射器和处理器适配器。

   ```xml
   <mvc:annotation-driven>
   ```

## 控制器的细则

### 访问相关

通过使用 **`@RequestMapping`** 注解，可以取代实现接口的工作。

1.  **`@RequestMapping`** 注解可以定义在方法上。可为一个出控制器定义多个 url 。

   ```java
   @Controller
   public class MyController {
       @RequestMapping({"/hello","/hello2"})
       public ModelAndView hello() {
           return new ModelAndView("hello");
       }
   }
   ```

   如上请求的 url 为：/hello 或 /hello2 

   

2. **`@RequestMapping`** 注解不仅可以定义在方法上，还可以定义在类之上。

   ```java
   @Controller
   @RequestMapping("/user")
   public class HelloController {
       @RequestMapping({"/hello","/hello2"})
       public ModelAndView hello() {
           return new ModelAndView("hello");
       }
   }
   ```

   如上请求的 url 为：/user/hello 或 /user/hello2

   

3. **`@RequestMapping`** 注解定义请求方法。

   	默认情况下，可以被 GET 请求 和 POST 请求访问。
   	
   	也可显示定义请求方法。

   ```java
   @Controller
   @RequestMapping("/user")
   public class HelloController {
       @RequestMapping(value = "/hello",method = RequestMethod.GET)
       public ModelAndView hello() {
           return new ModelAndView("hello");
       }
   }
   ```

   如上请求方法为：GET

   此时如果想通过 POST 方法访问，便会报错。

   如果想容纳多种方法，也可显示定义。

   ```java
   @Controller
   @RequestMapping("/user")
   public class HelloController {
       @RequestMapping(value = "/hello",method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
       public ModelAndView hello() {
           return new ModelAndView("hello");
       }
   }
   ```

   如上请求方法为：GET、POST、PUT、DELETE
   
   为了简化开发，在 Spring4.3 中，可以使用如下注解来定义请求方法。
   
   ```java
   @GetMapping
   @PostMapping
   @PutMapping
   @DeleteMapping
   ```

### 返回值相关

1. 在未实现前后端分离的情况下，返回类型为： `ModelAndView`。

   ```java
   @Controller
   @RequestMapping("/user")
   public class HelloController {
       @RequestMapping("/hello")
       public ModelAndView hello() {
           ModelAndView mv = new ModelAndView("hello");
           mv.addObject("username", "javaboy");
           return mv;
       }
   }
   ```

2. 如果按照之前 Servlet 的定义，返回类型为：void。

   ```java
   @RequestMapping("/hello2")
   public void hello2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       req.getRequestDispatcher("/jsp/hello.jsp").forward(req,resp);//服务器端跳转
       
       resp.sendRedirect("/jsp/hello.jsp");//重定向
       
       resp.setStatus(302);
       resp.addHeader("Location", "/jsp/hello.jsp");
       
       PrintWriter writer = response.getWriter();
       writer.write("hello zhangShan");
       writer.flush();
       writer.close();
   }
   ```

3. 返回类型为：String。

   若想定义数据模型，则方法需要一个数据模型参数。

   返回的 字符串 会被识别，

   若是普通一段字符串，会被认为成是逻辑视图名。

   ```java
   @RequestMapping("hello3")
   public String hello3(Model model) {
       model.addAttribute("name", "zhangSan");
       return "hello";
   }
   ```

   若有特定前缀，将会按照前缀所指定的形式去执行，比如跳转或是重定向。

   ```java
   @RequestMapping("hello4")
       public String hello4() {
           return "forward:/jsp/hello.jsp";
           return "redirect:/jsp/damn.jsp";
       }
   ```

   如果只想返回普通的字符串，那需要使用`@ResponseBody`注解。

   且如果是中文字符的话，需要定义编码格式，否则会乱码。

   ```java
       @RequestMapping(value = "hello5", produces = "text/html;charset=utf-8")
       @ResponseBody
       public String hello5() {
           return "张三你好！";
       }
   ```

### 参数相关

1. 默认支持的参数类型参数

   - HttpServletRequest
   - HttpServletResponse
   - HttpSession
   - Model 或 ModelMap

2. 简单数据类型参数

   前端回传的数据，经过控制器时，在框架的影响下，无需手动定义，在方法中直接使用数据即可。

   默认情况下，name 必须相同，也可通过注解手动定义对应的不同类名或是其他参数属性，如下。

   前端页面

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>Title</title>
   </head>
   <body>
   <form action="/addBook" method="post">
       <table>
           <tr>
               <td>书名：</td>
               <td><input type="text" name="name"></td>
           </tr>
           <tr>
               <td>作者：</td>
               <td><input type="text" name="author"></td>
           </tr>
           <tr>
               <td>价格：</td>
               <td><input type="text" name="price"></td>
           </tr>
           <tr>
               <td colspan="2">
                   <input type="submit" value="添加">
               </td>
           </tr>
       </table>
   </form>
   </body>
   </html>
   ```

   控制器逻辑

   ```java
       @PostMapping(value = "/addBook", produces = "text/html;charset=utf-8")
       @ResponseBody
       public String addBook(@RequestParam("name") String bookName, String author,@RequestParam(defaultValue = "99") Double price) {
           return bookName + "---" + author + "---" + price;
       }
   ```

   POST 回传的数据会乱码，这需要在 web 配置文件中配置。

   ```xml
   <filter>
       <filter-name>encoding</filter-name>
       <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
       <init-param>
           <param-name>encoding</param-name>
           <param-value>UTF-8</param-value>
       </init-param>
       <init-param>
           <param-name>forceRequestEncoding</param-name>
           <param-value>true</param-value>
       </init-param>
       <init-param>
           <param-name>forceResponseEncoding</param-name>
           <param-value>true</param-value>
       </init-param>
   </filter>
   <filter-mapping>
       <filter-name>encoding</filter-name>
       <url-pattern>/*</url-pattern>
   </filter-mapping>
   ```

3. 实体类类型（对象）参数

   ```java
   public class Author {
       private String name;
       private Integer age;
       /********* toString *********/
       /********* getter and setter *********/
   }
   ```

   ```java
   public class Book {
       private String name;
       private Author author;
       private Double price;
       /********* toString *********/
       /********* getter and setter *********/
   }
   ```

   ```html
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>Title</title>
   </head>
   <body>
   <form action="/addBook2" method="post">
       <table>
           <tr>
               <td>书名：</td>
               <td><input type="text" name="name"></td>
           </tr>
           <tr>
               <td>作者：</td>
               <td><input type="text" name="author.name"></td>
           </tr>
           <tr>
               <td>作者年龄：</td>
               <td><input type="text" name="author.age"></td>
           </tr>
           <tr>
               <td>价格：</td>
               <td><input type="text" name="price"></td>
           </tr>
           <tr>
               <td colspan="2" align="center">
                   <input type="submit" value="添加">
               </td>
           </tr>
       </table>
   </form>
   </body>
   </html>
   ```

   控制器逻辑

   ```java
   @PostMapping(value = "/addBook2", produces = "text/html;charset=utf-8")
   @ResponseBody
   public String addBook2(Book book) {
       return book.toString();
   }
   ```

4. 自定义类型转换 的参数

   对于特殊的数据类型，系统无法自动转换，需要我们自定义类型转换，比如 Date 类型就是这样。

   定义转换器

   ```java
   @Component
   public class MyDateConverter implements Converter<String, Date> {
   
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   
       @Override
       public Date convert(String source) {
           try {
               return sdf.parse(source);
           } catch (ParseException e) {
               e.printStackTrace();
           }
           return null;
       }
   }
   ```

   Spring mvc 配置文件

   ```xml
   <bean class="org.springframework.format.support.FormattingConversionServiceFactoryBean" id="conversionService">
       <property name="converters">
           <set>
               <ref bean="myDateConverter"/>
           </set>
       </property>
   </bean>
   <mvc:annotation-driven conversion-service="conversionService"/>
   ```

   model

   ```java
   public class Book {
       private String name;
       private Author author;
       private Double price;
       private Date publishDate;
       /********* toString *********/
       /********* getter and setter *********/
   }
   ```

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>Title</title>
   </head>
   <body>
   <form action="/book/addBook2" method="post">
       <table>
           <tr>
               <td>书名：</td>
               <td><input type="text" name="name"></td>
           </tr>
           <tr>
               <td>作者：</td>
               <td><input type="text" name="author.name"></td>
           </tr>
           <tr>
               <td>作者年龄：</td>
               <td><input type="text" name="author.age"></td>
           </tr>
           <tr>
               <td>价格：</td>
               <td><input type="text" name="price"></td>
           </tr>
           <tr>
               <td>出版时间：</td>
               <td><input type="date" name="publishDate"></td>
           </tr>
           <tr>
               <td colspan="2" align="center">
                   <input type="submit" value="添加">
               </td>
           </tr>
       </table>
   </form>
   </body>
   </html>
   ```

5. 集合类 参数

   除了前面提到的基本数据类型和实体类类型，还可以得到集合类 类型的参数。

   ---

   控制器逻辑

   ```java
   @PostMapping(value = "/addBook2", produces = "text/html;charset=utf-8")
   @ResponseBody
   //前端传来的数组对象，服务端不可以使用 List 集合去接收。
   public String addBook2(Book book,String[] favorites) {
       return book.toString() + Arrays.toString(favorites);
   }
   ```

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>Title</title>
   </head>
   <body>
   <form action="/book/addBook2" method="post">
       <table>
           <tr>
               <td>书名：</td>
               <td><input type="text" name="name"></td>
           </tr>
           <tr>
               <td>作者：</td>
               <td><input type="text" name="author.name"></td>
           </tr>
           <tr>
               <td>作者年龄：</td>
               <td><input type="text" name="author.age"></td>
           </tr>
           <tr>
               <td>价格：</td>
               <td><input type="text" name="price"></td>
           </tr>
           <tr>
               <td>出版时间：</td>
               <td><input type="date" name="publishDate"></td>
           </tr>
           <tr>
               <td>爱好：</td>
               <td>
                   <input type="checkbox" value="足球" name="favorites">足球
                   <input type="checkbox" value="篮球" name="favorites">篮球
                   <input type="checkbox" value="网球" name="favorites">网球
               </td>
           </tr>
           <tr>
               <td colspan="2" align="center">
                   <input type="submit" value="添加">
               </td>
           </tr>
       </table>
   </form>
   </body>
   </html>
   ```

### 文件上传

Spring mvc 对于文件上传提供了两个处理器。

- CommonsMultipartResolver （旧 可兼容较老版本的 Servlet，且需要额外依赖 commons-fileupload ）
- StandardServletMultipartResolver（新 兼容性稍微差一些，spring-boot 中更为常用）

1. CommonsMultipartResolver（通用多部分解析器）

   jsp 文件

   ```jsp
   <form action="/upload" method="post" enctype="multipart/form-data">
       <!-- 加密类型一定是 multipart/form-data -->
       <input type="file" name="file">
       <input type="submit" value="上传">
   </form>
   ```

   mvc 配置文件

   默认配置

   ```xml
   <bean class="org.springframework.web.multipart.commons.CommonsMultipartResolver" id="multipartResolver"/>
       <!-- id 必须是 multipartResolver -->
   ```

   自定义配置

   ```xml
   <bean class="org.springframework.web.multipart.commons.CommonsMultipartResolver" id="multipartResolver">
       <!--默认的编码-->
       <property name="defaultEncoding" value="UTF-8"/>
       <!--上传的总文件大小-->
       <property name="maxUploadSize" value="1048576"/>
       <!--上传的单个文件大小-->
       <property name="maxUploadSizePerFile" value="1048576"/>
       <!--内存中最大的数据量，超过这个数据量，数据就要开始往硬盘中写了-->
       <property name="maxInMemorySize" value="4096"/>
       <!--临时目录，超过 maxInMemorySize 配置的大小后，数据开始往临时目录写，等全部上传完成后，再将数据合并到正式的文件上传目录-->
       <property name="uploadTempDir" value="file:///E:\"/>
   </bean>
   
   ```

   控制器逻辑

   ```java
   @Controller
   public class MyController {
       SimpleDateFormat sdf = new SimpleDateFormat("/yy/MM/dddd/");
   
       @PostMapping(value = "/upload")
       @ResponseBody
       public String fileUpload(MultipartFile file, HttpServletRequest request) {
           //准备文件夹
           String format = sdf.format(new Date());
           String realPath = request.getServletContext().getRealPath("/") + format;
           File folder = new File(realPath);
           if (!folder.exists()) {
               folder.mkdirs();
               //如果文件不存在，就创建这个文件
           }
           //准备文件名
           String oldName = file.getOriginalFilename();
           String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
           //存储
           try {
               file.transferTo(new File(folder, newName));
               //组装url
               String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + format + newName;
               return url;
           } catch (IOException e) {
               e.printStackTrace();
           }
           return "error";
       }
   }
   ```

   上面的五个字符串都是什么：

   - format = `/22/11/0018/`
   - realPath = `D:\workConfig\tomcat_8\webapps\ROOT\/22/11/0018/`
   - oldName = `123.jpg`
   - newName = `bcf62367-0fd5-4f5c-bcc3-ab4ac879e46b.jpg`
   - url = `http://localhost:8080/22/11/0018/bcf62367-0fd5-4f5c-bcc3-ab4ac879e46b.jpg`

   上传文件时，后端需要完成两件事，即：

   1. 文件要存储到什么 地方。
   2. 文件叫什么 名。

   最后将前端提交的文件转移到新的地方。

   最后的 url 拼接：协议 + 域名 + 服务端口 + 格式 + 新文件名

   

2. StandardServletMultipartResolver （标准 Servlet 多部分解析器）

   mvc 配置文件

   ```xml
       <bean class="org.springframework.web.multipart.support.StandardServletMultipartResolver" id="multipartResolver"/>
   ```

   web.xml 中自定义配置（必须）

   ```xml
       <servlet>
           <servlet-name>springmvc</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <init-param>
               <param-name>contextConfigLocation</param-name>
               <param-value>classpath:spring-servlet.xml</param-value>
           </init-param>
           <multipart-config>
               <!--上传的文件将被存储的目录位置-->
   <!--            <location>C:\Users\15424\AppData\Local\Temp</location>-->
               <!--上传单个文件时的容量上限 单位是 b-->
               <max-file-size>2097152</max-file-size>
               <!--multipart/form-data 请求的最大大小限制 即总容量上限 单位是 b-->
               <max-request-size>2097152</max-request-size>
               <!--内存的最大容量上限，超过后将写入硬盘 单位是 b-->
               <file-size-threshold>1048576</file-size-threshold>
           </multipart-config>
       </servlet>
   ```

3. 多文件上传

   jsp 文件

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>fileupload</title>
   </head>
   <body>
   <form action="/upload" method="post" enctype="multipart/form-data">
       <input type="file" name="file" multiple>
       <input type="submit" value="上传">
   </form>
   </body>
   </html>
   ```

   控制器逻辑

   ```java
   @Controller
   public class MyController {
       SimpleDateFormat sdf = new SimpleDateFormat("/yy/MM/dddd/");
   
   @PostMapping(value = "/upload")
   @ResponseBody
   public String fileUpload(MultipartFile[] file, HttpServletRequest request) {
       //准备文件夹
       String format = sdf.format(new Date());
       String realPath = request.getServletContext().getRealPath("/") + format;
   
       File folder = new File(realPath);
       if (!folder.exists()) {
           folder.mkdirs();
       }
   
       ArrayList<String> urls = new ArrayList<>();
       try {
           for (MultipartFile f : file) {
               //准备文件名
               String oldName = f.getOriginalFilename();
               String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
   
               //存储
               f.transferTo(new File(folder, newName));
               String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + format + newName;
   
               urls.add(url);
           }
           System.out.println(urls.toString());
           return urls.toString();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return "error";
   }
   ```

### 异常处理

比方说，在文件上传时，文件超过了配置的大小，就会失败，并抛出一个异常，而接下来的工作就是要处理这个异常。

为什么要做异常处理？

对于不懂的人，会让他们很疑惑。
对于懂的人，将让程序变得不安全。

定义一个异常处理的 jsp 页面

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>error</title>
</head>
<body>
<h1>出错了！</h1>
<div>${error}</div>
</body>
</html>
```

定义一个逻辑类、用于处理异常报错。这将使用新的注解 增强版的控制器，不同于普通的控制器。

```java
@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(Exception.class)
    public ModelAndView fileUploadSizeLimit(Exception e) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("error", e.getMessage());
        return mv;
    }
}
```

### 服务端数据校验

尽管从用户体验方面考虑，客户端校验更加便捷迅速，但从安全性考虑，服务端数据校验更加安全，所以在实际开发中，二者可以结合使用。

以下为部分校验注解。

```JAVA
public class Book {
    @NotNull(message = "{book.id.notnull}")
    private Integer id;
    @NotNull(message = "{book.authorName.notnull}")
    @Size(min = 2, max = 10,message = "{book.authorName.size}")
    private String authorName;
    @Email(message = "{book.email.error}")
    private String email;
    @Max(message = "{book.age.error}")
    @Min(message = "{book.age.error}")
    private Integer age;
```

```properties
book.id.notnull=id不能为空！
book.authorName.notnull=名字不能为空！
book.authorName.size=名字长度应介于2到10之间！
book.email.error=email填写有误！
book.age.error=年龄填写有误！
```

```xml
<!--校验器-->
<bean class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean" id="validatorFactoryBean">
    <property name="providerClass" value="org.hibernate.validator.HibernateValidator"/>
    <property name="validationMessageSource" ref="bundleMessageSource"/>
</bean>
<bean class="org.springframework.context.support.ReloadableResourceBundleMessageSource" id="bundleMessageSource">
    <property name="basename">
        <value>classpath:MyMessage</value>
    </property>
    <property name="defaultEncoding" value="UTF-8"/>
    <property name="cacheSeconds" value="300"/>
</bean>
<mvc:annotation-driven validator="validatorFactoryBean"/>
```

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.1.0.Final</version>
</dependency>
```

```java
@Controller
public class BookController {

    @PostMapping(value = "/book", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String addBook(@Validated Book book, BindingResult result) {
        System.out.println("book = " + book);
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                System.out.println("error.getDefaultMessage() = " + error.getDefaultMessage());
            }
        }
        return book.toString() + result.getAllErrors();
    }
}
```

**分组校验**

只有创建初始信息时可能需要全部校验，后续的修改操作无需全部校验，就可以使用分组校验，即部分信息的校验。

定义接口即可，一个接口为一组。

```java
public interface ValidationGroup1 {
}
```

```java
public interface ValidationGroup2 {
}
```

```java
@Controller
public class BookController {

    @PostMapping(value = "/book", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String addBook(@Validated(ValidationGroup2.class) Book book, BindingResult result) {
        System.out.println("book = " + book);
        List<ObjectError> allErrors = result.getAllErrors();
        ArrayList<String> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : allErrors) {
                System.out.println("error.getDefaultMessage() = " + error.getDefaultMessage());
                errors.add(error.getDefaultMessage());
            }
        }
        return book.toString()+ "<br>" + errors;
    }
}
```

```java
public class Book {
@NotNull(message = "{book.id.notnull}", groups = {ValidationGroup1.class})
private Integer id;
@NotNull(message = "{book.authorName.notnull}", groups = {ValidationGroup1.class, ValidationGroup2.class})
@Size(min = 2, max = 10,message = "{book.authorName.size}", groups = {ValidationGroup1.class, ValidationGroup2.class})
private String authorName;
@Email(message = "{book.email.error}", groups = {ValidationGroup1.class, ValidationGroup2.class})
private String email;
@Max(message = "{book.age.error}", value = 150, groups = ValidationGroup2.class)
@Min(message = "{book.age.error}", value = 1, groups = ValidationGroup2.class)
private Integer age;
```

### 数据回填

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>addBook</title>
</head>
<body>
<form method="post" action="/addBook">
    <table>
        <tr>
            <td>作者：</td>
            <td>
                <input type="text" name="authorName" value="${author}">
            </td>
        </tr>
        <tr>
            <td>邮箱：</td>
            <td><input type="text" name="email" value="${email}"></td>
        </tr>
        <tr>
            <td>年龄：</td>
            <td><input type="text" name="age" value="${age}"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="提交"></td>
        </tr>
    </table>
</form>
</body>
</html>
```

```java
    @PostMapping("/addBook")
    public String addBook2(Model model, @Validated(ValidationGroup2.class) Book book, BindingResult result) {
        model.addAttribute("author", book.getAuthorName());
        model.addAttribute("email", book.getEmail());
        model.addAttribute("age", book.getAge());
        if (result.hasErrors()) {
            return "addBook";
        }
        return "fileupload";
    }
```

或

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>addBook</title>
</head>
<body>
<form method="post" action="/addBook">
    <table>
        <tr>
            <td>作者：</td>
            <td>
                <input type="text" name="authorName" value="${book.author}">
            </td>
        </tr>
        <tr>
            <td>邮箱：</td>
            <td><input type="text" name="email" value="${book.email}"></td>
        </tr>
        <tr>
            <td>年龄：</td>
            <td><input type="text" name="age" value="${book.age}"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="提交"></td>
        </tr>
    </table>
</form>
</body>
</html>
```

```java
@PostMapping("/addBook")
public String addBook2(@Validated(ValidationGroup2.class) Book book, BindingResult result) {
    if (result.hasErrors()) {
        return "addBook";
    }
    return "fileupload";
}
```

这种方式也可以使用 **@ModelAttribute("新名字") Book book**

前端就可以使用新名字而无需使用后端的实体类名了。

### @ModelAttribute

使用该注解可以完成全局数据的定义工作，当一个数据被多个类所公用时，可以使用这种方式一次定义一个全局数据进行使用。

```java
@ModelAttribute("info")
public Map<String, Object> info1() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "baidu");
    map.put("site", "https://www.baidu.com");
    return map;
}

@GetMapping("/2")
public String href() {
    return "addBook";
}
```

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>addBook</title>
</head>
<body>
<a href="${info.site}">${info.name}</a>
</body>
</html>
```

### JSON

1. Jackson

   pom 依赖

   ```xml
   <dependency>
       <groupId>com.fasterxml.jackson.core</groupId>
       <artifactId>jackson-databind</artifactId>
       <version>2.14.0</version>
   </dependency>
   ```

   model类

   ```java
   public class BookSelect {
       private Integer id;
       private String name;
       private String Author;
   //    @JsonFormat(pattern = "yyy-MM-dd",timezone = "Asia/Shanghai")
       private Date isPublish;
   ```

   逻辑

   ```java
   @GetMapping("/bookSelect")
   @ResponseBody
   public BookSelect bookSelect1(Integer id) {
       BookSelect book = new BookSelect();
       book.setId(id);
       book.setName("活着");
       book.setAuthor("余华");
       book.setIsPublish(new Date());
       return book;
   }
   
   @GetMapping("bookSelect2")
   @ResponseBody
   public List<BookSelect> bookSelects() {
       List<BookSelect> books = new ArrayList<>();
       for (int i = 0; i < 2; i++) {
           BookSelect book = new BookSelect();
           book.setId(i);
           book.setName("活着" + i);
           book.setAuthor("余华" + i);
           book.setIsPublish(new Date());
           books.add(book);
       }
       return books;
   }
   ```

   ```xml
   <mvc:annotation-driven validator="validatorFactoryBean">
       <mvc:message-converters>
           <ref bean="httpMessageConverter"/>
       </mvc:message-converters>
   </mvc:annotation-driven>
   <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter" id="httpMessageConverter">
       <property name="objectMapper">
           <bean class="com.fasterxml.jackson.databind.ObjectMapper">
               <property name="dateFormat">
                   <bean class="java.text.SimpleDateFormat">
                       <constructor-arg name="pattern" value="yyyy-MM-dd"/>
                   </bean>
               </property>
               <property name="timeZone" value="Asia/Shanghai"/>
           </bean>
       </property>
   </bean>
   ```

2. gson

   pom 依赖

   ```xml
   <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
   <dependency>
       <groupId>com.google.code.gson</groupId>
       <artifactId>gson</artifactId>
       <version>2.10</version>
   </dependency>
   ```

   ```xml
   <mvc:annotation-driven validator="validatorFactoryBean">
       <mvc:message-converters>
           <ref bean="httpMessageConverter"/>
       </mvc:message-converters>
   </mvc:annotation-driven>
   <bean class="org.springframework.http.converter.json.GsonHttpMessageConverter" id="httpMessageConverter">
       <property name="gson">
           <bean class="com.google.gson.Gson" factory-bean="builder" factory-method="create"/>
       </property>
   </bean>
   <bean class="com.google.gson.GsonBuilder" id="builder">
       <property name="dateFormat" value="yyyy-MM-dd"/>
   </bean>
   ```

### JSON 处理

```java
@RequestMapping("/addbook3")
@ResponseBody
public void addBook3(@RequestBody Book book) {
    System.out.println(book);
}
```


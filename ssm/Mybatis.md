# 三、`Mybatis`入门

简介：**`MyBatis`** 是一款优秀的持久层框架。
持久层：在系统逻辑层面，专注于实现数据持久化的一个相对独立的领域。（将数据保存到硬盘中）

## 核心组件

![四大组件](images/mybatis核心组件.png)

## 工作流程

1. 使用**`Mybatis`**的第一步，就是使用`SqlSessionFactoryBuilder`作为构造器去生成`SqlSessionFactory`。
2. 而它的唯一工作就是生产`Mybatis`的核心接口对象`SQL Session`。
3. 而`SQL Mapper`其将完成SQL语句和Java代码之间的映射工作，它负责发送SQL去执行，并返回结果，这项工作将在会话中完成。

## SqlSessionFactory

### 作用

- **工厂接口的构建工作**
- **作用**：`SqlSessionFactory`的唯一工作就是创建SQL会话，也就是说它的责任是唯一的，所以常采用单例模式进行处理。
- **生成**：由`SqlSessionFactoryBuilder`进行引导，再读取配置文件去构建。
- **配置文件**：可以是XML文件，也可以是Java文件。但很少使用`java`对`Mybatis`进行配置。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <typeAliases>
  	<typeAlias alias="Author" type="domain.blog.Author"/>
  </typeAliases>
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
      </dataSource>
    </environment>
  </environments>
  <mappers>
    <mapper resource="org/mybatis/example/BlogMapper.xml"/>
  </mappers>
</configuration>
```

### 创建方法

```java
	SqlSessionFactory sqlSessionFactory;
    try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
```

## SQLSession

### 作用

- 获取`Mapper`接口。
- 发送SQL给数据库。
- 控制数据库事务。

### 创建方法

```java
SqlSession session = sqlSessionFactory.openSession();
```

## Mapper

### 构成

由一个接口和与之对应的XML文件（或注解）组成。

### 创建方法

```java
public interface BrandMapper {
    @Select("select * from tb_brand")
    List<Brand> selectAll();
}
```

```xml
<mapper namespace="org.example.mapper.BrandMapper">
    <select id="selectAll">
    	select * from tb_brand;
    </select>
```

```java
public List<Brand> selectAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
        List<Brand> brands = mapper.selectAll();
        sqlSession.close();
    	return brands;
    }
```

## 生命周期

- `SqlSessionFactoryBuilder`：由于其作用仅仅是在开始时创建一个`SqlSessionFactory`，所以它在创建完成后就该不复存在。
- `SqlSessionFactory`：它就像一个数据库连接池，其中承载着`SqlSession`，所以它的生命周期将与`Mybatis`的工作周期一样长，且不应该重复的多次创建。
- `SqlSession`：一次数据库操作的工作周期即是它的生命周期，否则很快资源将被完全占用。
- `Mapper`：由于其存在于会话中，所以当会话关闭后，它的存在也将没有意义，生命周期与会话相同。
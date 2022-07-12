# 动态SQL

---

程序编写过程中，有一个必要的实现功能：根据不同情况响应不同的SQL语句。由于语句需要具有拼接性，导致这在其他框架中、包括在JDBC中的编写，都是十分复杂且易错的。

利用动态 SQL，可以彻底摆脱这种痛苦。

接下来是要学习的元素种类：

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

---

## if

瓷元素的作用和Java中的if条件语句是一样的，没有新的东西，都是条件判断。

使用动态 SQL 最常见情景是根据条件包含 where 子句的一部分。比如：

```xml
<select id="findActiveBlogWithTitleLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
</select>
```

如果希望通过 “title” 和 “author” 两个参数进行可选搜索该怎么办呢？

和 Java 一样，一个 if 是一个条件，如果有多个条件判断，那就设置多个 if 条件语句。

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

下一个元素

## choose (when, otherwise)

MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

> 即从多个条件中选址一个。

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
  </choose>
</select>
```

某些情况下，条件选择语句会拥有更高的性能。

下一个元素

## trim (where, set)

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

*where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。

当然也有更开放的元素来帮助我们更好的去诠释我们要做的事。

**注意**：且此例中的空格是必要的！！！

```xml
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```

上述例子会移除所有 *prefixOverrides* 属性中指定的内容，并且插入 *prefix* 属性中指定的内容。

---

用于动态更新语句的类似解决方案叫做 *set*。

```xml
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

这个例子中，*set* 元素会动态地在行首插入 SET 关键字，并会删掉额外的逗号（这些逗号是在使用条件语句给列赋值时引入的）。

或者自定义 前缀或后缀的值：

```xml
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
```

## foreach

对集合进行遍历（尤其是在构建 IN 条件语句的时候）。比如：

```xml
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  <where>
    <foreach item="item" index="index" collection="list"
        open="ID in (" separator="," close=")" nullable="true">
          #{item}
    </foreach>
  </where>
</select>
```

1. 它允许你指定一个集合。
2. 声明可以在元素体内使用的集合项（item）和索引（index）变量。
3. open / close ：它也允许你指定开头与结尾的字符串。
4. separator：集合项迭代之间的分隔符。

> 你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象作为集合参数传递给 *foreach*。
>
> 当使用可迭代对象或者数组时，index 是当前迭代的序号，item 的值是本次迭代获取到的元素。
>
> 当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。

## script

要在带注解的映射器接口类中使用动态 SQL，可以使用 *script* 元素。比如:

```xml
    @Update({"<script>",
      "update Author",
      "  <set>",
      "    <if test='username != null'>username=#{username},</if>",
      "    <if test='password != null'>password=#{password},</if>",
      "    <if test='email != null'>email=#{email},</if>",
      "    <if test='bio != null'>bio=#{bio}</if>",
      "  </set>",
      "where id=#{id}",
      "</script>"})
    void updateAuthorValues(Author author);
```

还有一些其他的扩展元素。[其他的](https://mybatis.org/mybatis-3/zh/dynamic-sql.html#script)

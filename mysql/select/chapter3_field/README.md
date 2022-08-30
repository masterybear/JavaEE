# 计算字段

---

数据库中的数据在面对实际需求时，往往显得过于原始，通常需要对数据进行再次加工而满足客户端的需要，但这种操作不可能在检索数据结束 以后拿到客户机进行，我们需要直接检索出我们想要的数据，这就是计算字段诞生的理由了。

计算字段做什么？比方说，将企业的名字和企业的地址这两列数据在检索时拼接成一列数据。



## 拼接字段

---

```mysql
SELECT Concat(vend_name,' (',vend_country,')')
FROM vendors
ORDER BY vend_name;
-- Concat()函数的作用相当于Java语言中的"+".
```

![p1](images/p3_1.png)



## `Trim`函数

---

- `RTrim()`去除数据右侧多余空格
- `LTrim()`去除数据左侧多余空格
- `Trim()`去除数据两侧的多余空格

```mysql
SELECT Concat(RTrim(vend_name),' (',RTrim(vend_country),')')
FROM vendors
ORDER BY vend_name;
```

![p2](images/p3_2.png)



## 别名

---

```mysql
SELECT Concat(RTrim(vend_name),' (',RTrim(vend_country),')') AS vend_title
FROM vendors
ORDER BY vend_name;
-- 使用AS关键字进行别名的命名。
-- 别名有时也被称作"导出列"。
```

![p3](images/p3_3.png)



## 算数字段

---

```mysql
SELECT prod_id,
quantity,
item_price,
quantity*item_price AS expanded_price
FROM orderitems
WHERE order_num = 20005;
-- 计算总价格
```

![p4](images/p3_4.png)



## 其他

---

还有一些有趣的计算字段和技巧

```mysql
SELECT 8*8;
```

```mysql
SELECT Concat('this is ',Trim('  adc   '),'.');
```

```mysql
SELECT Now();
-- 返回"现在"的时间。
```

# 数据处理函数

---

上述的计算字段，实际上就是数据处理函数的使用，接下来介绍更多常用的数据处理函数。

```mysql
SELECT vend_name,Upper(vend_name) AS vend_name_upcase
FROM vendors
ORDER BY vend_name;
-- Upper()函数可将文本全部转换为大写。
```

![p5](images/p3_5.png)



## 常用的文本处理函数

---

![p6](images/p3_6.png)

> 有关`Soundex()`，SOUNDEX是一个将任何文本串转换为描述其语音表示的字母数字模式的算法。

话不多说，实例展示

```mysql
SELECT cust_name,cust_contact
FROM customers;
-- 先进行一下正常的基础检索
```

![p7](images/p3_7.png)

```mysql
SELECT cust_name,cust_contact
FROM customers
WHERE cust_contact = 'Y Lie';
-- 'Y Lie'和'Y Lee'的写法不同，但读音相同，这样查找显然将得到一个空集。
```

![p8](images/p3_8.png)

```mysql
SELECT cust_name,cust_contact
FROM customers
WHERE Soundex(cust_contact) = Soundex('Y Lie');
-- 这就是Soundex()函数的作用
-- SOUNDEX是一个将任何文本串转换为描述其语音表示的字母数字模式的算法。
```

![p9](images/p3_9.png)



## 常用日期和时间处理函数

---

![p10](images/p3_10.png)

> 无论什么时候指定一个日期，其格式都必须是：`yyyy-mm-dd`。



### 基础

---

```mysql
SELECT * FROM orders;
```

![p11](images/p3_11.png)

```mysql
SELECT cust_id, order_num
FROM orders
WHERE order_date = '2005-09-01';
-- yyyy-mm-dd
```

![p12](images/p3_12.png)



### 进阶1

---

接下来进行更细致的检索，在检索前先插入一条新的数据：

```mysql
INSERT INTO orders(order_num, order_date, cust_id)
VALUES(20010, '2022-8-28 20:17:19', 10001);
```

```mysql
SELECT * FROM orders;
```

![p13](images/p3_13.png)

```mysql
SELECT cust_id, order_num
FROM orders
WHERE order_date = '2022-8-28';
-- 这将得到一个空集，即使日期在数据中能够匹配，但显然时间不对。
```

![p14](images/p3_14.png)

```mysql
SELECT cust_id, order_num
FROM orders
WHERE Date(order_date) = '2022-8-28';
-- 返回日期时间的日期部分。
-- 在实际应用中，显然应该使用这种方法进行日期时间数据的检索方式（即使用Date()函数）。
```

![p15](images/p3_15.png)



### 进阶2

---

```mysql
SELECT cust_id, order_num
FROM orders
WHERE Date(order_date) BETWEEN '2005-09-01' AND '2005-09-30';
-- 检索某一时间段
```

![p16](images/p3_16.png)



### 进阶3

---

```mysql
SELECT cust_id,order_num
FROM orders
WHERE Year(order_date) = 2005 AND Month(order_date) = 9;
-- 进行2005年9月的数据过滤。
```

![p17](images/p3_17.png)



## 常用数值处理函数

---

![p18](images/p3_18.png)


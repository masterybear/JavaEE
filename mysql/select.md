# 引言

现在我们正式进入MySQL的学习，先了解一些基本语句

```mysql
SHOW DATABASES;
-- 返回可用数据库的一个列表。
CREATE DATABASE crashcourse;
-- 创建一个名为‘crashcourse’的数据库
USE crashcourse;
-- 使用这个数据库
SHOW TABLES;
-- 返回此数据库内可用表的列表
SHOW COLUMNS FROM customers;
-- 返回‘customers’表中所有列名的列表
```

# 检索数据



## 基础检索

一个简单的检索语句：

```MySQL
SELECT prod_name FROM products;
-- 检索单个列
```

![](images\selectone.png)

---

```mysql
SELECT prod_id,vend_id,prod_name FROM products;
-- 检索多个列
```

![](images\selectN.png)

---

```mysql
SELECT * FROM products;
-- 检索所有列
```

![](images\selectAll.png)

---



## 鲜明检索

```mysql
SELECT vend_id FROM products;
-- 常规检索
```

![](images\selectExm.png)

---

检索时，在某些情况下得到的原始数据会有重复的情况，下面的语句可以让每个数据仅出现一次，使检索到的数据都是不同的。

```mysql
SELECT DISTINCT vend_id FROM products;
-- 鲜明检索
```

![](images\distinct.png)

---



## 限制检索

```mysql
SELECT prod_name FROM products;
-- 常规检索
```

![](images\selectExm1.png)

---

```mysql
SELECT prod_name FROM products LIMIT 5;
-- 使返回的检索数据不得超过5行。
```

![](images\limit.png)

---

```mysql
SELECT prod_name FROM products LIMIT 5,5;
-- 从行5开始检索，检索的数据不得超过5行。
-- 检索的数据第一行不是行1，而是行0。
```

![](images\limit1.png)

---

```mysql
SELECT prod_name FROM products LIMIT 2,3;
-- 从行2开始检索，检索的数据不得超过3行。
```

![](images\limit2.png)

---

由于类似`LIMIT 2,3`这样的写法容易产生歧义导致混乱，所以在MySQL_5版本中，可以更规范的书写相同语义的句子。

```mysql
SELECT prod_name FROM products LIMIT 3 OFFSET 2;
```

![](images\limit3.png)



## 完全限定名

```mysql
SELECT products.prod_name FROM crashcourse.products;
-- 完全限定名检索
```

![](images\Exm.png)

---

```mysql
SELECT prod_name FROM products;
-- 常规检索
```

![](images\selectExm1.png)

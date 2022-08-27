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

# 完全限定名

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

# 数据检索



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



# 排序检索

## 按单列

```mysql
SELECT prod_name FROM products ORDER BY prod_name;
-- 取prod_name列的名字据此进行排序
```

![](images\oby.png)

```mysql
SELECT prod_name FROM products ORDER BY prod_price;
-- 取其他列的名字据此进行排序也是合法的，但顺序就不尽如人意了
```

![](images\oby1.png)

## 按多列

```mysql
SELECT prod_id,prod_price,prod_name FROM products ORDER BY prod_price,prod_name;
-- 此种排序方式类似于对姓名进行排序，即先按姓氏、再按照名字。
```

![](images\oby2.png)



## 指定方向

```mysql
SELECT prod_id,prod_price,prod_name FROM products ORDER BY prod_price DESC;
-- DESC关键字为倒序，ASC为正序，默认为ASC
-- 当按多列进行倒序排序时如下
SELECT prod_id,prod_price,prod_name FROM products ORDER BY prod_price DESC,prod_name DESC;
```

![](images\desc.png)

![](images\oby3.png)



# 过滤检索

```mysql
SELECT prod_name,prod_price 
FROM products 
WHERE prod_price = 2.50;
-- 检索两列，仅返回满足过滤条件的数据“ prod_price = 2.50 ”。
```

![](images\where.png)

![](images\symbol.png)

```mysql
SELECT prod_name,prod_price 
FROM products 
WHERE prod_name = 'carrots';
-- 不区分大小写
```

![](images\whereExm.png)



## BETWEEN操作符

```mysql
SELECT prod_name,prod_price 
FROM products 
WHERE prod_price BETWEEN 2.50 AND 10.00 
ORDER BY prod_price;
-- 介于价格在2.50到10.00之间的数据
-- between后接着起始值，and后接着结束值
```

![](images\between.png)



## 空值检查

```mysql
SELECT * FROM vendors;
```

![](images\nullExm.png)

```mysql
SELECT vend_name,vend_state 
FROM vendors 
WHERE vend_state IS NULL;
-- 条件：vend_state值为空
```

![](images\null.png)



## AND操作符

```mysql
SELECT vend_id,prod_name,prod_price 
FROM products 
WHERE vend_id = 1003 AND prod_price <= 10;
-- 过滤条件：vend_id = 1003 且 prod_price <= 10。
```

![](images\and.png)



## OR操作符

```mysql
SELECT vend_id,prod_name,prod_price 
FROM products 
WHERE vend_id = 1002 OR vend_id = 1003;
-- 过滤条件：vend_id = 1002 或 vend_id = 1003。
```

![](images\or.png)



## 计算次序

```mysql
SELECT vend_id,prod_name,prod_price 
FROM products 
WHERE (vend_id = 1002 OR vend_id = 1003) AND prod_price >= 10;
```

![](images\sequence.png)



## IN操作符

```mysql
SELECT prod_name,prod_price 
FROM products 
WHERE vend_id IN (1002,1003) 
ORDER BY prod_price;
-- 免得使用过多的OR操作符引起不必要的混乱
```

![](images\in.png)



## NOT 操作符

```mysql
SELECT prod_name,prod_price
FROM products
WHERE vend_id NOT IN (1002,1003)
ORDER BY prod_price;
-- 起否定意义
```

![](images\not.png)



## like通配符

predicate：谓词，like其实起到的只是类似操作符的作用，like本身是predicate，通配符是下面要说的这些。

```mysql
SELECT prod_name,prod_id,prod_price
FROM products
WHERE prod_name LIKE 'jet%';
-- % 表示任意字符出现任意次数。
```

![](images\like1.png)

```mysql
SELECT prod_name,prod_id,prod_price
FROM products
WHERE prod_name LIKE '%anvil%';
```

![](images\like2.png)

---

```mysql
SELECT prod_name,prod_id,prod_price
FROM products
WHERE prod_name LIKE '_ ton anvil';
-- _ 通配符表示单个字符。
```

![](images\like3.png)

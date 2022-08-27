# 计算字段

数据库中的数据在面对实际需求时，往往显得过于原始，通常需要对数据进行再次加工而满足客户端的需要，但这种操作不可能在检索数据结束 以后拿到客户机进行，我们需要直接检索出我们想要的数据，这就是计算字段诞生的理由了。

计算字段做什么？比方说，将企业的名字和企业的地址这两列数据在检索时拼接成一列数据。

## 拼接字段

```mysql
SELECT Concat(vend_name,' (',vend_country,')')
FROM vendors
ORDER BY vend_name;
-- Concat()函数的作用相当于Java语言中的"+".
```

![](images\field01.png)



---

## `Trim`函数

> `RTrim()`去除数据右侧多余空格
>
> `LTrim()`去除数据左侧多余空格
>
> `Trim()`去除数据两侧的多余空格

```mysql
SELECT Concat(RTrim(vend_name),' (',RTrim(vend_country),')')
FROM vendors
ORDER BY vend_name;
```

![](images\field02.png)



---

## 别名

```mysql
SELECT Concat(RTrim(vend_name),' (',RTrim(vend_country),')') AS vend_title
FROM vendors
ORDER BY vend_name;
-- 使用AS关键字进行别名的命名。
-- 别名有时也被称作"导出列"。
```

![](images\field03.png)



---

## 算数字段

```mysql
SELECT prod_id,
quantity,
item_price,
quantity*item_price AS expanded_price
FROM orderitems
WHERE order_num = 20005;
-- 计算总价格
```

![](images\field04.png)

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



---
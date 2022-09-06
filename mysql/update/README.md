# 更新表中数据

> 更新与删除

# UPDATE语句

## 更新单个列

```mysql
UPDATE customers
SET cust_email = 'elmer@fudd.com'
WHERE cust_id = 10005;
```

释译：更新表`customers`中`cust_id = 10005`的`cust_email`信息为`elmer@fudd.com`。

## 更新多个列

```mysql
UPDATE customers
SET cust_name = 'The Fudds',
    cust_email = 'elmer@fudd.com'
WHERE cust_id = 10005;
```

在更新数据的整个过程中，即使已经完成了一部分数据的更新操作，但只要发生一项错误，那么就会将所有数据（包括已更新）全部恢复为执行之前的状态。如果有需要，可以保留已更新成功的数据，那么需要使用关键字`IGNORE`。

```mysql
UPDATE IGNORE customers
SET ...... 
WHERE cust_id = 10005;
```



# DELETE语句

## 删除单行

```mysql
DELETE FROM customers
WHERE cust_id = 10006;
```

释译：删除`customers`表中`cust_id = 10006`的行。

`DELETE`语句不能删除列，只能删除行。

## 删除多行

可使用`TRUNCATE TABLE`代替`DELETE`语句来完成，速度会更快。

- 就像刚才说到的，删除所有行，这只能删除表中的数据，而不是删除表！`TRUNCATE TABLE`比`DELETE`拥有更快的删除速度的原因是其先删除了整个表，又重新创建了这个表，无论是二者的哪一个，都不能真正意义上的删除表。

# info

无论是`UPDATE`语句还是`DELETE`语句，如果不添加`where`字句，就会对整个表进行操作！
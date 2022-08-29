# 联结

联结是一种机制，用来在一条`SELECT`语句中关联表，因此称之为联结。

由于数据冗余会降低数据库的性能，所以良好的数据库都会尽量避免数据冗余来换取更好可伸缩性。作为存储方来说，这样的数据库是优秀的，但作为提取方，在数据检索时，这种设计思想却成为了数据检索的问题，就是如何更轻量化的完成数据的精细检索？联结就是这个问题的解决方案，既然数据在存储时被分散了，那么就在提取时重新联结成整体从而方便接下来工作的进行。

## 联结的特性

联结与之前的数据处理函数的生命周期类似，都是在查询时开始，查询完成后结束，不会修改真正的数据表，更不是真正的数据表，而仅仅是这个过程中的一种临时产物。

## 创建联结

多说无益，按照惯例：

```mysql
SELECT vend_name,prod_name,prod_price
FROM vendors,products
WHERE vendors.vend_id = products.vend_id
ORDER BY vend_name,prod_name;
-- vendor_id是vendors表的主键，也是products表的MUL。
-- 这样，就通过WHERE字句建立了联结。
```

![](images\join01.png)


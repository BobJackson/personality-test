# 性格测试

## 当前算法已知bug:
* 当distinct score count = 3 or 2,计算不正确，for example:
````
 - Tiger got 21
 - Peacock got 20
 - Owl got 20
 - Koala got 18
 - Chameleon got 20
 - distinct score count = 3
 - result = Koala,Peacock,Owl
````
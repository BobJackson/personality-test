# 性格测试

## 取最高分(分值从高到低排列)
* 依次是A B C D E, 只取第一项A；
* 依次是A A B C D, 前两项为待选项，如果包含变色龙，就是变色龙，否则就是前两项组合；
* 依次是A A A B C, 前三项为待选项，如果包含变色龙，就是变色龙，否则就是前三项组合；
* 依次是A A A A B, 四项相同，就是变色龙；
* 依次是A A A A A, 五项相同，就是变色龙；

## 测试数据
看下面的测试数据来说，
````
Tiger got 21
Owl got 19
Koala got 19
Peacock got 18
Chameleon got 14
result = Tiger
````

````
Peacock got 19
Koala got 19
Chameleon got 19
Tiger got 15
Owl got 15
result = Chameleon
````


下面是有问题的数据
````
Tiger got 21
Owl got 21
Chameleon got 21
Koala got 18
Peacock got 16
result = Tiger,Owl
````
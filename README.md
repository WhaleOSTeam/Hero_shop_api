# Hero_shop_api
> A java project no any framework
## requier jar 

#api introduce
第一个login
userApi?method=login&username=jacye.li&password=1234567&autoLogin=autoLogine
error 0 成功 1 用户名密码错误 2 未激活

#后台管理 分类管理模块
查所有的分类
http://localhost:7070/Hero_shop_api/categoryApi?
method=findAllCategory&isOpen=0 & 1
isOpen 不传 或传 空字符串 查询所有  传 1 或 0 按开启关闭查

增加一个分类 
http://localhost:7070/Hero_shop_api/categoryApi?
method=addCategory&cname=日用商品&isOpen=0

开启或关闭一个分类
http://localhost:7070/Hero_shop_api/categoryApi?method=categoryState&cid=3cc21f25-687a-433a-8a3e-62a0796a15bd&isOpen=1


编辑一个分类(改变分类名称)
http://localhost:7070/Hero_shop_api/categoryApi?
method=updataCategory&cid=3cc21f25-687a-433a-8a3e-62a0796a15bd&cname=



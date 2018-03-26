# anyang-frame
基于mybatis，springboot，tk.mybatis等框架二次开发，实现crud，controller，service，dao。
<br>
联系方式qq：1513977901

#使用准备
##1.插件<br/>
lombok:自动生成setget等，安装方式：idea在setting-plugins 查询lombok；eclipse，官网下载，安装jar

##2.环境
dev为开发环境，staging为测试环境，prod生产环境

# 数据库命名规范
## 表名

* 单数
* 小写+下划线(_)

## 字段

* id, int,自增加
* 小写+下划线(_)
* 尽量描述清楚
* 除了多对多ID关联的表，一些特殊的三方数据表，默认需要加上这几个字段 

**Example:**

```
`created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
`creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新者',
`last_updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`sort` char(1) NOT NULL DEFAULT '1' COMMENT '激活标记',
`enabled` char(1) NOT NULL DEFAULT '1' COMMENT '激活标记',
`deleted` char(1) NOT NULL DEFAULT '0' COMMENT '激活标记',
```

## 实体类
* 实体名称只要遵循上述规则，不需要指定@Column注解，自动会将下划线的表字段名称转换成实体驼峰的字段

#业务代码引用方式
##1. controller
```
@Slf4j
@RestController
@RequestMapping("/User")
public class UserController extends AbstractCRUDHandler<Integer, UserDTO, UserService> {


}
```

##2. service
```
public interface ConsoleUserService extends PageableService<Integer,UserDTO> {



}
```
```
@Log4j
@Service
public class UserServiceImpl extends AbstractStatelessService<Integer,UserDTO, User, UserMapper>
        implements UserService {

}
```

##3. dao
未完成（暂时可以不需要写,仍然可以做到crud）


##4. entity
```
EqualsAndHashCode(callSuper = true)
   @Data
   @Table
   public class User extends AbstractEntity<Integer> {
   }
```

##5. dto
```
@Data
public class UserDTO extends AbstractDTO {
}
```

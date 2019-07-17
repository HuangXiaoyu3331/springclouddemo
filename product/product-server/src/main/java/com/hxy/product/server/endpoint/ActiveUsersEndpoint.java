package com.hxy.product.server.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义端点（endpoint）示例
 *
 * 若果设置enableByDefault = false，默认不在HTTP上公开，要公开的话需要在配置文件中配置
 *
 * 配置示例：management.endpoints.web.exposure.include="active-users"
 * 公开多个端口，用逗号隔开，如：management.endpoints.web.exposure.include="active-users,beans"
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ActiveUsersEndpoint
 * @date 2019年07月17日 17:42:35
 */
@Component
@Endpoint(id = "active-users", enableByDefault = false)
public class ActiveUsersEndpoint {

    private final Map<String, User> users = new HashMap();

    ActiveUsersEndpoint() {
        this.users.put("Fero", new User("Kebbour Benani Smiras"));
        this.users.put("Weld", new User("Chaabia Mert Kebour"));
        this.users.put("Hero", new User("Habib Saheb Kebbour"));
    }

    /**
     * ReadOperation 注解对应get请求
     * 示例：http://localhost:9976/actuator/active-users
     */
    @ReadOperation
    public List getAll() {
        return new ArrayList<>(users.values());
    }

    /**
     * 示例：http://localhost:9976/actuator/active-users/Fero
     */
    @ReadOperation
    public User getActiveUser(@Selector String user) {
        return this.users.get(user);
    }

    /**
     * WriteOperation 注解对应post请求
     * 示例：post http://localhost:9976/actuator/active-users/hxy
     * 参数：{"user":"Huang Xiao Yu"}
     */
    @WriteOperation
    public void updateActiveUser(@Selector String name, String user) {
        this.users.put(name, new User(user));
    }

    /**
     * DeleteOperation 注解对应delete请求
     * 示例：delete http://localhost:9976/actuator/active-users/hxy
     */
    @DeleteOperation
    public void deleteActiveUser(@Selector String name) {
        this.users.remove(name);
    }

    public static class User {
        private String name;

        User(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

package com.hxy.user.server.controller;

import com.hxy.common.core.ApiResponse;
import com.hxy.common.utils.CookieUtil;
import com.hxy.common.utils.RedisUtil;
import com.hxy.user.client.vo.reqeust.LoginUserVo;
import com.hxy.user.client.vo.reqeust.RegisterUserReqVo;
import com.hxy.user.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: UserController
 * @date 2019年08月26日 11:37:24
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 注册用户
     */
    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterUserReqVo registerUserReqVo) {
        return userService.register(registerUserReqVo);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody LoginUserVo loginUserVo, HttpSession session, HttpServletResponse response) {
        return userService.login(loginUserVo, session, response);
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public ApiResponse logout(HttpServletRequest request, HttpServletResponse response) {
        // 删除cookie
        CookieUtil.delLoginToken(request, response);
        // 删除redis缓存
        String loginToken = CookieUtil.readLoginToken(request);
        redisUtil.del(loginToken);
        return ApiResponse.createBySuccess();
    }

}

package com.hxy.user.server.service;

import com.hxy.common.core.ApiResponse;
import com.hxy.user.client.vo.reqeust.LoginUserVo;
import com.hxy.user.client.vo.reqeust.RegisterUserReqVo;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: UserService
 * @date 2019年08月26日 11:52:43
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param registerUserReqVo
     * @return
     */
    ApiResponse register(RegisterUserReqVo registerUserReqVo);

    /**
     * 用户登录
     *
     * @param loginUserVo 登录信息
     * @param session     session
     * @param httpServletResponse     httpServletResponse
     * @return
     */
    ApiResponse login(LoginUserVo loginUserVo, HttpSession session, HttpServletResponse httpServletResponse);
}

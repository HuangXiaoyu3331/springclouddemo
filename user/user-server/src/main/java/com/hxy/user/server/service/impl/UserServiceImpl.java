package com.hxy.user.server.service.impl;

import com.hxy.common.core.ApiResponse;
import com.hxy.common.enums.DeleteFlagEnum;
import com.hxy.common.error.CommonConst;
import com.hxy.common.error.SystemError;
import com.hxy.common.error.UserError;
import com.hxy.common.exception.AppException;
import com.hxy.common.utils.CookieUtil;
import com.hxy.common.utils.Md5Util;
import com.hxy.common.utils.ObjectMapperUtil;
import com.hxy.common.utils.RedisUtil;
import com.hxy.user.client.vo.LoginUserInfoVo;
import com.hxy.user.client.vo.reqeust.LoginUserVo;
import com.hxy.user.client.vo.reqeust.RegisterUserReqVo;
import com.hxy.user.server.bean.model.UserModel;
import com.hxy.user.server.common.Const;
import com.hxy.user.server.dao.UserMapper;
import com.hxy.user.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: UserServiceImpl
 * @date 2019年08月26日 11:54:26
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ApiResponse register(RegisterUserReqVo registerUserReqVo) {
        // 判断用户名是否已存在
        ApiResponse validResponse = checkValid(registerUserReqVo.getUserName(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            throw new AppException(UserError.USERNAME_ALREADY_EXISTS);
        }
        // 判断邮箱是否已存在
        validResponse = checkValid(registerUserReqVo.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            throw new AppException(UserError.EMAIL_ALREADY_EXISTS);
        }

        UserModel userModel = ObjectMapperUtil.convert(registerUserReqVo, UserModel.class);

        if (userModel != null) {
            //新增用户
            String randomSalt = Md5Util.getRandomSalt();
            userModel.setSalt(randomSalt);
            userModel.setPassword(Md5Util.md5EncodeUtf8(registerUserReqVo.getPassword() + randomSalt));
            userModel.setDeleteFlag(DeleteFlagEnum.NO.getCode());
            int resultCount = userMapper.insert(userModel);
            if (resultCount > 0) {
                return ApiResponse.createBySuccess();
            }
        }

        return ApiResponse.createByError(SystemError.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse login(LoginUserVo loginUserVo, HttpSession session, HttpServletResponse httpServletResponse) {
        // 根据用户名查询用户
        UserModel userModel = userMapper.selectByUsername(loginUserVo.getUsername());
        if (null != userModel) {
            // 判断密码是否正确
            String loginPassword = Md5Util.md5EncodeUtf8(loginUserVo.getPassword() + userModel.getSalt());
            if (loginPassword.equals(userModel.getPassword())) {
                // 将cookie写入到客户端
                CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                // 将用户信息存到redis
                LoginUserInfoVo loginUserInfoVo = ObjectMapperUtil.convert(userModel, LoginUserInfoVo.class);
                redisUtil.setex(session.getId(), ObjectMapperUtil.obj2Json(loginUserInfoVo), CommonConst.Cookie.REDIS_SESSION_EXPIRE_TIME);
                return ApiResponse.createBySuccess();
            }
        }
        //用户名/密码错误
        throw new AppException(UserError.USERNAME_OR_PASSWORD_NOT_EXISTS);
    }

    /**
     * 校验用户名|email是否可用
     *
     * @param str  用户名|email
     * @param type username|email
     * @return ApiResponse
     */
    private ApiResponse checkValid(String str, String type) {
        log.info("校验参数是否可用，type:{},str:{}", type, str);
        if (StringUtils.isNotBlank(str) || StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUserName(str);
                if (resultCount > 0) {
                    return ApiResponse.createByError(UserError.USERNAME_ALREADY_EXISTS);
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ApiResponse.createByError(UserError.EMAIL_ALREADY_EXISTS);
                }
            }
        } else {
            log.warn("校验失败,str|type为空");
            return ApiResponse.createByError(SystemError.PARAM_ERROR);
        }
        return ApiResponse.createBySuccess("校验通过");
    }
}

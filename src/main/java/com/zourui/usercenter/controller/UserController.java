package com.zourui.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zourui.usercenter.common.BaseResponse;
import com.zourui.usercenter.common.ErrorCode;
import com.zourui.usercenter.common.ResultUtils;
import com.zourui.usercenter.contant.UserContant;
import com.zourui.usercenter.exception.BusinessException;
import com.zourui.usercenter.model.domain.User;
import com.zourui.usercenter.model.domain.request.UserLoginRequest;
import com.zourui.usercenter.model.domain.request.UserRegisterRequest;
import com.zourui.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zourui.usercenter.contant.UserContant.ADMIN_ROLE;
import static com.zourui.usercenter.contant.UserContant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null){
            //核心，这里抛出异常会被GlobalExceptionHandler捕获，返回给前端指定信息
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求数据为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode=userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        //服务层返回的封装到BaseResponse中，这样返回给前端的内容就更多了
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        return ResultUtils.success(result);
    }
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求数据为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求数据为空");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
    //获取当前登录的用户接口
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser=(User)userObj;
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NO_Login,"用户未登录");
        }
        long userId=currentUser.getId();
        //todo 校验用户是否合法
        User user = userService.getById(userId); //根据当前登录态键的用户拿到其id
        User safetyUser = userService.getSafetyUser(user);//返回其脱敏的信息
        return ResultUtils.success(safetyUser);
    }
    //查询接口
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName,HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){  //username不为空
            queryWrapper.like("userName",userName);
        }
        List<User> userlist = userService.list(queryWrapper);
        /**先把suerList转化为数据流，然后遍历
         * userList中的每一个元素，每个元素密码设置为空
         * 最后把这些在拼成一个list
         */
        List<User> list = userlist.stream().map(user ->
                userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }
    //删除接口
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        if(!isAdmin(request)){  //如果不是管理员
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        boolean b = userService.removeById(id);//开启了逻辑删除，所以这里删除只是逻辑删除，数据库中还有数据
        return ResultUtils.success(b);
    }

    //把仅管理员操作提取为一个方法
    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User)userObj;
        if(user==null||user.getUserRole()!=ADMIN_ROLE){
            return false;
        }
        return true;
    }
}

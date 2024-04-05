package com.zourui.usercenter.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zourui.usercenter.common.ErrorCode;
import com.zourui.usercenter.common.ResultUtils;
import com.zourui.usercenter.exception.BusinessException;
import com.zourui.usercenter.service.UserService;
import com.zourui.usercenter.model.domain.User;
import com.zourui.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zourui.usercenter.contant.UserContant.USER_LOGIN_STATE;

/**
 * @author www21
 * @description 针对表【user(用户表)】的数据库操作Service实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;
    private static final String SALT = "zourui"; //加盐

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        //1.校验
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        if(planetCode.length()>5){ //校验星球编号长度
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }
        //账户不能包含特殊字符
        //清掉所有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount); //匹配正则表达式
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号有特殊字符");
        }
        //密码和校验密码一样
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");
        }
        //账户不能重复（这个放后面可以减少资源的占用）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复");
        }
        //星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
         count= userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号重复");
        }
        //2.密码加密（也可以用MD5Util,这里用了spring的工具类）
        final String SALT = "zourui"; //加盐
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {  ///如果saveResult为假，则执行
            //因为User类里面是Long类型，这里如果返回null，会拆箱错误，所以需要判断
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数据为空");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号过长");
        }
        //账户不能包含特殊字符
        //清掉所有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount); //匹配正则表达式
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号有特殊字符");
        }
        //2.密码加密（也可以用MD5Util,这里用了spring的工具类）
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);

        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");//日志信息
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        //脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登录态
        //如果登录成功
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }
    //3.用户脱敏,密码不返回
    @Override
    public User getSafetyUser(User originUser){
        if(originUser==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(new Date());
        safetyUser.setIsDelete(0);
        return safetyUser;
    }
    //用户登录时是记录登录态，那么注销时就移除登录态
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE); //移除登录态
        return 1;
    }
}






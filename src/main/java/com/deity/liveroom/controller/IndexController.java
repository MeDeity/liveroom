package com.deity.liveroom.controller;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class IndexController {

    @RequestMapping(value = "/index")
    public String index(Model model){
        return "index";
    }

    @RequestMapping(value = "/main")
    public String main(){
        return "/page/main";
    }
    @RequestMapping(value = "/newsList")
    public String newsList(){
        return "/page/news/newsList";
    }

    @RequestMapping(value = "/images")
    public String images(){
        return "/page/img/images";
    }

    @RequestMapping(value = "/404")
    public String notFound(){
        return "/page/404";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Map<String, Object> map){
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        String msg = "";
        if (null!=exception){
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -- > 账号不存在：");
                msg = "UnknownAccountException -- > 账号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else {
                msg = "else >> "+exception;
                System.out.println("else -- >" + exception);
            }
        }
        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理
        return "/page/login/login";
    }

    /**用户列表*/
    @RequestMapping(value = "/userList")
    public String userList(){
        return "/page/user/userList.html";
    }

    /**用户等级*/
    @RequestMapping(value = "/userGrade")
    public String userGrade(){
        return "/page/user/userGrade.html";
    }

    /**系统参数*/
    @RequestMapping(value = "/basicParameter")
    public String basicParameter(){
        return "/page/systemSetting/basicParameter.html";
    }

    /**系统参数*/
    @RequestMapping(value = "/logs")
    public String logs(){
        return "/page/systemSetting/logs";
    }

    /**友情链接*/
    @RequestMapping(value = "/linkList")
    public String linkList(){
        return "/page/systemSetting/linkList";
    }

    /**图标管理*/
    @RequestMapping(value = "/icons")
    public String icons(){
        return "/page/systemSetting/icons.html";
    }

    /**三级联动模块*/
    @RequestMapping(value = "/addressDoc")
    public String addressDoc(){
        return "/page/doc/addressDoc.html";
    }

    /**bodyTab模块*/
    @RequestMapping(value = "/bodyTabDoc")
    public String bodyTabDoc(){
        return "/page/doc/bodyTabDoc.html";
    }

    /**三级菜单*/
    @RequestMapping(value = "/navDoc")
    public String navDoc(){
        return "/page/doc/navDoc.html";
    }

    /**个人资料*/
    @RequestMapping(value = "/userInfo")
    public String userInfo(){
        return "/page/user/userInfo.html";
    }

    /**修改密码*/
    @RequestMapping(value = "/changePwd")
    public String changePwd(){
        return "/page/user/changePwd.html";
    }
}

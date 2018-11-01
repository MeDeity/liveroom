package com.deity.liveroom.controller;

import com.deity.liveroom.dao.StatDao;
import com.deity.liveroom.dao.UserDao;
import com.deity.liveroom.entity.MsgEntity;
import com.deity.liveroom.entity.UserEntity;
import com.deity.liveroom.utils.IpUtil;
import com.deity.liveroom.utils.NameGenerator;
import com.deity.liveroom.utils.UserAgentUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.invoke.empty.Empty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private StatDao statDao;

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

    @RequestMapping(value = "/403")
    public String notAccess(){
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

    @RequestMapping(value = "/live_room", method = RequestMethod.GET)
    public String hello(@RequestParam(name = "roomId",defaultValue = "10000") String roomId, HttpServletRequest request, ModelMap model) {
        //根据用户ip判断用户是否访问过本站
        String ip = IpUtil.getIp(request);
        HttpSession session = request.getSession();
        UserEntity user;
        if (userDao.findById(ip) != null) {//用户曾经访问过
            System.out.println("用户曾经访问过");
            Optional<UserEntity> userEntityOptional = userDao.findById(ip);
            user = userEntityOptional.get();
        } else {//用户未访问过，存储用户信息
            System.out.println("用户未访问过");
            user = new UserEntity();
            user.setIp(ip);
            user.setRandomName(NameGenerator.generate());
            //System.out.println("ip="+ip+"name="+user.getRandomName());
            userDao.save(user);
        }
        System.out.println("ip="+ip+"name="+user.getRandomName());
        session.setAttribute("user", user);
        model.addAttribute("roomId",roomId);
        //判断用户是手机还是电脑端
        if (UserAgentUtil.JudgeIsMoblie(request)) {//移动端访问

            return "live_m";
        } else {
            model.addAttribute("online_guests", getOnlineUser());
            model.addAttribute("history_guests", getHistoryGuests());
//            return "live";
            return "live_m";
        }

    }

    /**
     * MessageMapping 注解可以接收客户端发送到后台的消息
     * SendTo 会将处理后的消息重新定向到消息代理中
     * @return 封装后的消息,并发送给消息代理，客户端如需监听.需要找消息代理订阅
     */
    @MessageMapping(value = "/chat/{roomId}")
    @SendTo(value = "/topic/group.{roomId}")
    public MsgEntity routeMessage(@DestinationVariable String roomId, String message, @Header(value = "simpSessionAttributes") Map<String, Object> session){
        UserEntity user = (UserEntity) session.get("user");
        String username = user.getRandomName();
        MsgEntity msg = new MsgEntity();
        msg.setCreator(username);
        msg.setsTime(Calendar.getInstance());
        msg.setMsgBody(message);
        return msg;
    }



    @RequestMapping(value = "/online_guests", method = RequestMethod.GET)
    @ResponseBody
    public Set getOnlineUser() {
        return statDao.getAllUserOnline();
    }


    @RequestMapping(value = "/history_guests", method = RequestMethod.GET)
    @ResponseBody
    public List getHistoryGuests() {
        return statDao.getGuestHistory();
    }


}

package com.deity.liveroom.config;

import com.deity.liveroom.entity.UserInfo;
import com.deity.liveroom.entity.UserPermission;
import com.deity.liveroom.service.UserInfoService;
import com.deity.liveroom.service.UserRoleService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShiroRealmWrapper extends AuthorizingRealm {
    private static String TAG = ShiroRealmWrapper.class.getSimpleName();
    private Logger logger = LoggerFactory.getLogger(ShiroRealmWrapper.class);
    @Autowired
    public UserInfoService userInfoService;
    @Autowired
    private UserRoleService userRoleService;
    /**授权*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String userName = (String) principalCollection.getPrimaryPrincipal();
        List<UserPermission> list = userRoleService.findRoleByUserName(userName);
//        for (SysRole role:user.getRoleList()){
//            authorizationInfo.addRole(role.getRole());
//            logger.warn("add role:"+role.getRole());
//            System.out.println("add role:"+role.getRole());
//            for (SysPermission permission :role.getPermissions()){
//                authorizationInfo.addStringPermission(permission.getPermission());
//                System.out.println("add permission:"+permission.getPermission());
//            }
//        }
        for (UserPermission userPermission:list){
            authorizationInfo.addStringPermission(userPermission.getPermission());
            System.out.println("add permission:"+userPermission.getPermission());
        }
        for (UserPermission userPermission:list){
            authorizationInfo.addRole(userPermission.getRole());
            System.out.println("add role:"+userPermission.getRole());
        }
        return authorizationInfo;
    }

    /**认证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();//获取账号
        UserInfo userInfo = null;
        userInfo = userInfoService.findUserByUserName(userName);
        if (null==userInfo){
            return null;
        }
        System.out.println("start authentication: userName:"+userName
                +" password:"+userInfo.getPassword()+ " name:"+getName());
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                userInfo,userInfo.getPassword(),
//                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),getName()
//        );
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,userInfo.getPassword(),getName());
        return authenticationInfo;
    }
}

package com.tyt.qiuzhi.service;

import com.tyt.qiuzhi.dao.LoginTicketDAO;
import com.tyt.qiuzhi.dao.UserDAO;
import com.tyt.qiuzhi.model.HostHolder;
import com.tyt.qiuzhi.model.LoginTicket;
import com.tyt.qiuzhi.model.User;
import com.tyt.qiuzhi.util.QiuzhiUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LoginTicketDAO loginTicketDAO;



    public List<User> selectUsers(int offset, int limit){
        return userDAO.selectUsers(offset,limit);
    }

    public int updateNickName(int id,String nickName){
        return userDAO.updateNickName(id,nickName);
    }

    public int selectUsersCount(){
        return userDAO.selectUsersCount();
    }


    public int updateHeadUrl(int id, String headUrl) {
        return userDAO.updateHeadUrl(id, headUrl);
    }

    public Map<String, Object> register(String email, String password, String nickName, int expiredTime) {
        HashMap<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(email)) {
            map.put("msg", "邮箱不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(nickName)) {
            map.put("msg", "昵称不能为空");
            return map;
        }

        User user = userDAO.selectByEmail(email);

        if (user != null) {
            map.put("msg", "用户已存在");
            return map;
        }

        user = new User();

        user.setEmail(email);
        user.setNickName(nickName);
        user.setSalt(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(QiuzhiUtils.MD5(password + user.getSalt()));
        user.setCreatedDate(new Date());

        userDAO.addUser(user);
        user = userDAO.selectByEmail(user.getEmail());

        String ticket = addLoginTicket(user.getId(), expiredTime);
        if (ticket != null) {
            map.put("ticket", ticket);
        }

        return map;
    }

    public Map<String, Object> login(String email, String password, int expiredTime) {
        HashMap<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(email)) {
            map.put("msg", "邮箱不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByEmail(email);

        if (user == null) {
            map.put("msg", "用户不存在");
            return map;
        }
        if (!QiuzhiUtils.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }


        String ticket = addLoginTicket(user.getId(), expiredTime);
        if (ticket != null) {
            map.put("ticket", ticket);
        }

        return map;
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

    public String addLoginTicket(int userId, int expiredTime) {

        LoginTicket loginTicket = new LoginTicket();

        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24 * expiredTime);
        loginTicket.setExpired(date);

        int i = loginTicketDAO.addTicket(loginTicket);

        if (i > 0) {
            return loginTicket.getTicket();
        }
        return null;
    }


    public int updateProfile(User user) {
        return userDAO.updateProfile(user);
    }

    public int addUser(User user) {
        return userDAO.addUser(user);
    }

    public User selectById(int id) {
        return userDAO.selectById(id);
    }

    public User selectByEmail(String email) {
        return userDAO.selectByEmail(email);
    }

    public Map<String,Object> updatePassword(String nowpass, String pass, String repass) {
        HashMap<String, Object> map = new HashMap<>();

        if (hostHolder.getUser() == null){
            map.put("msg","您还未登录，不能修改密码");
            return map;
        }

        if (StringUtils.isBlank(nowpass)) {
            map.put("msg", "原密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(pass)) {
            map.put("msg", "新密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(repass)) {
            map.put("msg", "确认密码不能为空");
            return map;
        }
        if (!pass.equals(repass)) {
            map.put("msg","两次密码不一致");
            return map;
        }

        User user = hostHolder.getUser();

        if (!QiuzhiUtils.MD5(nowpass + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        userDAO.updatePassword(hostHolder.getUser().getId(), QiuzhiUtils.MD5(pass+user.getSalt()));
        return map;
    }

    public int deleteById(int id) {
        return userDAO.deleteById(id);
    }

}

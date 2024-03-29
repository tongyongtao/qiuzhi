package com.tyt.qiuzhi.interceptor;

import com.tyt.qiuzhi.dao.LoginTicketDAO;
import com.tyt.qiuzhi.dao.UserDAO;
import com.tyt.qiuzhi.model.HostHolder;
import com.tyt.qiuzhi.model.LoginTicket;
import com.tyt.qiuzhi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()){
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder != null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}

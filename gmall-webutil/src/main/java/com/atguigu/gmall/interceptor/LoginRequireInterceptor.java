package com.atguigu.gmall.interceptor;

import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.util.CookieUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginRequireInterceptor implements HandlerInterceptor {

    //目标方法执行前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //1.先判断这个方法是否需要登录后执行
        HandlerMethod handlerMethod = (HandlerMethod) o;
        LoginRequired annotation = handlerMethod.getMethodAnnotation(LoginRequired.class);

        if(annotation!=null) {
            //标了注解，验证是否登录
            //1.验证是否第一次过来只带了一个参数位置的token字符
            String token = request.getParameter("token");
            String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.SSO_COOKIE_NAME);
            boolean needLogin = annotation.needLogin();
            if(!StringUtils.isEmpty(token) ) {
                Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, token);
                cookie.setPath("/");
                //同子域共享cookie
                cookie.setDomain("gmall.com");
                response.addCookie(cookie);
                //将用户信息放入域中
                Map<String, Object> data = CookieUtils.resolveTokenData(token);
                request.setAttribute(CookieConstant.LOGIN_USER_INFO_KEY,data);
                return true;
            }
            //2.验证是否存在登录cookie
            if(!StringUtils.isEmpty(cookieValue)) {
                //说明之前登录过，cookie已经放好
                //验证令牌是否正确
                RestTemplate restTemplate = new RestTemplate();
                String confirmTokenUrl = "http://www.gmallsso.com/confirmToken?token="+cookieValue;

                try{
                    String result = restTemplate.getForObject(confirmTokenUrl, String.class);
                    //令牌验证通过
                    if("ok".equals(result)){
                        //获取jwt中的简单信息，并设置到域中
                        Map<String, Object> data = CookieUtils.resolveTokenData(cookieValue);
                        request.setAttribute(CookieConstant.LOGIN_USER_INFO_KEY,data);

                        return true;
                    } else {
                        //令牌验证失败,重新登录
                        if(needLogin==true) {
                            String redirectUrl = "http://www.gmallsso.com/login?originUrl="+request.getRequestURL();
                            response.sendRedirect(redirectUrl);
                            return false;
                        }
                        return true;
                    }

                } catch (Exception e) {

                }

            }
            //没cookie也没token
            if(StringUtils.isEmpty(cookieValue) && StringUtils.isEmpty(token)) {
                if(needLogin==true) {
                    String redirectUrl = "http://www.gmallsso.com/login?originUrl="+request.getRequestURL();
                    response.sendRedirect(redirectUrl);
                    return false;
                }
                return true;
            }

        } else {
            //没标注解，直接放行
            return true;
        }
        return false;

    }
    //目标方法执行后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    //页面渲染出来后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

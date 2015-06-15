package com.learn.common.filter;

import com.learn.common.utils.SessionAndIdCard;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Yi on 2015/6/7.
 */
public class SystemFilter  extends OncePerRequestFilter {

    /** 登录验证过滤器 */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        // 不过滤的uri
        String[] notFilter =
                new String[] { "/user/index.do", "/user/doLogin.do", "/user/register.do", "/user/doRegister.do", "/user/result.do"};

        // 请求的uri
        String uri = request.getRequestURI();
        // 是否过滤
        boolean doFilter = true;
        for (String s : notFilter)
        {
            if (uri.indexOf(s) != -1)
            {
                // 如果uri中包含不过滤的uri，则不进行过滤
                doFilter = false;
                break;
            }
        }
        if (doFilter)
        {
            // 执行过滤
            // 从session中获取登录者实体
            String userId = String.valueOf(request.getSession().getAttribute("userId"));
            String sessionId = (String) request.getSession().getId();
            if (null == userId || sessionId ==null || userId.equals("null"))
            {
                boolean isAjaxRequest = isAjaxRequest(request);
                if (isAjaxRequest)
                {
                    response.setCharacterEncoding("UTF-8");
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作,请刷新页面");
                    return ;
                }
                String message = "TimeOut";
                response.sendRedirect("/user/index.do?message=" + message);
                return;
            }
            else
            {
                // 如果session中存在登录者实体，则继续，判断是否飞记录中的session

                if(SessionAndIdCard.isTrue(userId,sessionId)){
                    filterChain.doFilter(request, response);
                }
                else{
                    String message = "OtherUser";
                    response.sendRedirect("/user/index.do?message=" + message);
                }
            }
        }
        else
        {
            // 如果不执行过滤，则继续
            filterChain.doFilter(request, response);
        }
    }

    /** 判断是否为Ajax请求
     * <功能详细描述>
     * @param request
     * @return 是true, 否false
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equals(header))
            return true;
        else
            return false;
    }

}


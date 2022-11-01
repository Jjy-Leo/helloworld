package cn.jjy.reggie.filter;

import cn.jjy.reggie.common.BaseUtils;
import cn.jjy.reggie.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: jiajunyu
 * @CreateTime: 2022-08-11  08:48
 * @Description:
 */
@WebFilter(filterName = "loginChkFilter", urlPatterns = "/*")
@Slf4j
public class LoginChkFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        log.info("拦截到请求");
        String requestURI = request.getRequestURI();


        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        for (int i = 0; i < urls.length; i++) {
            if (PATH_MATCHER.match(urls[i], requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (request.getSession().getAttribute("employee") != null) {
            ThreadLocal threadLocal = BaseUtils.getThreadLocal();
            Long empId = (Long) request.getSession().getAttribute("employee");
            threadLocal.set(empId);
            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }
}

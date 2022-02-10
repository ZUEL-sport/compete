package org.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * @author Administrator
 */
public class CorsInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        c.getResponse().setHeader("Access-Control-Allow-Origin","*");
        //允许跨域的方法
        c.getResponse().setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //表示允许的额外header
        c.getResponse().setHeader("Access-Control-Allow-Headers","Authorization,access-token, mobile, power-by, token, Accept, Origin, X-Requested-With, Content-Type, Last-Modified,withCredentials");
        c.getResponse().setHeader("Access-Control-Max-Age", "3600");
        //必须为true
        c.getResponse().setHeader("Access-Control-Allow-Credentials", "true");

        inv.invoke();

    }
}


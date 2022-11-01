package cn.jjy.reggie.common;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 05:04
 **/
public class BaseUtils {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static ThreadLocal getThreadLocal(){
        return threadLocal;
    }
}

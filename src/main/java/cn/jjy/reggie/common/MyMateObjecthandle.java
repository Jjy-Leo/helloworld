package cn.jjy.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 05:00
 **/
@Slf4j
@Component
public class MyMateObjecthandle implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("进入自动填充");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",BaseUtils.getThreadLocal().get());
        metaObject.setValue("updateUser",BaseUtils.getThreadLocal().get());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",BaseUtils.getThreadLocal().get());
    }
}

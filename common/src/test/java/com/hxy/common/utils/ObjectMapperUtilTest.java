package com.hxy.common.utils;

import com.hxy.common.bean.ObjectMapperItem;
import com.hxy.common.bean.ObjectMapperTestBean;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ObjectMapperUtilTest {

    @Test
    public void obj2Json() {
        ObjectMapperTestBean srcDeepObject = new ObjectMapperTestBean();
        srcDeepObject.setId(1L);
        srcDeepObject.setName("hxy");
        srcDeepObject.setStrList(Arrays.asList("a", "b"));
        srcDeepObject.setMap(Maps.newHashMap("key", "value"));
        srcDeepObject.setBeanList(Collections.singletonList(new ObjectMapperItem("15626267878", "lianTong")));

        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("name", null);
        objectMap.put("id", 1L);
        objectMap.put("strList", Arrays.asList("a", "b"));
        objectMap.put("map", Maps.newHashMap("key", "value"));
        objectMap.put("beanList", Collections.singletonList(new ObjectMapperItem("15626267878", "lianTong")));

        ObjectMapperTestBean desDeepObject = ObjectMapperUtil.convert(objectMap, ObjectMapperTestBean.class);
        log.info("desDeepObject:{}", desDeepObject.toString());
        String identObjectStr = ObjectMapperUtil.obj2Json("abcd");
        log.info("identObjectStr:{}", identObjectStr);
    }

    @Test
    public void threadTest(){
        System.out.println("启动了");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
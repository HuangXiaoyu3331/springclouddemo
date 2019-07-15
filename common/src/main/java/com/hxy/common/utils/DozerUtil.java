package com.hxy.common.utils;

import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * dozer 对象映射工具类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: DozerUtil
 * @date 2019年07月13日 12:39:52
 */
public class DozerUtil {

    /**
     * 将List<S>映射到List<T>
     * 示例：List<DestinationObject> destinationObjectClass = DozerUtil.mapList(mapper,sourList,DestinationObject.class);
     *
     * @param mapper                 dozerMapper实例
     * @param sourceList             原始数据List
     * @param destinationObjectClass 目标类类型
     * @return
     */
    public static <T, S> List<T> mapList(final Mapper mapper, List<S> sourceList, Class<T> destinationObjectClass) {
        List<T> targetList = new ArrayList<>();
        for (S s : sourceList) {
            targetList.add(mapper.map(s, destinationObjectClass));
        }
        return targetList;
    }

}

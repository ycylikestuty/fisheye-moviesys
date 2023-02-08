package com.gdesign.fisheyemoviesys.utils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConversionUtils {
    private static final Logger log = LoggerFactory.getLogger(ConversionUtils.class);

    public ConversionUtils() {
    }

    public static <T, V> List<T> transformList(List<V> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList();
        if (CollectionUtils.isEmpty(sourceList)) {
            return targetList;
        } else {
            try {
                Iterator var3 = sourceList.iterator();

                while (var3.hasNext()) {
                    V item = (V) var3.next();
                    T target = targetClass.newInstance();
                    BeanUtils.copyProperties(item, target);
                    targetList.add(target);
                }
            } catch (Exception var6) {
                log.error("参数转换异常", var6);
            }

            return targetList;
        }
    }

    public static <T, V> T transform(V source, Class<T> targetClass) {
        Object target = null;

        try {
            target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
        } catch (InstantiationException var4) {
            log.error("参数转换异常", var4);
        } catch (Exception var5) {
            log.error("未知异常", var5);
        }

        return (T) target;
    }
}

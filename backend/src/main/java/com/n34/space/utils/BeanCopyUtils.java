package com.n34.space.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyUtils {
    public static <T> T copyObject(Object source, Class<T> targetClass) {
        T target = null;
        try {
            target = targetClass.newInstance();
            if (source != null)
                BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public static <S, T> List<T> copyList(List<S> source, Class<T> targetClass) {
        List<T> target = null;
        if (source != null) {
            target = new ArrayList<>();
            for (S e : source) {
                target.add(copyObject(e, targetClass));
            }
        }
        return target;
    }

    public static <S, T> IPage<T> copyPage(IPage<S> source, Class<T> targetClass) {
        IPage<T> target = null;
        if (source != null) {
            target = new Page<>();
            target.setRecords(copyList(source.getRecords(), targetClass));
            target.setTotal(source.getTotal());
            target.setPages(source.getPages());
            target.setCurrent(source.getCurrent());
            target.setSize(source.getSize());
        }
        return target;
    }
}


package com.furongsoft.base.misc;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * JPA工具
 *
 * @author Alex
 */
public class JpaUtils {
    /**
     * 绑定QueryDSL
     *
     * @param querydslBindings 绑定对象
     * @param queryObject      查询对象
     */
    @SuppressWarnings("unchecked")
    public static void bindQuerydsl(QuerydslBindings querydslBindings, EntityPath queryObject) {
        List<Field> fields = getAllFields(queryObject.getType());
        if ((fields != null) && !fields.isEmpty()) {
            fields.forEach(field -> {
                try {
                    Field queryField = queryObject.getClass().getField(field.getName());
                    queryField.setAccessible(true);
                    Object object = queryField.get(queryObject);
                    MatchType type = field.getAnnotation(QueryField.class).type();

                    if (object instanceof StringPath) {
                        bindStringPath(querydslBindings, (StringPath) object, type);
                    } else if (object instanceof NumberPath<?>) {
                        bindNumberPath(querydslBindings, (NumberPath<?>) object, type);
                    } else if (object instanceof DateTimePath<?>) {
                        bindDatetimePath(querydslBindings, (DateTimePath<Date>) object, type);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Tracker.error(e);
                }
            });
        }
    }

    /**
     * 获取所有带有QueryField注解的字段
     *
     * @param clazz 类型
     * @return 字段列表
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        if ((fields != null) && (fields.length > 0)) {
            Stream.of(fields).filter(field -> field.getAnnotation(QueryField.class) != null).forEach(list::add);
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass == Object.class) {
            return list;
        }

        // 递归查询父类的field列表
        List<Field> superFields = getAllFields(superClass);
        if ((superFields != null) && !superFields.isEmpty()) {
            superFields.stream().filter(field -> !list.contains(field)).forEach(list::add);
        }

        return list;
    }

    /**
     * 绑定字符串查询方法
     *
     * @param querydslBindings 绑定对象
     * @param stringPath       字符串
     * @param matchType        比较类型
     */
    private static void bindStringPath(QuerydslBindings querydslBindings, StringPath stringPath, MatchType matchType) {
        switch (matchType) {
            case like:
                querydslBindings.bind(stringPath).first(((path, t) -> stringPath.like("%" + t + "%")));
                break;
            case notLike:
                querydslBindings.bind(stringPath).first(((path, t) -> stringPath.notLike("%" + t + "%")));
                break;
            default:
                break;
        }
    }

    /**
     * 绑定数值查询方法
     *
     * @param querydslBindings 绑定对象
     * @param numberPath       数值
     * @param matchType        比较类型
     */
    private static void bindNumberPath(QuerydslBindings querydslBindings, NumberPath<?> numberPath, MatchType matchType) {
        switch (matchType) {
            case gt:
                querydslBindings.bind(numberPath).first(((path, t) -> numberPath.gt(t)));
                break;
            case ge:
                querydslBindings.bind(numberPath).first(((path, t) -> numberPath.goe(t)));
                break;
            case lt:
                querydslBindings.bind(numberPath).first(((path, t) -> numberPath.lt(t)));
                break;
            case le:
                querydslBindings.bind(numberPath).first(((path, t) -> numberPath.loe(t)));
                break;
            default:
                break;
        }
    }

    /**
     * 绑定日期查询方法
     *
     * @param querydslBindings 绑定对象
     * @param dateTimePath     日期
     * @param matchType        比较类型
     */
    private static void bindDatetimePath(QuerydslBindings querydslBindings, DateTimePath<Date> dateTimePath, MatchType matchType) {
        switch (matchType) {
            case gt:
                querydslBindings.bind(dateTimePath).first(((path, t) -> dateTimePath.gt(t)));
                break;
            case ge:
                querydslBindings.bind(dateTimePath).first(((path, t) -> dateTimePath.goe(t)));
                break;
            case lt:
                querydslBindings.bind(dateTimePath).first(((path, t) -> dateTimePath.lt(t)));
                break;
            case le:
                querydslBindings.bind(dateTimePath).first(((path, t) -> dateTimePath.loe(t)));
                break;
            default:
                break;
        }
    }

    /**
     * 比较类型
     */
    public enum MatchType {
        /**
         * 相等
         */
        eq,

        /**
         * 大于
         */
        gt,

        /**
         * 大于等于
         */
        ge,

        /**
         * 小于
         */
        lt,

        /**
         * 小于等于
         */
        le,

        /**
         * 模糊匹配
         */
        like,

        /**
         * 模糊不匹配
         */
        notLike
    }

    /**
     * 查询字段
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface QueryField {
        /**
         * @return 比较类型
         */
        MatchType type() default MatchType.eq;

        /**
         * @return 是否可空
         */
        boolean nullable() default false;

        /**
         * @return 是否允许空白
         */
        boolean allowEmpty() default false;
    }
}

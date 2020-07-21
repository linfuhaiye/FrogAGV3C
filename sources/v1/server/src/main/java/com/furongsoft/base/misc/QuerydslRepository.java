package com.furongsoft.base.misc;

import com.querydsl.core.types.EntityPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;

/**
 * Querydsl数据仓库
 *
 * @author Alex
 */
@NoRepositoryBean
public interface QuerydslRepository<T, QT extends EntityPath<?>> extends QuerydslPredicateExecutor<T>, QuerydslBinderCustomizer<QT> {
    /**
     * 继承实现customize方法
     *
     * @param bindings Querydsl绑定
     * @param root     实体
     */
    @Override
    default void customize(@Nonnull QuerydslBindings bindings, @Nonnull QT root) {
        JpaUtils.bindQuerydsl(bindings, root);
    }
}

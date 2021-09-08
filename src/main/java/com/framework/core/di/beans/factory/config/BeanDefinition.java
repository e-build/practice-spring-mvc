package com.framework.core.di.beans.factory.config;

import com.framework.core.di.InjectType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

public interface BeanDefinition {
    Constructor<?> getInjectConstructor();
    Set<Field> getInjectFields();
    Class<?> getBeanClasses();
    InjectType getResolvedInjectMode();
}

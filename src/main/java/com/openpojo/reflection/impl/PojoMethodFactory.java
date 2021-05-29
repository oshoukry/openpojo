/*
 * Copyright (c) 2010-2018 Osman Shoukry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openpojo.reflection.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.openpojo.log.LoggerFactory;
import com.openpojo.reflection.PojoMethod;

import static com.openpojo.reflection.utils.AttributeHelper.getFieldNameVariations;

/**
 * This factory handles various method related operations on given Class or Field.
 *
 * @author oshoukry
 */
public class PojoMethodFactory {

  /**
   * Returns all methods on a given Class. Note: Constructors are treated as methods and will be returned in the list
   * as well.
   *
   * @param clazz
   *     The class to introspect for methods / constructors.
   * @return A list of all methods and constructors in a class.
   */
  public static List<PojoMethod> getPojoMethods(final Class<?> clazz) {
    final List<PojoMethod> pojoMethods = new LinkedList<PojoMethod>();

    for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
      pojoMethods.add(new PojoMethodImpl(constructor));
    }

    for (final Method method : clazz.getDeclaredMethods()) {
      pojoMethods.add(new PojoMethodImpl(method));
    }
    return Collections.unmodifiableList(pojoMethods);
  }

  /**
   * Returns a specific method given method name and parameters.
   *
   * @param clazz
   *     The Class that holds the method.
   * @param name
   *     The name of the method to return.
   * @param parameterTypes
   *     The Parameters to match.
   * @return A PojoMethod if found, or null otherwise.
   */
  public static PojoMethod getMethod(final Class<?> clazz, final String name, final Class<?>... parameterTypes) {
    for (final PojoMethod pojoMethod : getPojoMethods(clazz)) {
      if (pojoMethod.getName().equals(name) && Arrays.equals(pojoMethod.getParameterTypes(), parameterTypes)) {
        return pojoMethod;
      }
    }
    return null;
  }

  /**
   * Returns the Getter Method for a field.
   *
   * @param field
   *     The field to lookup the getter on.
   * @return The getter method or null if none exist.
   */
  public static PojoMethod getFieldGetter(final Field field) {
    PojoMethod pojoMethod = null;
    for (final String candidateName : generateGetMethodNames(field)) {
      final Class<?> clazz = field.getDeclaringClass();
      pojoMethod = PojoMethodFactory.getMethod(clazz, candidateName);
      if (pojoMethod != null) {
        if (pojoMethod.getReturnType().isAssignableFrom(field.getType())) {
          if (pojoMethod.isAbstract()) {
            LoggerFactory.getLogger(
                PojoMethodFactory.class).warn("Getter=[{0}] in class=[{1}] rejected due to method being abstract",
                pojoMethod.getName(), field.getDeclaringClass().getName());
            pojoMethod = null;
          }
          break;
        } else {
          LoggerFactory.getLogger(
              PojoMethodFactory.class).warn("Getter=[{0}] in class=[{1}] rejected due non-equal return types [{2} != {3}]",
              pojoMethod.getName(), field.getDeclaringClass().getName(), pojoMethod.getReturnType(), field.getType());
          pojoMethod = null;
        }
      }
    }
    return pojoMethod;
  }

  /**
   * Returns a list for candidate getter names.
   *
   * @param field
   *     Field to generate the candidate getter names for.
   * @return List of candidate method names.
   */
  private static List<String> generateGetMethodNames(final Field field) {
    final List<String> prefix = new LinkedList<String>();
    prefix.addAll(appendFieldNamesWithPrefix("get", field));
    if (field.getType() == boolean.class || field.getType() == Boolean.class) {
      prefix.addAll(appendFieldNamesWithPrefix("is", field));
      String fieldName = field.getName();
      if (fieldName.length() > 2 && fieldName.startsWith("is") && Character.isUpperCase(fieldName.charAt(2)))
        prefix.add(fieldName);
      if (AttributeHelper.getAttributeName(field).length() > 2 && AttributeHelper.getAttributeName(field).startsWith("Is") && Character.isUpperCase(AttributeHelper.getAttributeName(field).charAt(2)))
        prefix.add(AttributeHelper.getAttributeName(field).replaceFirst("Is", "is"));
    }
    return prefix;
  }

  private static List<String> appendFieldNamesWithPrefix(String prefix, Field field) {
    List<String> appendedList = new ArrayList<String>();
    for (String entry : getFieldNameVariations(field)) {
      appendedList.add(prefix + entry);
    }
    return appendedList;
  }

  /**
   * Returns the Setter Method for a field.
   *
   * @param field
   *     The field to lookup the setter on.
   * @return The setter method or null if none exist.
   */
  public static PojoMethod getFieldSetter(final Field field) {
    PojoMethod pojoMethod = null;

    for (final String candidateName : generateSetMethodNames(field)) {
      final Class<?> clazz = field.getDeclaringClass();
      pojoMethod = PojoMethodFactory.getMethod(clazz, candidateName, field.getType());

      if (pojoMethod != null) {
        if (pojoMethod.isAbstract()) {
          LoggerFactory.getLogger(
              PojoMethodFactory.class).warn("Setter=[{0}] in class=[{1}] rejected due to method being abstract",
              pojoMethod.getName(), field.getDeclaringClass().getName());
          pojoMethod = null;
        }
        break;
      }
    }
    return pojoMethod;
  }

  /**
   * Returns a list for candidate setter names.
   *
   * @param field
   *     The field to generate for.
   * @return List of candidate setter names, or empty list if there are none.
   */
  private static List<String> generateSetMethodNames(final Field field) {
    final List<String> prefix = new LinkedList<String>();
    prefix.addAll(appendFieldNamesWithPrefix("set", field));
    String fieldName = field.getName();
    if (fieldName.length() > 2 && fieldName.startsWith("is") && Character.isUpperCase(fieldName.charAt(2)))
      prefix.add("set" + fieldName.substring(2));
    if (AttributeHelper.getAttributeName(field).length() > 2 && AttributeHelper.getAttributeName(field).startsWith("Is") && Character.isUpperCase(AttributeHelper.getAttributeName(field).charAt(2)))
      prefix.add("set" + AttributeHelper.getAttributeName(field).replaceFirst("Is", "is"));

    return prefix;
  }

  private PojoMethodFactory() {
      throw new UnsupportedOperationException(PojoMethodFactory.class.getName() + " should not be constructed!");
    }
}

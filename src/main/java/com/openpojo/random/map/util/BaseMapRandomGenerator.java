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

package com.openpojo.random.map.util;

import java.util.Collection;
import java.util.Map;

import com.openpojo.random.ParameterizableRandomGenerator;
import com.openpojo.reflection.Parameterizable;

/**
 * @author oshoukry
 */
public abstract class BaseMapRandomGenerator implements ParameterizableRandomGenerator {

  @SuppressWarnings("unchecked")
  public Map doGenerate(Class<?> type) {
    return getBasicInstance(type);
  }

  public Map doGenerate(Parameterizable parameterizedType) {
    return MapHelper.buildMap(doGenerate(parameterizedType.getType()), parameterizedType.getParameterTypes().get(0),
        parameterizedType.getParameterTypes().get(1));
  }

  public abstract Collection<Class<?>> getTypes();

  protected abstract Map getBasicInstance(Class<?> type);
}

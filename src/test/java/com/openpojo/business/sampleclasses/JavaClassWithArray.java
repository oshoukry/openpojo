/*
 * Copyright (c) 2010-2015 Osman Shoukry
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

package com.openpojo.business.sampleclasses;

import com.openpojo.business.BusinessIdentity;

/**
 * @author oshoukry
 */
public class JavaClassWithArray {

  @SuppressWarnings({ "unused", "FieldCanBeLocal" })
  private String[][] data;

  public JavaClassWithArray(String[][] data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return BusinessIdentity.toString(this);
  }
}
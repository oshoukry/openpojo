/*
 * Copyright (c) 2010-2017 Osman Shoukry
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

package com.openpojo.issues.issue104;

import com.openpojo.issues.issue104.sample.ClassWithBooleanAndPrefix_prefixIsX;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.reflection.utils.AttributeHelper;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author oshoukry
 */
public class IssueTest {
  @Test
  public void whenBooleanStartsWithAPrefixFollowedBy_Is_AndThirdCharacterIsUpperCase_Getter_IsX_Setter_setIsX() {
    com.openpojo.reflection.cache.PojoCache.clear();;
    AttributeHelper.registerFieldPrefix("prefix");

    PojoClass pojoClass = PojoClassFactory.getPojoClass(ClassWithBooleanAndPrefix_prefixIsX.class);
    Validator pojoValidator = ValidatorBuilder.create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .build();

    pojoValidator.validate(pojoClass);
    }
}

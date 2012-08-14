/**
 * Copyright (C) 2011 Osman Shoukry
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.openpojo.random.impl;

import java.util.Collection;

import com.openpojo.log.Logger;
import com.openpojo.log.LoggerFactory;
import com.openpojo.random.RandomGenerator;
import com.openpojo.random.dynamic.EnumRandomGenerator;
import com.openpojo.random.dynamic.RandomInstanceFromInterfaceRandomGenerator;
import com.openpojo.random.exception.RandomGeneratorException;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.construct.InstanceFactory;
import com.openpojo.reflection.impl.PojoClassFactory;

/**
 * @author oshoukry
 *
 */
public class DefaultRandomGenerator implements RandomGenerator {
    private final RandomInstanceFromInterfaceRandomGenerator interfaceRandomGenerator = RandomInstanceFromInterfaceRandomGenerator.getInstance();
    private final EnumRandomGenerator enumRandomGenerator = EnumRandomGenerator.getInstance();
    private final Logger logger = LoggerFactory.getLogger(DefaultRandomGenerator.class);

    /*
     * (non-Javadoc)
     *
     * @see com.openpojo.random.RandomGenerator#getTypes()
     */
    public Collection<Class<?>> getTypes() {
        throw RandomGeneratorException.getInstance("UnImplemented, this default RandomGenerator should be registered as Default, and has no explicit Types declared");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.openpojo.random.RandomGenerator#doGenerate(java.lang.Class)
     */
    public Object doGenerate(Class<?> type) {
        PojoClass typePojoClass = PojoClassFactory.getPojoClass(type);
        if (typePojoClass.isInterface()) {
            return interfaceRandomGenerator.doGenerate(type);
        }

        if (typePojoClass.isEnum()) {
            return enumRandomGenerator.doGenerate(type);
        }

        if (typePojoClass.isAbstract()) {
            throw RandomGeneratorException.getInstance(String.format(this.getClass().getName()
                                                                             + " can't generate instance for Abstract class [%s], please register a RandomGenerator and try again",
                                                                     typePojoClass));
        }

        logger.info("Creating basic instance for type=[{0}] using InstanceFactory", type);
        return InstanceFactory.getLeastCompleteInstance(PojoClassFactory.getPojoClass(type));

    }
}
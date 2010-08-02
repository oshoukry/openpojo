/**
 * Copyright (C) 2010 Osman Shoukry
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.openpojo.reflection.construct;

import java.util.Arrays;
import java.util.List;

import com.openpojo.random.RandomFactory;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoMethod;
import com.openpojo.reflection.construct.utils.ArrayLengthBasedComparator;
import com.openpojo.reflection.construct.utils.GreaterThan;
import com.openpojo.reflection.construct.utils.LessThan;
import com.openpojo.reflection.exception.ReflectionException;

/**
 * This Factory has the ability to create an instance of any PojoClass.
 * This Factory can create a Pojo using exact argument list, or to randomly create an instance
 * using ANY available constructor.
 *
 * @author oshoukry
 */
public class InstanceFactory {

    /**
     * This method returns a new instance created using the parameters given.
     * If parameters array is null or not null but is of length zero, then the getInstance will call
     * the no arg constructor.
     * If you want to pass null to a single/multiple parameter constructor, then create an array of the correct size
     * matching the parameters.
     *
     * @param pojoClass
     *            The pojoClass to instantiate.
     * @param parameters
     *            The parameters to pass to the constructor.
     * @return
     *         a newly created instance of the class represented in the pojoClass.
     */
    public static Object getInstance(final PojoClass pojoClass, final Object... parameters) {
        List<PojoMethod> constructors = pojoClass.getPojoConstructors();
        for (PojoMethod constructor : constructors) {
            if (areEquivalentParameters(constructor.getParameterTypes(), getTypes(parameters))) {
                return constructor.invoke(null, parameters);
            }
        }
        throw ReflectionException.getInstance(String.format("No matching constructor found using parameters[%s]",
                Arrays.toString(getTypes(parameters))));
    }

    /**
     * This method returns a new instance using the constructor with the most parameters.
     * The values for the constructor will come from RandomFactory.
     *
     * @param pojoClass
     *            The pojoClass to instantiate.
     * @return
     *         a newly created instance of the class represented in the pojoClass.
     */
    public static Object getMostCompleteInstance(final PojoClass pojoClass) {
        PojoMethod constructor = getConstructorByCriteria(pojoClass, new GreaterThan());
        return createInstance(pojoClass, constructor);
    }

    /**
     * This method returns a new instance using the constructor with the least parameters.
     * The values for the constructor will come from RandomFactory.
     *
     * @param pojoClass
     *            The pojoClass to instantiate.
     * @return
     *         a newly created instance of the class represented in the pojoClass.
     */
    public static Object getLeastCompleteInstance(final PojoClass pojoClass) {
        PojoMethod constructor = getConstructorByCriteria(pojoClass, new LessThan());
        return createInstance(pojoClass, constructor);
    }

    /**
     * This method evaluates if the parameters are equivalent / compatible.
     * Null values in the givenTypes are treated as compatible.
     *
     * @param expectedTypes
     *            The expected types.
     * @param givenTypes
     *            The given types to compare with.
     * @return
     *         True if the given can be used as argument list for expected.
     */
    private static boolean areEquivalentParameters(final Class<?>[] expectedTypes, final Class<?>[] givenTypes) {
        if (expectedTypes.length == 0) {
            if (givenTypes.length == 0) {
                return true;
            }
            return false;
        }

        if (givenTypes.length != expectedTypes.length) {
            return false;
        }

        for (int idx = 0; idx < expectedTypes.length; idx++) {
            if (givenTypes[idx] != null && !givenTypes[idx].equals(expectedTypes[idx])) {
                return false;
            }
        }
        return true;
    }

    private static Class<?>[] getTypes(final Object... parameters) {

        if (parameters == null || parameters.length == 0) {
            return new Class<?>[0];
        }

        Class<?>[] types = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] != null) {
                types[i] = parameters[i].getClass();
            }
        }
        return types;
    }

    private static Object createInstance(final PojoClass pojoClass, final PojoMethod constructor) {

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = RandomFactory.getRandomValue(parameterTypes[i]);
        }
        return getInstance(pojoClass, parameters);

    }

    private static PojoMethod getConstructorByCriteria(final PojoClass pojoClass,
            final ArrayLengthBasedComparator comparator) {
        PojoMethod constructor = pojoClass.getPojoConstructors().get(0);
        for (PojoMethod pojoConstructor : pojoClass.getPojoConstructors()) {
            if (comparator.compare(pojoConstructor.getParameterTypes(), constructor.getParameterTypes())) {
                constructor = pojoConstructor;
                System.out.println("Switching constructors from " + pojoConstructor.getParameterTypes().length + " to "
                        + constructor.getParameterTypes().length);
            }
        }
        return constructor;
    }
}

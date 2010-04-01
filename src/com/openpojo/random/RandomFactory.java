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
package com.openpojo.random;

import java.util.HashMap;
import java.util.Map;

import com.openpojo.random.exception.RandomGeneratorException;
import com.openpojo.random.impl.BasicRandomGenerator;
import com.openpojo.random.impl.TimestampRandomGenerator;
import com.openpojo.random.thread.GeneratedRandomValues;

/**
 * This factory is responsible for generating the random values using the registered RandomGenerator(s). <br>
 * This Factory will automatically detect cyclic dependency and return null for the second time around.<br>
 * For example:<br>
 * If you have an Employee class that has the following constructor:<br>
 * <br>
 * <code>
 *     public Employee(final String fullName, final Employee manager) { 
 *       ... 
 *     }
 * </code> <br>
 * <br>
 * And you created the random generator as follows:<br>
 * <br>
 * <code>
 *   public Object doGenerate(Class<?> type) {<br>
 *       return new Employee(RandomFactory.getRandomValue(String.class), (Employee) RandomFactory.getRandomValue(Employee.class));<br>
 *   }
 * </code><br>
 * <br>
 * So to prevent stack over-flow (which would occur by trying to create a manager for every manager), this Factory has
 * built in protection to prevent such a thing by recording for a current recursive call if it's seen this type before,
 * if so, it will return null the second time around.
 * 
 * @author oshoukry
 */
public class RandomFactory {

    private static final Map<Class<?>, RandomGenerator> generators = new HashMap<Class<?>, RandomGenerator>();

    static {
        // register defaults with Factory.
        RandomFactory.addRandomGenerator(new BasicRandomGenerator());
        RandomFactory.addRandomGenerator(new TimestampRandomGenerator());
    }

    /**
     * Add a random generator to the list of available generators.
     * The latest random generator registered wins.
     * 
     * @param generator
     *            The generator to add.
     */
    public static synchronized void addRandomGenerator(final RandomGenerator generator) {
        for (Class<?> type : generator.getTypes()) {
            RandomFactory.generators.put(type, generator);
        }
    }

    /**
     * This method generates a random value of the requested type.<br>
     * If the requested type isn't registerd in the factory, an RandomGeneratorException will be thrown.
     * 
     * @param type
     *            The type to get a random value of.
     * @return
     *         Randomly created value.
     */
    public static final Object getRandomValue(Class<?> type) {
        if (GeneratedRandomValues.contains(type)) {
            return null; // seen before, break loop.
        }
        RandomGenerator randomGenerator = RandomFactory.generators.get(type);
        if (randomGenerator == null) {
            throw new RandomGeneratorException("No Random Generators registered for type " + type.getName());
        }
        GeneratedRandomValues.add(type);
        Object randomValue;
        randomValue = randomGenerator.doGenerate(type);
        GeneratedRandomValues.remove(type);

        return randomValue;
    }
}
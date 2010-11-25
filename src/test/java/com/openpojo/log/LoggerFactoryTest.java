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
package com.openpojo.log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.openpojo.log.impl.Log4JLogger;
import com.openpojo.log.utils.ActiveLogger;
import com.openpojo.validation.affirm.Affirm;

/**
 * @author oshoukry
 */
public class LoggerFactoryTest {
    Class<? extends Logger> defaultLoggerClass = Log4JLogger.class;

    /**
     * This method does the test setup, currently initializes the loggers.
     */
    @Before
    public final void setUp() {
        ActiveLogger.setActiveLogger(defaultLoggerClass);
    }

    @Test
    public final void shouldReturnDefaultLoggerClassByClass() {
        Logger log = LoggerFactory.getLogger(LoggerFactoryTest.class);
        Assert.assertNotNull(log);
        Assert.assertEquals(defaultLoggerClass.getName(), log.getClass().getName());
    }

    @Test
    public final void shouldReturnDefaultLoggerClassByCategory() {
        Logger log = LoggerFactory.getLogger("TestLogger");
        Assert.assertNotNull(log);
        Assert.assertEquals(defaultLoggerClass.getName(), log.getClass().getName());
    }

    /**
     * TODO: send some logs to underlying logging framework and ensure that they are captured on the other end.
     */
    @Test
    public final void shouldReturnDefaultCategoryByClass() {
        Logger log = LoggerFactory.getLogger((Class<?>) null);
        Affirm.affirmNotNull("Null logger returned when requested with null class", log);
        log = LoggerFactory.getLogger((String) null);
        Affirm.affirmNotNull("Null logger returned when requested with null category", log);
    }
}
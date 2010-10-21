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
package com.openpojo.log.common;

/**
 * @author oshoukry
 */
public class LoggerMsgTestData {
    private final String expected;
    private final Object message;

    /**
     * Full constructor
     *
     * @param message
     *            The message to log.
     */
    public LoggerMsgTestData(final String message) {
        this.message = message;
        expected = message;
    }

    public LoggerMsgTestData(final Throwable throwable) {
        message = throwable;
        expected = throwable.getMessage();
    }

    /**
     * @return the expected
     */
    public String getExpected() {
        return expected;
    }

    /**
     * @return the message
     */
    public Object getMessage() {
        return message;
    }
}

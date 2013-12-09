/*
 * Copyright (c) 2010-2013 Osman Shoukry
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License or any
 *    later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.openpojo.validation.utils;

import com.openpojo.business.identity.IdentityFactory;
import com.openpojo.business.identity.IdentityHandler;

/**
 * @author oshoukry
 *
 */
public class IdentityHandlerStub implements IdentityHandler {
    private Object handlerForObject;

    public static void registerIdentityHandlerStubForValue(Object value) {
        final IdentityHandlerStub identityHandlerStub = new IdentityHandlerStub();
        IdentityFactory.registerIdentityHandler(identityHandlerStub);
        identityHandlerStub.setHandlerForObject(value);
    }

    public static void unregisterIdentityHandlerStubForValue(Object value) {
        IdentityHandler identityHandler = IdentityFactory.getIdentityHandler(value);
        IdentityFactory.unregisterIdentityHandler(identityHandler);

    }

    public void setHandlerForObject(final Object handlerForObject) {
        this.handlerForObject = handlerForObject;
    }

    public boolean areEqual(final Object first, final Object second) {
        return first == second;
    }

    public void validate(final Object object) {
    }

    public int generateHashCode(final Object object) {
        throw new RuntimeException("UnImplemented, this class is a stub and doesn't implement unneeded methods");
    }

    public boolean handlerFor(final Object object) {
        return handlerForObject == object;
    }
}

/**
 * 2010 Copyright Osman Shoukry.
 */
package com.openpojo.log.impl;

import com.openpojo.business.BusinessIdentity;
import com.openpojo.log.Logger;
import com.openpojo.reflection.construct.InstanceFactory;
import com.openpojo.reflection.impl.PojoClassFactory;

/**
 * This class wrapps the SLF4J underlying layer.
 * If the underlying layer is Log4J and it has not been configured, it will be set to BasicLogging.
 */
public final class SLF4JLogger extends Logger {

    private final org.slf4j.Logger logger;

    static {
        configureUnderlyingLayer();
    }

    private static void configureUnderlyingLayer() {
        try {

            InstanceFactory.getInstance(PojoClassFactory.getPojoClass(Class
                    .forName("com.openpojo.log.impl.Log4JLogger")), SLF4JLogger.class.getName());
        } catch (Throwable ex) {
            // Not Log4J underlying perhaps.
        }
    }

    private SLF4JLogger(final String category) {
        logger = org.slf4j.LoggerFactory.getLogger(category);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void trace(final Object message) {
        if (message instanceof Throwable) {
            logger.trace(((Throwable) message).getMessage(), (Throwable) message);
        } else {
            logger.trace(toString(message));
        }
    }

    @Override
    public void debug(final Object message) {
        if (message instanceof Throwable) {
            logger.debug(((Throwable) message).getMessage(), (Throwable) message);
        } else {
            logger.debug(toString(message));
        }
    }

    @Override
    public void info(final Object message) {
        if (message instanceof Throwable) {
            logger.info(((Throwable) message).getMessage(), (Throwable) message);
        } else {
            logger.info(toString(message));
        }
    }

    @Override
    public void warn(final Object message) {
        if (message instanceof Throwable) {
            logger.warn(((Throwable) message).getMessage(), (Throwable) message);
        } else {
            logger.warn(toString(message));
        }
    }

    @Override
    public void error(final Object message) {
        if (message instanceof Throwable) {
            logger.error(((Throwable) message).getMessage(), (Throwable) message);
        } else {
            logger.error(toString(message));
        }
    }

    @Override
    public void fatal(final Object message) {
        // SLF4J has no fatal level, so we're marking this as error
        this.error(message);
    }

    private String toString(final Object message) {
        return message == null ? null : message.toString();
    }

    @Override
    public String toString() {
        return BusinessIdentity.toString(this);
    }

}

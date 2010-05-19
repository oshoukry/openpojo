package com.openpojo.random.dynamic;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.openpojo.random.dynamic.sampleclasses.AConcreteClass;
import com.openpojo.random.dynamic.sampleclasses.ASimpleInterface;
import com.openpojo.random.dynamic.sampleclasses.AnAbstractClass;
import com.openpojo.reflection.exception.ReflectionException;

public class PojoProxyFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetProxyPojoFor() {
        ASimpleInterface aSimpleInterface = (ASimpleInterface) PojoProxyFactory.getRandomReturnProxyPojoForInterface(ASimpleInterface.class);

        Assert.assertNotNull(aSimpleInterface.getName());

        String name = aSimpleInterface.getName();
        String otherName = aSimpleInterface.getName();
        if (name.equals(otherName)) { // Just incase they are the same
            Assert.assertFalse(String.format("RandomProxyFactory=[%s] returned a non-Random Pojo Proxy"),
                    name.equals(aSimpleInterface.getName()));
        }

        // now invoke a method that returns void.
        aSimpleInterface.doSomethingUseful();
    }


    @Test (expected = ReflectionException.class)
    public void shouldFailAbstractClass() {
        PojoProxyFactory.getRandomReturnProxyPojoForInterface(AnAbstractClass.class);
    }

    @Test (expected = ReflectionException.class)
    public void shouldFailConcreteClass() {
        PojoProxyFactory.getRandomReturnProxyPojoForInterface(AConcreteClass.class);
    }

}

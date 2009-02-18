/*
 * Copyright 2007-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.gradle.api.plugins

/**
 * @author Hans Dockter
 */
class DefaultConvention extends Convention {

    Map<String, Object> plugins = [:]

    def propertyMissing(String property) {
        if ( hasConventionValue(property) ) {
            return getConventionValue(property).getValue()
        }
        else {
            def pluginConvention = plugins.values().find { it.metaClass.hasProperty(it, property) }
            if (pluginConvention) {
                return pluginConvention."$property"
            }
            throw new MissingPropertyException(property, DefaultConvention)
        }
    }

    boolean hasProperty(String property) {
        if ( hasConventionValue(property) ) {
            return true;
        }
        else {
            def pluginConvention = plugins.values().find { it.metaClass.hasProperty(it, property) }
            if (pluginConvention) {
                return true
            }
            return false
        }
    }

    Map<String, Object> getProperties() {
        Map properties = [:]
        plugins.values().each { properties = it.properties + properties }
        conventionValues.values().each { properties = it.getValue() + properties }
        properties
    }

    void setProperty(String property, value) {
        if ( hasConventionValue(property) ) {
            getConventionValue(property).setValue(value)
        }
        else {
            def pluginConvention = plugins.values().find { it.metaClass.hasProperty(it, property) }
            if (pluginConvention) {
                pluginConvention."$property" = value
                return
            }
            throw new MissingPropertyException(property, DefaultConvention)
        }
    }

    public Object invokeMethod(String name, Object... arguments) {
        doInvokeMethod(name, arguments)
    }

    private Object doInvokeMethod(String method, Object... args) {
        def pluginConvention = plugins.values().find { it.metaClass.respondsTo(it, method, args) }
        if (pluginConvention) {
            return pluginConvention.invokeMethod(method, args)
        }
        throw new MissingMethodException(method, DefaultConvention, args)
    }

    def methodMissing(String method, arguments) {
        doInvokeMethod(method, arguments)
    }
    
    boolean hasMethod(String method, Object... args) {
        def pluginConvention = plugins.values().find { it.metaClass.respondsTo(it, method, args) }
        if (pluginConvention) {
            return true
        }
        return false
    }
}

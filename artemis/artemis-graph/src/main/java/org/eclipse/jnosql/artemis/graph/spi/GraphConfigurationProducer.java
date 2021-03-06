/*
 *  Copyright (c) 2018 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.artemis.graph.spi;

import jakarta.nosql.mapping.ConfigurationReader;
import jakarta.nosql.mapping.ConfigurationSettingsUnit;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.configuration.ConfigurationException;
import jakarta.nosql.mapping.reflection.Reflections;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.artemis.graph.GraphConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.eclipse.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

@ApplicationScoped
class GraphConfigurationProducer {

    @Inject
    private Reflections reflections;

    @Inject
    private Instance<ConfigurationReader> configurationReader;

    @ConfigurationUnit
    @Produces
    public Graph get(InjectionPoint injectionPoint) {
        return getGraphImpl(injectionPoint);
    }

    private Graph getGraphImpl(InjectionPoint injectionPoint) {

        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint);
        return getGraph(annotation);
    }

    Graph getGraph(ConfigurationUnit annotation) {
        ConfigurationSettingsUnit unit = configurationReader.get().read(annotation);
        Class<?> configurationClass = unit.getProvider()
                .orElseThrow(() -> new IllegalStateException("The GraphConfiguration provider is required in the configuration"));

        if (GraphConfiguration.class.isAssignableFrom(configurationClass)) {
            GraphConfiguration factory = (GraphConfiguration) reflections.newInstance(configurationClass);
            return factory.apply(unit.getSettings());
        }

        throw new ConfigurationException(String.format("The class %s does not match with GraphConfiguration",
                configurationClass));
    }
}

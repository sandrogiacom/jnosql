/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.keyvalue.spi;

import jakarta.nosql.mapping.configuration.ConfigurationException;
import jakarta.nosql.mapping.ConfigurationUnit;
import org.eclipse.jnosql.artemis.util.ConfigurationUnitUtils;
import org.eclipse.jnosql.artemis.util.StringUtils;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.BucketManagerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * It creates a {@link BucketManager} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class ManagerConfigurationProducer {

    @Inject
    private KeyValueConfigurationProducer configurationProducer;

    @ConfigurationUnit
    @Produces
    public BucketManager get(InjectionPoint injectionPoint) {
        BucketManagerFactory bucketManagerFactory = configurationProducer.get(injectionPoint);
        ConfigurationUnit annotation = ConfigurationUnitUtils.getConfigurationUnit(injectionPoint);
        String database = annotation.database();
        if(StringUtils.isBlank(database)){
            throw new ConfigurationException("The field database at ConfigurationUnit annotation is required");
        }
        return bucketManagerFactory.getBucketManager(database);
    }
}

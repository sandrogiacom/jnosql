/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.document;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateAsync;
import org.eclipse.jnosql.artemis.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

@ExtendWith(CDIExtension.class)
public class TemplateConfigurationProducer {


    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentTemplate templateA;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name-2", database = "database")
    private DocumentTemplate templateB;



    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name", database = "database")
    private DocumentTemplateAsync templateAsyncA;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name-2", database = "database")
    private DocumentTemplateAsync templateAsyncB;

    @Test
    public void shouldTemplate() {
        Assertions.assertNotNull(templateA);
        DocumentCollectionManager manager = ((AbstractDocumentTemplate) templateA).getManager();
        Assertions.assertNotNull(manager);

    }

    @Test
    public void shouldTemplateB() {
        Assertions.assertNotNull(templateB);
        DocumentCollectionManager manager = ((AbstractDocumentTemplate) templateB).getManager();
        Assertions.assertNotNull(manager);
    }

    @Test
    public void shouldTemplateAsync() {
        Assertions.assertNotNull(templateA);
        DocumentCollectionManagerAsync manager = ((AbstractDocumentTemplateAsync) templateAsyncA).getManager();
        Assertions.assertNotNull(manager);

    }

    @Test
    public void shouldTemplateAsyncB() {
        Assertions.assertNotNull(templateB);
        DocumentCollectionManagerAsync manager = ((AbstractDocumentTemplateAsync) templateAsyncB).getManager();
        Assertions.assertNotNull(manager);
    }

}

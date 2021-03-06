/*
 *
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
 *
 */
package org.eclipse.jnosql.diana;
import jakarta.nosql.Settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A reader that checks if it needs to use {@link SettingsEncryption}
 */
enum SettingsPropertyReader {

    INSTANCE;

    private static final Pattern PATTERN = Pattern.compile("(ENC)\\(..*?\\)");

    private static final Pattern EXTRACT = Pattern.compile("\\((.*?)\\)");


    public Object apply(Object value, Settings settings) {
        if (value instanceof String) {
            String property = value.toString();
            if (isValid(property)) {
                SettingsEncryption encryption = SettingsEncryption.get(settings);
                return encryption.decrypt(extract(property), settings);
            }
            return value;
        }
        return value;
    }

    boolean isValid(String property) {
        Matcher matcher = PATTERN.matcher(property);
        return matcher.matches();
    }

    String extract(String property) {
        Matcher matcher = EXTRACT.matcher(property);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }


}

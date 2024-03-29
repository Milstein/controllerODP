/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.yang2sources.spi;

import java.io.File;
import java.util.Collection;

import org.opendaylight.controller.yang.model.api.SchemaContext;

/**
 * Classes implementing this interface can be submitted to maven-yang-plugin's
 * generate-sources goal.
 */
public interface CodeGenerator {

    /**
     * Generate sources from provided {@link SchemaContext}
     * 
     * @param context
     *            parsed from yang files
     * @param outputBaseDir
     *            expected output directory for generated sources configured by
     *            user
     * @return collection of files that were generated from schema context
     */
    Collection<File> generateSources(SchemaContext context, File outputBaseDir);
}

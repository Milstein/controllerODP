/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.yang;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opendaylight.controller.sal.binding.generator.api.BindingGenerator;
import org.opendaylight.controller.sal.binding.generator.impl.BindingGeneratorImpl;
import org.opendaylight.controller.sal.binding.model.api.GeneratedTransferObject;
import org.opendaylight.controller.sal.binding.model.api.GeneratedType;
import org.opendaylight.controller.sal.binding.model.api.Type;
import org.opendaylight.controller.sal.java.api.generator.GeneratorJavaFile;
import org.opendaylight.controller.yang.model.api.Module;
import org.opendaylight.controller.yang.model.api.SchemaContext;
import org.opendaylight.controller.yang.model.parser.impl.YangModelParserImpl;

public class Demo {
    private static final String ERR_MSG = "2 parameters expected: 1. -f=<path-to-input-folder>, 2. -o=<output-folder>";

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(ERR_MSG);
            return;
        }

        String inputFilesDir = null;
        String outputFilesDir = null;
        if (args[0].startsWith("-f=")) {
            inputFilesDir = args[0].substring(3);
        } else {
            System.err.println("Missing input-folder declaration (-f=)");
        }

        if (args[1].startsWith("-o=")) {
            outputFilesDir = args[1].substring(3);
        } else {
            System.err.println("Missing output-folder declaration (-o=)");
        }

        File resourceDir = new File(inputFilesDir);
        if (!resourceDir.exists()) {
            throw new IllegalArgumentException(
                    "Specified input-folder does not exists: "
                            + resourceDir.getAbsolutePath());
        }

        String[] dirList = resourceDir.list();
        String[] absFiles = new String[dirList.length];

        int i = 0;
        for (String fileName : dirList) {
            File abs = new File(resourceDir, fileName);
            absFiles[i] = abs.getAbsolutePath();
            i++;
        }

        final YangModelParserImpl parser = new YangModelParserImpl();
        final BindingGenerator bindingGenerator = new BindingGeneratorImpl();
        final Set<Module> modulesToBuild = parser.parseYangModels(absFiles);

        final SchemaContext context = parser
                .resolveSchemaContext(modulesToBuild);
        final List<Type> types = bindingGenerator.generateTypes(context);
        final Set<GeneratedType> typesToGenerate = new HashSet<GeneratedType>();
        final Set<GeneratedTransferObject> tosToGenerate = new HashSet<GeneratedTransferObject>();
        for (Type type : types) {
            if (type instanceof GeneratedType) {
                typesToGenerate.add((GeneratedType) type);
            }

            if (type instanceof GeneratedTransferObject) {
                tosToGenerate.add((GeneratedTransferObject) type);
            }
        }

        final GeneratorJavaFile generator = new GeneratorJavaFile(typesToGenerate, tosToGenerate);
        
        generator.generateToFile(outputFilesDir);
        System.out.println("Modules built: " + modulesToBuild.size());
    }
}

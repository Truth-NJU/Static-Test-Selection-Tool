/*
 * Copyright (c) 2015 - Present. The STARTS Team. All Rights Reserved.
 */

package util;

import constants.StartsConstants;

import edu.illinois.yasgl.DirectedGraph;
import maven.RunAndStartJdeps;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;

/**
 * Some utility methods that are needed for RTS.
 */
public class RTSUtil implements StartsConstants {
    private static final Logger LOGGER = Logger.getGlobal();
    // Name of tools.jar in JDK
    private static final String TOOLS_JAR_NAME = "tools.jar";
    // Name of tools.jar on Mac in JDK
    private static final String CLASSES_JAR_NAME = "classes.jar";



    public static Map<String, Set<String>> runJdeps(List<String> args) {
        LOGGER.log(Level.FINE, "JDEPS ARGS:" + args);

        StringWriter output = RunAndStartJdeps.loadAndRunJdeps(args);
        // jdeps can return an empty output when run on .jar files with no .class files
        return output.getBuffer().length() != 0 ? getDepsFromJdepsOutput(output) : new HashMap<String, Set<String>>();
    }

    public static Map<String, Set<String>> getDepsFromJdepsOutput(StringWriter jdepsOutput) {
        Map<String, Set<String>> deps = new HashMap<>();
        List<String> lines = Arrays.asList(jdepsOutput.toString().split(System.lineSeparator()));
        for (String line : lines) {
            String[] parts = line.split("->");
            String left = parts[0].trim();
            if (left.startsWith(CLASSES) || left.startsWith(TEST_CLASSES) || left.endsWith(JAR_EXTENSION)) {
                continue;
            }
            String right = parts[1].trim().split(WHITE_SPACE)[0];
            if (deps.keySet().contains(left)) {
                deps.get(left).add(right);
            } else {
                deps.put(left, new HashSet<>(Arrays.asList(right)));
            }
        }
        return deps;
    }
}

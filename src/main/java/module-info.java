module pdk.util {

    // jdk
    requires java.xml;

    // 3rd party
    requires org.jspecify;
    requires org.jheaps;
    requires it.unimi.dsi.fastutil;
    requires org.apache.commons.io;
    requires com.google.common;

    // statistics
    requires org.apache.commons.numbers.gamma;
    requires org.apache.commons.statistics.descriptive;
    requires org.apache.commons.numbers.combinatorics;
    requires org.apache.commons.numbers.core;
    requires org.apache.commons.statistics.distribution;
    requires org.apache.commons.rng.sampling;

    // GUI
    requires java.desktop;
    requires org.jfree.jfreechart;
    requires org.jfree.svg;
    requires org.apache.commons.rng.simple;
    requires org.apache.commons.rng.api;


    // exports
    exports pdk.util;
    exports pdk.util.data;
    exports pdk.util.data.func;
    exports pdk.util.exception;
    exports pdk.util.graph;
    exports pdk.util.graph.util;
    exports pdk.util.io;
    exports pdk.util.math;
    exports pdk.util.tuple;
}
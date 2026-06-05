module pdk.util {

    // jdk
    requires java.xml;

    // 3rd party
    requires it.unimi.dsi.fastutil;
    requires com.google.common;
    requires org.jspecify;

    // math
    requires org.apache.commons.rng.simple;
    requires org.apache.commons.rng.api;
    requires org.apache.commons.rng.sampling;
    requires org.apache.commons.numbers.core;
    requires org.apache.commons.numbers.arrays;
    requires org.apache.commons.numbers.combinatorics;
    requires org.apache.commons.numbers.gamma;

    // statistics
    requires org.apache.commons.statistics.distribution;
    requires org.apache.commons.statistics.descriptive;
    requires hipparchus.core;
    requires hipparchus.stat;
    requires hipparchus.optim;

    // GUI
    requires java.desktop;
    requires pdk.chart;

    // exports
    exports pdk.util;
    exports pdk.util.bayesian;
    exports pdk.util.collect;
    exports pdk.util.data;
    exports pdk.util.data.fitting;
    exports pdk.util.data.func;
    exports pdk.util.exception;
    exports pdk.util.graph;
    exports pdk.util.graph.util;
    exports pdk.util.io;
    exports pdk.util.math;
    exports pdk.util.math.distribution;
    exports pdk.util.math.test;
    exports pdk.util.tuple;
}
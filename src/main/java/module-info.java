module pdk.util {

    // jdk
    requires java.xml;

    // 3rd party
    requires org.jspecify;
    requires commons.math3;
    requires org.jheaps;
    requires it.unimi.dsi.fastutil;

    // exports
    exports pdk.util;
    exports pdk.util.data;
    exports pdk.util.data.func;
    exports pdk.util.exception;
    exports pdk.util.graph;
    exports pdk.util.graph.util;
    exports pdk.util.io;
    exports pdk.util.tuple;
    exports pdk.util.math;
}
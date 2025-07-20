package pdk.util.graph.util;

import pdk.util.graph.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link NodeVisitor} implementation that just collect all nodes
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 30 Nov 2024, 18:35
 */
public class CollectListVisitor implements NodeVisitor {

    private final List<Integer> list;

    public CollectListVisitor() {
        list = new ArrayList<>();
    }

    public List<Integer> getList() {
        return list;
    }

    @Override
    public void visit(int node) {
        list.add(node);
    }
}

package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class NilNode extends Node {

    public static final NilNode INSTANCE = new NilNode();

    private NilNode() { }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitNilNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NILNODE;
    }
}

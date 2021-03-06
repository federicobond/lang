package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.List;

public class FalseNode extends Node {

    public static final FalseNode INSTANCE = new FalseNode();

    private FalseNode() { }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitFalseNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.FALSENODE;
    }
}

package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.List;

public class ContinueNode extends Node {

    public static final ContinueNode INSTANCE = new ContinueNode();

    private ContinueNode() { }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitContinueNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.CONTINUENODE;
    }
}

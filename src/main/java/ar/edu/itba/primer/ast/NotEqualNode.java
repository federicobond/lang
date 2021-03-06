package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

public class NotEqualNode extends BinaryOperationNode{

    public NotEqualNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitNotEqualNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NOTEQUALNODE;
    }
}

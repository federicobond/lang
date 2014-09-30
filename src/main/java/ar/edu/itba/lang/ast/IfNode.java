package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class IfNode extends Node {
    private final Node condition;
    private final Node thenBody;

    public IfNode(Node condition, Node thenBody) {
        this.condition = condition;
        this.thenBody = thenBody;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getThenBody() {
        return thenBody;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitIfNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(condition, thenBody);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.IFNODE;
    }
}

package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class IfNode extends Node {
    private final Node condition;
    private final Node thenBody;
    private final Node elseBody;

    public IfNode(Node condition, Node thenBody, Node elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getThenBody() {
        return thenBody;
    }

    public Node getElseBody() {
        return elseBody;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitIfNode(this);
    }

    @Override
    public List<Node> childNodes() {
        if (elseBody == null) {
            return Node.createList(condition, thenBody);
        }
        return Node.createList(condition, thenBody, elseBody);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.IFNODE;
    }
}
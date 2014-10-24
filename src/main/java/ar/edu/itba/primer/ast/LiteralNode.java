package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.List;

public class LiteralNode extends Node {

    private final Integer value;

    public LiteralNode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitLiteralNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.LITERALNODE;
    }

    @Override
    public String getNodeName() {
        return super.getNodeName() + "{value=" + value +  "}";
    }
}
package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;
import com.google.common.base.MoreObjects;

import java.util.List;

public class AssignmentNode extends Node {

    private final String name;
    private final Node value;

    public AssignmentNode(String name, Node value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitAssignmentNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return createList(value);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ASSIGNMENTNODE;
    }

    @Override
    public String getNodeName() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }

    public String getName() {
        return name;
    }

    public Node getValue() {
        return value;
    }
}

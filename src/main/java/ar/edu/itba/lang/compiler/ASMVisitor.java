package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMVisitor implements NodeVisitor<Void>, Opcodes {

    private final ClassWriter cw = new ClassWriter(0);
    private MethodVisitor mv;

    public ASMVisitor(Node root) {
        cw.visit(49,
                ACC_PUBLIC + ACC_SUPER,
                "Main",
                null,
                "java/lang/Object",
                null);

        cw.visitSource("Main.java", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                    "main",
                    "([Ljava/lang/String;)V",
                    null,
                    null);
            root.accept(this);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
    }

    public byte[] getByteArray() {
        return cw.toByteArray();
    }

    @Override
    public Void visitAddNode(AddNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IADD);

        return null;
    }

    @Override
    public Void visitAndNode(AndNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IAND);

        return null;
    }

    @Override
    public Void visitArgsNode(ArgsNode node) {
        for (Node child : node.childNodes()) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Void visitBlockNode(BlockNode node) {
        for (Node child : node.childNodes()) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Void visitCallNode(CallNode node) {
        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");
        node.getArgs().accept(this);
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                false);

        return null;
    }

    @Override
    public Void visitDivideNode(DivideNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IDIV);

        return null;
    }

    @Override
    public Void visitFalseNode(FalseNode node) {
        mv.visitInsn(ICONST_0);

        return null;
    }

    @Override
    public Void visitIfNode(IfNode node) {
        Label l1 = new Label();

        node.getCondition().accept(this);
        mv.visitJumpInsn(IFEQ, l1);
        node.getThenBody().accept(this);
        mv.visitLabel(l1);

        return null;
    }

    @Override
    public Void visitIfElseNode(IfElseNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getCondition().accept(this);
        mv.visitJumpInsn(IFEQ, l1);
        node.getThenBody().accept(this);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        node.getElseBody().accept(this);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitLiteralNode(LiteralNode node) {
        Object value = node.getValue();
        mv.visitLdcInsn(value);

        return null;
    }

    @Override
    public Void visitMultiplyNode(MultiplyNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IMUL);

        return null;
    }

    @Override
    public Void visitNegateNode(NegateNode node) {
        node.getNode().accept(this);
        mv.visitInsn(INEG);

        return null;
    }

    @Override
    public Void visitOrNode(OrNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IOR);

        return null;
    }

    @Override
    public Void visitSubstractNode(SubstractNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(ISUB);

        return null;
    }

    @Override
    public Void visitTrueNode(TrueNode node) {
        mv.visitInsn(ICONST_1);

        return null;
    }

    @Override
    public Void visitWhileNode(WhileNode node) {
        Label conditionLabel = new Label();
        Label endLabel = new Label();

        mv.visitLabel(conditionLabel);
        node.getConditionNode().accept(this);
        mv.visitJumpInsn(IFEQ, endLabel);
        node.getBodyNode().accept(this);
        mv.visitJumpInsn(GOTO, conditionLabel);
        mv.visitLabel(endLabel);

        return null;
    }
}

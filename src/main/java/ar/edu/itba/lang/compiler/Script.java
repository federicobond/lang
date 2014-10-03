package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.Lexer;
import ar.edu.itba.lang.Parser;
import ar.edu.itba.lang.ast.Node;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Scanner;
import java_cup.runtime.ScannerBuffer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

public class Script {

    public static boolean enableOptimizations = false;

    private final byte[] code;

    private final String fileName;

    private Script(byte[] code, String fileName) {
        this.code = code;
        this.fileName = fileName;
    }

    public static Script fromFile(File file) throws IOException {
        byte[] code = Files.readAllBytes(file.toPath());
        return new Script(code, file.getName());
    }

    public static Script fromString(String code) throws IOException {
        return new Script(code.getBytes("UTF-8"), "unnamed");
    }

    public static class ScriptException extends Exception {
        public ScriptException(String message) {
            super(message);
        }
    }

    public static class ByteClassLoader extends URLClassLoader {
        private final byte[] classBytes;

        public ByteClassLoader(URL[] urls, ClassLoader parent, byte[] bytes) {
            super(urls, parent);
            this.classBytes = bytes;
        }

        @Override
        protected Class<?> findClass(final String name) throws ClassNotFoundException {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
    }

    public Node parse() throws ScriptException {
        Node root = getRootNode(getParser());

        if (enableOptimizations) {
            root = root.accept(new ConstantOptimizerVisitor())
                    .accept(new FlattenBlocksOptimizerVisitor());
        }

        return root;
    }

    public String trace() throws ScriptException {
        Node root = parse();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        new ASMVisitor(root, new TraceClassVisitor(null, new Textifier(), pw));

        return sw.toString();
    }

    public void exec(String[] argv) throws ScriptException {
        Class<?> klass = compile();

        try {
            Method m = klass.getMethod("main", String[].class);
            m.invoke(null, (Object)argv);
        } catch (NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException ignored) { }
    }

    private Class<?> compile() throws ScriptException {
        Node root = parse();

        byte[] classBytes = new ASMVisitor(root, new ClassWriter(0)).getByteArray();
        URLClassLoader cl = new ByteClassLoader(new URL[0], ClassLoader.getSystemClassLoader(), classBytes);

        Class<?> klass = null;
        try {
            klass = cl.loadClass("Main");
        } catch (ClassNotFoundException e) {
            /* ignore */
        }

        return klass;
    }

    private Parser getParser() {
        ComplexSymbolFactory csf = new ComplexSymbolFactory();
        Lexer lexer = null;
        try {
            Reader reader = new InputStreamReader(
                    new ByteArrayInputStream(code), "UTF-8");
            lexer = new Lexer(reader, csf);
        } catch (UnsupportedEncodingException ignore) { }

        if (fileName != null) {
            lexer.setFilename(fileName);
        }

        Scanner scanner = new ScannerBuffer(lexer);
        return new Parser(scanner, csf);
    }

    private Node getRootNode(Parser parser) throws ScriptException {
        Node root;
        try {
            root = (Node)parser.parse().value;
        } catch (Throwable e) {
            throw new ScriptException(e.getMessage());
        }
        return root;
    }
}
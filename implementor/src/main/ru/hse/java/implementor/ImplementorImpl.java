package ru.hse.java.implementor;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class ImplementorImpl implements Implementor {

    private final String outputDirectory;

    public ImplementorImpl(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        try {
            URL url = new URL("file://" + directoryPath);
            ClassLoader classLoader = new URLClassLoader(new URL[]{url});
            Class<?> clazz = classLoader.loadClass(className);
            return implement(clazz, true);
        } catch (ClassNotFoundException | MalformedURLException exception) {
            throw new ImplementorException("Not found " + className + " in " + directoryPath, exception);
        }
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        try {
            Class<?> clazz = Class.forName(className);
            return implement(clazz, false);
        } catch (ClassNotFoundException exception) {
            throw new ImplementorException("Not found " + className + " in standard library", exception);
        }
    }

    private String implement(Class<?> clazz, boolean isFromDirectory) throws ImplementorException {
        Path pathToDirectory;
        if (isFromDirectory) {
            pathToDirectory = Path.of(outputDirectory, clazz.getPackageName().split("\\."));
        } else {
            pathToDirectory = Path.of(outputDirectory);
        }
        Path pathToFile = pathToDirectory.resolve(clazz.getSimpleName() + "Impl.java");

        try {
            Files.createDirectories(pathToDirectory);
            Files.createFile(pathToFile);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException exception) {
            throw new ImplementorException("Can not create the java file for " + clazz.getCanonicalName(), exception);
        }

        try (Writer writer = new FileWriter(pathToFile.toFile())) {
            writer.write(implementPackage(clazz, isFromDirectory));
            writer.write(implementDefinition(clazz));
            writer.write(" {\n");
            writer.write(implementMethods(clazz));
            writer.write("}");
        } catch (IOException exception) {
            throw new ImplementorException("Can not write to the java file for " + clazz.getCanonicalName(), exception);
        }

        return (isFromDirectory ? clazz.getCanonicalName() : clazz.getSimpleName()) + "Impl";
    }

    private String implementPackage(Class<?> clazz, boolean isFromDirectory) {
        if (isFromDirectory && !clazz.getPackageName().isEmpty()) {
            return "package " + clazz.getPackageName() + ";\n\n";
        }
        return "";
    }

    private String implementDefinition(Class<?> clazz) {
        String implementationName = clazz.getSimpleName() + "Impl";
        String type = Modifier.isInterface(clazz.getModifiers()) ? " implements " : " extends ";
        return "public class " + implementationName + type + clazz.getCanonicalName();
    }

    private String implementMethods(Class<?> clazz) {
        StringBuilder result = new StringBuilder();
        Set<MethodWrapper> implemented = new HashSet<>();
        for (Class<?> curClass = clazz; curClass != null; curClass = curClass.getSuperclass()) {
            for (var method : curClass.getDeclaredMethods()) {
                var methodWrapper = new MethodWrapper(method);
                String impl = implementMethod(method);
                if (Modifier.isAbstract(method.getModifiers()) && !implemented.contains(methodWrapper)) {
                    result.append(impl);
                    implemented.add(methodWrapper);
                } else if (!method.isBridge()) {
                    implemented.add(methodWrapper);
                }
            }
        }
        for (var method : clazz.getMethods()) {
            var methodWrapper = new MethodWrapper(method);
            String impl = implementMethod(method);
            if (Modifier.isAbstract(method.getModifiers()) && !implemented.contains(methodWrapper)) {
                result.append(impl);
                implemented.add(methodWrapper);
            }
        }
        return result.toString();
    }

    private String implementMethod(Method method) {
        StringBuilder definition = new StringBuilder();

        int modifiers = method.getModifiers();
        if (Modifier.isPublic(modifiers)) {
            definition.append("\tpublic ");
        } else if (Modifier.isProtected(modifiers)) {
            definition.append("\tprotected ");
        }

        definition.append(method.getReturnType().getTypeName()).append(" ");
        definition.append(method.getName()).append("(");

        StringJoiner parameters = new StringJoiner(", ");
        for (var parameter : method.getParameters()) {
            parameters.add(parameter.getType().getTypeName() + " " + parameter.getName());
        }
        definition.append(parameters).append(") ");

        Class<?>[] exceptionsArray = method.getExceptionTypes();
        if (exceptionsArray.length != 0) {
            definition.append("throws ");
            StringJoiner exceptions = new StringJoiner(", ");
            for (var exception : exceptionsArray) {
                exceptions.add(exception.getName());
            }
            definition.append(exceptions).append(" ");
        }

        definition.append("{\n\t\treturn");

        Class<?> returnedType = method.getReturnType();
        if (!returnedType.isPrimitive()) {
            definition.append(" null");
        } else if (returnedType.toString().equals("boolean")) {
            definition.append(" false");
        } else if (!returnedType.toString().equals("void")) {
            definition.append(" 0");
        }
        definition.append(";\n\t}\n\n");


        return definition.toString();
    }

    private static class MethodWrapper {
        private final String keyName;

        public MethodWrapper(Method method) {
            keyName = method.getName() + Arrays.toString(method.getParameters());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MethodWrapper)) {
                return false;
            }
            MethodWrapper that = (MethodWrapper) o;
            return Objects.equals(keyName, that.keyName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(keyName);
        }
    }

}

package fr.labri.patterndetector.lang;

import fr.labri.patterndetector.runtime.expressions.*;
import fr.labri.patterndetector.runtime.expressions.builtins.Register;
import org.atteo.classindex.ClassFilter;
import org.atteo.classindex.ClassIndex;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by morandat on 05/12/2016.
 */
public class Functions {
    protected Map<String, Entry<IPredicate>> _predicates = new HashMap<>();
    protected Map<String, Entry<IExpression>> _expressions = new HashMap<>();

    private Functions() {}

    public static Functions get() {
        Functions builder = new Functions();
        builder.init();
        return builder;
    }

    public IPredicate getPredicate(String name, IField... fields) throws InvalidUsage, UnkownFunction {
        return instantiate(_predicates.get(name), fields);
    }

    public IExpression getExpression(String name, IField... fields) throws InvalidUsage, UnkownFunction {
        return instantiate(_expressions.get(name), fields);
    }

    protected <K> K instantiate(Entry<K> entry, IField... fields) throws UnkownFunction, InvalidUsage {
        if (entry == null)
            throw new UnkownFunction();
        return entry._factory.instantiate(fields);
    }

    public void init() {
        initBuiltins(IPredicate.class, _predicates);
        initBuiltins(IExpression.class, _expressions);
    }

    protected <K> void initBuiltins(Class<K> clazz, Map<String, Entry<K>> map) {
        ClassFilter.only().topLevelOrStaticNested().satisfying(x -> clazz.isAssignableFrom(x))
                .from(ClassIndex.getAnnotated(Register.class))
                .forEach(builtin -> {
                    try {
                        register(map, (Class<K>)builtin);
                    } catch (InvalidUsage invalidBuiltIn) {
                        // TODO log
                        System.err.println("Unable to register builtin " + builtin.getCanonicalName());
                    }
                });
    }

    public void registerExpression(String name, Register.Factory<IExpression> factory, String documentation) {
        _expressions.put(name, new Entry(name, factory, documentation));
    }

    public void registerPredicate(String name, Register.Factory<IPredicate> factory, String documentation) {
        _predicates.put(name, new Entry(name, factory, documentation));
    }

    protected <K> void register(Map<String, Entry<K>> map, Class<? extends K> clazz) throws InvalidUsage {
        Register annotation = clazz.getAnnotation(Register.class);
        Objects.nonNull(annotation);
        Register.Factory factory;
        String name = annotation.name();
        if (name.equals(""))
            name = clazz.getSimpleName().toLowerCase();

        Class<? extends Register.Factory> factory_class = annotation.factory();
        map.put(name,
                new Entry(name, factory_class.equals(Register.Factory.class)
                        ? makeFactory(clazz, annotation)
                        : instantiateFactory(factory_class),
                        annotation.documentation()));
    }

    static private <K> Register.Factory<K> instantiateFactory(Class<? extends Register.Factory> factoryClass) throws InvalidUsage {
        try {
            return factoryClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InvalidUsage();
        }
    }

    static private <K> Register.Factory<K> makeFactory(Class<? extends K> clazz, Register annotation) throws InvalidUsage {
        int requiredParameters = annotation.parameters();
        if (annotation.parameters() == Integer.MIN_VALUE) {
            if (IPredicate.Unary.class.isAssignableFrom(clazz))
                requiredParameters = 1;
            else if (BinaryOperation.class.isAssignableFrom(clazz))
                requiredParameters = 2;
            else
                throw new InvalidUsage();
        }
        final int finalRequiredParameters = requiredParameters;
        return new Register.Factory<K>() {
            @Override
            public String toString() {
                return clazz.getCanonicalName() + " " + finalRequiredParameters;
            }

            @Override
            public K instantiate(IField[] fields) throws InvalidUsage {
                if (finalRequiredParameters < 0 && fields.length < -finalRequiredParameters)
                    throw new InvalidUsage();
                if (finalRequiredParameters >= 0 && fields.length != finalRequiredParameters)
                    throw new InvalidUsage();
                try {
                    return clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new InvalidUsage();
                }
            }
        };
    }

    public void inspect(PrintStream err) {
        err.println("** Predicates **");
        err.println(_predicates.values());
        err.println("** Expression **");
        err.println(_expressions.values());
    }

    private static class Entry<K> {
        final String _name;
        final Register.Factory<K> _factory;
        final String _documentation;

        private Entry(String name, Register.Factory<K> factory, String documentation) {
            _name = name;
            _factory = factory;
            _documentation = documentation;
        }

        public String toString() {
            return String.format("%s: %s\n\t%s\n", _name, _factory.toString(), _documentation);
        }
    }

    public static class UnkownFunction extends Exception {
    }

    public static class InvalidUsage extends Exception {
    }
}

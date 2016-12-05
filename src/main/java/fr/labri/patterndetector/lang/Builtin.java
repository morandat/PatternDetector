package fr.labri.patterndetector.lang;

import fr.labri.patterndetector.runtime.expressions.Expression;
import fr.labri.patterndetector.runtime.expressions.IField;
import fr.labri.patterndetector.runtime.expressions.IPredicate;
import fr.labri.patterndetector.runtime.expressions.Register;
import fr.labri.patterndetector.runtime.expressions.predicates.Predicate1;
import fr.labri.patterndetector.runtime.expressions.predicates.Predicate2;
import fr.labri.patterndetector.runtime.expressions.predicates.Factory;
import org.atteo.classindex.ClassFilter;
import org.atteo.classindex.ClassIndex;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by morandat on 05/12/2016.
 */
public class Builtin {
    protected Map<String, Factory<IPredicate>> _registeredPredicates = new HashMap<>();
    protected Map<String, Factory<Expression>> _registeredExpressions = new HashMap<>();

    public static Builtin get() {
        Builtin builder = new Builtin();
        builder.initBuiltins();
        return builder;
    }

    public IPredicate getPredicate(String name, IField... fields) throws InvalidBuiltin, UnknownBuiltin {
        return buildWith(_registeredPredicates, name, fields);
    }

    public Expression getExpression(String name, IField... fields) throws InvalidBuiltin, UnknownBuiltin {
        return buildWith(_registeredExpressions, name, fields);
    }

    protected <K> K buildWith(Map<String, Factory<K>> functionTable, String name, IField... fields) throws UnknownBuiltin, InvalidBuiltin {
        Factory<K> factory = functionTable.get(name);
        if (factory == null)
            throw new UnknownBuiltin();
        return factory.instanciate(fields);
    }

    public void initBuiltins() {
        initBuiltins(IPredicate.class, _registeredPredicates);
        initBuiltins(Expression.class, _registeredExpressions);
    }

    protected <K> void initBuiltins(Class<K> clazz, Map<String, Factory<K>> functionTable) {
        ClassFilter.only().topLevelOrStaticNested().satisfying(x -> clazz.isAssignableFrom(x))
                .from(ClassIndex.getAnnotated(Register.class))
                .forEach(builtin -> {
                    try {
                        register(functionTable, (Class<K>)builtin);
                    } catch (InvalidBuiltin invalidBuiltIn) {
                        // TODO log
                        System.err.println("Unable to register builtin " + builtin.getCanonicalName());
                    }
                });
    }

    public void registerExpression(String name, Factory<Expression> factory) {
        _registeredExpressions.put(name, factory);
    }

    public void registerPredicate(String name, Factory<IPredicate> factory) {
        _registeredPredicates.put(name, factory);
    }

    protected <K> void register(Map<String, Factory<K>> functionTable, Class<? extends K> clazz) throws InvalidBuiltin {
        Register annotation = clazz.getAnnotation(Register.class);
        Objects.nonNull(annotation);
        Factory factory;
        String name = annotation.name();
        if (name.equals(""))
            name = clazz.getSimpleName().toLowerCase();

        Class<? extends Factory> factory_class = annotation.factory();
        if (factory_class.equals(Factory.class)) {
            int requiredParameters = annotation.parameters();
            if (annotation.parameters() == Integer.MIN_VALUE) {
                if (Predicate1.class.isAssignableFrom(clazz))
                    requiredParameters = 1;
                else if (Predicate2.class.isAssignableFrom(clazz)
                        || Expression.class.isAssignableFrom(clazz))
                    requiredParameters = 2;
                else
                    throw new InvalidBuiltin();
            }
            final int finalRequiredParameters = requiredParameters;
            factory = new Factory<K>() {
                @Override
                public String toString() {
                    return clazz.getCanonicalName();
                }

                @Override
                public K instanciate(IField[] fields) throws InvalidBuiltin {
                    if (finalRequiredParameters < 0 && fields.length < -finalRequiredParameters)
                        throw new InvalidBuiltin();
                    if (finalRequiredParameters >= 0 && fields.length != finalRequiredParameters)
                        throw new InvalidBuiltin();
                    try {
                        return clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new InvalidBuiltin();
                    }
                }
            };
        } else {
            try {
                factory = factory_class.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new InvalidBuiltin();
            }
        }
        functionTable.put(name, factory);
    }

    public void inspect(PrintStream err) {
        err.println(_registeredPredicates);
        err.println(_registeredExpressions);
    }

    public static class UnknownBuiltin extends Exception {
    }

    public static class InvalidBuiltin extends Exception {
    }
}

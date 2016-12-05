package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.predicates.builtins.Equal;
import org.atteo.classindex.ClassFilter;
import org.atteo.classindex.ClassIndex;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by morandat on 05/12/2016.
 */
public class PredicateBuilder {
    static Map<String, PredicateFactory> _registeredPredicates = new HashMap<>();

    public static IPredicate buildWith(String name, IField... fields) throws UnknownPredicate, IPredicate.InvalidPredicate {
        PredicateFactory factory = _registeredPredicates.get(name);
        if (factory == null)
            throw new UnknownPredicate();
        IPredicate predicate = factory.instanciate(fields);
        predicate.validate();
        return predicate;
    }

    public static void initBuiltins() {
        Package p = Package.getPackage("fr.labri.patterndetector.runtime.predicates.buitins");
        Objects.nonNull(p);
        ClassFilter.only().topLevelOrStaticNested().satisfying(x -> IPredicate.class.isAssignableFrom(x))
                .from(ClassIndex.getAnnotated(Predicate.class))
                .forEach(builtin -> {
                    try {
                        registerPredicate((Class<? extends IPredicate>) builtin);
                    } catch (IPredicate.InvalidPredicate invalidPredicate) {
                        // TODO log
                        System.err.println("Unable to register builtin " + builtin.getCanonicalName());
                    }
                });
    }

    public static void registerPredicate(String name, PredicateFactory factory) {
        _registeredPredicates.put(name, factory);
    }

    public static void registerPredicate(Class<? extends IPredicate> clazz) throws IPredicate.InvalidPredicate {
        Predicate annotation = clazz.getAnnotation(Predicate.class);
        Objects.nonNull(annotation);
        PredicateFactory factory;
        String name = annotation.name();
        if (name.equals(""))
            name = clazz.getSimpleName().toLowerCase();

        Class<? extends PredicateFactory> factory_class = annotation.factory();
        if (factory_class.equals(PredicateFactory.class)) {
            int requiredParameters = annotation.parameters();
            if (annotation.parameters() == Integer.MIN_VALUE) {
                if (Predicate1.class.isAssignableFrom(clazz))
                    requiredParameters = 1;
                else if (Predicate2.class.isAssignableFrom(clazz))
                    requiredParameters = 2;
                else
                    throw new IPredicate.InvalidPredicate();
            }
            final int finalRequiredParameters = requiredParameters;
            factory = new PredicateFactory() {
                @Override
                public IPredicate instanciate(IField[] fields) throws IPredicate.InvalidPredicate {
                    if (finalRequiredParameters < 0 && fields.length < -finalRequiredParameters)
                        throw new IPredicate.InvalidPredicate();
                    if (finalRequiredParameters >= 0 && fields.length != finalRequiredParameters)
                        throw new IPredicate.InvalidPredicate();
                    IPredicate predicate = null;
                    try {
                        predicate = clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new IPredicate.InvalidPredicate();
                    }
                    predicate.validate();
                    return predicate;
                }
            };
        } else {
            try {
                factory = factory_class.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IPredicate.InvalidPredicate();
            }
        }
        registerPredicate(name, factory);
    }

    static class UnknownPredicate extends Exception {
    }
}

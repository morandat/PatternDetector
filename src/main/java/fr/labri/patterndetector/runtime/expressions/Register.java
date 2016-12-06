package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.lang.Builtin;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by morandat on 05/12/2016.
 */
@IndexAnnotated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Register {
    String name() default "";

    int parameters() default Integer.MIN_VALUE;

    Class<? extends Factory> factory() default Factory.class;

    String documentation() default NO_DOCUMENTATION;

    String NO_DOCUMENTATION = "No documentation";

    interface Factory<K> {
        K instantiate(IField[] fields) throws Builtin.InvalidUsage;
    }
}

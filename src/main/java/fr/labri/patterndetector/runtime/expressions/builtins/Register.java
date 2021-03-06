package fr.labri.patterndetector.runtime.expressions.builtins;

import fr.labri.patterndetector.lang.Functions;
import fr.labri.patterndetector.runtime.expressions.IField;
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
        K instantiate(IField[] fields) throws Functions.InvalidUsage;
    }
}

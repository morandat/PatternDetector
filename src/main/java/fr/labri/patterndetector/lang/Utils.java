package fr.labri.patterndetector.lang;

import xtc.util.Pair;

public class Utils {

    @SafeVarargs
    public static <E> Pair<E> makeList(E... objects) {
        if (objects.length == 0) return Pair.empty();
        Pair<E> pair = new Pair<>(objects[0]);
        Pair<E> current = pair;
        for (int i = 1; i < objects.length; i++) {
            Pair<E> previous = current;
            current = new Pair<>(objects[i]);
            previous.setTail(current);
        }
        return pair;
    }
}

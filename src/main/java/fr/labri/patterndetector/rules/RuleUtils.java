package fr.labri.patterndetector.rules;

/**
 * Created by william.braik on 11/08/2015.
 * <p>
 * Rule utility methods.
 */
public final class RuleUtils {

    private RuleUtils() {
    }

    /**
     * Get the rightmost, bottommost leaf of the rule tree.
     *
     * @param rule The rule to search.
     * @return
     */
    public static IAtom getRightmostAtom(IRule rule) {
        if (rule instanceof IAtom) {
            return (IAtom) rule;
        } else if (rule instanceof AbstractUnaryRule) {
            return getRightmostAtom(((AbstractUnaryRule) rule).getChildRule());
        } else { // binary rule
            return getRightmostAtom(((AbstractBinaryRule) rule).getRightChildRule());
        }
    }

    /**
     * Get the leftmost, bottommost leaf of the rule tree.
     *
     * @param rule The rule to search.
     * @return
     */
    public static IAtom getLeftmostAtom(IRule rule) {
        if (rule instanceof IAtom) {
            return (IAtom) rule;
        } else if (rule instanceof AbstractUnaryRule) {
            return getRightmostAtom(((AbstractUnaryRule) rule).getChildRule());
        } else { // binary rule
            return getRightmostAtom(((AbstractBinaryRule) rule).getLeftChildRule());
        }
    }
}

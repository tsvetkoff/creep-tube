package tsvetkoff.domain;

import lombok.Getter;

/**
 * @author SweetSupremum
 */
@Getter
public class Pair<F, S> {
    private F first;
    private S second;

    private Pair() {
    }

    public static <F, S> Pair<F, S> of(F first, S second) {
        Pair<F, S> pair = new Pair<>();
        pair.first = first;
        pair.second = second;
        return pair;
    }
}

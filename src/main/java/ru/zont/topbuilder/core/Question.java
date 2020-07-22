package ru.zont.topbuilder.core;

import javafx.util.Pair;

public interface Question<T> {
    /**
     * Get the question, which one is better
     *
     * @return Pair of objects to compare
     */
    Pair<T, T> getIt();

    /**
     * Answer to this question
     *
     * @param ans <p>negative = the left (first) wins;
     *            <p>zero = not stated;
     *            <p>positive = right (second) wins
     *            <p><b>WIP, so it's not recommended to put </b><tt>abs(ans) > 1</tt><b> here</b>
     */
    void answerIt(int ans);
}

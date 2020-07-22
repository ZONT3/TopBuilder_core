package ru.zont.topbuilder.core;

import java.util.ArrayList;

/**
 * @param <T> Type of Top-list items. MUST be correctly implemented method <tt>equals</tt>.
 */
public abstract class Top<T> {

    /**
     * Get the sorted list. It may be not completely sorted, if called before <tt>getQ</tt> returned a null.
     *
     * @return Sorted list, first one is the greatest one
     */
    public abstract ArrayList<T> getList();


    /**
     * Get new comparision question.
     *
     * @return <tt>Question</tt> interface instance. Should be null when Top-list is finished
     */
    public abstract Question<T> getQ();
}

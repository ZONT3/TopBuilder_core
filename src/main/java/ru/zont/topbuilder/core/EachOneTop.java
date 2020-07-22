package ru.zont.topbuilder.core;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class EachOneTop<T> extends Top<T> {
    private ArrayList<Entry<T>> list;
    private boolean enableSamePlace;
    private int qi;
    private int qj;

    @SafeVarargs
    public EachOneTop(T... items) {
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        init(list);
    }

    public EachOneTop(Collection<T> list) {
        init(list);
    }

    private void init(Collection<T> list) {
        enableSamePlace = false;
        qi = 0; qj = 0;
        this.list = new ArrayList<>();
        for (T t: list)
            this.list.add(new Entry<>(t));
    }


    public EachOneTop enableSamePlace(boolean enableSamePlace) {
        this.enableSamePlace = enableSamePlace;
        return this;
    }

    @Override
    public ArrayList<T> getList() {
        ArrayList<Entry<T>> list = new ArrayList<>(this.list);
        list.sort((o1, o2) -> getWinCount(o2) - getWinCount(o1));

        ArrayList<T> res = new ArrayList<>();
        for (Entry<T> e: list)
            res.add(e.get());
        return res;
    }

    @Override
    public Question<T> getQ() {
        Question<T> res = iterBase();
        if (res != null) return res;
        else {
            qi = 0; qj = 0;
            return iterBase();
        }
    }

    private Question<T> iterBase() {
        for (; qi < list.size(); qi++) {
            Entry<T> e = list.get(qi);
            if (e.relatives.size() < list.size()-1) {
                for (; qj < list.size(); qj++) {
                    Entry<T> e1 = list.get(qj);
                    if (e1.equals(e)) continue;
                    Pair<Entry<T>, Integer> rel = getRelative(e.relatives, e1);

                    if (rel == null)
                        return new QuestionHandler(e.get(), e1.get());
                    if (!enableSamePlace && rel.getValue().equals(0))
                        return new QuestionHandler(e.get(), e1.get());
                }
            }
            qj = 0;
        }

        return iterSameWins();
    }

    private Question<T> iterSameWins() {
        ArrayList<Pair<Entry<T>, Integer>> wins = new ArrayList<>();
        for (Entry<T> e: list) {
            int wc = getWinCount(e);
            for (Pair<Entry<T>, Integer> w: wins)
                if (!w.getKey().equals(e) && w.getValue().equals(wc))
                    return new QuestionHandler(e.get(), w.getKey().get(), true);
            wins.add(new Pair<>(e, wc));
        }
        return null;
    }

    private int getWinCount(Entry<T> e) {
        int i = 0;
        for (Pair<Entry<T>, Integer> r: e.relatives)
            if (r.getValue() < 0) i++;
        return i + e.getE();
    }

    private Pair<Entry<T>, Integer> getRelative(ArrayList<Pair<Entry<T>, Integer>> relatives, Entry<T> e1) {
        for (Pair<Entry<T>, Integer> r: relatives)
            if (r.getKey().equals(e1)) return r;
        return null;
    }

    private void handleAnswer(T left, T right, int ans, boolean ince) {
        Pair<Entry<T>, Entry<T>> pair = findPair(left, right);
        pair.getKey().relatives.add(new Pair<>(pair.getValue(), Integer.compare(ans, 0)));
        pair.getValue().relatives.add(new Pair<>(pair.getKey(), -Integer.compare(ans, 0)));
        if (ince) {
            if (ans < 0) pair.getKey().ince();
            if (ans > 0) pair.getValue().ince();
        }
    }

    private Entry<T> find(T item) {
        for (Entry<T> e: list)
            if (item.equals(e.get()))
                return e;
        throw new RuntimeException("Item not found");
    }

    private Pair<Entry<T>, Entry<T>> findPair(T i1, T i2) {
        Entry<T> r1 = null, r2 = null;
        for (Entry<T> e: list)
            if (i1.equals(e.get())) r1 = e;
            else if (i2.equals(e.get())) r2 = e;
        return new Pair<>(r1, r2);
    }

    private class QuestionHandler implements Question<T> {
        private final T lhs, rhs;
        boolean ince;

        private QuestionHandler(T lhs, T rhs) {
            this(lhs, rhs, false);
        }

        private QuestionHandler(T lhs, T rhs, boolean ince) {
            if (lhs == null || rhs == null)
                throw new NullPointerException();

            this.lhs = lhs;
            this.rhs = rhs;
            this.ince = ince;
        }

        @Override
        public Pair<T, T> getIt() {
            return new Pair<>(lhs, rhs);
        }

        @Override
        public void answerIt(int ans) {
            EachOneTop.this.handleAnswer(lhs, rhs, ans, ince);
        }
    }

    private static class Entry<E> {
        private E val;
        private ArrayList<Pair<Entry<E>, Integer>> relatives;
        private int extra = 0;

        private Entry(E val) {
            this.val = val;
            relatives = new ArrayList<>();
        }

        private E get() { return val; }

        private void ince() { extra++; }

        private int getE() { return extra; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?> entry = (Entry<?>) o;
            return Objects.equals(val, entry.val);
        }

        @Override
        public int hashCode() {
            return Objects.hash(val);
        }

        @Override
        public String toString() {
            return String.format("%s E:%d;R:%d", val.toString(), extra, relatives.size());
        }
    }
}

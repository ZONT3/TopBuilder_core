package ru.zont.topbuilder.core;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RoyaleTop<T> extends Top<T> {
    private ArrayList<T> list;
    private ArrayList<T> toRm;
    private int qi;

    @SafeVarargs
    public RoyaleTop(T... items) {
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        init(list);
    }

    public RoyaleTop(Collection<T> list) {
        init(list);
    }

    private void init(Collection<T> nList) {
        qi = 0;
        this.list = new ArrayList<>();
        this.list.addAll(nList);
        this.toRm = new ArrayList<>();
        if (!isPowOf2(list.size())) {
            if (list.size() < 2)
                throw new RuntimeException("List size is lesser than 2");
            Random rand = new Random();
            for (int i = 0; i < list.size() - previousPow2(nList.size()); i++)
                list.remove(list.get(rand.nextInt(list.size())));
        }
    }

    private boolean isPowOf2(long x) {
        return (x != 0) && ((x & (x - 1)) == 0);
    }

    private int previousPow2(long x) {
        int res = 0, pow;
        for (int i = 0; (pow = (int) Math.pow(2, i)) <= x; i++)
            res = pow;
        return res;
    }

    /**
     * @return List containing the only element when top-list is finished
     */
    @Override
    public ArrayList<T> getList() {
        return list;
    }

    @Override
    public Question<T> getQ() {
        if (!isPowOf2(list.size()))
            throw new RuntimeException("Unexpected exception: list size is'nt power of 2");

        Question<T> res = iterMain();
        if (res != null) return res;
        if (list.size() < 2)
            return null;

        list.removeIf(t -> toRm.contains(t));
        toRm.clear();
        qi = 0;
        return iterMain();
    }

    private Question<T> iterMain() {
        for (; qi < list.size() / 2; qi++) {
            if (qi < toRm.size()) continue;
            T e1 = list.get(qi);
            T e2 = list.get(qi + list.size() / 2);
            return new QuestionHandler(e1, e2);
        }
        return null;
    }

    private void handleAnswer(T lhs, T rhs, int ans) {
        if (ans < 0)
            toRm.add(rhs);
        else toRm.add(lhs);
    }

    private class QuestionHandler implements Question<T> {
        private final T lhs;
        private final T rhs;

        private QuestionHandler(T lhs, T rhs) {
            if (rhs == null || lhs == null)
                throw new NullPointerException();

            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public Pair<T, T> getIt() {
            return new Pair<>(lhs, rhs);
        }

        @Override
        public void answerIt(int ans) {
            RoyaleTop.this.handleAnswer(lhs, rhs, ans);
        }
    }
}

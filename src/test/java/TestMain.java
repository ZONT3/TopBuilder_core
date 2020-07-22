import javafx.util.Pair;
import org.junit.Test;
import ru.zont.topbuilder.core.EachOneTop;
import ru.zont.topbuilder.core.Question;
import ru.zont.topbuilder.core.RoyaleTop;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestMain {

    @Test
    public void eachOne() {
        EachOneTop<String> newTop = new EachOneTop<>("228", "1", "8", "3", "5", "6");
        Question<String> q;
        while ((q = newTop.getQ()) != null) {
            Pair<String, String> qs = q.getIt();
            q.answerIt(Integer.parseInt(qs.getKey()) - Integer.parseInt(qs.getValue()));
        }
        assertArrayEquals(new String[]{"1", "3", "5", "6", "8", "228"}, newTop.getList().toArray());
    }

    @Test
    public void royale() {
        RoyaleTop<String> kek = new RoyaleTop<>("lol", "kek", "cheburek", "eblek", "govnoek");
        System.out.println(kek.getList().toString());
        assertEquals(4, kek.getList().size());

        Question<String> q;
        Random rand = new Random(1338);
        while ((q = kek.getQ()) != null) {
            q.answerIt(rand.nextBoolean() ? 1 : -1);
        }

        System.out.println(kek.getList().toString());
        assertEquals(1, kek.getList().size());
    }
}

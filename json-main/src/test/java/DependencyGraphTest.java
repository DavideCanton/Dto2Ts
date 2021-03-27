import com.mdcc.dto2ts.json.main.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DependencyGraphTest
{
    @Test
    public void testOrder() {
        /*
            A -> B -> D
              -> C
            E
         */
        DependencyGraph d = new DependencyGraph();
        d.registerDependency("a", "b");
        d.registerDependency("a", "c");
        d.registerDependency("b", "d");
        d.registerClass("e");

        List<String> order = d.reversedTopologicalOrder();

        assertTrue(order.indexOf("a") > order.indexOf("b"));
        assertTrue(order.indexOf("a") > order.indexOf("c"));
        assertTrue(order.indexOf("b") > order.indexOf("d"));
    }
}

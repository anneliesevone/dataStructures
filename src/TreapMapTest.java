import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TreapMapTest {

    private TreapMap<Integer, String> treap;

    @BeforeEach
    public void setUp() {
        treap = new TreapMap<>();
        treap.put(20, "Twenty");
        treap.put(10, "Ten");
        treap.put(30, "Thirty");
        treap.put(5, "Five");
        treap.put(25, "Twenty Five");
    }

    @Test
    public void testPutAndGet() {
        assertEquals("Ten", treap.get(10));
        assertEquals("Twenty Five", treap.get(25));
        assertNull(treap.get(99));
    }

    @Test
    public void testUpdatePut() {
        treap.put(10, "Updated");
        assertEquals("Updated", treap.get(10));
    }

    @Test
    public void testRemove() {
        assertEquals("Ten", treap.remove(10));
        assertNull(treap.get(10));
        assertNull(treap.remove(99));
    }

    @Test
    public void testSizeAndIsEmpty() {
        assertEquals(5, treap.size());
        treap.remove(20);
        assertEquals(4, treap.size());
        assertFalse(treap.isEmpty());
        treap.remove(10);
        treap.remove(5);
        treap.remove(25);
        treap.remove(30);
        assertTrue(treap.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertTrue(treap.containsKey(20));
        assertFalse(treap.containsKey(100));
    }

    @Test
    public void testFirstAndLastEntry() {
        Map.Entry<Integer, String> first = treap.firstEntry();
        assertNotNull(first);
        assertEquals(5, first.getKey());

        Map.Entry<Integer, String> last = treap.lastEntry();
        assertNotNull(last);
        assertEquals(30, last.getKey());
    }

    @Test
    public void testKeySet() {
        Set<Integer> keys = treap.keySet();
        List<Integer> sortedKeys = new ArrayList<>(keys);
        assertEquals(List.of(5, 10, 20, 25, 30), sortedKeys);
    }

    @Test
    public void testCeilingEntry() {
        assertEquals(10, treap.ceilingEntry(9).getKey());
        assertEquals(20, treap.ceilingEntry(20).getKey());
        assertNull(treap.ceilingEntry(31));
    }

    @Test
    public void testFloorEntry() {
        assertEquals(10, treap.floorEntry(10).getKey());
        assertEquals(5, treap.floorEntry(7).getKey());
        assertNull(treap.floorEntry(4));
    }

    @Test
    public void testLowerEntry() {
        assertEquals(10, treap.lowerEntry(20).getKey());
        assertEquals(5, treap.lowerEntry(10).getKey());
        assertNull(treap.lowerEntry(5));
    }

    @Test
    public void testHigherEntry() {
        assertEquals(25, treap.higherEntry(20).getKey());
        assertEquals(10, treap.higherEntry(5).getKey());
        assertNull(treap.higherEntry(30));
    }

    @Test
    public void testSubMap() {
        List<Map.Entry<Integer, String>> sub = treap.subMap(10, 26);
        List<Integer> keys = sub.stream().map(Map.Entry::getKey).toList();
        assertEquals(List.of(10, 20, 25), keys);
    }
}
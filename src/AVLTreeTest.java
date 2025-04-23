import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AVLTreeTest {

    private AVLTree<Integer, String> avl;

    @BeforeEach
    public void setUp() {
        avl = new AVLTree<>();
        avl.put(20, "Twenty");
        avl.put(10, "Ten");
        avl.put(30, "Thirty");
        avl.put(5, "Five");
        avl.put(25, "Twenty Five");
    }

    @Test
    public void testPutAndGet() {
        assertEquals("Ten", avl.get(10));
        assertEquals("Thirty", avl.get(30));
        assertNull(avl.get(100));
    }

    @Test
    public void testUpdatePut() {
        avl.put(10, "Updated");
        assertEquals("Updated", avl.get(10));
    }

    @Test
    public void testRemove() {
        assertEquals("Ten", avl.remove(10));
        assertNull(avl.get(10));
        assertNull(avl.remove(100));
    }

    @Test
    public void testContainsKey() {
        assertTrue(avl.containsKey(20));
        assertFalse(avl.containsKey(100));
    }

    @Test
    public void testSizeAndIsEmpty() {
        assertEquals(5, avl.size());
        avl.clear();
        assertEquals(0, avl.size());
        assertTrue(avl.isEmpty());
    }

    @Test
    public void testKeySetIsSorted() {
        Set<Integer> keys = avl.keySet();
        List<Integer> sorted = new ArrayList<>(keys);
        assertEquals(List.of(5, 10, 20, 25, 30), sorted);
    }

    @Test
    public void testValuesOrder() {
        Collection<String> values = avl.values();
        assertTrue(values.contains("Twenty"));
        assertTrue(values.contains("Ten"));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, String>> entries = avl.entrySet();
        assertTrue(entries.contains(new AbstractMap.SimpleEntry<>(20, "Twenty")));
    }

    @Test
    public void testClear() {
        avl.clear();
        assertTrue(avl.isEmpty());
        assertEquals(0, avl.size());
    }
}


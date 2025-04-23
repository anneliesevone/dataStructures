import java.util.*;

public class TreapMap<K extends Comparable<K>, V> implements Map<K, V> {

    private static class TreapNode<K, V> {
        final K key;
        V value;
        final int priority;
        TreapNode<K, V> left, right;

        TreapNode(K key, V value, int priority) {
            this.key = key;
            this.value = value;
            this.priority = priority;
        }
    }

    private TreapNode<K, V> root;
    private int size;
    private final Random rand = new Random();

    @Override
    public V put(K key, V value) {
        int priority = rand.nextInt();
        root = insert(root, key, value, priority);
        return value;
    }

    @Override
    public V get(Object key) {
        if (!(key instanceof Comparable)) return null;
        return get((K) key);
    }

    private V get(K key) {
        TreapNode<K, V> node = find(root, key);
        return node == null ? null : node.value;
    }

    @Override
    public V remove(Object key) {
        if (!(key instanceof Comparable)) return null;
        @SuppressWarnings("unchecked")
        K castKey = (K) key;
        TreapNode<K, V>[] result = new TreapNode[1];
        root = delete(root, castKey, result);
        return result[0] == null ? null : result[0].value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Comparable)) return false;
        return get((K) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (V v : values()) {
            if (Objects.equals(v, value)) return true;
        }
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new LinkedHashSet<>();
        inorderKeys(root, keys);
        return keys;
    }

    private void inorderKeys(TreapNode<K, V> node, Set<K> keys) {
        if (node != null) {
            inorderKeys(node.left, keys);
            keys.add(node.key);
            inorderKeys(node.right, keys);
        }
    }

    @Override
    public Collection<V> values() {
        List<V> vals = new ArrayList<>();
        inorderValues(root, vals);
        return vals;
    }

    private void inorderValues(TreapNode<K, V> node, List<V> vals) {
        if (node != null) {
            inorderValues(node.left, vals);
            vals.add(node.value);
            inorderValues(node.right, vals);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new LinkedHashSet<>();
        inorderEntries(root, entries);
        return entries;
    }

    private void inorderEntries(TreapNode<K, V> node, Set<Entry<K, V>> entries) {
        if (node != null) {
            inorderEntries(node.left, entries);
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inorderEntries(node.right, entries);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Treap core methods

    private TreapNode<K, V> find(TreapNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return find(node.left, key);
        if (cmp > 0) return find(node.right, key);
        return node;
    }

    private TreapNode<K, V> insert(TreapNode<K, V> node, K key, V value, int priority) {
        if (node == null) {
            size++;
            return new TreapNode<>(key, value, priority);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value, priority);
            if (node.left.priority > node.priority)
                node = rotateRight(node);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value, priority);
            if (node.right.priority > node.priority)
                node = rotateLeft(node);
        } else {
            node.value = value;
        }
        return node;
    }

    private TreapNode<K, V> delete(TreapNode<K, V> node, K key, TreapNode<K, V>[] result) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key, result);
        } else if (cmp > 0) {
            node.right = delete(node.right, key, result);
        } else {
            result[0] = node;
            size--;
            return merge(node.left, node.right);
        }
        return node;
    }

    private TreapNode<K, V> merge(TreapNode<K, V> left, TreapNode<K, V> right) {
        if (left == null) return right;
        if (right == null) return left;
        if (left.priority > right.priority) {
            left.right = merge(left.right, right);
            return left;
        } else {
            right.left = merge(left, right.left);
            return right;
        }
    }

    private TreapNode<K, V> rotateLeft(TreapNode<K, V> node) {
        TreapNode<K, V> r = node.right;
        node.right = r.left;
        r.left = node;
        return r;
    }

    private TreapNode<K, V> rotateRight(TreapNode<K, V> node) {
        TreapNode<K, V> l = node.left;
        node.left = l.right;
        l.right = node;
        return l;
    }

    // Extra methods for SortedMap functionality

    public Entry<K, V> firstEntry() {
        if (root == null) return null;
        TreapNode<K, V> current = root;
        while (current.left != null) current = current.left;
        return new AbstractMap.SimpleEntry<>(current.key, current.value);
    }

    public Entry<K, V> lastEntry() {
        if (root == null) return null;
        TreapNode<K, V> current = root;
        while (current.right != null) current = current.right;
        return new AbstractMap.SimpleEntry<>(current.key, current.value);
    }

    public Entry<K, V> ceilingEntry(K key) {
        TreapNode<K, V> res = ceiling(root, key);
        return res == null ? null : new AbstractMap.SimpleEntry<>(res.key, res.value);
    }

    public Entry<K, V> floorEntry(K key) {
        TreapNode<K, V> res = floor(root, key);
        return res == null ? null : new AbstractMap.SimpleEntry<>(res.key, res.value);
    }

    public Entry<K, V> lowerEntry(K key) {
        TreapNode<K, V> res = lower(root, key);
        return res == null ? null : new AbstractMap.SimpleEntry<>(res.key, res.value);
    }

    public Entry<K, V> higherEntry(K key) {
        TreapNode<K, V> res = higher(root, key);
        return res == null ? null : new AbstractMap.SimpleEntry<>(res.key, res.value);
    }

    private TreapNode<K, V> ceiling(TreapNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        if (cmp > 0) return ceiling(node.right, key);
        TreapNode<K, V> left = ceiling(node.left, key);
        return (left != null) ? left : node;
    }

    private TreapNode<K, V> floor(TreapNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        if (cmp < 0) return floor(node.left, key);
        TreapNode<K, V> right = floor(node.right, key);
        return (right != null) ? right : node;
    }

    private TreapNode<K, V> lower(TreapNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp <= 0) return lower(node.left, key);
        TreapNode<K, V> right = lower(node.right, key);
        return (right != null) ? right : node;
    }

    private TreapNode<K, V> higher(TreapNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp >= 0) return higher(node.right, key);
        TreapNode<K, V> left = higher(node.left, key);
        return (left != null) ? left : node;
    }

    public List<Entry<K, V>> subMap(K fromKey, K toKey) {
        List<Entry<K, V>> entries = new ArrayList<>();
        subMap(root, fromKey, toKey, entries);
        return entries;
    }

    private void subMap(TreapNode<K, V> node, K fromKey, K toKey, List<Entry<K, V>> entries) {
        if (node == null) return;
        int cmpLow = fromKey.compareTo(node.key);
        int cmpHigh = toKey.compareTo(node.key);
        if (cmpLow < 0) subMap(node.left, fromKey, toKey, entries);
        if (cmpLow <= 0 && cmpHigh > 0)
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        if (cmpHigh > 0) subMap(node.right, fromKey, toKey, entries);
    }
}
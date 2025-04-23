import java.util.*;

public class AVLTree<K extends Comparable<K>, V> implements Map<K, V> {

    private static class AVLNode<K, V> {
        K key;
        V value;
        int height;
        AVLNode<K, V> left, right;

        AVLNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private AVLNode<K, V> root;
    private int size;

    @Override
    public V put(K key, V value) {
        root = insert(root, key, value);
        return value;
    }

    @Override
    public V get(Object key) {
        if (!(key instanceof Comparable)) return null;
        return get((K) key);
    }

    private V get(K key) {
        AVLNode<K, V> node = getNode(root, key);
        return node == null ? null : node.value;
    }

    @Override
    public V remove(Object key) {
        if (!(key instanceof Comparable)) return null;
        AVLNode<K, V>[] result = new AVLNode[1];
        root = delete(root, (K) key, result);
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
        inOrderKeys(root, keys);
        return keys;
    }

    private void inOrderKeys(AVLNode<K, V> node, Set<K> keys) {
        if (node != null) {
            inOrderKeys(node.left, keys);
            keys.add(node.key);
            inOrderKeys(node.right, keys);
        }
    }

    @Override
    public Collection<V> values() {
        List<V> vals = new ArrayList<>();
        inOrderValues(root, vals);
        return vals;
    }

    private void inOrderValues(AVLNode<K, V> node, List<V> vals) {
        if (node != null) {
            inOrderValues(node.left, vals);
            vals.add(node.value);
            inOrderValues(node.right, vals);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new LinkedHashSet<>();
        inOrderEntries(root, entries);
        return entries;
    }

    private void inOrderEntries(AVLNode<K, V> node, Set<Entry<K, V>> entries) {
        if (node != null) {
            inOrderEntries(node.left, entries);
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inOrderEntries(node.right, entries);
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

    // Internal AVL methods

    private int height(AVLNode<K, V> node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(AVLNode<K, V> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AVLNode<K, V> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private AVLNode<K, V> rotateRight(AVLNode<K, V> y) {
        AVLNode<K, V> x = y.left;
        y.left = x.right;
        x.right = y;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private AVLNode<K, V> rotateLeft(AVLNode<K, V> x) {
        AVLNode<K, V> y = x.right;
        x.right = y.left;
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private AVLNode<K, V> rebalance(AVLNode<K, V> node) {
        updateHeight(node);
        int balance = balanceFactor(node);

        if (balance > 1) {
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1) {
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode<K, V> insert(AVLNode<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new AVLNode<>(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insert(node.left, key, value);
        else if (cmp > 0) node.right = insert(node.right, key, value);
        else node.value = value;
        return rebalance(node);
    }

    private AVLNode<K, V> delete(AVLNode<K, V> node, K key, AVLNode<K, V>[] result) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = delete(node.left, key, result);
        else if (cmp > 0) node.right = delete(node.right, key, result);
        else {
            result[0] = node;
            size--;
            if (node.left == null || node.right == null) {
                return node.left != null ? node.left : node.right;
            } else {
                AVLNode<K, V> min = getMin(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = delete(node.right, min.key, new AVLNode[1]);
            }
        }
        return rebalance(node);
    }

    private AVLNode<K, V> getNode(AVLNode<K, V> node, K key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node;
        }
        return null;
    }

    private AVLNode<K, V> getMin(AVLNode<K, V> node) {
        while (node.left != null) node = node.left;
        return node;
    }
}
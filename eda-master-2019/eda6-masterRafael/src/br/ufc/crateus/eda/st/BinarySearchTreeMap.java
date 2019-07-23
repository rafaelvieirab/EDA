package br.ufc.crateus.eda.st;

public class BinarySearchTreeMap<K extends Comparable<K>, V> extends AbstractBinarySearchTreeMap<K, V> {
	private class BSTNode extends Node {
		BSTNode left;
		BSTNode right;
		
		public BSTNode(K key, V value) {
			super(key, value);
		}

		@Override
		Node left() {
			return left;
		}

		@Override
		Node right() {
			return right;
		}
		
	}
	
	private BSTNode root; 
	
	public Node getRoot() {
		return root;
	}
	
	@Override
	public void put(K key, V value) {
		root = put(root, key, value);
	}
	
	protected BSTNode put(BSTNode r, K key, V value) {
		if (r == null) return new BSTNode(key, value);
		
		if (key.compareTo(r.key) < 0) r.left = put(r.left, key, value);
		else if (key.compareTo(r.key) > 0) r.right = put(r.right, key, value);
		else r.value = value;
		
		r.count = count(r.left) + count(r.right) + 1;
		return r;
	}
	
	private BSTNode removeMin(BSTNode r) {
		if (r.left == null) return r.right;
		r.left = removeMin(r.left);
		r.count = count(r.left) + count(r.right) + 1;
		return r;
	}

	@Override
	public void removeMin() {
		root = removeMin(root);
	}
	
	private BSTNode removeMax(BSTNode r) {
		if (r.right == null) return r.left;
		r.right = removeMax(r.right);
		r.count = count(r.left) + count(r.right) + 1;
		return r;
	}

	@Override
	public void removeMax() {
		root = removeMax(root);
	}
	
	@SuppressWarnings("unchecked")
	private BSTNode remove(BSTNode r, K key) {
		if (r == null) return null;
		
		int cmp = key.compareTo(r.key); 
		if (cmp < 0) r.left = remove(r.left, key);
		else if (cmp > 0) r.right = remove(r.right, key);
		else {
			if (r.left == null) return r.right;
			if (r.right == null) return r.left;
			BSTNode t = r;
			r = (BSTNode) maxNode(r.left);
			r.right = t.right;
			r.left = removeMax(t.left);
		}
		
		r.count = count(r.left) + count(r.right) + 1;
		return r;
	}

	@Override
	public void remove(K key) {
		root = remove(root, key);
	}
}

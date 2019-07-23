package br.ufc.crateus.eda.st;

public class TreeAVLMap<K extends Comparable<K>,V> extends AbstractBinarySearchTreeMap<K, V> {
	protected class AVLNode extends Node{
		AVLNode left;
		AVLNode right;
		
		public AVLNode(K key, V value) {
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
	private AVLNode root;
	
	@Override
	protected AVLNode getRoot() {
		return root;
	}
	
	private int height(AVLNode r) {
		if(r==null) return -1;
		int hLeft = height(r.left);
		int hRight = height(r.right);
		
		if(hLeft > hRight) return hLeft+1;
		return hRight+1;
	}
	
	private int fatorBalanceamento(AVLNode r) {
		if(r == null) return 0;
		return height(r.left) - height(r.right);
	}
	
	private AVLNode put(AVLNode r, K key, V value) {
		if(r == null) return new AVLNode(key,value);
		
		int cmp = key.compareTo(r.key);
		if(cmp < 0) r.left = put(r.left, key, value);
		if(cmp > 0) r.right = put(r.right, key, value);
		r.value = value;
		
		return balanceamento(r);
	}
	
	@Override
	public void put(K key, V value) {
		root = put(root,key,value);
	}

	@SuppressWarnings("unchecked")
	private AVLNode remove(AVLNode r, K key) {
		if(r == null) return null;
		
		int cmp = key.compareTo(r.key);
		if(cmp < 0) r.left = remove(r.left,key);
		else if(cmp > 0) r.right = remove(r.right,key);
		else {
			if(r.left == null) return r.right;
			if(r.right== null) return r.left;
			AVLNode temp = r;
			r = (AVLNode) maxNode(r.left);
			r.right = temp.right;
			r.left = removeMax(temp.left);
		}
		
		return balanceamento(r);
	}

	@Override
	public void remove(K key) {
		root = remove(root, key);
	}
	
	private AVLNode removeMax(AVLNode r) {
		if(r.right == null) return r.left;
		r.right = removeMax(r.right);
		r.count = count(r.left) + count(r.right) +1;
		return r;
	}
	
	@Override
	public void removeMax() {
		if(root!=null) root = removeMax(root);
	}
	private AVLNode removeMin(AVLNode r) {
		if(r.left == null) return r.right;
		r.left = removeMin(r.left);
		r.count = count(r.left) + count(r.right) + 1;
		return r;
	}

	@Override
	public void removeMin() {
		if(root!=null) root = removeMin(root);
	}
	
	private AVLNode balanceamento(AVLNode r) {
		if(fatorBalanceamento(r) == 2) {
			if(fatorBalanceamento(r.left) == 1) {
				r = rotateRight(r);
			}
			else {//fatorBalanceamento(r.left) == -1
				r.left = rotateLeft(r.left);
				r = rotateRight(r);
			}
		}
		if(fatorBalanceamento(r) == -2) {
			if(fatorBalanceamento(r.right) == 1) {
				r.right = rotateRight(r.right);
				r = rotateLeft(r);
			}
			else { //fatorBalanceamento(r.left) == -1
				r = rotateLeft(r);	
			}
		}
		r.count = count(r.left) + count(r.right) + 1;
		return r;
	}
	
	
	private AVLNode rotateRight(AVLNode r) {
		AVLNode novo_r = r.left;
		r.left = novo_r.right;
		novo_r.right = r;
		
		r.count = count(r.left) + count(r.right) + 1;
		novo_r.count = count(novo_r.left) + r.count + 1;
		return novo_r;
	}
	
	private AVLNode rotateLeft(AVLNode r) {
		AVLNode novo_r = r.right;	
		r.right = novo_r.left;
		novo_r.left = r;
		
		r.count = count(r.left) + count(r.right) + 1;
		novo_r.count = r.count + count(novo_r.right) + 1;
		return novo_r;
	}

	private boolean estaBalanceada(AVLNode r) {
		if(r==null) return true;
		
		if(estaBalanceada(r.left) && estaBalanceada(r.right))
			if(fatorBalanceamento(r) > -2 && fatorBalanceamento(r) < 2)
				return true;
		
		return false;
	}
	public boolean estaBalanceada() {
		return estaBalanceada(root);
	}
}

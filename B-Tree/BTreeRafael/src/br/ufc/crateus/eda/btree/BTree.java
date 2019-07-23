package br.ufc.crateus.eda.btree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

import br.ufc.crateus.eda.btree.dtypes.IntegerDT;
import br.ufc.crateus.eda.btree.dtypes.StringDT;

public class BTree<K extends Comparable<K>, V> {

	private Page<K> root;
	private PageSerializer<K> pageSerializer;
	private DataSerializer<V> dataSerializer;

	public BTree(PageSerializer<K> pageSerializer, DataSerializer<V> dataSerializer) throws Exception {
		this.pageSerializer = pageSerializer;
		this.dataSerializer = dataSerializer;
		this.root = pageSerializer.readRoot();
	}

	public V get(K key) throws Exception {
		return get(root, key);
	}

	private V get(Page<K> r, K key) throws Exception {
		if (r == null)
			return null;

		Long offset = r.getDataOffset(key);
		if (offset != null) {
			return dataSerializer.read(offset);
		}
		return get(r.next(key), key);
	}

	public boolean contains(K key) throws Exception {
		return contains(root, key);
	}

	private boolean contains(Page<K> r, K key) throws Exception {
		if (r == null)
			return false;

		if (!r.holds(key)) {
			if (r.isExternal())
				return false;
			return contains(r.next(key), key);
		}
		return true;
	}

	public void put(K key, V value) throws Exception {
		put(root, key, value);
		if (root.isOverflowed()) {
			BinarySearchST<K, Long> bsST = root.split2();
			root = pageSerializer.append(bsST, false);
			pageSerializer.updateRootOffset(root.getOffset());
		}
	}

	private void put(Page<K> r, K key, V value) throws Exception {
		if (r.holds(key)) {
			dataSerializer.write(r.getDataOffset(key), value);
			return;
		}
		if (r.isExternal()) {
				long offset = dataSerializer.append(value);
				r.insert(key, offset);
				pageSerializer.keyInserted();
				r.close();
		} else {
			Page<K> next = r.next(key);
			if (next == null)
				return;
			put(next, key, value);
			if (next.isOverflowed()) {
				BinarySearchST<K, Long> bsST = next.split2();
				r.join(bsST);
				r.close();
			}
		}
	}

	public Iterable<K> keys() throws Exception {
		Queue<K> queue = new LinkedList<>();
		collect(root, queue);
		return queue;
	}

	private void collect(Page<K> r, Queue<K> queue) throws Exception {
		if (r == null)
			return;
		BinarySearchST<K, Long> bsST = r.asSymbolTable();

		for (K key : bsST.keys())
			queue.add(key);

		if (!r.isExternal())
			for (Long filho : bsST.filhos())
				collect(r.nextPage(filho), queue);
	}

	public int size() {
		return pageSerializer.getNumberOfKeys();
	}

	public BinarySearchST<K, Long> minTree(Page<K> r) throws Exception {
		if (r == null)
			return null;

		if (r.isExternal()) {
			K key = r.asSymbolTable().min();
			Long value = r.getDataOffset(key);
			BinarySearchST<K, Long> bs = new BinarySearchST<K, Long>(1);// mudar o tamanho depois
			bs.put(key, value);
			return bs;
		} else {
			Long filhoMenor = r.asSymbolTable().getFilhos(0);
			Page<K> p = pageSerializer.read(filhoMenor);
			return minTree(p);
		}
	}

	public BinarySearchST<K, Long> maxTree(Page<K> r) throws Exception {
		if (r == null)
			return null;

		if (r.isExternal()) {
			K key = r.asSymbolTable().max();
			Long value = r.asSymbolTable().get(key);
			BinarySearchST<K, Long> bs = new BinarySearchST<K, Long>(1);// mudar o tamanho depois
			bs.put(key, value);
			return bs;
		} else {
			Long filhoMaior = r.asSymbolTable().getFilhos(r.asSymbolTable().size() - 1);
			Page<K> p = pageSerializer.read(filhoMaior);
			return maxTree(p);
		}
	}

	public void remove(K key) throws Exception {
		if(root.isExternal()) {
			root.delete(key);
			root.close();
		}
		else {
			remove(null, root, key);
			this.root = pageSerializer.readRoot();
			
			if(root.asSymbolTable().size() == 0) {
				Page<K> filhoLeft = pageSerializer.read(root.asSymbolTable().getFilhos(0));
				Page<K> filhoRight = pageSerializer.read(root.asSymbolTable().getFilhos(1));
				
				if(filhoLeft != null) {
					Long off = filhoLeft.getOffset();
					filhoLeft.setOffset(root.getOffset());
					filhoLeft.close();
					pageSerializer.delete(off);
				}else if(filhoRight != null) {
					Long off = filhoRight.getOffset();
					filhoRight.setOffset(root.getOffset());
					filhoRight.close();
					pageSerializer.delete(off);
				}
			}
		}
	}

	private void remove(Page<K> pattern, Page<K> r, K key) throws Exception {
		if (r == null) return;

		BinarySearchST<K, Long> bsST = r.asSymbolTable();
		int pos = bsST.rank(key);
		
		if (bsST.contains(key)) {// encontrei a chaveS
			
			if (r.isExternal()) {
				int posF = pattern.asSymbolTable().rank(key);
				r.delete(key);
					
				if (r.qtdMin()) {
					aux(pattern, r, key);
				}
			}
			else {// Nó interno
				Page<K> left = r.nextPage(bsST.getFilhos(pos));
				Page<K> right = r.nextPage(bsST.getFilhos(pos + 1));
				
				if (left.qtdMin() && right.qtdMin()) {
					System.out.println("Os dois filhos tem a Qtd Min");
					left.uniao(right);
					r.delete(key);
					r.close();
				} else if (left.asSymbolTable().size() >= right.asSymbolTable().size()) {
					System.out.println("O filho left tem mais keys que right");
					BinarySearchST<K, Long> bs = maxTree(left);
					r.asSymbolTable().update(bs, pos);
					r.close();
					remove(r,left,key);
					
				} else {
					System.out.println("O filho right tem mais keys que left");
					BinarySearchST<K, Long> bs = minTree(right);
					r.asSymbolTable().update(bs, pos);
					r.close();
					remove(r,right,key);
				}
			}
		}
		else {
			garanteMkeys(r,key,pos);
			remove(r, r.next(key), key);
			
			if(pattern== null) return;
			
			if (r.qtdMin()) {
				aux(pattern, r, key);
			}
		}
	}
	
	private void garanteMkeys(Page<K> r, K key, int pos) throws Exception {
		Page<K> p = r.next(key);
		if(p == null || !p.qtdMin()) return;
		
		K ks = r.select(pos);
		Long vl = r.getDataOffset(ks);
		
		BinarySearchST<K, Long> bsST = r.asSymbolTable();
		
		Page<K> left = r.nextPage(bsST.getFilhos(pos));
		Page<K> right = r.nextPage(bsST.getFilhos(pos + 1));
		
		if (left.qtdMin() && right.qtdMin()) {
			System.out.println("Os dois filhos tem a Qtd Min");
			left.insert(ks, vl);
			left.uniao(right);
			r.asSymbolTable().delete(ks);
			r.close();
		} else if (left.asSymbolTable().size() >= right.asSymbolTable().size()) {
			System.out.println("O filho left tem mais keys que right");
			rotateRight(left, r, p, pos, ks, vl);
			
		} else {
			System.out.println("O filho right tem mais keys que left");
			rotateLeft(right, r, p, pos, ks, vl);
		}
	}
	
	private void rotateRight(Page<K> left,Page<K> pattern,Page<K> r,int pos, K k,Long v) throws Exception {
		BinarySearchST<K, Long> bs = maxTree(left);
		left.asSymbolTable().deleteMax();
		pattern.asSymbolTable().update(left.asSymbolTable(),pos);
		r.insert(k, v);
		left.close();
		pattern.close();
		r.close();
	}
	
	private void rotateLeft(Page<K> right,Page<K> pattern,Page<K> r,int pos, K k,Long v) throws Exception {
		BinarySearchST<K, Long> bs = maxTree(right);
		right.asSymbolTable().deleteMax();
		pattern.asSymbolTable().update(right.asSymbolTable(),pos);
		r.insert(k, v);
		right.close();
		pattern.close();
		r.close();
	}
	
	private void aux(Page<K> pattern, Page<K> r, K key) throws Exception {
		int posF = pattern.asSymbolTable().rank(key);
		if (r.qtdMin()) {
			System.out.println("---O nó tem a Qtd min---");
			Page<K> irmaoLeft = null;
			Page<K> irmaoRight = null;
			
			if(posF-1 >= 0)
				irmaoLeft = pattern.nextPage(pattern.asSymbolTable().getFilhos(posF-1));
			if(posF+1 < pageSerializer.getPageSize())
				irmaoRight = pattern.nextPage(pattern.asSymbolTable().getFilhos(posF+1));
			
			K ks = pattern.select(posF);
			Long vl = pattern.getDataOffset(ks);
			if(vl==null) {
				ks = pattern.select(posF-1);
				vl = pattern.getDataOffset(ks);
			}
			
			if(irmaoLeft!=null && !irmaoLeft.qtdMin()) {
				System.out.println("O irmao left não tem QTD MIN");
				rotateLeft(irmaoLeft, pattern, r, posF, ks, vl);
			
			}else if(irmaoRight!=null && !irmaoRight.qtdMin()) {
				System.out.println("O irmao right não tem QTD MIN");
				rotateRight(irmaoRight, pattern, r, posF+1, ks, vl);
				
			}else {//os dois tem a quantidade minima ou um deles é nulo
				if(irmaoLeft!=null && irmaoLeft.qtdMin()) {
					System.out.println("O irmao left tem QTD MIN");
					irmaoLeft.insert(ks, vl);
					irmaoLeft.uniao(r);
					pattern.asSymbolTable().delete(ks);
				}
				else if(irmaoRight!=null && irmaoRight.qtdMin()){
					System.out.println("O irmao right tem QTD MIN");
					r.insert(ks, vl);
					r.uniao(irmaoRight);
					pattern.asSymbolTable().delete(ks);
				}
			}
			r.close();
			pattern.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		IntegerDT  keyDT = new IntegerDT();
		IntegerDT  valDT = new IntegerDT();
		
		File keysFile = new File("keys.dat");
		PageSerializer<Integer> ps = new PageSerializer<>(keysFile, keyDT, 5);
		
		File valuesFile = new File("data.dat");
		DataSerializer<Integer> ds = new DataSerializer<>(valuesFile, valDT);
		
		BTree<Integer, Integer> st = new BTree<Integer, Integer>(ps, ds);
		
		for(int i = 4 ;i < 50 ; i++) st.put(i, i);
		  
		System.out.println("\n---------\n");
		  
		//st.remove(7);
		//st.put(7, 7);
		// for(Integer ks : st.keys()) System.out.println(ks+" ");		 
		 
		/*
		StringDT keyDT = new StringDT(21);
		StringDT valDT = new StringDT(16);

		File keysFile = new File("keys.dat");
		PageSerializer<String> ps = new PageSerializer<>(keysFile, keyDT, 4);

		File valuesFile = new File("data.dat");
		DataSerializer<String> ds = new DataSerializer<>(valuesFile, valDT);

		BTree<String, String> st = new BTree<String, String>(ps, ds);

		st.put("www.cs.princeton.edu", "128.112.136.12");
		st.put("www.cs.princeton.edu", "128.112.136.11");
		st.put("www.princeton.edu", "128.112.128.15");
		st.put("www.yale.edu", "130.132.143.21");
		st.put("www.simpsons.com", "209.052.165.60");
		st.put("www.apple.com", "17.112.152.32");
		st.put("www.amazon.com", "207.171.182.16");
		st.put("www.ebay.com", "66.135.192.87");
		st.put("www.cnn.com", "64.236.16.20");
		st.put("www.google.com", "216.239.41.99");
		st.put("www.nytimes.com", "199.239.136.200");
		st.put("www.microsoft.com", "207.126.99.140");
		st.put("www.dell.com", "143.166.224.230");
		st.put("www.slashdot.org", "66.35.250.151");
		st.put("www.espn.com", "199.181.135.201");
		st.put("www.weather.com", "63.111.66.11");
		st.put("www.yahoo.com", "216.109.118.65");
		st.put("www.crateus.ufc.br", "200.19.190.7");
		st.put("www.ufc.br", "200.17.41.185");

		System.out.println("Keys:");
		for (String key : st.keys())
			System.out.println(key);
		System.out.println("Get" + st.get("www.crateus.ufc.br"));
		*/
	}

}

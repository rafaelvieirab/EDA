package br.ufc.crateus.eda.btree;


public class Page<K extends Comparable<K>> {

	private boolean bottom;
	
	private BinarySearchST<K, Long> st;
	private PageSerializer<K> serializer;
	private long offset;
	
	public Page(boolean bottom, long offset, PageSerializer<K> serializer) {
		this(new BinarySearchST<>(serializer.getPageSize()), bottom, offset, serializer);
	}
	
	public Page(BinarySearchST<K, Long> st, boolean bottom, long offset, PageSerializer<K> serializer) {
		this.st = st;
		this.bottom = bottom;
		this.serializer = serializer;
		this.offset = offset;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (offset != other.offset)
			return false;
		return true;
	}

	public void insert(K key, Long offset) throws Exception {
		st.put(key, offset);
	}
	
	public void enter(Page<K> p) {//Pega a menor key de uma pagina, e insere na atual
		K min = p.st.min();
		st.put(min, p.offset);
	}
	
	//uni duas paginas. É usado 
	public void join(BinarySearchST<K, Long> bsST) {
		st.insert(bsST);
	}
	
	public void delete(K key) throws Exception {//Pega a menor key de uma pagina, e insere na atual
		st.delete(key);
		this.close();
		serializer.keyDeleted();
	}
	
	public Page<K> next(K key) throws Exception {
		int pos = st.rank(key);
		long next = st.getFilhos(pos);
		return serializer.read(next);	
	}
	
	public void uniao(Page<K> p) throws Exception {
		st.uniao(p.asSymbolTable());
		this.close();
		serializer.delete(p.getOffset());
	}
	
	public K select(int p) {
		return st.select(p);
	}
	
	//Não apagar! É usado no collect de Btree!
	public Page<K> nextPage(Long adress) throws Exception {
		return serializer.read(adress);
	}
	
	public BinarySearchST<K, Long> split2() throws Exception {
		BinarySearchST<K, Long> newST = this.st.split();
		this.close();
		
		K keyPai = newST.min();
		Long valuePai = newST.get(keyPai);
		newST.deleteMin();
		
		Page<K> newPage = serializer.append(newST, bottom);
		Long filho1 = offset;
		Long filho2 = newPage.offset;
		
		BinarySearchST<K,Long> bsST =  new BinarySearchST<>(serializer.getPageSize());
		bsST.constroiRoot(keyPai,valuePai,filho1,filho2);
		return bsST; 
	}
	
	public Long getDataOffset(K key) {
		return st.get(key);
	}

	public Long getOffset() {
		return offset;
	}
	
	public void setOffset(Long newOffset) {
		this.offset = newOffset;
	}
	
	public boolean holds(K key) {
		return st.contains(key);
	}

	public boolean isExternal() {
		return bottom;
	}

	public boolean isOverflowed() {
		return st.size() == serializer.getPageSize();
	}

	public void close() throws Exception {
		serializer.write(offset, this);
	}
	
	public BinarySearchST<K, Long> asSymbolTable() {
		return st;
	}

	public boolean qtdMin() {
		return st.qtdMin();
	}
}

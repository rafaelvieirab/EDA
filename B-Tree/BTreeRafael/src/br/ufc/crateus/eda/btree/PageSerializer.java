package br.ufc.crateus.eda.btree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import br.ufc.crateus.eda.btree.dtypes.DataType;
import br.ufc.crateus.eda.btree.dtypes.LongDT;

public class PageSerializer<K extends Comparable<K>> {
	private DataType<K> keyDT;
	private LongDT adressDT;
	private int m;
	private File file; 
	
	static final int FILE_SIZE_OFFSET = 0;
	static final int N_KEYS_OFFSET = 8;
	static final long ROOT_OFFSET = 12L;
	
	private long fileSize;
	private int nKeys;
	
	PageSerializer(File file, DataType<K> dt, int m) throws IOException {
		this.file = file;
		this.keyDT = dt;
		this.adressDT = new LongDT();
		this.m = m;
		
		if (file.exists()) {
			RandomAccessFile fileStore = new RandomAccessFile(file, "r");
			this.fileSize = fileStore.readLong();
			System.out.println("fileSize: "+fileSize);
			this.nKeys = fileStore.readInt();
			System.out.println("Num keys inseridas: "+nKeys);
			Long rootOffset = fileStore.readLong();
			fileStore.close();
			System.out.println("rootOffset: "+rootOffset);
		}
		else {
			RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
			fileStore.writeLong(20L);//fileStore
			fileStore.writeInt(0);//Num Keys
			fileStore.writeLong(20L);//Root_Offset
			this.fileSize = 20L;
			this.nKeys = 0;
			fileStore.close();
			System.out.println("fileSize: "+fileSize);
			System.out.println("rootOffset: "+12L);
		}
	}
	
	private int pageSize() {
		return 1 + 4 + m * (keyDT.size() + adressDT.size()) + (m+1) * adressDT.size();  
	}
	
	private void write(long offset, Page<K> page, boolean append) throws Exception {
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
		fileStore.seek(offset);
		fileStore.writeBoolean(page.isExternal());
		BinarySearchST<K, Long> st = page.asSymbolTable();
		fileStore.writeInt(st.size());
		keyDT.write(fileStore, st.keys(), m);
		adressDT.write(fileStore, st.values(), m);
		adressDT.write(fileStore, st.filhos(), m+1);
		
		if (append) {
			fileSize += pageSize();
			fileStore.seek(FILE_SIZE_OFFSET);
			fileStore.writeLong(fileSize);
			fileStore.seek(FILE_SIZE_OFFSET);
			fileStore.close();
		}
		fileStore.close();
		
		RandomAccessFile fileStore2 = new RandomAccessFile(file, "r");
		fileStore2.seek(offset);
		System.out.println("\nOffset:"+offset);
		System.out.println("External"+fileStore2.readBoolean());
		System.out.println("\nNum keys: "+fileStore2.readInt());
		List<K> lKeys = keyDT.read(fileStore2, m);
		K[] keys = (K[]) lKeys.toArray(new Comparable[m]);
		System.out.println();
		for(int i =0; i<keys.length ;i++) System.out.print(" "+keys[i]);
		Long[] values = adressDT.read(fileStore2, m).toArray(new Long[m]);
		System.out.println();
		
		for(int i =0; i<values.length ;i++) System.out.print(" "+values[i]);
		Long[] filhos = adressDT.read(fileStore2, m+1).toArray(new Long[m+1]);
		System.out.println();
		
		for(int i =0; i<filhos.length ;i++) System.out.print(" "+filhos[i]);
		System.out.println();
		fileStore2.close();
		
	}
	
	void write(long offset, Page<K> page) throws Exception {
		write(offset, page, false);
	}
	
	void delete(long offset) throws Exception {
		System.out.println("Pagina sendo deletada!!!");
		BinarySearchST<K, Long> st = new BinarySearchST<>(m);
		st.put(keyDT.getDefaultValue(), -1L);
		st.deleteMin();
		Page<K> page = new Page<K>(st,false,offset,this);
		write(offset, page, false);
	}
	
	Page<K> append(BinarySearchST<K, Long> st, boolean bottom) throws Exception {
		Page<K> page = new Page<>(st, bottom, fileSize, this);
		write(fileSize, page, true);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	Page<K> read(long offset) throws Exception {
		if(offset <= 0L) return null;
		RandomAccessFile fileStore = new RandomAccessFile(file, "r");
		fileStore.seek(offset);
		boolean botton = fileStore.readBoolean();
		int size = fileStore.readInt();
		List<K> lKeys = keyDT.read(fileStore, m);
		K[] keys = (K[]) lKeys.toArray(new Comparable[m]);
		
		Long[] values = adressDT.read(fileStore, m).toArray(new Long[m]);
		Long[] filhos = adressDT.read(fileStore, m+1).toArray(new Long[m+1]);
		
		BinarySearchST<K, Long> st = new BinarySearchST<>(keys, values,filhos, size);
		fileStore.close();
		return new Page<>(st, botton, offset, this);
	}
	
	int getPageSize() {
		return m;
	}

	Page<K> readRoot() throws Exception {
		Page<K> root = null;
		
		if (fileSize > 20L) { 
			RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
			fileStore.seek(ROOT_OFFSET);
			Long offset = fileStore.readLong();
			fileStore.close();
			root = read(offset);
		}
		else {
			BinarySearchST<K, Long> st = new BinarySearchST<>(m);
			st.put(keyDT.getDefaultValue(), -1L);
			st.deleteMin();
			root = append(st, true);
		}
		return root;
	}
	
	public void updateRootOffset(long offset) throws Exception {
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
		fileStore.seek(ROOT_OFFSET);
		fileStore.writeLong(offset);
		fileStore.close();
	}
	
	void keyInserted() throws IOException {
		nKeys++;
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
		fileStore.seek(N_KEYS_OFFSET);
		fileStore.writeInt(nKeys);
		fileStore.close();
	}
	
	void keyDeleted() throws IOException {
		nKeys--;
		RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
		fileStore.seek(N_KEYS_OFFSET);
		fileStore.writeInt(nKeys);
		fileStore.close();
	}
	
	public int getNumberOfKeys() {
		return nKeys;
	}
}

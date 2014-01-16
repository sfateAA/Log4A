package com.sfateaa.minilog.core.output.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class LogFile {
	
	static final String sHEADER = "/**********Header**********/";
	static final String sFOOTER = "/**********Footer**********/";
	
	File mFile;
	FileLogAppender mAppender;
	
	boolean mEnd = false;
	
	public LogFile(File file, FileLogAppender appender) {
		
		if (file == null) {
			throw new RuntimeException("File is null!!");
		}
		
		if (!file.isFile()) {
			throw new RuntimeException("It is not File!!!");
		}
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		mFile = file;
		
		init();
	}
	
	private void init() {
		String header = readHeader();
		
		if (!sHEADER.equals(header)) {
			//throw new RuntimeException("Header format is Error!!!");
			writeHeader();
			return;
		}
		
		if (hasEnd()) {
			return;
		}
		
		if (!hasSpace()) {
			writeFooter();
		}
	}

	public boolean hasSpace() {
		
		File file = mFile;
		long maxSize = mAppender.getMaxFileSize();
		long size = file.length();
		
		return size > maxSize;
	}
	
	public boolean hasEnd() {
		
		boolean endFlag = mEnd;
		if (endFlag) {
			return true;
		}
		
		File file = mFile;
		
		long size = file.length();
		if (size == 0) {
			return false;
		}
		
		long footerSize = sFOOTER.length();
		String lastLine = null;
		RandomAccessFile raf = null;
		
		try {
			raf = new RandomAccessFile(file, "r");
			raf.seek(size -1 - footerSize);
			lastLine = raf.readLine();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (sFOOTER.equals(lastLine)) {
			mEnd = true;
			return true;
		}
		return false;
	}
	
	public boolean writeHeader() {
		return write(sHEADER, false);
	}
	
	public boolean writeFooter() {
		boolean res = write(sFOOTER, true);
		if (res) {
			mEnd = res;
		}
		return res;
	}
	
	public boolean write(String str) {
		return write(str, true);
	}

	public boolean write(String str, boolean append) {
		
		File file = mFile;
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new FileWriter(file, append) , true);
			pw.println(str);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		
		return true;
	}
	
	public String readHeader() {
		
		String str = null;
		File file = mFile;
		BufferedReader br = null;
		
		try {
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			str = br.readLine();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return str;
	}
	
	public int getIndex() {
		
		File file = mFile;
		String fileName = file.getName();
		String prefix = FileLogAppender.DEFAULT_FILE_PREFIX;
		
		int index = 0;
		try {
			index = Integer.valueOf(fileName.replaceFirst("^" + prefix + "$", ""));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}
	
	public void beginTraction() {
		// TODO
	}
	
	public void commit() {
		// TODO
	}
	
}

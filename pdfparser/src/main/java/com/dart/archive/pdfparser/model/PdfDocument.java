package com.dart.archive.pdfparser.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfDocument {

	List<Page> pages;
	String name;
	File file;
	String isbn;

	public PdfDocument(String name, File file) {
		this(name, file, null);
	}

	public PdfDocument(String name, File file, String isbn) {
		this(name, file, isbn, new ArrayList<Page>());
	}

	public PdfDocument(String name, File file, String isbn, List<Page> pages) {
		this.name = name;
		this.file = file;
		this.pages = pages;
		this.isbn = isbn;
	}
	
	public boolean isEmpty() {
		return pages==null || pages.isEmpty();
	}
	
	public void addPage(Page page) {
		this.pages.add(page);
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getPagesNumber() {
		return pages==null?0:pages.size();
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Override
	public String toString() {
		return this.name + "[" + this.getPagesNumber() + " pages]";
	}
	
}

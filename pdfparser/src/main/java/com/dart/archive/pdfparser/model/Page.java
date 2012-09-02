/**
 * 
 */
package com.dart.archive.pdfparser.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author massi
 *
 */
public class Page {
	
	List<Image> images;
	String text;
	int pageNumber;
	
	public Page(int pageNumber) {
		this(pageNumber, null);
	}

	public Page(int pageNumber, String text) {
		this(pageNumber, text, new ArrayList<Image>());
	}

	public Page(int pageNumber, String text, List<Image> images) {
		this.pageNumber = pageNumber;
		this.images = images;
		this.text = text;
	}
	
	public boolean isEmpty() {
		return !hasImages() && !hasText();
	}
	
	public boolean hasImages() {
		return images!=null && !images.isEmpty();
	}
	
	public boolean hasText() {
		return !StringUtils.isBlank(text);
	}
	
	public int getImagesNumber() {
		return images==null?0:images.size();
	}
	
	public void setImages(List<Image> images) {
		this.images = images;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addImage(Image image) {
		images.add(image);
	}

	public List<Image> getImages() {
		return images;
	}

	public String getText() {
		return text;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public String toString() {
		return "page "+this.pageNumber + (this.isEmpty()?"":"["+this.getImagesNumber()+" images]");
	}
	
	

}

/**
 * 
 */
package com.dart.archive.pdfparser.model;

/**
 * @author massi
 *
 */
public class Image {
	
	String name;
	String path;
	
	public Image(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return name;
	}

}

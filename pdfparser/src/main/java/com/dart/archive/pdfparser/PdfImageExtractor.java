/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;

/**
 * @author massimiliano.gerardi
 *
 */
public class PdfImageExtractor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String action = args[0];
		File pdf = new File(args[1]);
		File dest = new File(args[2]);
		System.out.println(action + " " + pdf + " " + dest);
		CommandHelper helper = new CommandHelper();
		if (action.equalsIgnoreCase("extract")) {
			int result = helper.extractImages(pdf, dest);
			System.out.println(result);
		} else if (action.equalsIgnoreCase("convert")) {
			int result = helper.convertPpmToJpg(dest);
			System.out.println(result);
		} else if (action.equalsIgnoreCase("do")) {
			int result = helper.extractImages(pdf, dest);
			System.out.println(result);
			if (result>0) {
				int result2 = helper.convertPpmToJpg(dest);
				System.out.println(result2);
			}
		}
		
	}
	

}

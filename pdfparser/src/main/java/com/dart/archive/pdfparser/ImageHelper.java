/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.dart.archive.pdfparser.model.Image;

/**
 * @author massimiliano.gerardi
 *
 */
public class ImageHelper {
	
	private CommandHelper helper = new CommandHelper();
	
	private List<File> images;
	
	private List<File> getImages(final File file, final File dir) {
		int result = helper.extractImages(file, dir);
		if (result>-1) {
			result = helper.convertPpmToJpg(dir);
		}
		System.out.println("result "+result);
		List<File> files = helper.listJpegImages(dir, true);
		System.out.println("files: "+files);
		return files;
	}

	public List<Image> filterPageImages(File src, File dest, int pageNumber) {
		if (images==null) {
			images = getImages(src, dest);
		}
		final String pdfName = FilenameUtils.getBaseName(src.getName());
		final List<Image> results = new ArrayList<Image>();
		final String suffix = pdfName+"-"+StringUtils.leftPad(String.valueOf(pageNumber), 3, '0');
		for (File image : images) {
			if (image.getName().startsWith(suffix)) {
				results.add(new Image(FilenameUtils.getBaseName(image.getName()), image.getAbsolutePath()));
			}
		}
		return results;
	}

	public void setCommandHelper(CommandHelper helper) {
		this.helper = helper;
	}
	
	
	
}


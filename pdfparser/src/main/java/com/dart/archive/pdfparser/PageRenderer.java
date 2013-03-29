/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.dart.archive.pdfparser.model.Image;
import com.dart.archive.pdfparser.model.Page;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * @author massi
 *
 */
public class PageRenderer extends SimpleTextExtractionStrategy {


	Page page;
	boolean writeImage;
	boolean writeText;
	
	String name;
	
	public static final String IMAGE_NAME = "%s-%s-%s.%s";
    public static final String TEXT_NAME = "%s-%s.txt";

    private File outputDir;

	/**
	 * Creates a RenderListener that will look for images.
	 * @param path
	 * @param pageNumber 
	 */
	public PageRenderer(int pageNumber, String path, boolean writeText, boolean writeImage) {
		this(pageNumber, path, "img", writeText, writeImage);
	}
	
    public PageRenderer(int pageNumber, String path, String name, boolean writeText, boolean writeImage) {
		super();
		this.name = name;
    	this.page = new Page(pageNumber);
    	this.writeImage = writeImage;
    	this.writeText = writeText;
        if (path!=null) {
            this.outputDir = new File(path);
            if (!outputDir.exists()) {
            	outputDir.mkdirs();
            }
        }
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(
     *     com.itextpdf.text.pdf.parser.ImageRenderInfo)
     */
    public void renderImage(ImageRenderInfo renderInfo) {
		if (!writeImage) {
			return;
		}
    	PdfImageObject image = null;
		try {
			image = renderInfo.getImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (image == null) return;
    	
        //String filename = String.format(IMAGE_NAME, name, StringUtils.leftPad(String.valueOf(page.getPageNumber()), 3, '0'), renderInfo.getRef().getNumber(), image.getFileType());
    	//String filePath = writeImage(filename, image);
        //page.addImage(new Image(filename, filePath));
    }

	private String writeImage(String filename, PdfImageObject image) {
    	File file = getFile(filename);
		try {
			FileUtils.writeByteArrayToFile(file, image.getImageAsBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return file.getAbsolutePath();
	}

	public Page getPage() {
		return page;
	}

	private File getFile(String filename) {
		File file = new File(outputDir, filename);
        return file;
	}

	/**
     * Captures text using a simplified algorithm for inserting hard returns and spaces
     * @param	renderInfo	render info
     */
    public void renderText(TextRenderInfo renderInfo) {
    	super.renderText(renderInfo);
		page.setText(getResultantText());
    	writeText();
    }

	private void writeText() {
		if (!writeText) {
			return;
		}
    	String filename = String.format(TEXT_NAME, name, StringUtils.leftPad(String.valueOf(page.getPageNumber()), 3, '0'));
    	File file = getFile(filename);
    	try {
    		String text = getResultantText();
			FileUtils.writeStringToFile(file, text);
		} catch (IOException e) {
			System.out.println("error writing text file");
			e.printStackTrace();
		}
	}

}

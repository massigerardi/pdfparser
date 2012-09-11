package com.dart.archive.pdfparser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTest {

	String pdfFolder = "src/test/resources";
	String pdfPath = "src/test/resources/learmidellarte.pdf";
	String pdfDir = "target/test-results/learmidellarte";
	String destinationDir = "target/test-results";

	@Before
	public void setUp() throws IOException {
		clean();
	}

	@After
	public void clean() throws IOException {
		File dest = new File(destinationDir);
		FileUtils.deleteDirectory(dest);
	}
	
	@Test
	public void testMainFile() {
		Main.main(new String[] {pdfPath, pdfDir});
		checkFolder(pdfDir, 58, 57);
		
	}

	private void checkFolder(String dir, int images, int text) {
		File dest = new File(dir);
		assertTrue(dest.exists());
		assertTrue(dest.isDirectory());
		assertFalse(dest.list().length==0);
		assertEquals("images for "+dest.getName(), images, FileUtils.listFiles(dest, new String[] {"jpg"}, true).size());
		assertEquals("text for "+dest.getName(), text, FileUtils.listFiles(dest, new String[] {"txt"}, true).size());
		
	}

	@Test
	public void testMainFolder() {
		Main.main(new String[] {pdfFolder, destinationDir});
		File dest = new File(destinationDir);
		assertTrue(dest.exists());
		assertTrue(dest.isDirectory());
		assertTrue(dest.list().length==3);
		checkFolder(pdfDir, 58, 57);
	}

}

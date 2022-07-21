package com.beastshop.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AmazonS3UtilTests {
	
	@Test
	public void testListFolder() {
		String folderName ="product-images/18";
		List<String> listKeys = AmazonS3Util.listFolder(folderName);
		listKeys.forEach(System.out::println);
	}
	
	//we test uploading file to amazon server
	@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test-upload/one/two/three/four";
		String fileName="afrojack.jpg";
		String filePath = "C:\\AmazonS3\\"+fileName;
		InputStream inputStream = new FileInputStream(filePath);
		
		AmazonS3Util.uploadFile(folderName, fileName, inputStream);
	}
	
	@Test
	public void testDeleteFile() {
		String fileName = "test-upload/one/two/three/four/afrojack.jpg";
		AmazonS3Util.deleteFile(fileName);
	}
	
	@Test
	public void testDeleteFolder() {
		String folderName = "test-upload";
		AmazonS3Util.removeFolder(folderName);
	}
}

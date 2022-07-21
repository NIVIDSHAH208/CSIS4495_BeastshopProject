package com.beastshop.admin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class AmazonS3Util {
	private static final String BUCKET_NAME;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Util.class);


	static {
		BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
	}

	// method to get the files by the folder name
	public static List<String> listFolder(String folderName) {
		S3Client client = S3Client.builder().build();

		// Specifying the request
		ListObjectsRequest listRequests = ListObjectsRequest.builder().bucket(BUCKET_NAME).prefix(folderName).build();

		// Building the response
		ListObjectsResponse response = client.listObjects(listRequests);

		// From this response we can get the content
		List<S3Object> contents = response.contents();

		ListIterator<S3Object> listIterator = contents.listIterator();

		List<String> listKeys = new ArrayList<>();
		while (listIterator.hasNext()) {
			S3Object object = listIterator.next();
			listKeys.add(object.key());
		}

		return listKeys;
	}

	// method to upload the file(object)
	public static void uploadFile(String folderName, String fileName, InputStream inputStream) {
		S3Client client = S3Client.builder().build();

		// we use put object request
		PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET_NAME).key(folderName + "/" + fileName)
				.acl("public-read").build();
		
		try(inputStream){
			//available returns the bytes in the file(file size)
			int contentLength = inputStream.available();
			client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
		}catch (IOException ex) {
			LOGGER.error("Could not upload file to Amazon S3", ex);
		}
	}
	
	
	//method to delete file(object)
	public static void deleteFile(String fileName) {
		S3Client client = S3Client.builder().build();
		//Delete request
		DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(fileName).build();
		client.deleteObject(request);
	}
	
	//method to remove the entire folder
	public static void removeFolder(String folderName) {
		S3Client client = S3Client.builder().build();

		// Specifying the request
		ListObjectsRequest listRequests = ListObjectsRequest.builder().bucket(BUCKET_NAME).prefix(folderName).build();

		// Building the response
		ListObjectsResponse response = client.listObjects(listRequests);

		// From this response we can get the content
		List<S3Object> contents = response.contents();

		ListIterator<S3Object> listIterator = contents.listIterator();

		while (listIterator.hasNext()) {
			S3Object object = listIterator.next();
			DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(object.key()).build();
			client.deleteObject(request);
		}
	}
}

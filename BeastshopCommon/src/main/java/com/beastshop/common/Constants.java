package com.beastshop.common;

public class Constants {
	public static final String S3_BASE_URI;
	
	//get bucket name from the environment variable
	static {
		String bucketName = System.getenv("AWS_BUCKET_NAME");
		String region = System.getenv("AWS_REGION");
		String pattern = "https:///%s.s3.%s.amazonaws.com";
		
		//Get the buckets and then format it as a link
		String uri = String.format(pattern, bucketName, region);
		
		S3_BASE_URI = bucketName==null?"":uri;
	}
	
//	public static void main(String[] args) {
//		//sample printing
//		System.out.println("S3 Base URI: "+ S3_BASE_URI);
//	}
}

package com.qat.helper.files.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FTPTestFileProvider {

	private FTPDownloader ftpDownloader;

	public FTPTestFileProvider() throws IOException {
		ftpDownloader = new FTPDownloader();
	}

	public String getAbsoluteFilePath(String filename) throws FileNotFoundException {

		File localFile;
		try {
			localFile = ftpDownloader.downloadFile(filename);
		} catch (IOException e) {
			throw new FileNotFoundException("Could not find file " + filename);
		}
		return localFile.getAbsolutePath();
	}

	public String downloadFolder(String folderName) {
		return ftpDownloader.downloadDir(folderName);
	}
	
	public String getRandomFileFromFolder(String folderName) throws IOException {
		String randomFile = ftpDownloader.getRandomFileFromRemoteFolder(folderName);
		return getAbsoluteFilePath(folderName+randomFile);
	}

}
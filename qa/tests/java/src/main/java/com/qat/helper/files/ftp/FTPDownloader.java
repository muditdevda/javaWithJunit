package com.dfs.helper.files.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPDownloader {
	private static final char FTP_SEPARATOR = '/';
	private static Logger logger = LoggerFactory.getLogger(FTPDownloader.class);

	private static final String SERVER_ADDRESS = "mtjenkins.global.sdl.corp";
	private static final String USERNAME = "Anonymous";
	private static final String REMOTE_ROOT_DIR = "/Testing/files/Automation/";
	private static final String LOCAL_DIR = System.getProperty("java.io.tmpdir")+File.separator + "Testing"+File.separator+"Files"+File.separator+"Automation"+File.separator;

	private FTPClient ftpClient;

	public FTPDownloader() throws IOException {
		ftpClient = new FTPClient();

		ftpClient.connect(SERVER_ADDRESS);
		ftpClient.login(USERNAME, "");

		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	}

	public File downloadFile(String filename) throws IOException {
		File localfile = new File(LOCAL_DIR + filename);
		downloadSingleFile(REMOTE_ROOT_DIR + filename, localfile.getAbsolutePath());
		return localfile;
	}

	public String downloadDir(String dir) {
		final String localDir = LOCAL_DIR + dir;
		final String remoteDir = REMOTE_ROOT_DIR + dir.replace("\\", "/");
		logger.info("Downloading from {} to {}", remoteDir, localDir);
		try {
			downloadDirectory(remoteDir, "", localDir);
		} catch (IOException e) {
			logger.error("Exception while downloading dir {}", dir, e);
		}
		return localDir;
	}

	public String getRandomFileFromRemoteFolder(String folder) throws IOException {
		FTPFile[] subFiles =  ftpClient.listFiles(REMOTE_ROOT_DIR + folder);
		return subFiles[new Random().nextInt(subFiles.length)].getName();
	}
	
	private void downloadDirectory(String parentDir, String currentDir, String saveDir) throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}

		logger.info("Listing folder {}", dirToList);
		FTPFile[] subFiles = ftpClient.listFiles(dirToList);

		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					continue;
				}
				String filePath = parentDir + FTP_SEPARATOR + currentDir + FTP_SEPARATOR + currentFileName;
				if (currentDir.equals("")) {
					filePath = parentDir + FTP_SEPARATOR + currentFileName;
				}

				String newDirPath = saveDir + parentDir + File.separator + currentDir
						+ File.separator + currentFileName;
				if (currentDir.equals("")) {
					newDirPath = saveDir + File.separator + currentFileName;
				}

				if (aFile.isDirectory()) {
					// create the directory in saveDir
					File newDir = new File(newDirPath);
					boolean created = newDir.mkdirs();
					logger.info("{} dir created? {}", newDir, created);

					// download the sub directory
					downloadDirectory(dirToList, currentFileName, saveDir);
				} else {
					// download the file
					downloadSingleFile(filePath, newDirPath);
				}
			}
		}
		else {
			logger.warn("No files found on {} ", dirToList);
		}
	}

	private boolean downloadSingleFile(String remoteFilePath, String savePath) throws IOException {
		File destFile = new File(savePath);
		boolean downloaded = false;
		if (destFile.exists()) {
			logger.info("File {} already exists. Deleting... ", savePath);
			if(!destFile.delete()) {
				logger.warn("File not deleted");
			}
		}
		File parentDir = destFile.getParentFile();
		if (!parentDir.exists()) {
			boolean dirCreated = parentDir.mkdirs();
			logger.info("{} dir created? {}", parentDir, dirCreated);
		}

		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile))) {
			downloaded = ftpClient.retrieveFile(remoteFilePath, outputStream);
			if (downloaded) {
				logger.info("Downloaded file as {}", destFile);
			} else {
				logger.error("Could not download file {} as {}", remoteFilePath, destFile);
			}
		}
		catch(Exception e) {
			logger.info(e.getMessage());
		}

		return downloaded;
	}

}
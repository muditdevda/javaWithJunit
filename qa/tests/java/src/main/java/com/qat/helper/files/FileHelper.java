package com.qat.helper.files;

import com.qat.helper.properties.PropertiesHelper;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

public class FileHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
	private static final File DOWNLOADS_FOLDER = new File( new PropertiesHelper().getDownloadsPath());

	private FileHelper() {}

	public static List<File> getExportedFiles(String sfileName) {
		List<File> files = new ArrayList<>();
		LOGGER.info("Checking downloads at {}", DOWNLOADS_FOLDER);
		LOGGER.info ("Looking for file {}", sfileName);
		try (Stream<Path> filesStream = Files.walk(Paths.get(DOWNLOADS_FOLDER.getAbsolutePath()))) {
			filesStream.filter(Files::isRegularFile)
					.forEach(fileS -> LOGGER.info(String.valueOf(fileS)));
		} catch (IOException e) {
			LOGGER.error(e.toString());
		}

		waitUntilDownloadComplete();

		Collections.addAll(files, DOWNLOADS_FOLDER.listFiles((dir, name) -> name.startsWith(sfileName)));
		return files;
	}

	// wait until there in no tem files -> download completes.
	private static void waitUntilDownloadComplete() {
		await().atMost(60, SECONDS).ignoreExceptions().until(() ->
				Objects.requireNonNull(DOWNLOADS_FOLDER.listFiles((directory, fileName) -> {
			// this is for chrome, get temp names for different browsers if needed
			return fileName.endsWith("download");
		})).length == 0, is(true));
	}

	

	//	get list of downloaded files
	public static List<File> getDownloadFiles(String fileName) {
		LOGGER.info("************ getDownloadFiles ************");
		return getExportedFiles(fileName);
	}

	public static List<File> getDownloadFiles(List<String> filesName) {
		List<File> files = new ArrayList<>();
		for (String subsFilename: filesName){
			//LOGGER.info(FileHelper.getExportedFiles(subsFilename).toString());
			files.addAll(FileHelper.getExportedFiles(subsFilename));
		}
		return files;
	}

	public static File findExportFile(String fileName) {
		LOGGER.info("************ findExportFile ************");

		await().atMost(30, SECONDS).until(() -> getDownloadFiles(fileName).size(), is(1));
		File exportFile = getDownloadFiles(fileName).get(0);
		LOGGER.info("Export File found: {}" , exportFile);
		return exportFile;
	}

	public static void removeFileFromDownloadsFolder(String fileName)  {
		//Delete file from download folder
		LOGGER.info("************ removeFileFromDownloadsFolder ************");

		List<File> files = getDownloadFiles(fileName);

		for (File file : files) {
			LOGGER.info("Deleting {} ", file.getPath());
			if (file.delete()) {
				LOGGER.info("File deleted successfully");
			}
		}
	}
	public static void removeDownloadFiles(List<File> files) {
		//Delete file from download folder
		LOGGER.info("************ removeDownloadFiles ************");

		for (File file : files) {
			LOGGER.info("Deleting {} ", file.getPath());
			if (file.delete()) {
				LOGGER.info("File deleted successfully");
			}
			else{
				LOGGER.warn("File was not deleted");
			}
		}
	}

	@Step("Reading file names of a ZIP")
	public static List<String> getAllZipFilenames(File pathZipFile) throws IOException {
		List<String> zipFilesList = new ArrayList<>();

		try (ZipFile zipFile = new ZipFile(pathZipFile)) {
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			LOGGER.info("Getting all filenames of a zip");
			while (zipEntries.hasMoreElements()) {
				String fileName = zipEntries.nextElement().getName();
				LOGGER.info("File {} found", fileName);
				zipFilesList.add(fileName);
			}
		} catch (Exception ex){
			LOGGER.error("Error while opening the ZIP {}", pathZipFile);
		}
		return zipFilesList;
	}


	@Step("Converting file to base 64")
	public static String convertFileToBase64(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		LOGGER.info("The file to convert is on: " + path);
		byte[] data = Base64.getEncoder().encode(Files.readAllBytes(path));
		String dataForDebug = new String(data, "UTF-8");
		return dataForDebug;
	}

	public static class FileOnlyFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			File full = new File(dir, name);
			return full.isFile();
		}
	}
}

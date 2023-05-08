package com.qat.helper.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.qat.ssl.FlakySSLConfigurator;

public class ImageComparator {

	private ImageComparator() {
	}

	public static boolean checkImages(String webImage, String localImage) throws IOException {
		boolean equal = false;
		FlakySSLConfigurator.install(); //why this what this

		try (InputStream img1 = new URL(webImage).openStream(); InputStream img2 = new FileInputStream(localImage)) {
			equal = IOUtils.contentEquals(img1, img2);
		}
		return equal;
	}

}
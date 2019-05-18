package org.dimensinfin.eveonline.neocom.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidFileSystemAdapter implements IFileSystem {
	private static Logger logger = LoggerFactory.getLogger(AndroidFileSystemAdapter.class);

	private String applicationFolder = "NeoCom";
	private Context applicationContext;

	// - C O N S T R U C T O R
	private AndroidFileSystemAdapter( final String applicationStoreDirectory, final Context applicationContext ) {
		logger.info(">< [AndroidFileSystemAdapter.constructor]> applicationStoreDirectory: {}", applicationStoreDirectory);
		this.applicationFolder = applicationStoreDirectory;
		this.applicationContext = applicationContext;
	}

	// - I F I L E S Y S T E M   I N T E R F A C E
	@Override
	public InputStream openResource4Input( final String filePath ) throws IOException {
		return new FileInputStream(new File(
				Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + applicationFolder + "/" + filePath)
		);
	}

	@Override
	public OutputStream openResource4Output( final String filePath ) throws IOException {
		return new FileOutputStream(new File(
				Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + applicationFolder + "/" + filePath)
		);
	}

	@Override
	public InputStream openAsset4Input( final String filePath ) throws IOException {
		return this.applicationContext.getAssets().open(filePath);
	}

	/**
	 * The is no direct access to the root path for the assets. The path is just the relative path from the <b>assets</b> position. So
	 * there is not transformation for the input path.
	 *
	 * @param filePath filepath for the asset to get the whole asset path.
	 */
	@Override
	public String accessAsset4Path( final String filePath ) throws IOException {
		return filePath;
	}

	@Override
	public String accessResource4Path( final String filePath ) {
		if (null != filePath)
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + applicationFolder + "/" + filePath;
		else
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + applicationFolder;
	}

	// - B U I L D E R
	public static class Builder {
		private AndroidFileSystemAdapter onConstruction;

		public Builder( final String applicationStoreDirectory, final Context applicationContext ) {
			this.onConstruction = new AndroidFileSystemAdapter(applicationStoreDirectory, applicationContext);
		}

		public AndroidFileSystemAdapter build() {
			return this.onConstruction;
		}
	}

}

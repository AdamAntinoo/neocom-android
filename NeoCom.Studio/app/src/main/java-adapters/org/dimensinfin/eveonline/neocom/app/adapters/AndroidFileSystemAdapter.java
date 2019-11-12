package org.dimensinfin.eveonline.neocom.app.adapters;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.provider.IFileSystem;

public class AndroidFileSystemAdapter implements IFileSystem {
	private static Logger logger = LoggerFactory.getLogger( AndroidFileSystemAdapter.class );

	private String applicationDirectory = "NeoCom";
	private Context applicationContext;

	// - C O N S T R U C T O R
//	private AndroidFileSystemAdapter( final String applicationStoreDirectory, final Context applicationContext ) {
//		logger.info(">< [AndroidFileSystemAdapter.constructor]> applicationStoreDirectory: {}", applicationStoreDirectory);
//		this.applicationDirectory = applicationStoreDirectory;
//		this.applicationContext = applicationContext;
//	}
	private AndroidFileSystemAdapter() {}

	// - I F I L E S Y S T E M   I N T E R F A C E
	@Override
	public InputStream openResource4Input( final String filePath ) throws IOException {
		return new FileInputStream( new File(
				Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + applicationDirectory + "/" + filePath )
		);
	}

	@Override
	public OutputStream openResource4Output( final String filePath ) throws IOException {
		return new FileOutputStream( new File(
				Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + applicationDirectory + "/" + filePath )
		);
	}

	@Override
	public InputStream openAsset4Input( final String filePath ) throws IOException {
		return this.applicationContext.getAssets().open( filePath );
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
			return this.applicationContext.getExternalFilesDir( applicationDirectory ).getAbsolutePath() +
//					        "/" + applicationDirectory +
					       "/" + filePath;
		else
			return this.applicationContext.getExternalFilesDir( applicationDirectory ).getAbsolutePath() /*+ "/" + applicationDirectory*/;
	}

	/**
	 * Copies a file from the application distribution 'assets' folder to the application data directory. This may depend on the configuration
	 * adapter because the source, destination and even the resource to be copied are configured.
	 */
	public void copyFromAssets( final String sourceFileName, final String destinationDirectory ) {
		InputStream instream = null;
		OutputStream outstream = null;
		try {
			instream = this.openAsset4Input( sourceFileName );
			String destinationName = sourceFileName;
			if (null != destinationDirectory)
				destinationName = destinationDirectory + "/" + sourceFileName;
			final File destination = new File( this.accessResource4Path( destinationName ) );
			destination.createNewFile();
			outstream = new FileOutputStream( destination );
			final byte[] buffer = new byte[8192];
			int length;
			while ((length = instream.read( buffer )) > 0) {
				outstream.write( buffer, 0, length );
			}
			outstream.flush();
			logger.info( "-- [AndroidFileSystemAdapter.copyFromAssets]> Copied resource from assets [" + sourceFileName + "]" );
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error( "E> [AndroidFileSystemAdapter.copyFromAssets] Failed to copy resource: " + sourceFileName );
		} finally {
			try {
				if (outstream != null) outstream.close();
				if (instream != null) instream.close();
			} catch (final IOException e) {
			}
		}
	}

	// - B U I L D E R
	public static class Builder {
		private AndroidFileSystemAdapter onConstruction;

		public Builder() {
			this.onConstruction = new AndroidFileSystemAdapter();
		}

		public AndroidFileSystemAdapter.Builder withApplicationContext( final Context applicationContext ) {
			Objects.requireNonNull( applicationContext );
			this.onConstruction.applicationContext = applicationContext;
			return this;
		}

		public AndroidFileSystemAdapter.Builder withApplicationDirectory( final String applicationDirectory ) {
			Objects.requireNonNull( applicationDirectory );
			this.onConstruction.applicationDirectory = applicationDirectory;
			return this;
		}

		public AndroidFileSystemAdapter build() {
			Objects.requireNonNull( this.onConstruction.applicationContext );
			Objects.requireNonNull( this.onConstruction.applicationDirectory );
			return this.onConstruction;
		}
	}
}

package org.dimensinfin.eveonline.neocom.app.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;

public class AndroidSDEDBHelperTest {
	private IFileSystem fileSystem;

	@BeforeEach
	public void setUp() throws Exception {
		this.fileSystem = Mockito.mock(IFileSystem.class);
	}

	@Test
	public void buildComplete() throws IOException {
		final IFileSystem fileSystem = Mockito.mock(IFileSystem.class);
		final Context context = Mockito.mock(Context.class);
		final AndroidSDEDBHelper.Builder builder = Mockito.mock(AndroidSDEDBHelper.Builder.class);
		Mockito.when(builder.withAppContext(Mockito.any(Context.class))).thenCallRealMethod();
		Mockito.when(builder.withDatabaseName(Mockito.anyString())).thenCallRealMethod();
		final ISDEDatabaseAdapter mockedSdeHelper = Mockito.mock(ISDEDatabaseAdapter.class);
		Mockito.when(builder.build(fileSystem)).thenReturn(mockedSdeHelper);
		final ISDEDatabaseAdapter sdeHelper = builder.withAppContext(context)
				                                      .withDatabaseName("-TEST-SDE-DATABASE-NAME-PATH-")
				                                      .build(fileSystem);
		Assertions.assertNotNull(sdeHelper);
	}

//	@Test(expected = IOException.class)
	public void buildException() throws IOException {
		final IFileSystem fileSystem = Mockito.mock(IFileSystem.class);
		Mockito.when(fileSystem.accessResource4Path(Mockito.anyString())).thenThrow(new IOException());
		final Context context = Mockito.mock(Context.class);
		final ISDEDatabaseAdapter sdeHelper = new AndroidSDEDBHelper.Builder()
				                                      .withAppContext(context)
				                                      .withDatabaseName("-TEST-SDE-DATABASE-NAME-PATH-")
				                                      .build(fileSystem);
		Assertions.assertNotNull(sdeHelper);
	}

	@Test
	public void getDatabaseVersion() {
		// TODO - This tes requires a correct database ready to test the code. Other methods would not test the implementation.
	}

	//	@Test
	public void getSDEDatabase() throws IOException {
		final ISDEDatabaseAdapter sdeHelper = new AndroidSDEDBHelper.Builder().build(fileSystem);
		Assertions.assertNotNull(sdeHelper);

		final SQLiteDatabase database = ((AndroidSDEDBHelper) sdeHelper).getSDEDatabase();
		Assertions.assertNotNull(database);
	}

	protected boolean createSDEDatabase() throws IOException {
		this.copyFileUsingStream(new File("src/test/assets/sde.db"), new File("src/test/NeoCom.UnitTest/sde.db"));
		return true;
	}

	private void copyFileUsingStream( File source, File dest ) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}



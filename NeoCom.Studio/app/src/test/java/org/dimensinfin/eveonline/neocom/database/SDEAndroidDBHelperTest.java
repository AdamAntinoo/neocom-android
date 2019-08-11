package org.dimensinfin.eveonline.neocom.database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import org.dimensinfin.eveonline.neocom.app.adapters.SDEAndroidDBHelper;
import org.junit.Assert;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Environment.class)
public class SDEAndroidDBHelperTest {
//	@Test
	public void build_complete() {
		final ISDEDatabaseAdapter sdeHelper = new SDEAndroidDBHelper.Builder().build();
		Assert.assertNotNull(sdeHelper);
	}

	/**
	 * To open the database successfully we should have the database file located on the test files. We should create it first.
	 */
//	@Test(expected = NullPointerException.class)
	public void openSDEDB_nodb() {
		PowerMockito.mockStatic(Environment.class);
		BDDMockito.given(Environment.getExternalStorageDirectory()).willReturn(new File("src/test/NeoCom.UnitTest"));
		final ISDEDatabaseAdapter sdeHelper = new SDEAndroidDBHelper.Builder().build();
		Assert.assertNotNull(sdeHelper);

//		final boolean open = ((SDEAndroidDBHelper) sdeHelper).openSDEDB();
//		Assert.assertTrue("Database should be valid.", open);
	}

//	@Test
	public void openSDEDB_dbok() throws IOException {
		PowerMockito.mockStatic(Environment.class);
		BDDMockito.given(Environment.getExternalStorageDirectory()).willReturn(new File("src/test/NeoCom.UnitTest"));
		final ISDEDatabaseAdapter sdeHelper = new SDEAndroidDBHelper.Builder().build();
		Assert.assertNotNull(sdeHelper);
		Assert.assertTrue(this.createSDEDatabase());

//		final boolean open = ((SDEAndroidDBHelper) sdeHelper).openSDEDB();
//		Assert.assertTrue("Database should be valid.", open);
	}

//	@Test
	public void getSDEDatabase() {
		final ISDEDatabaseAdapter sdeHelper = new SDEAndroidDBHelper.Builder().build();
		Assert.assertNotNull(sdeHelper);

		final SQLiteDatabase database = ((SDEAndroidDBHelper) sdeHelper).getSDEDatabase();
		Assert.assertNotNull(database);
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

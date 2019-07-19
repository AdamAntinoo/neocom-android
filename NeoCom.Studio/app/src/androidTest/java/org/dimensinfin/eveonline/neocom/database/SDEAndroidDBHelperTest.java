package org.dimensinfin.eveonline.neocom.database;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SDEAndroidDBHelperTest {
//	@Test
	public void openSDEDB_dbok() throws IOException {
//		PowerMockito.mockStatic(Environment.class);
//		BDDMockito.given(Environment.getExternalStorageDirectory()).willReturn(new File("src/test/NeoCom.UnitTest"));
		final ISDEDatabaseAdapter sdeHelper = new SDEAndroidDBHelper.Builder().build();
		Assert.assertNotNull(sdeHelper);
//		Assert.assertTrue(this.createSDEDatabase());

//		final boolean open = ((SDEAndroidDBHelper) sdeHelper).openSDEDB();
//		Assert.assertTrue("Database should be valid.", open);
	}
}

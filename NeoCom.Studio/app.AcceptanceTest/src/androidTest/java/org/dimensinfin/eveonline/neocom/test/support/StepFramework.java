package org.dimensinfin.eveonline.neocom.test.support;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.adapter.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;

public class StepFramework {
	protected static Logger logger = LoggerFactory.getLogger( StepFramework.class );
	protected NeoComWorld world;

	// -  C O M P O N E N T S
	protected Context applicationContext = NeoComComponentFactory.getSingleton().getApplication().getApplicationContext();
	protected IFileSystem fileSystemAdapter = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	protected IConfigurationProvider configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();

	public StepFramework( final NeoComWorld world ) {
		this.world = world;
	}

	public void initializeApplicationStorage() throws IOException {
		AcceptanceNeoComLogger.enter();
		final boolean createAppResult = this.createAppDir();
		Assert.assertTrue( createAppResult );
		final boolean createPublicAppResult = this.createPublicAppDir();
//		Assert.assertTrue( createPublicAppResult );
		final boolean createCacheResult = this.createCacheDirectories();
		Assert.assertTrue( createCacheResult );
		if (!this.sdeDatabaseExists()) {
			this.fileSystemAdapter.copyFromAssets(
					this.getResources().getString( R.string.sdedatabasefilename ), null );
		}
		AcceptanceNeoComLogger.exit();
	}

	// - C O M P O N E N T   I N I T I A L I Z A T I O N
	protected boolean createAppDir() {
		// Create file application directory if it doesn't exist
		final File sdCardDir = new File( this.applicationContext.getExternalFilesDir( null )
				, this.getResources().getString( R.string.appname ) );
		if (!sdCardDir.exists()) sdCardDir.mkdir();
		return sdCardDir.exists();
	}

	protected boolean createPublicAppDir() {
		// Create file application directory if it doesn't exist
		final File sdCardDir = new File( Environment.getExternalStorageDirectory(),
				this.getResources().getString( R.string.appname ) );
		if (!sdCardDir.exists()) sdCardDir.mkdir();

		final File cdir = new File( "/sdcard/NeoComGT" );
		final boolean result = cdir.mkdir();
		return sdCardDir.exists();
	}

	protected boolean createCacheDirectories() {
		File cachedir = new File( this.applicationContext.getExternalFilesDir( null )
				, this.getResources().getString( R.string.appname )
						  + "/" + this.configurationProvider.getResourceString( "P.cache.directory.path" ) );
		cachedir.mkdir();
		return cachedir.exists();
	}

	protected boolean sdeDatabaseExists() throws IOException {
		return new File( this.fileSystemAdapter.accessResource4Path( this.getResources().getString( R.string.sdedatabasefilename ) ) ).exists();
	}

	protected Resources getResources() {
		return NeoComComponentFactory.getSingleton().getApplication().getResources();
	}

	protected View searchFirstPanelByClass( final Class filterClass, final ViewGroup container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = container.getChildAt( i );
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) return target;
		}
		return null;
	}

	protected View searchFirstPanelByRenderClass( final String filterClassName, final ListView container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = container.getChildAt( i );
			final IAndroidController controller = (IAndroidController) target.getTag();
			final IRender render = controller.buildRender(
					this.world.getActiveActivity().getBaseContext() );
			if (render.getClass().getSimpleName().equals( filterClassName )) return target;
		}
		return null;
	}

	// ----

	protected View searchPanelByClass( final Class filterClass, final ListView container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = this.getViewByPosition( i, container );
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) return target;
		}
		return null;
	}

	protected View getViewByPosition( int position, ListView listView ) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition) {
			return listView.getAdapter().getView( position, listView.getChildAt( position ), listView );
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt( childIndex );
		}
	}


	protected List<View> accessPanelsOfClass( final Class filterClass, final ListView container ) {
		final List<View> panels = new ArrayList<>();
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = this.getViewByPosition( i, container );
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) panels.add( target );
		}
		return panels;
	}

	protected List<View> accessPanelsOfClass( final Class filterClass, final ViewGroup container ) {
		final List<View> panels = new ArrayList<>();
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = container.getChildAt( i );
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) panels.add( target );
		}
		return panels;
	}
}

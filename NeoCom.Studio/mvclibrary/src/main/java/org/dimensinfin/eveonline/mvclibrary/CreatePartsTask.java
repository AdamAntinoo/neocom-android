package org.dimensinfin.eveonline.mvclibrary;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.logging.Logger;

//- CLASS IMPLEMENTATION ...................................................................................
public class CreatePartsTask extends AsyncTask<Activity, Void, Void> {
	// - S T A T I C - S E C T I O N ..........................................................................
	protected static Logger logger					= Logger.getLogger("CreatePartsTask");

	// - F I E L D - S E C T I O N ............................................................................
	private Activity _activity = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public CreatePartsTask(final Activity targetactivity) {
		_activity = targetactivity;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/** The datasource is ready and the new hierarchy should be created from the current model. All the stages are executed at this time both the model contents update and the list of parts to be used on the ListView. First, the model is checked to be initialized and if not then it is created. Then the model is run from start to end to create all the visible elements and from this list then we create the full list of the parts with their right renders.<br> This is the task executed every time a datasource gets its model modified and hides all the update time from the main thread as it is recommended by Google. */
	@Override
	protected Void doInBackground(final Activity... arg0) {
		logger.info(">> [CreatePartsTask.doInBackground]");
		try {
			// Create the hierarchy structure to be used on the Adapter.
			_datasource.collaborate2Model();
			_datasource.createContentHierarchy();
		} catch (final RuntimeException rtex) {
			rtex.printStackTrace();
		}
		logger.info("<< [CreatePartsTask.doInBackground]");
		return null;
	}

	@Override
	protected void onPostExecute(final Void result) {
		logger.info(">> CreatePartsTask.onPostExecute");
		// Activate the display of the list to force a redraw. Stop user UI waiting.
		_adapter = new DataSourceAdapter(_activity, _datasource);
		_modelContainer.setAdapter(_adapter);

//		_progressLayout.setVisibility(android.view.View.GONE);
		_modelContainer.setVisibility(android.view.View.VISIBLE);
		_container.invalidate();

//		// Add the header parts once the display is initialized.
//		ArrayList<AbstractAndroidPart> headerContents = _datasource.getHeaderParts();
//		if (headerContents.size() > 0) {
//			_headerContainer.removeAllViews();
//			_headerContainer.invalidate();
//			for (final AbstractAndroidPart part : headerContents) {
//				_activity.addViewtoHeader(part);
//			}
//		}
		super.onPostExecute(result);
		logger.info("<< CreatePartsTask.onPostExecute");
	}
}

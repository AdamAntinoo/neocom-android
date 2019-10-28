package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.ui.CustomTypefaceSpan;

public class MenuActivatedActivity extends MVCMultiPageActivity {
	private static Menu activityMenu = null;

	@Override
	public boolean onCreateOptionsMenu( final Menu menu ) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.neocom_menu, menu);
		activityMenu = menu;

		// Change the style for the application menu. This changes the text font and the background.
		// - B A C K G R O U N D - By intercepting the creation of the menu view.
		getLayoutInflater().setFactory(( String name, Context context, AttributeSet attrs ) -> {
			if (name.startsWith("com.android.internal.view.menu.ListMenuItemView")) {
				try {
					// Create the view and replace the desired attributes before releasing.
					final View view = getLayoutInflater().createView(name, null, attrs);
					new Handler().post(() -> {
						// Set the background drawable
						view.setBackgroundResource(R.drawable.blacktraslucent80);
					});
					return view;
				} catch (InflateException ie) {
					ie.printStackTrace();
				} catch (ClassNotFoundException cnfe) {
					cnfe.printStackTrace();
				}
			}
			return null;
		});
		// - F O N T - By replacing this property on each menu item found.
		for (int i = 0; i < menu.size(); i++) {
			MenuItem mi = menu.getItem(i);

			//for applying a font to subMenu ...
			SubMenu subMenu = mi.getSubMenu();
			if (subMenu != null && subMenu.size() > 0) {
				for (int j = 0; j < subMenu.size(); j++) {
					MenuItem subMenuItem = subMenu.getItem(j);
					applyFontToMenuItem(subMenuItem);
				}
			}
			applyFontToMenuItem(mi);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected( final MenuItem item ) {
		final int menuItemId = item.getItemId();
		if (menuItemId == R.id.action_settings) {
			final Intent intent = new Intent(this, SettingsActivity.class);
			this.startActivity(intent);
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void applyFontToMenuItem( MenuItem mi ) {
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Proxima Nova Thin.ttf");
		SpannableString mNewTitle = new SpannableString(mi.getTitle());
		mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mi.setTitle(mNewTitle);
	}
}

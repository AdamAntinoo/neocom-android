package org.dimensinfin.eveonline.neocom.app.ui;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.annimon.stream.Optional;

import org.dimensinfin.android.mvc.core.MVCScheduler;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetStatusOk;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;

public class SplashActionBar implements ICollaboration {
	private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";
	private static final String DEFAULT_ESI_SERVER = "tranquility".toUpperCase();

	private GetStatusOk serverData;
	// - C O M P O N E N T S
	private SharedPreferences preferences;
	private ESIDataProvider esiDataProvider;

	private SplashActionBar() {}

	private void downloadData() {
		MVCScheduler.backgroundExecutor.submit( () -> {
			this.serverData = this.esiDataProvider.getUniverseStatus(
					this.preferences.getString( DATASOURCE_PREFERENCE, DEFAULT_ESI_SERVER )
			);
		} );
	}

	public String getServerName() {
		return this.preferences.getString( DATASOURCE_PREFERENCE, DEFAULT_ESI_SERVER );
	}

	public String getServerStatus() {
		return "Online".toUpperCase();
	}

	public Optional<Integer> getPlayers() {
		if (null != this.serverData)
			return Optional.of( this.serverData.getPlayers() );
		else return Optional.empty();
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	// - B U I L D E R
	public static class Builder {
		private SplashActionBar onConstruction;

		public Builder() {
			this.onConstruction = new SplashActionBar();
		}

		public SplashActionBar.Builder withEsiDataProvider( final ESIDataProvider esiDataProvider ) {
			Objects.requireNonNull( esiDataProvider );
			this.onConstruction.esiDataProvider = esiDataProvider;
			return this;
		}

		public Builder withPreferences( final SharedPreferences preferences ) {
			this.onConstruction.preferences = preferences;
			return this;
		}

		public SplashActionBar build() {
			Objects.requireNonNull( this.onConstruction.esiDataProvider );
			this.onConstruction.downloadData();
			return this.onConstruction;
		}
	}
}

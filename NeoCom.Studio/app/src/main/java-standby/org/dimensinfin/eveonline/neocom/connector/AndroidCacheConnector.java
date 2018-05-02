//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2015 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.connector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;

import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.ModelWideConstants;
import org.dimensinfin.eveonline.neocom.core.ComparatorFactory;
import org.dimensinfin.eveonline.neocom.core.SimpleDiskCache;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.enums.EComparatorField;
import org.dimensinfin.eveonline.neocom.enums.EMarketSide;
import org.dimensinfin.eveonline.neocom.enums.ERequestClass;
import org.dimensinfin.eveonline.neocom.enums.ERequestState;
import org.dimensinfin.eveonline.neocom.market.MarketDataSet;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.services.PendingRequestEntry;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class AndroidCacheConnector implements ICacheConnector {
	private class DrawableCache extends Statisticscache {
		// - F I E L D - S E C T I O N ............................................................................
		private SimpleDiskCache cacheDrawables		= null;
		private volatile int					loads							= 0;
		private final CompressFormat	mCompressFormat		= CompressFormat.PNG;
		private final int							mCompressQuality	= 90;

		public DrawableCache() {
			final File cacheFolder = new File(NeoComAppConnector.getSingleton().getStorageConnector().getCacheStorage(),
					NeoComAppConnector.getSingleton().getResourceString(R.string.drawablecachefoldername));
			try {
				cacheDrawables = SimpleDiskCache.open(cacheFolder, 1, 100 * 1024 * 1024);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public synchronized void add(final String key, final Drawable target) {
			AndroidCacheConnector.logger.info("-- Storing Drawable instance [" + key + "] ");
			// cacheDrawables.put(key, target);
			this.writeDrawableToCache(key, target);
		}

		public synchronized Drawable getByURL(final String url) {
			final String hash = new Integer(Math.abs(new Integer(url.hashCode()))).toString();
			try {
				final SimpleDiskCache.BitmapEntry bit = cacheDrawables.getBitmap(hash);
				if (null == bit) return null;
				final Drawable draw = new BitmapDrawable(
						NeoComAppConnector.getSingleton().getApplicationContext().getResources(), bit.getBitmap());
				return draw;
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		public void loads() {
			loads++;
		}

		@Override
		public String toString() {
			return super.toString();
		}

		private void writeDrawableToCache(final String urlname, final Drawable data) {
			AndroidCacheConnector.logger.info(">> NewCacheStorage.writeDrawableToDisk");
			OutputStream out = null;
			try {
				// Create a valid hask key from the resource URL
				final String hash = new Integer(Math.abs(new Integer(urlname.hashCode()))).toString();
				if (data instanceof BitmapDrawable) {
					final Bitmap bit = ((BitmapDrawable) data).getBitmap();
					out = cacheDrawables.openStream(hash, null);
					bit.compress(mCompressFormat, mCompressQuality, out);
				}
				AndroidCacheConnector.logger.info("<< NewCacheStorage.writeDrawableToDisk [true]"); //$NON-NLS-1$
				return;
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					out.flush();
					out.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			AndroidCacheConnector.logger.info("<< NewCacheStorage.writeDrawableToDisk [false]"); //$NON-NLS-1$
		}
	}

	private class DrawableDownloaderTask extends AsyncTask<String, Void, Drawable> {
		// - F I E L D - S E C T I O N ............................................................................
		private ImageView	targetImage	= null;
		private Drawable	source			= null;

		// - M E T H O D - S E C T I O N  ..........................................................................
		public void setImageTarget(final ImageView target) {
			// TODO Auto-generated method stub
			targetImage = target;
		}

		@Override
		protected Drawable doInBackground(final String... reference) {
			InputStream is = null;
			URLConnection urlConn = null;
			try {
				urlConn = new URL(reference[0]).openConnection();
				is = urlConn.getInputStream();
				source = Drawable.createFromStream(is, "src");
				NeoComAppConnector.getSingleton().addDrawableToCache(reference[0], source);
				return source;
			} catch (final Exception ex) {
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (final IOException e) {
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Drawable result) {
			// Invalidate the view to force a refresh.
			if (null != result) if (null != targetImage) {
				targetImage.setImageDrawable(source);
				targetImage.invalidate();
				super.onPostExecute(result);
			}
		}
	}

	/**
	 * Stores the cache data for all the items accessed. A put of another item into the cache will add its
	 * reference to both lists, the one accessed by ID and the one accessed by Name.<br>
	 * The class also keeps track of the access statistics counting the hits, misses and faults. For the later
	 * the class stores the references that where not found for later reporting.
	 */
	private class EveItemCache extends Statisticscache {

		// - F I E L D - S E C T I O N
		// ............................................................................
		// private volatile int access = 0;
		// private volatile int hit = 0;
		// private volatile int miss = 0;
		// private volatile int fault = 0;
		private final HashMap<Integer, EveItem>	eveItemCachebyID		= new HashMap<Integer, EveItem>();
		private final HashMap<String, EveItem>	eveItemCachebyName	= new HashMap<String, EveItem>();
		private final Vector<String>						itemFaults					= new Vector<String>();

		// - M E T H O D - S E C T I O N
		// ..........................................................................
		// public synchronized void access() {
		// access++;
		// hit++;
		// }

		public synchronized void add(final EveItem item) {
			final int id = item.getItemID();
			final String name = item.getName();
			AndroidCacheConnector.logger.info("-- Storing EveItem instance [" + id + "] " + name);
			eveItemCachebyID.put(id, item);
			eveItemCachebyName.put(name, item);
		}

		// public synchronized void fault(final String reference) {
		// fault++;
		// hit--;
		// itemFaults.add(reference);
		// }

		public synchronized EveItem getByID(final int typeID) {
			return eveItemCachebyID.get(typeID);
		}

		public synchronized EveItem getByName(final String name) {
			if (null != name)
				return eveItemCachebyName.get(name);
			else
				return null;
		}

		//
		// public synchronized void miss() {
		// miss++;
		// hit--;
		// }

		@Override
		public String toString() {
			// StringBuffer buffer = new
			// StringBuffer("NewCacheStorage.EveItemCache [");
			// buffer.append("access=").append(access).append("
			// hits=").append(hit).append(" miss=").append(miss);
			// buffer.append(" faults=").append(fault);
			// if (fault > 0) buffer.append("\n").append("Fault Refs
			// [").append(itemFaults).append("]");
			// buffer.append(" ]");
			return super.toString();
		}
	}

	private abstract class Statisticscache {
		// - F I E L D - S E C T I O N
		// ............................................................................
		private volatile int					access			= 0;
		private volatile int					hit					= 0;
		private volatile int					miss				= 0;
		private volatile int					fault				= 0;
		private final Vector<String>	itemFaults	= new Vector<String>();

		// - M E T H O D - S E C T I O N
		// ..........................................................................
		public synchronized void access() {
			access++;
			hit++;
		}

		public synchronized void fault(final String reference) {
			fault++;
			hit--;
			itemFaults.add(reference);
		}

		public synchronized void miss() {
			miss++;
			hit--;
		}

		@Override
		public String toString() {
			final StringBuffer buffer = new StringBuffer("NewCacheStorage.Statisticscache [");
			buffer.append("access=").append(access).append(" hits=").append(hit).append(" miss=").append(miss);
			buffer.append(" faults=").append(fault);
			if (fault > 0) {
				buffer.append("\n").append("Fault Refs [").append(itemFaults).append("]");
			}
			buffer.append(" ]");
			return buffer.toString();
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger											logger							= Logger.getLogger("AndroidCacheConnector");

	// - F I E L D - S E C T I O N ............................................................................
	//	private Context														_context					= null;
	private final EveItemCache								_eveItemCache				= new EveItemCache();
	private DrawableCache											_cacheDrawables			= null;
	private Vector<PendingRequestEntry>				_pendingRequests		= new Vector<PendingRequestEntry>();
	private final SparseArray<MarketDataSet>	buyMarketDataCache	= new SparseArray<MarketDataSet>();
	private final SparseArray<MarketDataSet>	sellMarketDataCache	= new SparseArray<MarketDataSet>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AndroidCacheConnector() {
		super();
	}
	public AndroidCacheConnector(final Context app) {
		this();
		//		_context = app;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Add a new request to download and update the database of locations. This is an special request that it is
	 * initialized at startup and probably in some other conditions like when the information is stale and needs
	 * to be updated.
	 */
	@Override
	public void addCharacterUpdateRequest(final long localizer) {
		AndroidCacheConnector.logger.info(">> [AndroidCacheConnector.addCharacterUpdateRequest]");
		final PendingRequestEntry request = new PendingRequestEntry(localizer);
		request.setClass(ERequestClass.CHARACTERUPDATE);
		request.setPriority(20);
		if (this.addNoDuplicate(request)) {
			this.incrementTopCounter();
		}
		AndroidCacheConnector.logger.info("<< [AndroidCacheConnector.addCharacterUpdateRequest]");
	}

	@TargetApi(12)
	public void addDrawableToCache(final String key, final Drawable image) {
		this.getCache().add(key, image);
		this.getCache().loads();
	}

	/**
	 * Adds a new request to download character information. The information to download depends on the timing
	 * for the different blocks of data. Each block has a different refresh time. When a blocks finishes it may
	 * trigger the update of another block.
	 */
	public void addLocationUpdateRequest(final ERequestClass locationClass) {
		AndroidCacheConnector.logger.info(">> [AndroidCacheConnector.addLocationUpdateRequest]");
		final PendingRequestEntry request = new PendingRequestEntry(locationClass.name().hashCode());
		request.setClass(locationClass);
		request.setPriority(30);
		if (this.addNoDuplicate(request)) {
			this.incrementTopCounter();
		}
		AndroidCacheConnector.logger.info("<< [AndroidCacheConnector.addLocationUpdateRequest]");
	}

	/**
	 * Queues a new request to download Market Data for an Item. We only register the ID of the item because the
	 * side will not be used. On the download phase we will download both sides. Using the ID as key will avoid
	 * requesting the same item multiple times. <br>
	 *
	 * @param localizer
	 *          identifier of the item related to the data to download.
	 */
	@Override
	public void addMarketDataRequest(final long localizer) {
		AndroidCacheConnector.logger.info(">> [CoreCacheConnector.addMarketDataRequest]>Localizer: " + localizer);
		final EveItem item = GlobalDataManager.searchItem4Id(Long.valueOf(localizer).intValue());
		AndroidCacheConnector.logger
				.info("-- [AndroidCacheConnector.addMarketDataRequest] Posting market update for: " + item.getName());
		// Detect priority from the Category of the item. Download data from Asteroids and Minerals first.
		final String category = item.getCategory();
		final String group = item.getGroupName();
		int priority = 30;
		if (category.equalsIgnoreCase("Material")) {
			priority = 5;
		}
		if (category.equalsIgnoreCase("Asteroid")) {
			priority = 6;
		}
		if (category.equalsIgnoreCase("Planetary Commodities")) {
			priority = 7;
		}
		if (category.equalsIgnoreCase("Planetary Resources")) {
			priority = 7;
		}
		if (group.equalsIgnoreCase("Datacores")) {
			priority = 8;
		}
		if (category.equalsIgnoreCase("Module")) {
			priority = 10;
		}
		final PendingRequestEntry request = new PendingRequestEntry(localizer);
		request.setPriority(priority);
		if (this.addNoDuplicate(request)) {
			this.incrementMarketCounter();
		}
	}

	@Override
	public synchronized void clearPendingRequest(final long localizer) {
		this.clearPendingRequest(Long.valueOf(localizer).toString());
	}

	@Override
	public synchronized void clearPendingRequest(final String localizer) {
		for (final PendingRequestEntry entry : _pendingRequests) {
			final String entryid = entry.getIdentifier();
			if (entryid.equalsIgnoreCase(localizer)) {
				entry.state = ERequestState.COMPLETED;
				// Update the right counter depending on the priority range.
				if (entry.getPriority() < 20) {
					this.decrementMarketCounter();
				} else {
					this.decrementTopCounter();
				}
			}
		}
	}

	@Override
	public int decrementMarketCounter() {
		NeoComApp.marketCounter--;
		if (NeoComApp.marketCounter < 0) {
			NeoComApp.marketCounter = 0;
		}
		final Activity activity = NeoComModelStore.getSingleton().getActivity();
		if (null != activity) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					NeoComApp.updateProgressSpinner();
				}
			});
		}
		return NeoComApp.marketCounter;
	}

	@Override
	public int decrementTopCounter() {
		NeoComApp.topCounter--;
		if (NeoComApp.topCounter < 0) {
			NeoComApp.topCounter = 0;
		}
		final Activity activity = NeoComModelStore.getSingleton().getActivity();
		if (null != activity) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					NeoComApp.updateProgressSpinner();
				}
			});
		}
		return NeoComApp.topCounter;
	}

	public synchronized Vector<PendingRequestEntry> getAndroidPendingRequests() {
		if (null == _pendingRequests) {
			_pendingRequests = new Vector<PendingRequestEntry>();
		}
		// Clean up all completed requests.
		final Vector<PendingRequestEntry> openRequests = new Vector<PendingRequestEntry>(_pendingRequests.size());
		for (final PendingRequestEntry entry : _pendingRequests)
			if (entry.state != ERequestState.COMPLETED) {
				openRequests.add(entry);
			}
		_pendingRequests = openRequests;
		Collections.sort(_pendingRequests, ComparatorFactory.createComparator(EComparatorField.REQUEST_PRIORITY));
		return _pendingRequests;
	}

	/**
	 * Gets a drawable by its URL. Most of the Eve icons can be reached though an URL and also this is valid for
	 * the pilot avatar. The process checks if the image is available at the cache. If the image is not there
	 * then it will try to locate it on the cache filesystem. If not found there then it will open a request to
	 * get it form the internet location once the network is available.
	 *
	 * @param urlString
	 *          the location of the resource. This is already developed by the caller and it is treated as a
	 *          black box name.
	 * @param target
	 *          the UI object where we have to write the drawable once we get it to replace the dummy image that
	 *          will be shown while we retrieve the not cache image. This will be kept on a list because there
	 *          may be more that one pending call for the same image resource.
	 * @return the cached image or a dummy is still not available.
	 */
	public synchronized Drawable getCacheDrawable(final String urlString, final ImageView target) {
		// Try to get a hit from the memory cache.
		Drawable hit = this.getCache().getByURL(urlString);
		if (null == hit) {
			synchronized (this) {
				hit = this.getCache().getByURL(urlString);
				if (null == hit) {
					try {
						// hit = readDrawableFromDisk(urlString);
						// if (null == hit) {
						// No luck. We have to download the data from the
						// network.
						this.postDrawableRequest(urlString, target);
						this.getCache().miss();
						// }
					} catch (final Exception rtex) {
						AndroidCacheConnector.logger.info("E> Exception reading cached data. " + rtex.getMessage());
						this.getCache().fault(urlString);
					}
				}
			}
		}
		this.getCache().access();
		if (null == hit) {
			hit = NeoComAppConnector.getSingleton().getApplicationContext().getResources()
					.getDrawable(R.drawable.defaulticonplaceholder);
		}
		return hit;
	}

	//	@Override
	//	public synchronized Vector<PendingRequestEntry> getPendingRequests() {
	//		if (null == _pendingRequests) {
	//			_pendingRequests = new Vector<PendingRequestEntry>();
	//		}
	//		// Clean up all completed requests.
	//		final Vector<PendingRequestEntry> openRequests = new Vector<PendingRequestEntry>(_pendingRequests.size());
	//		for (final PendingRequestEntry entry : _pendingRequests)
	//			if (entry.state != ERequestState.COMPLETED) {
	//				openRequests.add(entry);
	//			}
	//		_pendingRequests = openRequests;
	//		return _pendingRequests;
	//	}

	public File getCacheStorage() {
		return null;
	}

	@Override
	public PriorityBlockingQueue<PendingRequestEntry> getPendingRequests() {
		return new PriorityBlockingQueue<PendingRequestEntry>();
	}

	public String getURLForItem(final int typeID) {
		final String iconUrl = "http://image.eveonline.com/Type/" + typeID + "_64.png";
		return iconUrl;
	}

	// /**
	// * Searches inside the memory data structures for an already available
	// <code>EveItem</code> with a matching
	// * ID. If the object is found we return the single reference to the
	// persistent database information.<br>
	// * If the reference is not found we go to the database layer to search for
	// that object and then we keep a
	// * copy on memory.<br>
	// * Multiple access will require synchronization. The synch has to be
	// performed to the item level to allow
	// * other threads searching for different items to continue undisturbed.
	// Double locking will be implemented
	// * to avoid waiting threads to go again to database once the lock is
	// removed.<br>
	// * The locking will be set on the database access activity. Read
	// operations or update changes will not need
	// * to be synchronized.<br>
	// * There is a coupled method entry to search by Name instead of ID. The
	// cache data structures keep track of
	// * pointers from ID and Name to the same Item.
	// *
	// * @param typeID
	// * identifier of the item being searched.
	// * @return the persistent <code>EveItem</code> object with the id
	// requested from the
	// * <code>evedroid.db</code> or <code>null</code> if not found.
	// */
	// public EveItem searchItembyID(final int typeID) {
	// // Check if the item already on the cache.
	// EveItem hit = _eveItemCache.getByID(typeID);
	// if (null == hit) {
	// synchronized (this) {
	// hit = _eveItemCache.getByID(typeID);
	// if (null == hit) {
	// // Get for the data to the database.
	// try {
	// Dao<EveItem, String> dao = NeoComAppConnector.getSingleton().getDBConnector().getItemDAO();
	// hit = dao.queryForId(new Integer(typeID).toString());
	// _eveItemCache.add(hit);
	// _eveItemCache.miss();
	// } catch (final Exception ex) {
	// logger.warning("W> Item <" + typeID + "> not found.");
	// _eveItemCache.fault(new Integer(typeID).toString());
	// hit = new EveItem();
	// }
	// }
	// }
	// }
	// _eveItemCache.access();
	// return hit;
	// }

	public String getURLForStation(final int typeID) {
		final String iconUrl = "http://image.eveonline.com/Render/" + typeID + "_64.png";
		return iconUrl;
	}

	// /**
	// * This method does the same of the coupled <code>searchItembuID</code>
	// but instead searching for the
	// * <code>EveItem</code> by it's ID it will perform the search by the Item
	// Name.
	// *
	// * @param name
	// * @return
	// */
	// public EveItem searchItembyName(final String name) {
	// // Check if the item already on the cache.
	// EveItem hit = _eveItemCache.getByName(name);
	// if (null == hit) {
	// synchronized (this) {
	// hit = _eveItemCache.getByName(name);
	// if (null == hit) {
	// // Get for the data to the database.
	// try {
	// Dao<EveItem, String> itemDao =
	// NeoComAppConnector.getSingleton().getDBConnector().getItemDAO();
	// QueryBuilder<EveItem, String> queryBuilder = itemDao.queryBuilder();
	// Where<EveItem, String> where = queryBuilder.where();
	// where.eq("name", name);
	// PreparedQuery<EveItem> preparedQuery = queryBuilder.prepare();
	// List<EveItem> itemList = itemDao.query(preparedQuery);
	// if (itemList.size() < 2) {
	// _eveItemCache.fault(name);
	// hit = null;
	// } else {
	// hit = itemList.get(0);
	// _eveItemCache.miss();
	// }
	// } catch (java.sql.SQLException sqle) {
	// sqle.printStackTrace();
	// logger.warning("W> Item <" + name + "> not found.");
	// _eveItemCache.fault(name);
	// hit = null;
	// }
	// }
	// }
	// }
	// return hit;
	// }
	@Override
	public int incrementMarketCounter() {
		return NeoComApp.marketCounter++;
	}

	@Override
	public int incrementTopCounter() {
		return NeoComApp.topCounter++;
	}

	/**
	 * Search for this market data on the cache. <br>
	 * The cache used for the search depends on the side parameter received on the call. All default prices are
	 * references to the cost of the price to be spent to buy the item.<br>
	 * If not found on the memory cache then try to load from the serialized version stored on disk. This is an
	 * special implementation for SpringBoot applications that may run on a server so the cache disk location is
	 * implemented in a different way that on Android, indeed, because we can access the market data online we
	 * are not going to cache the data but get a fresh copy if not found on the cache.<br>
	 * If the data is not located on the case call the market data downloader and processor to get a new copy
	 * and store it on the cache.
	 *
	 * @param itemID
	 *          item id code of the item assigned to this market request.
	 * @param side
	 *          differentiates if we like to BUY or SELL the item.
	 * @return the cached data or an empty locator ready to receive downloaded data.
	 */
	/**
	 * Search for this data on the cache. <br>
	 * The cache used for the search depends on the side parameter received on the call. All default prices are
	 * references to the cost of the price to be spent to buy the item.<br>
	 * If not found on the memory cache then try to load from the serialized version stored on disk. In case
	 * this action also fails return an empty data structure with the minimum filed data information from the
	 * related item and fire the update request.
	 *
	 * @param itemID
	 *          item id code of the item assigned to this market request.
	 * @param side
	 *          differentiates if we like to BUY or SELL the item.
	 * @return the cached data or an empty locator ready to receive downloaded data.
	 */
	public MarketDataSet searchMarketData(final int itemID, final EMarketSide side) {
		// Log.i("EVEI","-- MarketUpdaterService.searchMarketDataByID. Searching
		// for Market Data: " + itemID + " - " + side);
		// Search on the cache. By default load the SELLER as If I am buying the
		// item.
		SparseArray<MarketDataSet> cache = sellMarketDataCache;
		if (side == EMarketSide.BUYER) {
			cache = buyMarketDataCache;
		}
		MarketDataSet entry = cache.get(itemID);
		if (null == entry) {
			// Try to get the data from disk.
			entry = NeoComAppConnector.getSingleton().getStorageConnector().readDiskMarketData(itemID, side);
			if (null == entry) {
				// Neither on disk. Make a request for download and return a
				// dummy placeholder.
				entry = new MarketDataSet(itemID, side);
				if (true) {
					NeoComAppConnector.getSingleton().getCacheConnector().addMarketDataRequest(itemID);
				}
			}
			cache.put(itemID, entry);
		} else {
			Log.i("EVEI", "-- MarketUpdaterService.searchMarketDataByID. Cache hit on memory.");
			// Check again the location. If is the default then request a new
			// update and remove it from the cache.
			long lid = entry.getBestMarket().getLocation().getID();
			if (lid < 0) {
				NeoComAppConnector.getSingleton().getCacheConnector().addMarketDataRequest(itemID);
				cache.put(itemID, null);
			}
		}
		// Check entry timestamp before return. Post an update if old.
		// if (side.equalsIgnoreCase(ModelWideConstants.marketSide.CALCULATE))
		// return entry;
		// else {
		if (NeoComAppConnector.getSingleton().checkExpiration(entry.getTS().getMillis(), ModelWideConstants.HOURS24))
			if (true) {
			NeoComAppConnector.getSingleton().getCacheConnector().addMarketDataRequest(itemID);
			}
		return entry;
		// }
	}

	/**
	 * Adds a new entry if not already found on the pending list.
	 *
	 * @param request
	 */
	private synchronized boolean addNoDuplicate(final PendingRequestEntry request) {
		// Check for duplicates before adding the new element.
		final String requestid = request.getIdentifier();
		for (final PendingRequestEntry entry : _pendingRequests) {
			final String entryid = entry.getIdentifier();
			if (entryid.equalsIgnoreCase(requestid)) return false;
		}
		_pendingRequests.add(request);
		return true;
	}

	// private void incrementMarketCounter() {
	// EVEDroidApp.marketCounter++;
	// Activity activity = EVEDroidApp.getApplication().getActivity();
	// if (null != activity) {
	// activity.runOnUiThread(new Runnable() {
	// public void run() {
	// EVEDroidApp.updateProgressSpinner();
	// }
	// });
	// }
	// }

	private DrawableCache getCache() {
		if (null == _cacheDrawables) {
			_cacheDrawables = new DrawableCache();
		}
		return _cacheDrawables;
	}

	/**
	 * Keeps track of the requests to download data that are pending and of all the images that are waiting to
	 * replace their content by the new downloaded content. The real download process is done on an asych task
	 * as the writing on disk of the downloaded data for later accesses.<br>
	 * The tasks will kept the request list updated with the downloaded data and theirs download states.
	 *
	 * @param urlString
	 *          url of the resource to access.
	 * @param target
	 *          UI image that is waiting for this drawable to replace the dummy we have set while we download
	 *          the data.
	 */
	private void postDrawableRequest(final String urlString, final ImageView target) {
		// TODO Check if already on pending list.
		// CacheEntry request = _pendingDrawableDownloads.get(urlString);

		// Launch a background task to get the image.
		final DrawableDownloaderTask task = new DrawableDownloaderTask();
		task.setImageTarget(target);
		task.execute(urlString);
	}

}
// - UNUSED CODE ............................................................................................

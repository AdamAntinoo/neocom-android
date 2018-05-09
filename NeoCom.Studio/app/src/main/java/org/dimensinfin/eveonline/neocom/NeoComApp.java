//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Android Application related to the Eve Online game. The purpose is to download and organize
//                  the game data to help capsuleers organize and prioritize activities. The strong points are
//                  help at the Industry level tracking and calculating costs and benefits. Also the market
//                  information update service will help to identify best prices and locations.
//                  Planetary Interaction and Ship fittings are point under development.
//                  ESI authorization is a new addition that will give continuity and allow download game data
//                  from the new CCP data services.
//                  This is the Android application version but shares libraries and code with other application
//                  designed for Spring Boot Angular 4 platform.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom;

import android.app.Application;
import android.os.Environment;

import org.dimensinfin.eveonline.neocom.conf.GlobalAndroidConfigurationProvider;
import org.dimensinfin.eveonline.neocom.database.NeoComAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.database.SDEAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.datamngmt.FileSystemAndroidImplementation;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerExceptions;
import org.dimensinfin.eveonline.neocom.model.ANeoComEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComApp extends Application /*implements INeoComAppConnector*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("NeoComApp");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	//	public NeoComApp () {
	//		NeoComApp.logger.info(">> [NeoComApp.<contructor>]");
	//		// Setup the referencing structures that will serve as proxy and global references.
	//		if ( null == NeoComApp.singleton ) {
	//			// Just connect the singleton and the Connector chain to get access to all the methods.
	//			NeoComApp.singleton = this;
	//			_appConnector = new NeoComAppConnector(this);
	//			_modelAppConnector = new ModelAppConnector(this);
	//		}
	//		NeoComApp.logger.info("<< [NeoComApp.<consructor>]");
	//	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * This Application lifecycle event is only called when the application is started or completely removed from memory.
	 * The important and key point of this code is to initialize the Global chain because if the app is not propagated
	 * to the Connectors the core calls may fail.
	 */
	@Override
	public void onCreate () {
		NeoComApp.logger.info(">> [NeoComApp.onCreate]");
		super.onCreate();

		// Connect the application to the global context to be able to use all the resources.
		logger.info("-- [NeoComApp.onCreate]> Connecting the Context to the Global...");
		GlobalAndroidDataManager.setApplicationContext(this);

		// Connect the file system to be able to read the assets and other application resources stored externally.
		logger.info("-- [NeoComApp.onCreate]> Connecting the File System to Global...");
		GlobalAndroidDataManager.installFileSystem(new FileSystemAndroidImplementation(
				GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appname)
		));

		// Connect the Configuration manager. Dothis after the file system because it reads configuration files.
		logger.info("-- [NeoComApp.onCreate]> Connecting the Configuration Manager...");
		GlobalDataManager.connectConfigurationManager(new GlobalAndroidConfigurationProvider("resources/properties"));

		// Initialize the Model with the current global instance.
		logger.info("-- [NeoComApp.onCreate]> Connecting Global to Model...");
		ANeoComEntity.connectGlobal(new GlobalAndroidDataManager());

		// Initializing the ESI api network controller.
		//		ESINetworkManager.initialize();

		// Check if the files and folders to run on Android are in place. If not create/copy them.
		createAppDir();
		createCacheDirectories();
		copyPreloadFiles();
		// Check if the SDE database file is at the right place. If not copy it.
		if ( !GlobalAndroidDataManager.checkAppFile(
				GlobalAndroidDataManager.getResourceString("R.database.sdedatabase.databasename")) ) {
			// TODO The CCP database is not distributed with the application. If not available then close the App
			this.copyFromAssets(GlobalAndroidDataManager.getResourceString("R.database.sdedatabase.databasename"));
		}

		// Connect the SDE database.
		logger.info("-- [NeoComApp.onCreate]> Connecting SDE database...");
		try {
			GlobalDataManager.connectSDEDBConnector(new SDEAndroidDBHelper()
					.setDatabaseSchema(GlobalDataManager.getResourceString("R.database.sdedatabase.databaseschema"))
					.setDatabasePath(GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appname) + "/")
					.setDatabaseName(GlobalDataManager.getResourceString("R.database.sdedatabase.databasename"))
					.build()
			);
		} catch (SQLException sqle) {
			GlobalAndroidDataManager.registerException("NeoComApp.onCreate", sqle, GlobalDataManagerExceptions.EExceptionSeverity.SEVERE);
			sqle.printStackTrace();
		}
		ANeoComEntity.connectSDEHelper(new GlobalDataManager().getSDEDBHelper());

		// Connect the NeoCom database.
		logger.info("-- [NeoComApp.onCreate]> Connecting NeoCom private database...");
		try {
			GlobalDataManager.connectNeoComDBConnector(new NeoComAndroidDBHelper.NeoComAndroidDBHelperBuilder()
					.setDatabaseLocation(GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appname))
					//					.setDatabaseHost(GlobalDataManager.getResourceString("R.database.neocom.databasehost"))
					.setDatabaseName(GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appdatabasefilename))
					//					.setDatabaseUser(GlobalDataManager.getResourceString("R.database.neocom.databaseuser"
					//							, "NEOCOM"))
					//					.setDatabasePassword(GlobalDataManager.getResourceString("R.database.neocom.databasepassword"))
					.setDatabaseVersion(
							Integer.valueOf(GlobalAndroidDataManager.getApplication().getResources().getString(
									R.string.appdatabaseversion)))
					.build()
			);
		} catch (SQLException sqle) {
			GlobalAndroidDataManager.registerException("NeoComApp.onCreate", sqle, GlobalDataManagerExceptions.EExceptionSeverity.SEVERE);
			sqle.printStackTrace();
		}

		//		// Connect the MarketData service.
		//		logger.info("-- [NeoComMicroServiceApplication.main]> Starting Market Data service...");
		//		mdServer = new MarketDataServer().start();
		//		GlobalDataManager.setMarketDataManager(mdServer);

		// Load the Locations cache to speed up the Citadel and Outpost search.
		logger.info("-- [NeoComApp.onCreate]> Read Locations data cache...");
		GlobalDataManager.readLocationsDataCache();

		//		// Connect the Timed Upgrade scan.
		//		logger.info("-- [NeoComMicroServiceApplication.main]> Connecting the background timed download scanner...");
		//		timedService = new TimedUpdater();

		//		logger.info("-- [NeoComMicroServiceApplication.main]> Starting application instance...");
		//		SpringApplication.run(NeoComMicroServiceApplication.class, args);
		//		logger.info("<< [NeoComApp.onCreate]");


		//		// Initialize global objects that are going to be used on main code.
		//		NeoComApp.daysFace = Typeface.createFromAsset(super.getApplicationContext().getAssets(), "fonts/Days.otf");
		//		// Set the initial activity as a return point in case of exceptions.
		//		_firstActivity = new AssetsManagerActivity();
		//
		//		// Setup the referencing structures that will serve as proxy and global references.
		//			NeoComApp.singleton = this;
		//			_appConnector = new NeoComAppConnector(this);
		//			_modelAppConnector = new ModelAppConnector(this);
		//
		//		// Close databases if they were left open.
		//		this.getDBConnector().closeDatabases();
		//		// Remove any reference to old adapters and helpers.
		//		storage = null;
		//		dbCCPConnector = null;
		//		dbNeocomConnector = null;
		//		cache = null;
		//
		//		// Initialize the Network with the base location for the cache.
		//		ESINetworkManager.retrofitApplicationParameter=getApplicationContext().getCacheDir();
		NeoComApp.logger.info("<< [NeoComApp.onCreate]");
	}

	/**
	 * Creates the application directory if it already does not exist on the sdcard.
	 */
	private boolean createAppDir () {
		// Create file application directory if it doesn't exist
		final File sdcarddir = new File(Environment.getExternalStorageDirectory()
				, GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appname));
		final boolean existence = sdcarddir.exists();
		if ( !sdcarddir.exists() ) {
			sdcarddir.mkdir();
		}
		return !existence;
	}

	private void createCacheDirectories () {
		// Create additional cache folders.
		File cachedir = new File(Environment.getExternalStorageDirectory()
				, GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appname)
				+ "/" + GlobalAndroidDataManager.getResourceString("R.cache.directorypath"));
		//				+ GlobalAndroidDataManager.getResourceString("R.cache.cachefoldername"));
		cachedir.mkdir();
		File alternatedir = new File(cachedir
				, GlobalAndroidDataManager.getApplication().getResources().getString(R.string.drawablecachefoldername));
		alternatedir.mkdir();
		//		alternatedir = new File(cachedir,
		//				NeoComAppConnector.getSingleton().getResourceString(R.string.marketdatacachefoldername));
		//		alternatedir.mkdir();
	}

	/**
	 * Copies the parameter files from the assets installation directory to the user application directory.
	 * Current contents are the static database and the initial API list if found.
	 */
	private void copyFromAssets (final String sourceFileName) {
		InputStream istream = null;
		OutputStream ostream = null;
		//		String resourceName = NeoComAppConnector.getSingleton().getResourceString(resourceId);
		//		final Context context = singleton.getApplicationContext();
		try {
			istream = GlobalAndroidDataManager.openAsset4Input(sourceFileName);
			final File destination = GlobalAndroidDataManager.accessAppStorageFile(sourceFileName);
			destination.createNewFile();
			ostream = new FileOutputStream(destination);
			final byte[] buffer = new byte[8192];
			int length;
			while ((length = istream.read(buffer)) > 0) {
				ostream.write(buffer, 0, length);
			}
			ostream.flush();
			logger.info("-- [NeoComApp.copyFromAssets]> Copied resource from assets [" + sourceFileName + "]");
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("E> [NeoComApp.copyFromAssets] Failed to copy resource: " + sourceFileName);
		} finally {
			try {
				if ( ostream != null ) {
					ostream.close();
				}
				if ( istream != null ) {
					istream.close();
				}
			} catch (final IOException e) {
			}
		}
	}

	public void copyPreloadFiles () {
		try {
			for (String name : GlobalAndroidDataManager.getApplication().getAssets().list("preloadcache")) {
				copyFromAssets("preloadcache/"+name);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	//	public File accessAppStorage (final String resourceString) {
	//		if ( null != resourceString )
	//			return new File(Environment.getExternalStorageDirectory()
	//					, GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appfoldername)
	//					+ "/" + resourceString);
	//		else
	//			return new File(Environment.getExternalStorageDirectory()
	//					, GlobalAndroidDataManager.getApplication().getResources().getString(R.string.appfoldername));
	//	}

	//	private static NeoComApp singleton = null;
	//	/** Background receiver to process pending data downloads. */
	//	private static BroadcastReceiver timeTickReceiver = null;
	//	/** Background pending tasks counters. */
	//	public static int topCounter = 0;
	//	public static int marketCounter = 0;
	//
	//	/** Global typeface to be used on nos standard activities or in places with no layout. */
	//	private static Typeface daysFace = null;
	//	private static DecimalFormat pendingCounter = new DecimalFormat("0.0##");

	//	private static boolean						firstTimeInitialization	= false;

	//	@Deprecated
	//	public static Comparator<ICollaboration> createComparator(final int code) {
	//		Comparator<ICollaboration> comparator = new Comparator<ICollaboration>() {
	//			public int compare(final ICollaboration left, final ICollaboration right) {
	//				return 0;
	//			}
	//		};
	//		switch (code) {
	//			case AppWideConstants.comparators.COMPARATOR_NAME:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						String leftField = null;
	//						String rightField = null;
	//						if (left instanceof INamedPart ) {
	//							leftField = ((INamedPart) left).getName();
	//						}
	//						if (right instanceof INamedPart) {
	//							rightField = ((INamedPart) right).getName();
	//						}
	//						//						if (left instanceof INamed) {
	//						//							leftField = ((INamed) left).getOrderingName();
	//						//						}
	//						//						if (right instanceof INamed) {
	//						//							rightField = ((INamed) right).getOrderingName();
	//						//						}
	//
	//						if (null == leftField) return 1;
	//						if (null == rightField) return -1;
	//						if ("" == leftField) return 1;
	//						if ("" == rightField) return -1;
	//						return leftField.compareTo(rightField);
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_ASSET_COUNT:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						long leftField = -1;
	//						long rightField = -1;
	//						if (left instanceof NeoComAsset) {
	//							final NeoComAsset intermediate = (NeoComAsset) left;
	//							leftField = intermediate.getQuantity();
	//						}
	//
	//						if (right instanceof NeoComAsset) {
	//							final NeoComAsset intermediate = (NeoComAsset) right;
	//							rightField = intermediate.getQuantity();
	//						}
	//						if (leftField < rightField) return 1;
	//						if (leftField > rightField) return -1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_ITEM_TYPE:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						int leftField = -1;
	//						int rightField = -1;
	//						// if (left instanceof BlueprintPart) leftField = 100;
	//						if (left instanceof AssetPart) {
	//							leftField = 0;
	//						}
	//						if (left instanceof ShipPart) {
	//							leftField = 200;
	//						}
	//						if (left instanceof ContainerPart) {
	//							leftField = -300;
	//						}
	//
	//						// if (right instanceof BlueprintPart) rightField = 100;
	//						if (right instanceof AssetPart) {
	//							rightField = 0;
	//						}
	//						if (right instanceof ShipPart) {
	//							rightField = 200;
	//						}
	//						if (right instanceof ContainerPart) {
	//							rightField = -300;
	//						}
	//
	//						if (leftField < rightField) return -1;
	//						if (leftField > rightField) return 1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_RESOURCE_TYPE:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						int leftField = -1;
	//						int rightField = -1;
	//						if (left instanceof ResourcePart) {
	//							final Resource resource = ((ResourcePart) left).getCastedModel();
	//							leftField = 0;
	//							if (resource.getCategory().equalsIgnoreCase("Material")) {
	//								leftField = -300;
	//							}
	//							if (resource.getCategory().equalsIgnoreCase("Module")) {
	//								leftField = -200;
	//							}
	//							if (resource.getCategory().equalsIgnoreCase("Blueprint")) {
	//								leftField = -100;
	//							}
	//							if (resource.getName().contains("Datacore")) {
	//								leftField = 100;
	//							}
	//						}
	//
	//						if (right instanceof ResourcePart) {
	//							final Resource resource = ((ResourcePart) left).getCastedModel();
	//							rightField = 0;
	//							if (resource.getCategory().equalsIgnoreCase("Material")) {
	//								rightField = -300;
	//							}
	//							if (resource.getCategory().equalsIgnoreCase("Module")) {
	//								rightField = -200;
	//							}
	//							if (resource.getCategory().equalsIgnoreCase("Blueprint")) {
	//								rightField = -100;
	//							}
	//							if (resource.getName().contains("Datacore")) {
	//								rightField = 100;
	//							}
	//						}
	//
	//						if (leftField < rightField) return -1;
	//						if (leftField > rightField) return 1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_APIID_ASC:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						long leftField = -1;
	//						long rightField = -1;
	//						if (left instanceof ApiKeyPart) {
	//							final NeoComApiKey intermediate = ((ApiKeyPart) left).getCastedModel();
	//							leftField = intermediate.getKey();
	//						}
	//
	//						if (right instanceof ApiKeyPart) {
	//							final NeoComApiKey intermediate = ((ApiKeyPart) right).getCastedModel();
	//							rightField = intermediate.getKey();
	//						}
	//						if (leftField < rightField) return -1;
	//						if (leftField > rightField) return 1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_APIID_DESC:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						long leftField = -1;
	//						long rightField = -1;
	//						if (left instanceof ApiKeyPart) {
	//							final NeoComApiKey intermediate = ((ApiKeyPart) left).getCastedModel();
	//							leftField = intermediate.getKey();
	//						}
	//
	//						if (right instanceof ApiKeyPart) {
	//							final NeoComApiKey intermediate = ((ApiKeyPart) right).getCastedModel();
	//							rightField = intermediate.getKey();
	//						}
	//						if (leftField > rightField) return -1;
	//						if (leftField < rightField) return 1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_REQUEST_PRIORITY:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						long leftField = -1;
	//						long rightField = -1;
	//						if (left instanceof PendingRequestEntry) {
	//							final PendingRequestEntry intermediate = (PendingRequestEntry) left;
	//							leftField = intermediate.getPriority();
	//						}
	//
	//						if (right instanceof PendingRequestEntry) {
	//							final PendingRequestEntry intermediate = (PendingRequestEntry) right;
	//							rightField = intermediate.getPriority();
	//						}
	//						if (leftField < rightField) return -1;
	//						if (leftField > rightField) return 1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_PRIORITY:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						int leftField = -1;
	//						int rightField = -1;
	//						if (left instanceof PendingRequestEntry) {
	//							final PendingRequestEntry intermediate = (PendingRequestEntry) left;
	//							leftField = intermediate.getPriority();
	//						}
	//
	//						if (right instanceof PendingRequestEntry) {
	//							final PendingRequestEntry intermediate = (PendingRequestEntry) right;
	//							rightField = intermediate.getPriority();
	//						}
	//						if (leftField < rightField) return 1;
	//						if (leftField > rightField) return -1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_WEIGHT:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						int leftField = -1;
	//						int rightField = -1;
	//						if (left instanceof IWeigthedNode) {
	//							leftField = ((IWeigthedNode) left).getWeight();
	//						}
	//						if (right instanceof IWeigthedNode) {
	//							rightField = ((IWeigthedNode) right).getWeight();
	//						}
	//						if (leftField < rightField) return -1;
	//						if (leftField > rightField) return 1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_TIMEPENDING:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						int leftField = -1;
	//						int rightField = -1;
	//						if (left instanceof JobQueue) {
	//							leftField = ((JobQueue) left).getTimeUsed();
	//						}
	//						if (right instanceof JobQueue) {
	//							rightField = ((JobQueue) right).getTimeUsed();
	//						}
	//
	//						if (leftField > rightField) return 1;
	//						if (leftField < rightField) return -1;
	//						return 0;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_CARD_RATIO:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						double leftField = 0.0;
	//						double rightField = 0.0;
	//						if (left instanceof BlueprintPart) {
	//							leftField = ((BlueprintPart) left).getProfitIndex();
	//						}
	//						if (right instanceof BlueprintPart) {
	//							rightField = ((BlueprintPart) right).getProfitIndex();
	//						}
	//
	//						if (leftField > rightField)
	//							return -1;
	//						else if (leftField == rightField) return 0;
	//						return 1;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_NEWESTDATESORT:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						DateTime leftField = new DateTime(DateTimeZone.UTC);
	//						DateTime rightField = new DateTime(DateTimeZone.UTC);
	//						if (left instanceof IDateTimeComparator) {
	//							leftField = ((IDateTimeComparator) left).getComparableDate();
	//						}
	//						if (right instanceof IDateTimeComparator) {
	//							rightField = ((IDateTimeComparator) right).getComparableDate();
	//						}
	//
	//						if (leftField.isAfter(rightField))
	//							return -1;
	//						else
	//							return 1;
	//					}
	//				};
	//				break;
	//			case AppWideConstants.comparators.COMPARATOR_OLDESTDATESORT:
	//				comparator = new Comparator<ICollaboration>() {
	//					public int compare(final ICollaboration left, final ICollaboration right) {
	//						DateTime leftField = new DateTime(DateTimeZone.UTC);
	//						DateTime rightField = new DateTime(DateTimeZone.UTC);
	//						if (left instanceof IDateTimeComparator) {
	//							leftField = ((IDateTimeComparator) left).getComparableDate();
	//						}
	//						if (right instanceof IDateTimeComparator) {
	//							rightField = ((IDateTimeComparator) right).getComparableDate();
	//						}
	//
	//						if (leftField.isAfter(rightField))
	//							return 1;
	//						else
	//							return -1;
	//					}
	//				};
	//				break;
	//		}
	//		return comparator;
	//	}

	//	public static NeoComApp getSingletonApp () {
	//		if ( null == NeoComApp.singleton ) {
	//			new NeoComApp();
	//		}
	//		return NeoComApp.singleton;
	//	}
	//
	//	@Deprecated
	//	public static Element parseDOMDocument (final InputStream stream) throws IOException, ParserConfigurationException {
	//		NeoComApp.logger.info(">> EVEDroidApp.downloadDOMDocument");
	//		Element elementSingleton = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
	//		                                                 .getDocumentElement();
	//		if ( null == stream ) return elementSingleton;
	//		try {
	//			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	//			final DocumentBuilder builder = factory.newDocumentBuilder();
	//			final Document doc = builder.parse(stream);
	//			elementSingleton = doc.getDocumentElement();
	//		} catch (final MalformedURLException me) {
	//			me.printStackTrace();
	//		} catch (final SAXException se) {
	//			se.printStackTrace();
	//		} catch (final ParserConfigurationException pce) {
	//			pce.printStackTrace();
	//		}
	//		return elementSingleton;
	//	}
	//
	//	@Deprecated
	//	public static void runTimer () {
	//		NeoComApp.timeTickReceiver.onReceive(NeoComApp.getSingletonApp().getApplicationContext(), null);
	//	}
	//
	//	//	@Deprecated
	//	//	public static void setFirstInitalization(final boolean state) {
	//	//		NeoComApp.firstTimeInitialization = state;
	//	//	}
	//	@Deprecated
	//	public static void updateProgressSpinner () {
	//		if ( (NeoComApp.marketCounter > 0) || (NeoComApp.topCounter > 0) ) {
	//			double divider = 10.0;
	//			if ( NeoComApp.topCounter > 10 ) {
	//				divider = 100.0;
	//			}
	//			NeoComApp.getSingletonApp().startProgress(
	//					new Double(NeoComApp.marketCounter + new Double(NeoComApp.topCounter / divider)));
	//		} else {
	//			NeoComApp.getSingletonApp().stopProgress();
	//		}
	//	}

	//	/**
	//	 * Connectors are stores just for reference. Theis instances are not required after creation because the link if from
	//	 * the code to their internal connector that it is initialized while it's construction.
	//	 */
	//	private NeoComAppConnector _appConnector = null;
	//	private ModelAppConnector _modelAppConnector = null;
	//	/**
	//	 * Reference to the startup activity of the application. This Activity gets restarted when there is an
	//	 * intercepted runtime exception.
	//	 */
	//	private Activity _firstActivity = null;
	//
	//	private Instant chrono = null;
	//	private AndroidStorageConnector storage = null;
	//	private AndroidCCPDatabaseConnector dbCCPConnector = null;
	//	private AndroidDatabaseConnector dbNeocomConnector = null;
	//	private AndroidCacheConnector cache = null;

	//private INeocomDBHelper									neocomDBHelper		= null;
	// - C O N S T R U C T O R - S E C T I O N ................................................................
	//	public NeoComApp () {
	//		NeoComApp.logger.info(">> [NeoComApp.<contructor>]");
	//		// Setup the referencing structures that will serve as proxy and global references.
	//		if ( null == NeoComApp.singleton ) {
	//			// Just connect the singleton and the Connector chain to get access to all the methods.
	//			NeoComApp.singleton = this;
	//			_appConnector = new NeoComAppConnector(this);
	//			_modelAppConnector = new ModelAppConnector(this);
	//		}
	//		NeoComApp.logger.info("<< [NeoComApp.<consructor>]");
	//	}

	// - M E T H O D - S E C T I O N ..........................................................................
	//	public File accessAppStorage (final String resourceString) {
	//		if ( null != resourceString )
	//			return new File(Environment.getExternalStorageDirectory(), NeoComAppConnector.getSingleton().getResourceString(
	//					R.string.appfoldername)
	//					/* + NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix) */ + "/" + resourceString);
	//		else
	//			return new File(Environment.getExternalStorageDirectory(), NeoComAppConnector.getSingleton().getResourceString(
	//					R.string.appfoldername)
	//			/* + NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix) */);
	//	}
	//
	//	/**
	//	 * Puts in the processing queue a new request for the character received in the parameter. This method is
	//	 * required by the <code>AppConnector</code> implementation.
	//	 */
	//	public void addCharacterUpdateRequest (final long characterID) {
	//		this.getCacheConnector().addCharacterUpdateRequest(characterID);
	//	}
	//
	//	@TargetApi(12)
	//	public void addDrawableToCache (final String key, final Drawable image) {
	//		ICacheConnector cache = NeoComAppConnector.getSingleton().getCacheConnector();
	//		if ( cache instanceof AndroidCacheConnector ) {
	//			((AndroidCacheConnector) cache).addDrawableToCache(key, image);
	//		} else
	//			throw new RuntimeException(
	//					"RTEX [GymAppConnector.addDrawableToCache]> Application connection not defined. Functionality 'addDrawableToCache' disabled.");
	//	}
	//
	//	/**
	//	 * Checks that the current parameter timestamp is still on the frame of the window.
	//	 *
	//	 * @param timestamp the current and last timestamp of the object.
	//	 * @param window    time span window in milliseconds.
	//	 */
	//	public boolean checkExpiration (final Instant timestamp, final long window) {
	//		if ( null == timestamp ) return true;
	//		return this.checkExpiration(timestamp.getMillis(), window);
	//	}
	//
	//	public boolean checkExpiration (final long timestamp, final long window) {
	//		if ( 0 == timestamp ) return true;
	//		final long now = GregorianCalendar.getInstance().getTimeInMillis();
	//		final long endWindow = timestamp + window;
	//		if ( now < endWindow )
	//			return false;
	//		else
	//			return true;
	//	}
	//
	//	@Deprecated
	//	public void closeDB () {
	//		this.getDBConnector().closeDatabases();
	//	}
	//
	//	/**
	//	 * Return the path to a file resource from the stand point of the application. We have to add the folder
	//	 * path where the user wishes to store the user application data. This directory is initially hardcoded but
	//	 * later may be changed on the configuration settings.
	//	 *
	//	 * @param fileresourceid
	//	 * @return
	//	 */
	//	public String getAppFilePath (final int fileresourceid) {
	//		final String sdcarddir = NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
	//		/* + NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix) */;
	//		final String file = NeoComAppConnector.getSingleton().getResourceString(fileresourceid);
	//		return sdcarddir + "/" + file;
	//	}
	//
	//	public String getAppFilePath (final String fileresourceName) {
	//		final String sdcarddir = NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
	//		/* + NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix) */;
	//		final String file = fileresourceName;
	//		return sdcarddir + "/" + file;
	//	}
	//
	//	@Override
	//	@Deprecated
	//	public Context getApplicationContext () {
	//		// TODO Auto-generated method stub
	//		return super.getApplicationContext();
	//	}
	//
	//	public AndroidCacheConnector getCacheConnector () {
	//		if ( null == cache ) {
	//			cache = new AndroidCacheConnector(this);
	//		}
	//		return cache;
	//	}
	//
	//	public ICCPDatabaseConnector getCCPDBConnector () {
	//		if ( null == dbCCPConnector ) {
	//			dbCCPConnector = new AndroidCCPDatabaseConnector();
	//		}
	//		return dbCCPConnector;
	//	}
	//
	//	public AndroidDatabaseConnector getDBConnector () {
	//		if ( null == dbNeocomConnector ) {
	//			dbNeocomConnector = new AndroidDatabaseConnector(this);
	//		}
	//		return dbNeocomConnector;
	//	}
	//	public NeocomAndroidDBHelper getNewDBConnector () {
	//		if ( null == dbNeocomConnector ) {
	//			dbNeocomConnector = new AndroidDatabaseConnector(this);
	//		}
	//		return dbNeocomConnector.getNeocomDBHelper();
	//	}
	//
	//	public Activity getFirstActivity () {
	//		if ( null == _firstActivity )
	//			throw new RuntimeException(
	//					"RTEX [NeoComApp.getFirstActivity]> Target destination First Activity of application is not defined.");
	//		return _firstActivity;
	//	}
	//
	//	public NeoComModelStore getModelStore () {
	//		return NeoComModelStore.getSingleton();
	//	}
	//
	//	public IStorageConnector getStorageConnector () {
	//		if ( null == storage ) {
	//			storage = new AndroidStorageConnector(this);
	//		}
	//		return storage;
	//	}
	//
	//	@Deprecated
	//	public File getUserDataStorage () {
	//		return new File(Environment.getExternalStorageDirectory(), NeoComAppConnector.getSingleton().getResourceString(
	//				R.string.userdatamodelfilename));
	//	}

	//	public void init () {
	//		NeoComApp.logger.info(">> [NeoComApp.init]");
	//		// Do again the initialization. This code is controlled by Android lifecycle and not Java.
	//		// Just connect the singleton and the Connector chanin to get access to all the methods.
	//		NeoComApp.singleton = this;
	//		//		_appConnector = new NeoComAppConnector(this);
	//		// Set the initial activity as a return point in case of exceptions.
	//		_firstActivity = LoginProgressActivity.class;
	//		// Close databases if they were left open.
	//		this.getDBConnector().closeDatabases();
	//		// Remove any reference to old adapters and helpers.
	//		storage = null;
	//		dbCCPConnector = null;
	//		dbNeocomConnector = null;
	//		cache = null;
	//		NeoComApp.logger.info("<< [NeoComApp.init]");
	//	}


	/**
	 * Events called during the Application life cycle.
	 */
	@Override
	public void onTerminate () {
		NeoComApp.logger.info(">> [NeoComApp.onTerminate]");
		//		this.unregisterReceiver(NeoComApp.timeTickReceiver);
		super.onTerminate();
		NeoComApp.logger.info("<< [NeoComApp.onTerminate]");
	}

	//	@Deprecated
	//	public void openDatabases () {
	//		this.getCCPDBConnector().openCCPDataBase();
	//		this.getDBConnector().openAppDataBase();
	//	}
	//
	//	public void startChrono () {
	//		chrono = new Instant();
	//	}
	//
	//	/**
	//	 * Start the background broadcast receiver to intercept the minute tick and process the data structures
	//	 * checking elements that should be updated because they are obsolete. New updates will be launched as
	//	 * separate async tasks.
	//	 */
	//	public void startTimer () {
	//		if ( null == NeoComApp.timeTickReceiver ) {
	//			NeoComApp.timeTickReceiver = new TimeTickReceiver(this);
	//			this.registerReceiver(NeoComApp.timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
	//		}
	//	}
	//
	//	public Duration timeLapse () {
	//		return new Duration(chrono, new Instant());
	//	}
	//
	//	/**
	//	 * Gets the menu context and updates the background progress indicator with the counters.
	//	 *
	//	 * @param countIndicator
	//	 */
	//	private void startProgress (final double countIndicator) {
	//		// Activate menu icon of progress.
	//		final Menu menu = NeoComModelStore.getSingleton().getAppMenu();
	//		if ( null != menu ) {
	//			final MenuItem updatingItem = menu.findItem(R.id.action_launchUpdate);
	//			final LayoutInflater mInflater = (LayoutInflater) NeoComModelStore.getSingleton().getActivity()
	//			                                                                  .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	//			final View actionView = mInflater.inflate(R.layout.actionbar_indeterminateprogress, null);
	//			if ( null != updatingItem ) {
	//				updatingItem.setVisible(true);
	//				updatingItem.setActionView(actionView);
	//			}
	//			final TextView counter = (TextView) actionView.findViewById(R.id.progressCounter);
	//			if ( null != counter ) {
	//				counter.setVisibility(View.VISIBLE);
	//				// REFACTOR This should be configured on the XML and not on the
	//				// code. If the progress is set on a library
	//				// then it should be styled.
	//				counter.setTypeface(NeoComApp.daysFace);
	//				counter.setText(NeoComApp.pendingCounter.format(countIndicator));
	//			}
	//			actionView.invalidate();
	//		}
	//	}
	//
	//	/**
	//	 * Hides the background progress indicator.
	//	 */
	//	private void stopProgress () {
	//		// Clear the update progress.
	//		final Menu menu = NeoComModelStore.getSingleton().getAppMenu();
	//		if ( null != menu ) {
	//			final MenuItem updatingItem = menu.findItem(R.id.action_launchUpdate);
	//			final LayoutInflater mInflater = (LayoutInflater) NeoComModelStore.getSingleton().getActivity()
	//			                                                                  .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	//			final View actionView = mInflater.inflate(R.layout.actionbar_indeterminateprogress, null);
	//			if ( null != updatingItem ) {
	//				updatingItem.setActionView(null);
	//				updatingItem.setVisible(false);
	//			}
	//			final TextView counter = (TextView) actionView.findViewById(R.id.progressCounter);
	//			if ( null != counter ) {
	//				counter.setVisibility(View.GONE);
	//			}
	//			actionView.invalidate();
	//		}
	//	}
}
// - UNUSED CODE ............................................................................................

//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom;

// - IMPORT SECTION .........................................................................................
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dimensinfin.android.model.INamed;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.AbstractPropertyChanger;
import org.dimensinfin.eveonline.neocom.activity.LoginListActivity;
import org.dimensinfin.eveonline.neocom.connector.AndroidCCPDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.AndroidCacheConnector;
import org.dimensinfin.eveonline.neocom.connector.AndroidDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.AndroidStorageConnector;
import org.dimensinfin.eveonline.neocom.connector.ICCPDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.IStorageConnector;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.industry.JobQueue;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.interfaces.IDateTimeComparator;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComAppConnector;
import org.dimensinfin.eveonline.neocom.interfaces.IWeigthedNode;
import org.dimensinfin.eveonline.neocom.model.NeoComApiKey;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.part.ApiKeyPart;
import org.dimensinfin.eveonline.neocom.part.AssetPart;
import org.dimensinfin.eveonline.neocom.part.BlueprintPart;
import org.dimensinfin.eveonline.neocom.part.ContainerPart;
import org.dimensinfin.eveonline.neocom.part.ResourcePart;
import org.dimensinfin.eveonline.neocom.part.ShipPart;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver;
import org.dimensinfin.eveonline.neocom.services.PendingRequestEntry;
import org.dimensinfin.eveonline.neocom.storage.AppModelStore;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComApp extends Application implements INeoComAppConnector {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger							logger						= Logger.getLogger("NeoComApp");
	private static NeoComApp					singleton					= null;
	private static BroadcastReceiver	timeTickReceiver	= null;
	public static int									topCounter				= 0;
	public static int									marketCounter			= 0;

	/** Global typeface to be used on nos standard activities or in places with no layout. */
	//	public static Typeface						daysFace								= null;
	private static DecimalFormat			pendingCounter		= new DecimalFormat("0.0##");
	//	private static boolean						firstTimeInitialization	= false;

	@Deprecated
	public static Comparator<AbstractPropertyChanger> createComparator(final int code) {
		Comparator<AbstractPropertyChanger> comparator = new Comparator<AbstractPropertyChanger>() {
			public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
				return 0;
			}
		};
		switch (code) {
			case AppWideConstants.comparators.COMPARATOR_NAME:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						String leftField = null;
						String rightField = null;
						if (left instanceof INamedPart) {
							leftField = ((INamedPart) left).getName();
						}
						if (right instanceof INamedPart) {
							rightField = ((INamedPart) right).getName();
						}
						if (left instanceof INamed) {
							leftField = ((INamed) left).getOrderingName();
						}
						if (right instanceof INamed) {
							rightField = ((INamed) right).getOrderingName();
						}

						if (null == leftField) return 1;
						if (null == rightField) return -1;
						if ("" == leftField) return 1;
						if ("" == rightField) return -1;
						return leftField.compareTo(rightField);
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_ASSET_COUNT:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						long leftField = -1;
						long rightField = -1;
						if (left instanceof NeoComAsset) {
							final NeoComAsset intermediate = (NeoComAsset) left;
							leftField = intermediate.getQuantity();
						}

						if (right instanceof NeoComAsset) {
							final NeoComAsset intermediate = (NeoComAsset) right;
							rightField = intermediate.getQuantity();
						}
						if (leftField < rightField) return 1;
						if (leftField > rightField) return -1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_ITEM_TYPE:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						int leftField = -1;
						int rightField = -1;
						// if (left instanceof BlueprintPart) leftField = 100;
						if (left instanceof AssetPart) {
							leftField = 0;
						}
						if (left instanceof ShipPart) {
							leftField = 200;
						}
						if (left instanceof ContainerPart) {
							leftField = -300;
						}

						// if (right instanceof BlueprintPart) rightField = 100;
						if (right instanceof AssetPart) {
							rightField = 0;
						}
						if (right instanceof ShipPart) {
							rightField = 200;
						}
						if (right instanceof ContainerPart) {
							rightField = -300;
						}

						if (leftField < rightField) return -1;
						if (leftField > rightField) return 1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_RESOURCE_TYPE:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						int leftField = -1;
						int rightField = -1;
						if (left instanceof ResourcePart) {
							final Resource resource = ((ResourcePart) left).getCastedModel();
							leftField = 0;
							if (resource.getCategory().equalsIgnoreCase("Material")) {
								leftField = -300;
							}
							if (resource.getCategory().equalsIgnoreCase("Module")) {
								leftField = -200;
							}
							if (resource.getCategory().equalsIgnoreCase("Blueprint")) {
								leftField = -100;
							}
							if (resource.getName().contains("Datacore")) {
								leftField = 100;
							}
						}

						if (right instanceof ResourcePart) {
							final Resource resource = ((ResourcePart) left).getCastedModel();
							rightField = 0;
							if (resource.getCategory().equalsIgnoreCase("Material")) {
								rightField = -300;
							}
							if (resource.getCategory().equalsIgnoreCase("Module")) {
								rightField = -200;
							}
							if (resource.getCategory().equalsIgnoreCase("Blueprint")) {
								rightField = -100;
							}
							if (resource.getName().contains("Datacore")) {
								rightField = 100;
							}
						}

						if (leftField < rightField) return -1;
						if (leftField > rightField) return 1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_APIID_ASC:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						long leftField = -1;
						long rightField = -1;
						if (left instanceof ApiKeyPart) {
							final NeoComApiKey intermediate = ((ApiKeyPart) left).getCastedModel();
							leftField = intermediate.getKey();
						}

						if (right instanceof ApiKeyPart) {
							final NeoComApiKey intermediate = ((ApiKeyPart) right).getCastedModel();
							rightField = intermediate.getKey();
						}
						if (leftField < rightField) return -1;
						if (leftField > rightField) return 1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_APIID_DESC:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						long leftField = -1;
						long rightField = -1;
						if (left instanceof ApiKeyPart) {
							final NeoComApiKey intermediate = ((ApiKeyPart) left).getCastedModel();
							leftField = intermediate.getKey();
						}

						if (right instanceof ApiKeyPart) {
							final NeoComApiKey intermediate = ((ApiKeyPart) right).getCastedModel();
							rightField = intermediate.getKey();
						}
						if (leftField > rightField) return -1;
						if (leftField < rightField) return 1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_REQUEST_PRIORITY:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						long leftField = -1;
						long rightField = -1;
						if (left instanceof PendingRequestEntry) {
							final PendingRequestEntry intermediate = (PendingRequestEntry) left;
							leftField = intermediate.getPriority();
						}

						if (right instanceof PendingRequestEntry) {
							final PendingRequestEntry intermediate = (PendingRequestEntry) right;
							rightField = intermediate.getPriority();
						}
						if (leftField < rightField) return -1;
						if (leftField > rightField) return 1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_PRIORITY:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						int leftField = -1;
						int rightField = -1;
						if (left instanceof PendingRequestEntry) {
							final PendingRequestEntry intermediate = (PendingRequestEntry) left;
							leftField = intermediate.getPriority();
						}

						if (right instanceof PendingRequestEntry) {
							final PendingRequestEntry intermediate = (PendingRequestEntry) right;
							rightField = intermediate.getPriority();
						}
						if (leftField < rightField) return 1;
						if (leftField > rightField) return -1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_WEIGHT:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						int leftField = -1;
						int rightField = -1;
						if (left instanceof IWeigthedNode) {
							leftField = ((IWeigthedNode) left).getWeight();
						}
						if (right instanceof IWeigthedNode) {
							rightField = ((IWeigthedNode) right).getWeight();
						}
						if (leftField < rightField) return -1;
						if (leftField > rightField) return 1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_TIMEPENDING:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						int leftField = -1;
						int rightField = -1;
						if (left instanceof JobQueue) {
							leftField = ((JobQueue) left).getTimeUsed();
						}
						if (right instanceof JobQueue) {
							rightField = ((JobQueue) right).getTimeUsed();
						}

						if (leftField > rightField) return 1;
						if (leftField < rightField) return -1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_CARD_RATIO:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						double leftField = 0.0;
						double rightField = 0.0;
						if (left instanceof BlueprintPart) {
							leftField = ((BlueprintPart) left).getProfitIndex();
						}
						if (right instanceof BlueprintPart) {
							rightField = ((BlueprintPart) right).getProfitIndex();
						}

						if (leftField > rightField)
							return -1;
						else if (leftField == rightField) return 0;
						return 1;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_NEWESTDATESORT:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						DateTime leftField = new DateTime(DateTimeZone.UTC);
						DateTime rightField = new DateTime(DateTimeZone.UTC);
						if (left instanceof IDateTimeComparator) {
							leftField = ((IDateTimeComparator) left).getComparableDate();
						}
						if (right instanceof IDateTimeComparator) {
							rightField = ((IDateTimeComparator) right).getComparableDate();
						}

						if (leftField.isAfter(rightField))
							return -1;
						else
							return 1;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_OLDESTDATESORT:
				comparator = new Comparator<AbstractPropertyChanger>() {
					public int compare(final AbstractPropertyChanger left, final AbstractPropertyChanger right) {
						DateTime leftField = new DateTime(DateTimeZone.UTC);
						DateTime rightField = new DateTime(DateTimeZone.UTC);
						if (left instanceof IDateTimeComparator) {
							leftField = ((IDateTimeComparator) left).getComparableDate();
						}
						if (right instanceof IDateTimeComparator) {
							rightField = ((IDateTimeComparator) right).getComparableDate();
						}

						if (leftField.isAfter(rightField))
							return 1;
						else
							return -1;
					}
				};
				break;
		}
		return comparator;
	}

	@Deprecated
	public static Comparator<IPart> createPartComparator(final int code) {
		Comparator<IPart> comparator = new Comparator<IPart>() {
			public int compare(final IPart left, final IPart right) {
				return 0;
			}
		};
		switch (code) {
			case AppWideConstants.comparators.COMPARATOR_NAME:
				comparator = new Comparator<IPart>() {
					public int compare(final IPart left, final IPart right) {
						String leftField = null;
						String rightField = null;
						if (left instanceof INamedPart) {
							leftField = ((INamedPart) left).getName();
						}
						if (right instanceof INamedPart) {
							rightField = ((INamedPart) right).getName();
						}
						if (left instanceof INamed) {
							leftField = ((INamed) left).getOrderingName();
						}
						if (right instanceof INamed) {
							rightField = ((INamed) right).getOrderingName();
						}

						if (null == leftField) return 1;
						if (null == rightField) return -1;
						if ("" == leftField) return 1;
						if ("" == rightField) return -1;
						return leftField.compareTo(rightField);
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_GROUPNAME:
				comparator = new Comparator<IPart>() {
					public int compare(final IPart left, final IPart right) {
						String leftField = null;
						String rightField = null;
						if (left instanceof AssetPart) {
							leftField = ((AssetPart) left).getCastedModel().getGroupName();
						}
						if (right instanceof AssetPart) {
							rightField = ((AssetPart) right).getCastedModel().getGroupName();
						}

						if (null == leftField) return 1;
						if (null == rightField) return -1;
						if ("" == leftField) return 1;
						if ("" == rightField) return -1;
						return leftField.compareTo(rightField);
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_ITEM_TYPE:
				comparator = new Comparator<IPart>() {
					public int compare(final IPart left, final IPart right) {
						int leftField = -1;
						int rightField = -1;
						// if (left instanceof BlueprintPart) leftField = 100;
						if (left instanceof AssetPart) {
							leftField = 0;
						}
						if (left instanceof ShipPart) {
							leftField = 200;
						}
						if (left instanceof ContainerPart) {
							leftField = -300;
						}

						// if (right instanceof BlueprintPart) rightField = 100;
						if (right instanceof AssetPart) {
							rightField = 0;
						}
						if (right instanceof ShipPart) {
							rightField = 200;
						}
						if (right instanceof ContainerPart) {
							rightField = -300;
						}

						if (leftField < rightField) return -1;
						if (leftField > rightField) return 1;
						return 0;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_CARD_RATIO:
				comparator = new Comparator<IPart>() {
					public int compare(final IPart left, final IPart right) {
						double leftField = 0.0;
						double rightField = 0.0;
						if (left instanceof BlueprintPart) {
							leftField = ((BlueprintPart) left).getProfitIndex();
						}
						if (right instanceof BlueprintPart) {
							rightField = ((BlueprintPart) right).getProfitIndex();
						}

						if (leftField > rightField)
							return -1;
						else if (leftField == rightField) return 0;
						return 1;
					}
				};
				break;
			case AppWideConstants.comparators.COMPARATOR_NEWESTDATESORT:
				comparator = new Comparator<IPart>() {
					public int compare(final IPart left, final IPart right) {
						DateTime leftField = new DateTime(DateTimeZone.UTC);
						DateTime rightField = new DateTime(DateTimeZone.UTC);
						if (left instanceof IDateTimeComparator) {
							leftField = ((IDateTimeComparator) left).getComparableDate();
						}
						if (right instanceof IDateTimeComparator) {
							rightField = ((IDateTimeComparator) right).getComparableDate();
						}

						if (leftField.isAfter(rightField))
							return -1;
						else
							return 1;
					}
				};
				break;
		}
		return comparator;
	}

	public static NeoComApp getSingletonApp() {
		if (null == NeoComApp.singleton) {
			new NeoComApp();
		}
		return NeoComApp.singleton;
	}

	@Deprecated
	public static Element parseDOMDocument(final InputStream stream) throws IOException, ParserConfigurationException {
		NeoComApp.logger.info(">> EVEDroidApp.downloadDOMDocument");
		Element elementSingleton = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
				.getDocumentElement();
		if (null == stream) return elementSingleton;
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(stream);
			elementSingleton = doc.getDocumentElement();
		} catch (final MalformedURLException me) {
			me.printStackTrace();
		} catch (final SAXException se) {
			se.printStackTrace();
		} catch (final ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return elementSingleton;
	}

	@Deprecated
	public static void runTimer() {
		NeoComApp.timeTickReceiver.onReceive(NeoComApp.getSingletonApp().getApplicationContext(), null);
	}

	//	@Deprecated
	//	public static void setFirstInitalization(final boolean state) {
	//		NeoComApp.firstTimeInitialization = state;
	//	}
	@Deprecated
	public static void updateProgressSpinner() {
		if ((NeoComApp.marketCounter > 0) || (NeoComApp.topCounter > 0)) {
			double divider = 10.0;
			if (NeoComApp.topCounter > 10) {
				divider = 100.0;
			}
			NeoComApp.getSingletonApp()
					.startProgress(new Double(NeoComApp.marketCounter + new Double(NeoComApp.topCounter / divider)));
		} else {
			NeoComApp.getSingletonApp().stopProgress();
		}
	}

	// - F I E L D - S E C T I O N ............................................................................
	private NeoComAppConnector					_appConnector			= null;
	/**
	 * Reference to the startup activity of the application. This Activity gets restarted when there is an
	 * intercepted runtime exception.
	 */
	private Class<?>										_firstActivity		= null;

	private AndroidStorageConnector			storage						= null;
	private AndroidCCPDatabaseConnector	dbCCPConnector		= null;
	private AndroidDatabaseConnector		dbNeocomConnector	= null;
	private AndroidCacheConnector				cache							= null;

	//private INeocomDBHelper									neocomDBHelper		= null;
	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComApp() {
		NeoComApp.logger.info(">> [NeoComApp.<contructor>]");
		// Setup the referencing structures that will serve as proxy and global references.
		if (null == NeoComApp.singleton) {
			// Just connect the singleton and the Connector chanin to get access to all the methods.
			NeoComApp.singleton = this;
			_appConnector = new NeoComAppConnector(this);
		}
		NeoComApp.logger.info("<< [NeoComApp.<consructor>]");
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public File accessAppStorage(final String resourceString) {
		if (null != resourceString)
			return new File(Environment.getExternalStorageDirectory(),
					NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
							+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix) + "/" + resourceString);
		else
			return new File(Environment.getExternalStorageDirectory(),
					NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
							+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix));
	}

	/**
	 * Puts in the processing queue a new request for the character received in the parameter. This method is
	 * required by the <code>AppConnector</code> implementation.
	 */
	public void addCharacterUpdateRequest(final long characterID) {
		this.getCacheConnector().addCharacterUpdateRequest(characterID);
	}

	@Deprecated
	public void closeDB() {
		this.getDBConnector().closeDatabases();
	}

	/**
	 * Return the path to a file resource from the stand point of the application. We have to add the folder
	 * path where the user wishes to store the user application data. This directory is initially hardcoded but
	 * later may be changed on the configuration settings.
	 * 
	 * @param fileresourceid
	 * @return
	 */
	public String getAppFilePath(final int fileresourceid) {
		final String sdcarddir = NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
				+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix);
		final String file = NeoComAppConnector.getSingleton().getResourceString(fileresourceid);
		return sdcarddir + "/" + file;
	}

	public String getAppFilePath(final String fileresourceName) {
		final String sdcarddir = NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
				+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix);
		final String file = fileresourceName;
		return sdcarddir + "/" + file;
	}

	@Override
	@Deprecated
	public Context getApplicationContext() {
		// TODO Auto-generated method stub
		return super.getApplicationContext();
	}

	public AndroidCacheConnector getCacheConnector() {
		if (null == cache) {
			cache = new AndroidCacheConnector(this);
		}
		return cache;
	}

	public ICCPDatabaseConnector getCCPDBConnector() {
		if (null == dbCCPConnector) {
			dbCCPConnector = new AndroidCCPDatabaseConnector();
		}
		return dbCCPConnector;
	}

	public AndroidDatabaseConnector getDBConnector() {
		if (null == dbNeocomConnector) {
			dbNeocomConnector = new AndroidDatabaseConnector(this);
		}
		return dbNeocomConnector;
	}

	public Class<?> getFirstActivity() {
		if (null == _firstActivity) throw new RuntimeException(
				"RTEX [NeoComApp.getFirstActivity]> Target destionation First Activity of application is not defined.");
		return _firstActivity;
	}

	public NeoComModelStore getModelStore() {
		return NeoComModelStore.getSingleton();
	}

	public IStorageConnector getStorageConnector() {
		if (null == storage) {
			storage = new AndroidStorageConnector(this);
		}
		return storage;
	}

	@Deprecated
	public File getUserDataStorage() {
		return new File(Environment.getExternalStorageDirectory(),
				NeoComAppConnector.getSingleton().getResourceString(R.string.userdatamodelfilename));
	}

	public void init() {
		NeoComApp.logger.info(">> [NeoComApp.init]");
		// Do again the initialization. This code is controlled by Android lifecycle and not Java.
		// Just connect the singleton and the Connector chanin to get access to all the methods.
		NeoComApp.singleton = this;
		_appConnector = new NeoComAppConnector(this);
		// Set the initial activity as a return point in case of exceptions.
		_firstActivity = LoginListActivity.class;
		// Close databases if they were left open.
		this.getDBConnector().closeDatabases();
		// Remove any reference to old adapters and helpers.
		storage = null;
		dbCCPConnector = null;
		dbNeocomConnector = null;
		cache = null;
		NeoComApp.logger.info("<< [NeoComApp.init]");
	}

	/**
	 * Events called during the Application life cycle.
	 */
	@Override
	public void onCreate() {
		NeoComApp.logger.info(">> [NeoComApp.onCreate]");
		super.onCreate();
		//		NeoComApp.daysFace = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "fonts/Days.otf");
		// REFACTOR Remove the secure XML access and configure the ApiConnector.
		//		ApiConnector.setSecureXmlProcessing(false);
		NeoComApp.logger.info("<< [NeoComApp.onCreate]");
	}

	/**
	 * Events called during the Application life cycle.
	 */
	@Override
	public void onTerminate() {
		NeoComApp.logger.info(">> [NeoComApp.onTerminate]");
		this.unregisterReceiver(NeoComApp.timeTickReceiver);
		super.onTerminate();
		NeoComApp.logger.info("<< [NeoComApp.onTerminate]");
	}

	@Deprecated
	public void openDatabases() {
		this.getCCPDBConnector().openCCPDataBase();
		this.getDBConnector().openAppDataBase();
	}

	/**
	 * Start the background broadcast receiver to intercept the minute tick and process the data structures
	 * checking elements that should be updated because they are obsolete. New updates will be launched as
	 * separate async tasks.
	 */
	public void startTimer() {
		if (null == NeoComApp.timeTickReceiver) {
			NeoComApp.timeTickReceiver = new TimeTickReceiver(this);
			this.registerReceiver(NeoComApp.timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		}
	}

	/**
	 * Gets the menu context and updates the background progress indicator with the counters.
	 * 
	 * @param countIndicator
	 */
	private void startProgress(final double countIndicator) {
		// Activate menu icon of progress.
		final Menu menu = AppModelStore.getSingleton().getAppMenu();
		if (null != menu) {
			final MenuItem updatingItem = menu.findItem(R.id.action_launchUpdate);
			final LayoutInflater mInflater = (LayoutInflater) AppModelStore.getSingleton().getActivity()
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			final View actionView = mInflater.inflate(R.layout.actionbar_indeterminateprogress, null);
			if (null != updatingItem) {
				updatingItem.setVisible(true);
				updatingItem.setActionView(actionView);
			}
			final TextView counter = (TextView) actionView.findViewById(R.id.progressCounter);
			if (null != counter) {
				counter.setVisibility(View.VISIBLE);
				// REFACTOR This should be configured on the XML and not on the
				// code. If the progress is set on a library
				// then it should be styled.
				counter.setTypeface(NeoComApp.daysFace);
				counter.setText(NeoComApp.pendingCounter.format(countIndicator));
			}
			actionView.invalidate();
		}
	}

	/**
	 * Hides the background progress indicator.
	 */
	private void stopProgress() {
		// Clear the update progress.
		final Menu menu = AppModelStore.getSingleton().getAppMenu();
		if (null != menu) {
			final MenuItem updatingItem = menu.findItem(R.id.action_launchUpdate);
			final LayoutInflater mInflater = (LayoutInflater) AppModelStore.getSingleton().getActivity()
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			final View actionView = mInflater.inflate(R.layout.actionbar_indeterminateprogress, null);
			if (null != updatingItem) {
				updatingItem.setActionView(null);
				updatingItem.setVisible(false);
			}
			final TextView counter = (TextView) actionView.findViewById(R.id.progressCounter);
			if (null != counter) {
				counter.setVisibility(View.GONE);
			}
			actionView.invalidate();
		}
	}
}
// - UNUSED CODE ............................................................................................

//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import org.dimensinfin.core.model.AbstractModelStore;
import org.dimensinfin.core.parser.IPersistentHandler;
import org.dimensinfin.eveonline.neocom.activity.LoginListActivity;
import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.core.NeocomRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.INeoComModelStore;
import org.dimensinfin.eveonline.neocom.model.Fitting;
import org.dimensinfin.eveonline.neocom.model.Login;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;

import android.app.Activity;
import android.view.Menu;

// - CLASS IMPLEMENTATION ...................................................................................
/**
 * This class has as main responsibility the access to the data input by the user and the store of all Eve
 * data downloaded from the CCP servers. The data to be manipulated has some levels, starting on the list of
 * credentials to access the CCP api and ending on processed and generated data from the other levels.<br>
 * The quantity of information makes a priority to keep it updated to the minor cost of processing. After
 * storing the data in serialized form or developing some cache variations I have choose the store into a
 * local database as the fastest way to have ready data while the lengthy download processes update most of
 * that information.<br>
 * <br>
 * The class is a singleton with two main structures, the list of api keys and the list of user defined
 * fittings. Both sets have to contain unique identifiers so any addition of a duplicated one will only
 * replace the older one. The persistence mechanics are delegated to a new PersistenceManager that will have
 * access to a SQLite database to keep track of this data structures, so all input/output should be handled by
 * that Manager.<br>
 * <br>
 * There should be some type of notification mechanism to report the UI about changes on the data contents
 * performed with background tasks. This is integrated on the GEF model hierarchy but has to be reviewed
 * inside Android special UI/non UI threads structure.
 * 
 * @author Adam Antinoo
 */
public class NeoComModelStore extends AbstractModelStore implements INeoComModelStore {

	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long			serialVersionUID	= 8777607802616543118L;
	private static Logger					logger						= Logger.getLogger("AppModelStore");
	private static NeoComModelStore	singleton					= null;

	/**
	 * Returns the single global instance of the Store to be used as an instance. In case the instance does not
	 * exists then we initiate the initialization process trying to create the most recent instance we have
	 * recorded or recreate it from scratch from the api_list file.
	 * 
	 * @return the single global instance
	 */
	public static NeoComModelStore getSingleton() {
		if (null == NeoComModelStore.singleton) {
			// Initiate the recovery.
			NeoComModelStore.initialize();
		}
		return NeoComModelStore.singleton;
	}

	/**
	 * Forces the initialization of the Model store from the api list file. Instead reading the data from the
	 * store file it will process the api list and reload all the character information from scratch.
	 */
	public static void initialize() {
		NeoComModelStore.logger.info(">> [AppModelStore.initialize]");
		// Create a new from scratch. Read the api key list.
		NeoComModelStore.singleton = new NeoComModelStore(new UserModelPersistenceHandler());
		// Load any data from storage and then update the information from CCP.
		//		AppModelStore.getSingleton().restore();
		//	AppModelStore.readApiKeys();

		// Make sure we get the characters on a thread out of the main one.
		//	AppModelStore.getSingleton().getCharacters();
		// Set back the current pilot whose id is stored on the _pilotIdentifier
		if (NeoComModelStore.getSingleton()._pilotIdentifier > 0) {
			NeoComModelStore.getSingleton().activatePilot(NeoComModelStore.getSingleton()._pilotIdentifier);
		}
		NeoComModelStore.logger.info("<< [AppModelStore.initialize]");
	}

	// - F I E L D - S E C T I O N ............................................................................
	/** Reference to the application menu to make it accessible to any level. */
	private transient Menu											_appMenu					= null;

	/** Reference to the current active Activity. Sometimes this is needed to access application resources. */
	private transient Activity									_activity					= null;
	/** This is the unique list for all registered distinct Logins at the database. */
	private Hashtable<String, Login>						_loginList				= null;
	/**
	 * This is the string identifier assigned to this session and that relates to an specific set of api keys.
	 */
	private Login																_loginIdentifier	= null;
	/** Reference to the current active Character, be it a Pilot or a Corporation */
	private transient NeoComCharacter						_pilot						= null;

	/** The list of keys related to a login identifier */
	//	private List<NeoComApiKey>									_neocomApiKeys		= null;
	//	private List<NeoComCharacter>								_neocomCharacters	= null;
	protected long															_pilotIdentifier	= -1L;
	/** Check to verify if the recovery process is successful. */
	private boolean															recovered					= false;
	/** List of registered DataSources. This data is not stored on switch or termination. */
	//	private transient DataSourceManager			dsManager					= null;
	/** List of fittings by name. This is the source for the Fittings DataSource. */
	private transient HashMap<String, Fitting>	fittings					= new HashMap<String, Fitting>();
	//	private transient HashMap<Long, EveChar>	charCache					= null;
	//	private final long											lastCCPAccessTime	= 0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	private NeoComModelStore(final IPersistentHandler storageHandler) {
		super();
		// On creation we can set the proper model persistence handler.
		try {
			this.setPersistentStorage(storageHandler);
			this.setAutomaticUpdate(false);
		} catch (final Exception ex) {
			// TODO This is a quite serious error because invalidates any storage of the model data
			ex.printStackTrace();
		}
	}

	/**
	 * Gets a unique list of all the different characters available to the system from the different Logins.
	 * This action is done without disturbing the login selected or any other AppModel structure. The list is
	 * not kept and is calculated each time the call is received.
	 */
	public Set<NeoComCharacter> accessCharacterList() {
		Hashtable<String, Login> loginList = AppConnector.getModelStore().accessLoginList();
		Set<NeoComCharacter> charList = new HashSet<NeoComCharacter>();
		for (String key : loginList.keySet()) {
			Login login = loginList.get(key);
			for (NeoComCharacter c : login.getCharacters()) {
				charList.add(c);
			}
		}
		return charList;
	}

	/**
	 * Gets access to the complete list of Logins. If this list is empty we go back to the database to populate
	 * it.
	 */
	public Hashtable<String, Login> accessLoginList() {
		if (null == _loginList) {
			_loginList = AppConnector.getDBConnector().queryAllLogins();
		}
		return _loginList;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Stores a reference to the current activity if this is required for some actions inside the DataSources.
	 * 
	 * @param currentActivity
	 */
	public void activateActivity(final Activity currentActivity) {
		_activity = currentActivity;
	}

	/**
	 * This is the main activation entry point for the Login. If there are no logins already on the ModelStore
	 * we should get them from the backend database and then search for the indicated login. If finally that
	 * login is not found we can fire an Exception.
	 * 
	 * @param login
	 */
	public Login activateLoginIdentifier(final String loginTarget) {
		NeoComModelStore.logger.info(">< [AppModelStore.activateLoginIdentifier]loginTarget: " + loginTarget);
		if (null == _loginList) {
			_loginList = AppConnector.getDBConnector().queryAllLogins();
		}
		// OPTIMIZATION: If the character is already the selected one do nothing.
		if (null != _loginIdentifier) if (_loginIdentifier.getName().equalsIgnoreCase(loginTarget)) return _loginIdentifier;
		for (String key : _loginList.keySet()) {
			if (_loginList.get(key).getName().equalsIgnoreCase(loginTarget)) {
				_loginIdentifier = _loginList.get(key);
				//				// Update the list of api keys and the characters related to the login.
				//				this.readApiKeys();
				return _loginIdentifier;
			}
		}
		// We run through all the login list and did not found the login. Or a problem of an error. Fire exception.
		throw new NeocomRuntimeException(
				"RT [AppModelStore.activateLoginIdentifier]>Login " + loginTarget + " not found on current login list.");
	}

	/**
	 * Searches for the pilot on the character list of the active Login and stores it to the current slot.<br>
	 * On the new implementation change the search to delegate the action to a new data model where the list of
	 * keys comes from a database search and the list of associated keys are stored on the App Model. The list
	 * of characters are on the Login class and there should be an active Login. Otherwise we raise and
	 * exception because there is no Login or the Character is not found.
	 * 
	 * @param characterid
	 *          id of the character to activate and select for work with.
	 */
	public NeoComCharacter activatePilot(final long characterid) {
		NeoComModelStore.logger.info(">< [AppModelStore.activatePilot]Id: " + characterid);
		// If the current pilot is the one searched simplify the search.
		if ((null != _pilot) && ((_pilot.getCharacterID()) == characterid)) return _pilot;
		// If the active Login is empty then we do not know where to search.
		if (null == _loginIdentifier) throw new NeocomRuntimeException(
				"RT [AppModelStore.activatePilot]> There is no active Login. Cannot search for character.");

		// Search for character at the active Login.
		NeoComCharacter hit = _loginIdentifier.searchCharacter(characterid);
		if (null == hit) throw new RuntimeException(
				"RT [AppModelStore.activatePilot]>Pilot with id: " + characterid + " not located at active Login.");
		_pilot = hit;
		return hit;
	}

	public NeoComCharacter activatePilot(final String characterstring) {
		return this.activatePilot(Long.valueOf(characterstring).longValue());
	}

	/**
	 * Adds a new fitting to the list of stored ones and persists the data structures.
	 * 
	 * @param fit
	 * @param label
	 */
	public void addFitting(final Fitting fit, final String label) {
		fittings.put(label, fit);
		this.setDirty(true);
		this.save();
	}

	//	@Override
	//	@Deprecated
	//	public void clean() {
	//		_neocomApiKeys.clear();
	//		fittings = new HashMap<String, Fitting>();
	//	}

	/**
	 * Returns any previously activated character
	 */
	public NeoComCharacter getActiveCharacter() {
		return _pilot;
	}

	/**
	 * Return the list of pilots that are currently active as selected by the user. The user may deactivate some
	 * pilots to reduce the view of data or pilots that are no longer active.<br>
	 * The start list is the list of characters associated to the llist of kays assocated to the current login.
	 * 
	 * @return
	 */
	@Deprecated
	public List<NeoComCharacter> getActiveCharacters() {
		if (null == _loginIdentifier)
			return new Vector<NeoComCharacter>();
		else
			return _loginIdentifier.getCharacters();
	}

	public Activity getActivity() {
		if (null == _activity) return new LoginListActivity();
		return _activity;
	}

	@Deprecated
	public Menu getAppMenu() {
		return _appMenu;
	}

	//	public List<NeoComApiKey> getApiKeys() {
	//		if (null == _neocomApiKeys) {
	//			this.readApiKeys();
	//		}
	//		return _neocomApiKeys;
	//	}

	//	public IDataSourceConnector getDataSourceConector() {
	//		if (null == dsManager) {
	//			dsManager = new DataSourceManager();
	//		}
	//		return dsManager;
	//	}
	/**
	 * Returns the current active pilot or corporation if exists. If the value is not defined then this means
	 * that the model store has been initialized and that we should go back to the pilot roaster page to select
	 * a new one.
	 */
	public NeoComCharacter getCurrentPilot() {
		if (null == _pilot)
			throw new NeocomRuntimeException("UNEX. Selected character is not selected. Go back to home to select one.");
		else
			return _pilot;
	}

	public Class<?> getFirstActivity() {
		return LoginListActivity.class;
	}

	public HashMap<String, Fitting> getFittings() {
		return fittings;
	}

	@Deprecated
	public String getLoginIdentifier() {
		if (null != _loginIdentifier) return _loginIdentifier.getName();
		throw new NeocomRuntimeException(
				"RT [AppModelStore.getLoginIdentifier]>Login Identifier not defined. Setting to default if possible.");
	}

	@Deprecated
	public NeoComCharacter getPilot() {
		return this.getCurrentPilot();
	}

	//	/**
	//	 * Checks if there is a need to retrieve again from the servers the user data.
	//	 * 
	//	 * @return
	//	 */
	//	public boolean needsUpdate() {
	//		return AppConnector.checkExpiration(lastCCPAccessTime, ModelWideConstants.HOURS3);
	//	}

	//	public String printReport() {
	//		final StringBuffer buffer = new StringBuffer("[UserModelStore (");
	//		buffer.append("keys: ").append(apiKeys).append(" ");
	//		buffer.append("]");
	//		return buffer.toString();
	//	}

	//	/**
	//	 * Refreshes the api keys and all other depending structures from the api list file. Keeps as much of the
	//	 * current information as possible while updating the list of keys.
	//	 */
	//	public void refresh() {
	//		// TODO Auto-generated method stub
	//	}

	/**
	 * Checks if we have data or we have to restore from disk, usually only on the initialization.
	 * 
	 * @return true is we have to read the data because the containers are empty.
	 */
	public boolean needsRestore() {
		//	if (_neocomApiKeys.size() < 1) return true;
		return false;
	}

	/**
	 * Restores the stored state from the disk file. If there any problem the method returns true.
	 * 
	 * @return <code>true</code> if there is a problem and the restore was not executed.
	 */
	@Override
	public boolean restore() {
		AppConnector.startChrono();
		recovered = super.restore();
		if (recovered) {
			NeoComModelStore.logger
					.info("~~ [AppModelStore.restore] Time lapse for APPSTORE[RESTORE] - " + AppConnector.timeLapse());
			return recovered;
		} else {
			// The handler was not able to retrieve the file. Possibly because there was no file.
			this.setDirty(true);
			return false;
		}
	}

	/**
	 * Save data to disk only when we have detected it has changed.
	 * 
	 */
	@Override
	public boolean save() {
		if (this.isDirty()) {
			// Clean the dirty flag.
			this.setDirty(false);
			recovered = true;
			AppConnector.startChrono();
			final boolean state = super.save();
			NeoComModelStore.logger.info("~~ Time lapse for APPSTORE[SAVE] - " + AppConnector.timeLapse());
			return state;
		} else
			return false;
	}

	//	/**
	//	 * Search for the specified character id in the list of associated characters list.
	//	 * 
	//	 * @param characterID
	//	 * @return
	//	 */
	//	//	@Deprecated
	//	public NeoComCharacter searchCharacter(final long characterID) {
	//		for (NeoComCharacter character : _neocomCharacters) {
	//			if (character.getCharacterID() == characterID) return character;
	//		}
	//		throw new NeocomRuntimeException("Character with id=" + characterID + " not found on list");
	//	}

	public Fitting searchFitting(final String fittingLabel) {
		return fittings.get(fittingLabel);
	}

	//	/**
	//	 * Stored the keys on the store fields from the persistence store. This code should reconnect pointers to
	//	 * fields not stored and marked as transient.
	//	 * 
	//	 * @param newkeys
	//	 */
	//	public void setApiKeys(final List<NeoComApiKey> newkeys) {
	//		_neocomApiKeys = newkeys;
	//		// we have to reparent the new data because this is not stored on the serialization.
	//		// Also reinitialize transient fields that are not saved
	//		//		final Iterator<ApiKey> eit = apiKeys.values().iterator();
	//		//		while (eit.hasNext()) {
	//		//			final ApiKey apiKey = eit.next();
	//		//			//	apiKey.setParent(this);
	//		//		}
	//	}

	public void setAppMenu(final Menu appMenu) {
		_appMenu = appMenu;
	}

	public void setFittings(final HashMap<String, Fitting> readObject) {
		fittings = readObject;
	}

	//	public void setLoginIdentifier(final String login) {
	//		_loginIdentifier = login;
	//		// Update the list of api keys and the characters related to the,.
	//		this.readApiKeys();
	//	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("AppModelStore [");
		//		buffer.append(" _neocomApiKeys(").append(_neocomApiKeys.size()).append("): ").append(_neocomApiKeys);
		buffer.append(" fittings(").append(fittings.size()).append("): ").append(fittings.size());
		buffer.append(" ]");
		return buffer.toString();
	}

	//	/**
	//	 * Get a complete list of the characters available indexed by the character id.
	//	 * 
	//	 * @return
	//	 */
	//	private HashMap<Long, NeoComCharacter> getCharacters() {
	//		HashMap<Long, NeoComCharacter> charCache = new HashMap<Long, NeoComCharacter>();
	//		for (final ApiKey key : apiKeys) {
	//			try {
	//				for (final NeoComCharacter eveChar : key.getApiCharacters()) {
	//					charCache.put(eveChar.getCharacterID(), eveChar);
	//				}
	//			} catch (ApiException apiex) {
	//				apiex.printStackTrace();
	//			}
	//		}
	//		return charCache;
	//	}

	private boolean isRestored() {
		return recovered;
	}

	//	@Deprecated
	//	private void readApiKeys() {
	//		AppModelStore.logger.info(">> [AppModelStore.readApiKeys]");
	//		// Access the database to get the list of keys. From that point on we can retrieve the characters easily.
	//		List<ApiKey> apiKeys = AppConnector.getDBConnector().getApiList4Login(this.getLoginIdentifier());
	//		// For each key get the list of characters and instantiate them to the resulting list.
	//		_neocomApiKeys = new ArrayList<NeoComApiKey>();
	//		_neocomCharacters = new ArrayList<NeoComCharacter>();
	//		for (ApiKey apiKey : apiKeys) {
	//			try {
	//				NeoComApiKey key = NeoComApiKey.build(apiKey.getKeynumber(), apiKey.getValidationcode());
	//				_neocomApiKeys.add(key);
	//				// Scan for the characters declared into this key.
	//				for (NeoComCharacter pilot : key.getApiCharacters()) {
	//					_neocomCharacters.add(pilot);
	//					AppModelStore.logger
	//							.info("-- [AppModelStore.readApiKeys]> Adding " + pilot.getName() + " to the _neocomCharacters");
	//				}
	//			} catch (ApiException apiex) {
	//				apiex.printStackTrace();
	//			}
	//		}
	//		AppModelStore.logger.info("<< [AppModelStore.readApiKeys]");
	//	}

}

// - UNUSED CODE ............................................................................................

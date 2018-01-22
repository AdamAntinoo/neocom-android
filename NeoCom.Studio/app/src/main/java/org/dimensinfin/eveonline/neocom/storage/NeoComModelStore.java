//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.storage;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;

import org.dimensinfin.core.parser.IPersistentHandler;
import org.dimensinfin.eveonline.neocom.activity.LoginProgressActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComModelStore extends DataManagementModelStore {

	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 8777607802616543118L;
	private static Logger logger = LoggerFactory.getLogger(NeoComModelStore.class);
	private static NeoComModelStore singleton = null;
	//	private static DataManagementModelStore singletonDelegate=DataManagementModelStore.getSingleton();

	/**
	 * Returns the single global instance of the Store to be used as an instance. In case the instance does not
	 * exists then we initiate the initialization process trying to create the most recent instance we have
	 * recorded or recreate it from scratch from the stored credentials.
	 *
	 * @return the single global sinleton.
	 */
	@Deprecated
	public static NeoComModelStore getSingleton () {
		//		NeoComModelStore.logger.info(">> [NeoComModelStore.getSingleton]");
		if ( null == NeoComModelStore.singleton ) {
			// Initiate the recovery.
			NeoComModelStore.initialize();
		}
		//		NeoComModelStore.logger.info("<< [NeoComModelStore.getSingleton]");
		return NeoComModelStore.singleton;
	}

	/**
	 * <p>Forces the initialization of the Model store from the api list file. Instead reading the data from the store
	 * file it will process the api list and reload all the character information from scratch.</p> <p>With the latest
	 * implementation the persistence of the Model is not required since we can replicate its stae from scratch with the
	 * parameters recovered at the Activities. We still connect the persistence Handler but should not be used.</p>
	 */
	public static void initialize () {
		NeoComModelStore.logger.info(">> [AppModelStore.initialize]");
		// Create a new from scratch. Tag it with the persistence handler so we can read/write its state.
		NeoComModelStore.singleton = new NeoComModelStore(new UserModelPersistenceHandler());
		// Load any data from storage and then update the information from CCP.
		//		AppModelStore.getSingleton().restore();
		//	AppModelStore.readApiKeys();

		// Make sure we get the characters on a thread out of the main one.
		//	AppModelStore.getSingleton().getCharacters();
		// Set back the current pilot whose id is stored on the _pilotIdentifier
		//		if ( NeoComModelStore.getSingleton()._pilotIdentifier > 0 ) {
		//			NeoComModelStore.getSingleton().activatePilot(NeoComModelStore.getSingleton()._pilotIdentifier);
		//		}
		NeoComModelStore.logger.info("<< [AppModelStore.initialize]");
	}

	// - S T A T I C   R E P L I C A T E D   M E T H O D S
	public static void activateActivity (final Activity currentActivity) {
		if ( null == singleton ) NeoComModelStore.initialize();
		singleton.activateActivityImpl(currentActivity);
	}

	public static Context getContext () {
		if ( null == singleton ) NeoComModelStore.initialize();
		return singleton.getContextImpl();
	}

	//	private static DataManagementModelStore getSingletonDelegated(){
	//		return singletonDelegate;
	//	}

	//	public static List<Credential> accessCredentialList () {
	//		return DataManagementModelStore.accessCredentialList();
	//	}
	//
	//	public static Credential getActiveCredential () {
	//		return DataManagementModelStore.getActiveCredential();
	//	}
	//
	//	public static Credential getCredential4Id (final long identifier) {
	//		return DataManagementModelStore.getCredential4Id(identifier);
	//	}
	//
	//	public static Credential activateCredential (final long identifier) {
	//		return DataManagementModelStore.activateCredential(identifier);
	//	}
	//	public static void cleanModel () {
	//		singletonDelegate.cleanModel();
	//	}
	//	public static List<Credential> accessCredentialList () {
	//		return getSingleton().accessCredentialList();
	//	}
	//
	//	public static Credential getActiveCredential () {
	//		return getSingleton().getActiveCredential();
	//	}
	//
	//	/**
	//	 * Search this identifier on the list of credentials and returns the findings.
	//	 */
	//	public static Credential getCredential4Id (final long identifier) {
	//		return getSingleton().getCredential4Id(identifier);
	//	}

	// - F I E L D - S E C T I O N ............................................................................
	/** Reference to the application menu to make it accessible to any level. */
	private transient Menu _appMenu = null;
	/** Reference to the current active Activity. Sometimes this is needed to access application resources. */
	private transient Activity _activity = null;
	/** Real pinter to the context that is really the item required to resolve most of the Android calls. */
	private Context _context = null;
	/** This is the unique list for all registered distinct credentials at the database. */
	//	private Hashtable<String, Login> _loginList = null;
	//	private final List<Credential> _credentialList = new ArrayList<>();
	/**
	 * Reference to the Active Credential that points to the Active Character. If null we should go back to the Login
	 * Activity to select a new Credential.
	 */
	//	private Credential _activeCredential = null;
	/**
	 * Reference to the current active Character. If this field is null we have to go back to the Login Activity to select
	 * another one.
	 */
	//	private NeoComCharacter _activeCharacter = null;

	/** This is the credential identifier assigned to this session and that relates to an specific credential. */
	//	private Login _loginIdentifier = null;
	/** Reference to the current active Character, be it a Pilot or a Corporation. */
	//	private transient NeoComCharacter _pilot = null;

	/** Check to verify if the recovery process is successful. Persistence is now not being used. */
	private boolean recovered = false;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	private NeoComModelStore (final IPersistentHandler storageHandler) {
		super(storageHandler);
		// On creation we can set the proper model persistence handler.
		try {
			this.setPersistentStorage(storageHandler);
			this.setAutomaticUpdate(false);
		} catch (final Exception ex) {
			// TODO This is a quite serious error because invalidates any storage of the model data
			ex.printStackTrace();
		}
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Stores a reference to the current activity. This is a mandatory action to point to the active Activity and maybe
	 * optional because most of the time the call is used to get access to a valid context.
	 */
	public void activateActivityImpl (final Activity currentActivity) {
		if ( null != currentActivity ) _activity = currentActivity;
		if ( null != currentActivity ) _context = currentActivity.getBaseContext();
	}

	/**
	 * Used to get access to some of the Android services.
	 *
	 * @return the last registered Activity or a new Activity that maybe will have connected to the Android service.
	 */
	public Activity getActivity () {
		if ( null == _activity ) return new LoginProgressActivity();
		return _activity;
	}

	/**
	 * Returns an ever valid <code>Context</code> that is obtained from the last registered Activity.
	 */
	public Context getContextImpl () {
		if ( null == _context )
			if ( null == _activity )
				throw new RuntimeException("RTX [NeoComModelStore]> Unable to get a valid Context.");
			else return _activity.getBaseContext();
		else return _context;
	}

	@Deprecated
	public Menu getAppMenu () {
		return _appMenu;
	}

	//	/**
	//	 * Gets a unique list of all the different characters available to the system from the different Logins.
	//	 * This action is done without disturbing the login selected or any other AppModel structure. The list is
	//	 * not kept and is calculated each time the call is received.
	//	 */
	//	@Deprecated
	//	public Set<NeoComCharacter> accessCharacterList () {
	//		Hashtable<String, Login> loginList = NeoComAppConnector.getSingleton().getModelStore().accessLoginList();
	//		Set<NeoComCharacter> charList = new HashSet<NeoComCharacter>();
	//		for (String key : loginList.keySet()) {
	//			Login login = loginList.get(key);
	//			for (NeoComCharacter c : login.getCharacters()) {
	//				charList.add(c);
	//			}
	//		}
	//		return charList;
	//	}

	//	/**
	//	 * Returns the current list of active credentials. At this stage credentials are generated from a mix of
	//	 * api keys and esi tokens. After march 2018 only esi tokens will be available.
	//	 * If the list is empty we go to the database to fetch any valid key or identifier and merge all them
	//	 * into a single table indexed by the Character identifier. If there are more that one key or credential for
	//	 * the same character they are merged taking on account the mask value for keys or the scope list for
	//	 * credentials.
	//	 */
	//	public List<Credential> accessCredentialList () {
	//		if ( _credentialList.size() < 1 ) {
	//			// Read and process the list of ApiKeys and Credentials to get a single Character list.
	//			final Hashtable<String, Login> logins = NeoComDatabase.accessAllLogins();
	//			final List<Credential> credentials = NeoComDatabase.accessAllCredentials();
	//			//			final List<Credential> results = new ArrayList<>();
	//			_credentialList.clear();
	//			for (Credential current : credentials) {
	//				_credentialList.add(current);
	//				// Scan the keys to search for matches.
	//				final long cid = current.getAccountId();
	//				for (Login login : logins.values()) {
	//					for (NeoComCharacter character : login.getCharacters()) {
	//						if ( character.getCharacterID() == cid ) {
	//							current.setKeyCode(character.getAuthorization().getKeyID())
	//										 .setValidationCode(character.getAuthorization().getVCode())
	//										 //							current.setAuthorizationmask(character.getAuthorization().get...)
	//										 .store();
	//						}
	//					}
	//				}
	//			}
	//		}
	//		return _credentialList;
	//	}

	//
	//	/**
	//	 * Activated the selected credential as the active Credential. This will also point to the current Character but
	//	 * allows for lazy evaluation of most of the Character data to be obtained when the character is later used on the
	//	 * interfaces.
	//	 * @param identifier unique account number of character identifier.
	//	 * @return the new credential made active. Raises a RuntimeException if the Credential is not found.
	//	 */
	//	public Credential activateCredential (final long identifier) {
	//		// Check if the list of credentials is already loaded. If not get it from the database.
	//		if ( _credentialList.size() < 1 ) accessCredentialList();
	//		if ( null != _activeCredential )
	//			if ( _activeCredential.getAccountId() == identifier ) return _activeCredential;
	//		// Search for the credential on the list.
	//		for (Credential target : _credentialList) {
	//			if ( target.getAccountId() == identifier ) {
	//				_activeCredential = target;
	//				return _activeCredential;
	//			}
	//		}
	//		// If we reach this point this means that we have not found the credential. This is an exception.
	//		throw new RuntimeException("RT [NeoComModelStore]> Credential with id " + identifier + " not found.");
	//	}

	//	/**
	//	 * Gets access to the complete list of Logins. If this list is empty we go back to the database to populate
	//	 * it.
	//	 */
	//	@Deprecated
	//	public Hashtable<String, Login> accessLoginList () {
	//		if ( null == _loginList ) {
	//			_loginList = NeoComAppConnector.getSingleton().getDBConnector().queryAllLogins();
	//		}
	//		return _loginList;
	//	}


	//	/**
	//	 * This is the main activation entry point for the Login. If there are no logins already on the ModelStore
	//	 * we should get them from the backend database and then search for the indicated login. If finally that
	//	 * login is not found we can fire an Exception.
	//	 */
	//	@Deprecated
	//	public Login activateLoginIdentifier (final String loginTarget) {
	//		NeoComModelStore.logger.info(">< [NeoComModelStore.activateLoginIdentifier]> loginTarget: {}", loginTarget);
	//		if ( null == _loginList ) {
	//			_loginList = NeoComAppConnector.getSingleton().getDBConnector().queryAllLogins();
	//		}
	//		// OPTIMIZATION: If the character is already the selected one do nothing.
	//		if ( null != _loginIdentifier )
	//			if ( _loginIdentifier.getName().equalsIgnoreCase(loginTarget) ) return _loginIdentifier;
	//		for (String key : _loginList.keySet()) {
	//			if ( _loginList.get(key).getName().equalsIgnoreCase(loginTarget) ) {
	//				_loginIdentifier = _loginList.get(key);
	//				//				// Update the list of api keys and the characters related to the login.
	//				//				this.readApiKeys();
	//				return _loginIdentifier;
	//			}
	//		}
	//		// We run through all the login list and did not found the login. Or a problem of an error. Fire exception.
	//		throw new NeocomRuntimeException(
	//				"RT [NeoComModelStore.activateLoginIdentifier]> Login " + loginTarget + " not found on current login list.");
	//	}

	//	/**
	//	 * Searches for the pilot on the character list of the active Login and stores it to the current slot.<br>
	//	 * On the new implementation change the search to delegate the action to a new data model where the list of
	//	 * keys comes from a database search and the list of associated keys are stored on the App Model. The list
	//	 * of characters are on the Login class and there should be an active Login. Otherwise we raise and
	//	 * exception because there is no Login or the Character is not found.
	//	 *
	//	 * @param characterid id of the character to activate and select for work with.
	//	 */
	//	@Deprecated
	//	public NeoComCharacter activatePilot (final long characterid) {
	//		NeoComModelStore.logger.info(">< [NeoComModelStore.activatePilot]> Id: {}", characterid);
	//		// If the current pilot is the one searched simplify the search.
	//		if ( (null != _pilot) && ((_pilot.getCharacterID()) == characterid) ) return _pilot;
	//		// If the active Login is empty then we do not know where to search.
	//		if ( null == _loginIdentifier ) throw new NeocomRuntimeException(
	//				"RT [NeoComModelStore.activatePilot]> There is no active Login. Cannot search for character.");
	//
	//		// Search for character at the active Login.
	//		NeoComCharacter hit = _loginIdentifier.searchCharacter(characterid);
	//		if ( null == hit ) throw new RuntimeException(
	//				"RT [NeoComModelStore.activatePilot]> Pilot with id: " + characterid + " not located at active Login.");
	//		_pilot = hit;
	//		return hit;
	//	}

	//	@Deprecated
	//	public NeoComCharacter activatePilot (final String characterstring) {
	//		return this.activatePilot(Long.valueOf(characterstring).longValue());
	//	}

	//	/**
	//	 * Returns any previously activated character. Raises a runtime exception if the character is null.
	//	 */
	//	public NeoComCharacter getActiveCharacter () {
	//		if ( null == _pilot ) {
	//			// Try to get the Character if the credential is set.
	//			if ( null == _activeCredential )
	//				throw new RuntimeException("RT> Accessing an invalid Character. Select a new character from the list of Credentials.");
	//			else {
	//				// Get the Character from the Active Credential.
	//				return activatePilot(_activeCredential.getAccountId());
	//			}
	//		}
	//		return _pilot;
	//	}

	//	/**
	//	 * Returns the current active Credential that matched the active character. Raises a runtime exception if the
	//	 * character is null.
	//	 */
	//	public Credential getActiveCredential () {
	//		if ( null == _activeCredential )
	//			throw new RuntimeException("RT> Accessing an invalid Credential. Select a new character from the list of Credentials.");
	//		return _activeCredential;
	//	}

	//	/**
	//	 * Return the list of pilots that are currently active as selected by the user. The user may deactivate some
	//	 * pilots to reduce the view of data or pilots that are no longer active.<br>
	//	 * The start list is the list of characters associated to the llist of kays assocated to the current login.
	//	 */
	//	@Deprecated
	//	public List<NeoComCharacter> getActiveCharacters () {
	//		if ( null == _loginIdentifier )
	//			return new Vector<NeoComCharacter>();
	//		else
	//			return _loginIdentifier.getCharacters();
	//	}

	//	@Deprecated
	//	@Override
	//	public Login getActiveLogin () {
	//		return _loginIdentifier;
	//	}
	//
	//	@Deprecated
	//	@Override
	//	public String getActiveLoginName () {
	//		return _loginIdentifier.getName();
	//	}


	//	/**
	//	 * Returns the current active pilot or corporation if exists. If the value is not defined then this means
	//	 * that the model store has been initialized and that we should go back to the pilot roaster page to select
	//	 * a new one.
	//	 */
	//	@Deprecated
	//	public NeoComCharacter getCurrentPilot () {
	//		if ( null == _pilot )
	//			throw new NeocomRuntimeException("UNEX. Selected character is not selected. Go back to home to select one.");
	//		else
	//			return _pilot;
	//	}

	public Activity getFirstActivity () {
		return new LoginProgressActivity();
	}

	//	public HashMap<String, Fitting> getFittings() {
	//		return fittings;
	//	}

	//	@Deprecated
	//	public String getLoginIdentifier () {
	//		if ( null != _loginIdentifier ) return _loginIdentifier.getName();
	//		throw new NeocomRuntimeException(
	//				"RT [AppModelStore.getLoginIdentifier]>Login Identifier not defined. Setting to default if possible.");
	//	}

	//	@Deprecated
	//	public NeoComCharacter getPilot () {
	//		return this.getCurrentPilot();
	//	}

	/**
	 * Checks if we have data or we have to restore from disk, usually only on the initialization.
	 *
	 * @return true is we have to read the data because the containers are empty.
	 */
	public boolean needsRestore () {
		//	if (_neocomApiKeys.size() < 1) return true;
		return false;
	}

	/**
	 * Restores the stored state from the disk file. If there any problem the method returns true.
	 *
	 * @return <code>true</code> if there is a problem and the restore was not executed.
	 */
	@Override
	public boolean restore () {
		NeoComAppConnector.getSingleton().startChrono();
		recovered = super.restore();
		if ( recovered ) {
			NeoComModelStore.logger.info("~~ [AppModelStore.restore] Time lapse for APPSTORE[RESTORE] - "
					+ NeoComAppConnector.getSingleton().timeLapse());
			return recovered;
		} else {
			// The handler was not able to retrieve the file. Possibly because there was no file.
			this.setDirty(true);
			return false;
		}
	}

	/**
	 * Save data to disk only when we have detected it has changed.
	 */
	@Override
	public boolean save () {
		if ( this.isDirty() ) {
			// Clean the dirty flag.
			this.setDirty(false);
			recovered = true;
			NeoComAppConnector.getSingleton().startChrono();
			final boolean state = super.save();
			NeoComModelStore.logger
					.info("~~ Time lapse for APPSTORE[SAVE] - " + NeoComAppConnector.getSingleton().timeLapse());
			return state;
		} else
			return false;
	}

	@Deprecated
	public void setAppMenu (final Menu appMenu) {
		_appMenu = appMenu;
	}

	@Override
	public String toString () {
		final StringBuffer buffer = new StringBuffer("NeoComModelStore [");
		//		buffer.append("Credentials [").append(_credentialList.size()).append("]: ").append(_credentialList.toString());
		buffer.append(" ]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	private boolean isRestored () {
		return recovered;
	}

	//	/**
	//	 * This methods should do a complete cleanup of the data structures to force a new reload of the data from its
	//	 * sources. Also check that the delegated instances are on place. In the future it will also need to close databases
	//	 * to reset its status.
	//	 */
	//	public void cleanModel () {
	////		_loginList = null;
	////		_loginIdentifier = null;
	////		_pilot = null;
	//		singleton = null;
	//		_credentialList.clear();
	//	}
}

// - UNUSED CODE ............................................................................................

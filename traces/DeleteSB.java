	/**
	 * removes from the application database any asset and blueprint that contains the special -1 code as the
	 * owner identifier. Those records are from older downloads and have to be removed to avoid merging with the
	 * new download.
	 */
	public synchronized void clearInvalidRecords( final long pilotid ) {
		logger.info(">> [NeoComSBDBHelper.clearInvalidRecords]> pilotid", pilotid);
		synchronized (connectionSource) {
			try {
				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
					public Void call() throws Exception {
						// Remove all assets that do not have a valid owner.
						final DeleteBuilder<NeoComAsset, String> deleteBuilder = getAssetDao().deleteBuilder();
						deleteBuilder.where().eq("ownerId", (pilotid * -1));
						int count = deleteBuilder.delete();
						logger.info("-- [NeoComSBDBHelper.clearInvalidRecords]> Invalid assets cleared for owner {}: {}", (pilotid * -1), count);

						// Remove all blueprints that do not have a valid owner.
						final DeleteBuilder<NeoComBlueprint, String> deleteBuilderBlueprint = getBlueprintDao().deleteBuilder();
						deleteBuilderBlueprint.where().eq("ownerId", (pilotid * -1));
						count = deleteBuilderBlueprint.delete();
						logger.info("-- [NeoComSBDBHelper.clearInvalidRecords]> Invalid blueprints cleared for owner {}: {}", (pilotid * -1),
								count);
						return null;
					}
				});
			} catch (final SQLException ex) {
				logger.warn("W> [NeoComSBDBHelper.clearInvalidRecords]> Problem clearing invalid records. " + ex.getMessage());
			} finally {
				logger.info("<< [NeoComSBDBHelper.clearInvalidRecords]");
			}
		}
	}

	/**
	 * Changes the owner id for all records from a new download with the id of the current character. This
	 * completes the download and the assignment of the resources to the character without interrupting the
	 * processing of data by the application.
	 */
	public synchronized void replaceAssets( final long pilotid ) {
		logger.info(">> [NeoComSBDBHelper.clearInvalidRecords]> pilotid: {}", pilotid);
		synchronized (connectionSource) {
			try {
				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
					public Void call() throws Exception {
						// Remove all assets from this owner before adding the new set.
						final DeleteBuilder<NeoComAsset, String> deleteBuilder = getAssetDao().deleteBuilder();
						deleteBuilder.where().eq("ownerId", pilotid);
						int count = deleteBuilder.delete();
						logger.info("-- [NeoComSBDBHelper.clearInvalidAssets]> Invalid assets cleared for owner {}: {}", pilotid, count);

						// Replace the owner to vake the assets valid.
						final UpdateBuilder<NeoComAsset, String> updateBuilder = getAssetDao().updateBuilder();
						updateBuilder.updateColumnValue("ownerId", pilotid)
								.where().eq("ownerId", (pilotid * -1));
						count = updateBuilder.update();
						logger.info("-- [NeoComSBDBHelper.replaceAssets]> Replace owner {} for assets: {}", pilotid, count);
						return null;
					}
				});
			} catch (final SQLException ex) {
				logger.warn("W> [NeoComSBDBHelper.replaceAssets]> Problem replacing records. " + ex.getMessage());
			} finally {
				logger.info("<< [NeoComSBDBHelper.replaceAssets]");
			}
		}
	}

	public synchronized void replaceBlueprints( final long pilotid ) {
		logger.info(">> [NeoComSBDBHelper.replaceBlueprints]> pilotid: {}", pilotid);
		synchronized (connectionSource) {
			try {
				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
					public Void call() throws Exception {
						// Remove all assets that do not have a valid owner.
						final UpdateBuilder<NeoComBlueprint, String> updateBuilder = getBlueprintDao().updateBuilder();
						updateBuilder.updateColumnValue( "ownerId", pilotid)
								.where().eq("ownerId", (pilotid * -1));
						int count = updateBuilder.update();
						logger.info("-- [NeoComSBDBHelper.replaceBlueprints]> Replace owner {} for blueprints: {}", pilotid, count);
						return null;
					}
				});
			} catch (final SQLException ex) {
				logger.warn("W> [NeoComSBDBHelper.replaceBlueprints]> Problem replacing records. " + ex.getMessage());
			} finally {
				logger.info("<< [NeoComSBDBHelper.replaceBlueprints]");
			}
		}
	}
	private int readDatabaseVersion() {
		// Access the version object persistent on the database.
		try {
			Dao<DatabaseVersion, String> versionDao = this.getVersionDao();
			QueryBuilder<DatabaseVersion, String> queryBuilder = versionDao.queryBuilder();
			PreparedQuery<DatabaseVersion> preparedQuery = queryBuilder.prepare();
			List<DatabaseVersion> versionList = versionDao.query(preparedQuery);
			if (versionList.size() > 0) {
				DatabaseVersion version = versionList.get(0);
				return version.getVersionNumber();
			} else
				return 0;
		} catch (SQLException sqle) {
			logger.warn("W- [NeoComSBDBHelper.readDatabaseVersion]> Database exception: " + sqle.getMessage());
			return 0;
		}
	}

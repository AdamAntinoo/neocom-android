package org.dimensinfin.eveonline.neocom.test.support.adapters;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

import java.sql.SQLException;
import java.util.Objects;

public class SupportCredentialRepository extends CredentialRepository {
	public int deleteAll() {
		try {
			final DeleteBuilder<Credential, String> deleteBuilder = this.credentialDao.deleteBuilder();
			deleteBuilder.where().isNotNull("uniqueCredential");
			final int recordsDeleted = deleteBuilder.delete();
			logger.info("-- [SupportCredentialRepository.deleteAll]> Deleted {} records", recordsDeleted);
			return recordsDeleted;
		} catch (SQLException sqle) {
			logger.info("EX [SupportCredentialRepository.deleteAll]> SQL exception while deleting records: {}",
			            sqle.getMessage());
			return 0;
		}
	}

	public Long countRecords() {
		try {
			return this.credentialDao.countOf();
		} catch (SQLException sqle) {
			logger.info("EX [SupportCredentialRepository.countRecords]> SQL exception while counting records: {}",
			            sqle.getMessage());
			return 0L;
		}
	}

	// - B U I L D E R
	public static class Builder {
		protected SupportCredentialRepository onConstruction;

		public Builder() {
			this.onConstruction = new SupportCredentialRepository();
		}

		public SupportCredentialRepository.Builder withCredentialDao( final Dao<Credential, String> credentialDao ) {
			this.onConstruction.credentialDao = credentialDao;
			return this;
		}

		public SupportCredentialRepository build() {
			Objects.requireNonNull(this.onConstruction.credentialDao);
			return this.onConstruction;
		}
	}
}

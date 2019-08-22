package org.dimensinfin.eveonline.neocom.test.support.converters;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;

import java.util.Map;

public class CucumberTable2CredentialConverter extends CucumberTableConverter<Credential> {
	private static final String ACCOUNT_ID = "accountId";
	private static final String ACCOUNT_NAME = "accountName";
	private static final String DATA_SOURCE = "dataSource";
	private static final String ACCESS_TOKEN = "accessToken";
	private static final String REFRESH_TOKEN = "refreshToken";
	private static final String SCOPE = "scope";

	@Override
	public Credential convert( final Map<String, String> cucumberRow ) {
		return new Credential.Builder(Integer.parseInt(cucumberRow.get(ACCOUNT_ID)))
				       .withAccountName(cucumberRow.get(ACCOUNT_NAME))
				       .withDataSource(cucumberRow.get(DATA_SOURCE))
				       .withAccessToken(cucumberRow.get(ACCESS_TOKEN))
				       .withRefreshToken(cucumberRow.get(REFRESH_TOKEN))
				       .withScope(cucumberRow.get(SCOPE))
				       .build();
	}
}
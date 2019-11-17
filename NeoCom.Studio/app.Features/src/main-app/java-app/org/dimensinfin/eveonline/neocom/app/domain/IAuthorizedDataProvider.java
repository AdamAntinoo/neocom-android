package org.dimensinfin.eveonline.neocom.app.domain;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;

@FunctionalInterface
public interface IAuthorizedDataProvider {
	String getDataFormatted ( final Credential credential);
}

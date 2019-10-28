package org.dimensinfin.eveonline.neocom.app.datasource;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.exception.ExceptionReport;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

public class SplashActionsDataSourceTest {
	private Bundle extras;
	private ControllerFactory factory;
	private SplashActionsDataSource dataSource4Test;
	private CredentialRepository credentialRepository;

	@Before
	public void setUp() {
		this.extras = new Bundle();
		this.factory = Mockito.mock(ControllerFactory.class);
		this.credentialRepository = Mockito.mock(CredentialRepository.class);
		this.dataSource4Test = new SplashActionsDataSource.Builder()
				                       .addIdentifier("-TEST-DATASOURCE-IDENTIFIER-")
				                       .withExtras(extras)
				                       .withVariant(PageNamesType.SPLASH.name())
				                       .withFactory(factory)
				                       .withCredentialRepository(this.credentialRepository)
				                       .withName("-TEST-NAME-")
				                       .withName("-TEST-VERSION-")
				                       .build();
		Assert.assertNotNull(this.dataSource4Test);
	}

	@Test
	public void buildComplete() {
		final Bundle extras = new Bundle();
		final ControllerFactory factory = Mockito.mock(ControllerFactory.class);
		final CredentialRepository credentialRepository = Mockito.mock(CredentialRepository.class);
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier("-TEST-DATASOURCE-IDENTIFIER-")
				                                           .withExtras(extras)
				                                           .withVariant(PageNamesType.SPLASH.name())
				                                           .withFactory(factory)
				                                           .withCredentialRepository(credentialRepository)
				                                           .withName("-TEST-NAME-")
				                                           .withName("-TEST-VERSION-")
				                                           .build();
		Assert.assertNotNull(dataSource);
	}

	@Test
	public void prepareModel() {
		this.dataSource4Test.prepareModel();
		Assert.assertEquals(1, this.dataSource4Test.getHeaderModelRoot().size());
		final ICollaboration item = this.dataSource4Test.getHeaderModelRoot().get(0);
		Assert.assertEquals(AppVersion.class, item.getClass());
	}

	@Test
	public void collaborate2Model() {
		final Credential credential = Mockito.mock(Credential.class);
		Mockito.when(credential.getRefreshToken()).thenReturn("-TEST-REFRESH-DUMMY-TOKEN-");
		Mockito.when(credential.getAccountName()).thenReturn("-TEST-ACCOUNT-NAME-");
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add(credential);
		this.credentialRepository = Mockito.mock(CredentialRepository.class);
		Mockito.when(this.credentialRepository.accessAllCredentials()).thenReturn(credentialList);
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier("-TEST-DATASOURCE-IDENTIFIER-")
				                                           .withExtras(extras)
				                                           .withVariant(PageNamesType.SPLASH.name())
				                                           .withFactory(factory)
				                                           .withCredentialRepository(this.credentialRepository)
				                                           .withName("-TEST-NAME-")
				                                           .withName("-TEST-VERSION-")
				                                           .build();
		dataSource.collaborate2Model();

		Assert.assertEquals(0, dataSource.getHeaderModelRoot().size());
		Assert.assertEquals(4, dataSource.getDataModelRoot().size());
		Assert.assertEquals(PanelTitle.class, dataSource.getDataModelRoot().get(0).getClass());
		Assert.assertEquals(credential.getClass(), dataSource.getDataModelRoot().get(1).getClass());
	}

	@Test
	public void collaborate2ModelException() {
		final Credential credential = Mockito.mock(Credential.class);
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add(credential);
		this.credentialRepository = Mockito.mock(CredentialRepository.class);
		Mockito.when(this.credentialRepository.accessAllCredentials()).thenReturn(credentialList);
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier("-TEST-DATASOURCE-IDENTIFIER-")
				                                           .withExtras(extras)
				                                           .withVariant(PageNamesType.SPLASH.name())
				                                           .withFactory(factory)
				                                           .withCredentialRepository(this.credentialRepository)
				                                           .withName("-TEST-NAME-")
				                                           .withName("-TEST-VERSION-")
				                                           .build();
		dataSource.collaborate2Model();

		Assert.assertEquals(0, dataSource.getHeaderModelRoot().size());
		Assert.assertEquals(4, dataSource.getDataModelRoot().size());
		Assert.assertEquals(PanelTitle.class, dataSource.getDataModelRoot().get(0).getClass());
		Assert.assertEquals(ExceptionReport.class, dataSource.getDataModelRoot().get(1).getClass());
	}
}
package com.wennsoft.test;

//import static org.junit.Assert.*;

//import org.junit.*;

import org.exoplatform.container.StandaloneContainer;

public class PortalSettingsTest {

	//@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	//@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	//@Before
	public void setUp() throws Exception {
			System.out.println("Set-Up");
			
			String containerConf = PortalSettingsTest.class.getResource("/conf/standalone/test-configuration.xml").toString();
			   StandaloneContainer.addConfigurationURL(containerConf);

			      StandaloneContainer container = StandaloneContainer.getInstance();
	
	}

	//@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void test() {
		
		/*
		RepositoryResult portalSettings = ConnectRepository.INSTANCE.loadConnectSettings();
		
		System.out.println(portalSettings.getHistoryLimit());
		
		PortalSettings portalSettingsOrig = portalSettings;
		
		//changing portal settings
		portalSettings.setHistoryLimit(25);
		portalSettings.setTermsAndConditions(false);
		portalSettings.setWebServiceUri("http://test.com");
		
		System.out.println(portalSettings.getHistoryLimit());
		
		PageAccess pageAccessElement = new PageAccess();
		pageAccessElement.setEntity("testEntity");
		List<PageAccess> pageAccess = new ArrayList<PageAccess>();
		pageAccess.add(pageAccessElement);
		portalSettings.setPageAccess(pageAccess);
		
		System.out.println("After setting change");
		
		//saving portal settings
		ConnectRepository.INSTANCE.saveConnectSettings(portalSettings);
		
		System.out.println("After setting save");
		
		//Retrieving saved portal settings
		PortalSettings portalSettingsSaved = ConnectRepository.INSTANCE.loadConnectSettings();
		
		System.out.println(portalSettingsSaved.getHistoryLimit());
		System.out.println("After reload");
		
		//assert statements
		assertEquals(25,portalSettingsSaved.getHistoryLimit());
		assertFalse("TOC.html",portalSettingsSaved.getTermsAndConditions());
		assertEquals("http://test.com",portalSettingsSaved.getWebServiceUri());
		
		System.out.println("After assertions");
		
		//resetting portal settings
		ConnectRepository.INSTANCE.saveConnectSettings(portalSettingsOrig);
		System.out.println("FINISHED");
		
		System.out.println("After settings reset"); */
	}


}

package com.dfs.helper.properties;

import com.dfs.selenium.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class PropertiesHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesHelper.class);
	
	private static final String FLOW_DB = "flowDB";
	private static final String FLOW_DB_HOST = "flowDBHost"; 
	private static final String FLOW_DB_PORT = "flowDBPort"; 
	private static final String FLOW_DB_USER = "flowDBUser"; 
	private static final String FLOW_DB_PASSWD = "flowDBPasswd";

	private static final String SELUSER_DOWNLOADS_PATH = "/home/seluser/Downloads";

	private Properties propertiesFile;
	private Properties labelsFile;

	public PropertiesHelper() {

		propertiesFile = new java.util.Properties();
		labelsFile = new java.util.Properties();
		try {
			propertiesFile.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
			labelsFile.load(this.getClass().getClassLoader().getResourceAsStream("labels.properties"));
		} catch (Exception eta) {
			LOGGER.error("Exception while loading config file", eta);
		}

	}


	public String getSbetProjectCode() {
		return propertiesFile.getProperty("sbetProjectCode");
	}

	public String getSbetCustomer() {
		return propertiesFile.getProperty("sbetCustomer");
	}

	public String getSbetContact() {
		return propertiesFile.getProperty("sbetContact");
	}

	public String getSbetContactPassword() { return propertiesFile.getProperty("SbetContactPassword"); }


	public String getWebEditorPort() {
		return propertiesFile.getProperty("webEditorPort");
	}

	public String getProperty(Properties configFile, String key) {
		return configFile.getProperty(key);
	}

	public String getPMFullname() {
		return propertiesFile.getProperty("pmFullName");
	}

	public String getBTFullname() {
		return propertiesFile.getProperty("btFullName");
	}

	public String getBeeUser() {
		return propertiesFile.getProperty("btUser");
	}

	public String getBeePass() {
		return propertiesFile.getProperty("btPwd");
	}

	public String getCustomer() {
		return propertiesFile.getProperty("custUser");
	}

	public String getCustomerPass() {
		return propertiesFile.getProperty("custPwd");
	}
	
	public String getCustomerAdmin() {
		return propertiesFile.getProperty("custAdminUser");
	}
	
	
	public String getCustomerAdminPass() {
		return propertiesFile.getProperty("custAdminPwd");
	}
	
	public String getCustomerEmail() {
		return propertiesFile.getProperty("custEmail");
	}

	public String getPmUser() {
		return propertiesFile.getProperty("pmUser");
	}

	public String getPmTeam() {
		return propertiesFile.getProperty("pmUser") + "Team";
	}


	public String getSpecialProvider() {
		return propertiesFile.getProperty("specialProvider");
	}

	public String getSpecialProviderPwd() {
		return propertiesFile.getProperty("specialProviderPwd");
	}

	public String getPmPass() {
		return propertiesFile.getProperty("pmPwd");
	}
	
	public String getPmEmail() {
		return propertiesFile.getProperty("pmEmail");
	}

	public String getTitle() {
		return propertiesFile.getProperty("title");
	}

	public String getURL() {
		String url = System.getProperty("url") != null ? System.getProperty("url") : propertiesFile.getProperty("url");
		LOGGER.debug("Using url {}", url);
		return url;
	}

	public String getDownloadsPath() {
		return DriverManager.isDriverIsRemote() ? SELUSER_DOWNLOADS_PATH : System.getProperty("user.home") + File.separator + "Downloads";
	}

	public String getFlowDBHost() {
		return System.getProperty(FLOW_DB_HOST) != null ? System.getProperty(FLOW_DB_HOST) : propertiesFile.getProperty(FLOW_DB_HOST);
	}
	
	public String getFlowDBPort() {
		return System.getProperty(FLOW_DB_PORT) != null ? System.getProperty(FLOW_DB_PORT) : propertiesFile.getProperty(FLOW_DB_PORT);
	}
	
	public String getFlowDBUser() {
		return System.getProperty(FLOW_DB_USER) != null ? System.getProperty(FLOW_DB_USER) : propertiesFile.getProperty(FLOW_DB_USER);
	}
	
	public String getFlowDBPasswd() {
		return System.getProperty(FLOW_DB_PASSWD) != null ? System.getProperty(FLOW_DB_PASSWD) : propertiesFile.getProperty(FLOW_DB_PASSWD);
	}
	
	public String getWebDriver() {
		String litwebdriver = "webdriver";
		String webdriver = System.getProperty(litwebdriver) != null ? System.getProperty(litwebdriver) : propertiesFile.getProperty(litwebdriver);
		LOGGER.debug("Using webdriver {}", webdriver);
		return webdriver;
	}

	public String getVMName() {
		String vmname;
		vmname = getURL().replace("http://", "").replace(".global.sdl.corp", "");
		vmname = vmname.replace("https://", "");
		return vmname;
	}

	private String getFlowDB() {
		String flowDB = System.getProperty(FLOW_DB) != null ? System.getProperty(FLOW_DB)
				: propertiesFile.getProperty(FLOW_DB);
		LOGGER.debug("Using flow DB {}", flowDB);
		return flowDB;
	}

	public String getFlowDBName() {
		String vmName = getVMName();
		String flowDB = null;

		if (isLocalURL()) {
			flowDB = getFlowDB();
		}

		return flowDB != null && !flowDB.isEmpty() ? flowDB : vmName + "_Flow";
	}
	
	
	public String getMTDBName() {
		return getVMName() + "_MT";
	}
	
	public boolean isLocalURL() {
		String vmName = getVMName();
		return vmName.contains("localhost") || vmName.contains("127.0.0") || vmName.contains("0.0.0.0");
	}

	public String getLocalLogo() {
		return propertiesFile.getProperty("localLogo");
	}

	public Integer getWaitTimeout() {
		return Integer.parseInt(propertiesFile.getProperty("waitTimeout"));
	}

	public String getAdminUser() {
		return propertiesFile.getProperty("adminUser");
	}

	public String getAdminPwd() {
		return propertiesFile.getProperty("adminPwd");
	}

	public String getProviderUser() {
		return propertiesFile.getProperty("providerUser");
	}

	public String getProviderUserPwd() {
		return propertiesFile.getProperty("providerPwd");
	}

	public String getProviderUserInitialPwd() {
		return propertiesFile.getProperty("providerInitialPwd");
	}

	public String getProviderFullName() {
		return propertiesFile.getProperty("providerFullName");
	}

	// internal provider user
	public String getInternalProviderName() {
		return propertiesFile.getProperty("intProviderName");
	}
	public String getInternalProviderUser() {
		return propertiesFile.getProperty("internalProviderUser");
	}
	
	public String getInternalProviderPwd() {
		return propertiesFile.getProperty("internalProviderPass");
	}
	
	public String getInternalProviderFullName() {
		return propertiesFile.getProperty("intproviderFullName");
	}
	
	// agency user
	public String getAgencyName() {
		return propertiesFile.getProperty("automationAgencyName");
	}
	
	public String getAgencyFullName() {
		return propertiesFile.getProperty("automationAgencyFullName");
	}
	
	//agency PM user
	public String getAgencyPMName() {
		return propertiesFile.getProperty("automationAgencyPMName");
	}
	
	public String getAgencyPMFullName() {
		return propertiesFile.getProperty("automationAgencyPMFullName");
	}

	public String getAgencyPMUser() {
		return propertiesFile.getProperty("automationAgencyPMUser");
	}
	
	public String getAgencyPMPass() {
		return propertiesFile.getProperty("automationAgencyPMPass");
	}
	
	// agency Provider user
	public String getAgencyProviderName() {
		return propertiesFile.getProperty("automationAgencyProviderName");
	}

	public String getAgencyProviderFullName() {
		return propertiesFile.getProperty("automationAgencyProviderFullName");
	}

	public String getAgencyProviderUser() {
		return propertiesFile.getProperty("automationAgencyProviderUser");
	}

	public String getAgencyProviderPass() {
		return propertiesFile.getProperty("automationAgencyProviderPass");
	}

	// email sending config
	public String getSerializationPath() {
		return propertiesFile.getProperty("serializationPath");
	}

	public String getEmailServer() {
		return propertiesFile.getProperty("server");
	}

	public String getSenderEmail() {
		return propertiesFile.getProperty("email");
	}
	
	public String getEmailPort() {
		return propertiesFile.getProperty("emailPort");
	}
	
	public String getEmailUserName() {
		return propertiesFile.getProperty("emailUserName");
	}
	
	public String getEemailUserPassword() {
		return propertiesFile.getProperty("emailUserPassword");
	}

	public String getIdentifierPrefix() {
		return propertiesFile.getProperty("api.id.prefix");
	}

	public String getClientFullName() {
		return propertiesFile.getProperty("clientFullName");
	}
	public String getClientFullName1() {
		return propertiesFile.getProperty("clientFullName1");
	}

	public String getProjectFile() {
		return propertiesFile.getProperty("projectFile");
	}
	
	public String projectAPI() {
		return propertiesFile.getProperty("projectAPI");
	}
	
	public String getProjectReadyPrefix() {
		return propertiesFile.getProperty("projectReadyPrefix");
	}
	
	public String getProjectReadyProviderPrefix() {
		return propertiesFile.getProperty("projectReadyProviderPrefix");
	}
	
	public String getProjectReadyWebEditorPrefix() {
		return propertiesFile.getProperty("projectReadyWebEditorPrefix");
	}
	
	public String getProjectCompletedPrefix() {
		return propertiesFile.getProperty("projectCompletedPrefix");
	}
	
	public String getProjectWebEditorFullDocsPrefix() {
		return propertiesFile.getProperty("projectWebEditorFullDocsPrefix");
	}
	
	public String getProjectWebEditorSpliDocsPrefix() {
		return propertiesFile.getProperty("projectWebEditorSplitDocsPrefix");
	}
	

	/////////////////////////////////////////////////////////
	// Returning properties from the Labels.properties File//
	/////////////////////////////////////////////////////////

	public String getWrongUsePwdrMsg() {
		return labelsFile.getProperty("WrongUsePwdrMsg");
	}

	public String getEmptyPwdrMsg() {
		return labelsFile.getProperty("EmptyPwdrMsg");

	}

	public String getForcedChangePwdMsg() {
		return labelsFile.getProperty("ForcedChangePwd");

	}
	public String getProjectListLabel() {
		return labelsFile.getProperty("Project.menu.list");

	}

	public String getProjectGroupsLabel() {
		return labelsFile.getProperty("Project.menu.groups");

	}

	public String getProjectTasksLabel() {
		return labelsFile.getProperty("Project.menu.tasks");

	}
	
	public String getEnterAgencyCorporateNameLabel() {
		return labelsFile.getProperty("EnterCorporateNameAlertMsg");
	}

	public String getCorporateNameExistsLabel() {
		return labelsFile.getProperty("CorporateNameExistAlertMsg");
	}
	
	public String getPooledTasksLabel() {
		return labelsFile.getProperty("Project.menu.pooledTasks");

	}

	public String getProjectsToInvoiceLabel() {
		return labelsFile.getProperty("Project.menu.toInvoice");

	}

	public String getCustomersManageLabel() {
		return labelsFile.getProperty("customers.topMenu.manage");

	}

	public String getCustomersInvoicesLabel() {
		return labelsFile.getProperty("customers.topMenu.providers");

	}

	public String getCustomersInvoicesSBET() {
		return labelsFile.getProperty("customers.topMenu.sbet");

	}

	public String getProvidersTopMenuManageLabel() {
		return labelsFile.getProperty("providers.topMenu.manage");

	}

	public String getProvidersTopMenuScheduleLabel() {
		return labelsFile.getProperty("providers.topMenu.schedule");

	}

    public String getProvidersTopMenuInvoicesLabel() {
        return labelsFile.getProperty("providers.topMenu.invoices");

    }

    public String getProvidersTopMenuContractsLabel() {
        return labelsFile.getProperty("providers.topMenu.contracts");

    }

    public String getProvidersTopMenuApplicationsLabel() {
        return labelsFile.getProperty("providers.topMenu.applications");

    }

    public String getReportsTopMenuLabel() {
        return labelsFile.getProperty("Reports.TopMenu");
    }

    public String getLateralTasks() {
        return labelsFile.getProperty("menu.lateral.tasks");
    }

    public String getLateralProviders() {
        return labelsFile.getProperty("menu.lateral.Providers");
    }

    public String getLateralDashBoard() {
        return labelsFile.getProperty("menu.lateral.Dashboard");
    }

    public String getLateralProjects() {
        return labelsFile.getProperty("menu.lateral.Projects");
    }

    public String getLateralSettings() {
        return labelsFile.getProperty("menu.lateral.Settings");
    }

    public String getBreadCrumbsDocsTab() {
        return labelsFile.getProperty("breadCrumbs.Documents");
    }

    public String getLateralmtWeb() {
        return labelsFile.getProperty("menu.lateral.mtWeb");
    }

    public String getNewProjectPmNotification() {
        return labelsFile.getProperty("notification.pm.newProject");
    }

    public String getServiceProviderNotification() {
        return labelsFile.getProperty("notification.provider.serviceToDo");
    }

    public String getCustomerProjectDeliveryNotification() {
        return labelsFile.getProperty("notification.customer.projectDelivered");
    }

	public String getProviderServiceSummaryNotification() {
		return labelsFile.getProperty("notification.provider.serviceSummary");
	}

    public String getCustomerName() {
        return propertiesFile.getProperty("customerName");
    }

	public String getCustomerName1() {
		return propertiesFile.getProperty("customerName1");
	}
    public String getProjectCode() {
        return propertiesFile.getProperty("projectCode");
    }
	public String getProjectCode1() {
		return propertiesFile.getProperty("projectCode1");
	}
    public String getIdentifierPrefixContact() {
        return propertiesFile.getProperty("identifierPrefixContact");
    }

    public String getIdentifierPrefixCustomer() {
        return propertiesFile.getProperty("identifierPrefixCustomer");
    }

    public String getPcfUrl() {
        return propertiesFile.getProperty("pcfUrl");
    }

    public String getCustomFieldsURL() {
        return propertiesFile.getProperty("customFieldsURL");
    }

    public String getDefaultAutomationWF() {
        return propertiesFile.getProperty("defaultAutomationWorkflow");
    }

    public String getRDTextBase() {
        return propertiesFile.getProperty("rdTextBase");
    }
    
    public String getGB301TextBase() {
        return propertiesFile.getProperty("gb301TextBase");
    }
      
	public String getDefaultTextBase() {
		return propertiesFile.getProperty("defaultTextBase");
	}

	public String getRDTextBaseFolder() {
		return propertiesFile.getProperty("rdTextBaseFolder");
	}

	public String getDefaultTextBaseFolder() {
		return propertiesFile.getProperty("defaultTextBaseFolder");
	}

    public String getapiAdminUser() {
        return propertiesFile.getProperty("apiAdminUser");
    }

    public String geApiAdminPassword() {
        return propertiesFile.getProperty("apiAdminPassword");
    }

    public String getIdentifierPrefixProvider() {
        return propertiesFile.getProperty("identifierPrefixProvider");
    }

    public String getCustForPasswordUser() {
        return propertiesFile.getProperty("custForPasswordUser");
    }

    public String getCustForPasswordPwd() {
        return propertiesFile.getProperty("custForPasswordPwd");
    }

    public String getCustForPasswordEmail() {
        return propertiesFile.getProperty("custForPasswordEmail");
    }

    public String getProviderEmail() {
        return propertiesFile.getProperty("providerEmail");
    }

    public String getAdminBUContact() {
        return propertiesFile.getProperty("custBUadmin");
    }

    public String getProviderName() {
        return propertiesFile.getProperty("providerName");
    }

    public String getCustomerNameAPP(int i) {
        return propertiesFile.getProperty("customerName" + i);
    }

    public String getProjectCodeAPP(int i) {
        return propertiesFile.getProperty("projectCode" + i);
    }

    public String getBuAPP(int i, int j) {
        return propertiesFile.getProperty("bu" + i + "Customer" + j);
    }

    public String getContactAPP(int i, int j) {
        return propertiesFile.getProperty("contactBu" + i + "Customer" + j);
    }

    public String getContactPwdAPP(int i, int j) {
        return propertiesFile.getProperty("contactBu" + i + "Customer" + j + "Pwd");
    }

    public String getContactAdminBuAPP(int i, int j) {
        return propertiesFile.getProperty("adminBu" + i + "Customer" + j);
    }

    public String getContactAdminBuPwdAPP(int i, int j) {
        return propertiesFile.getProperty("adminBu" + i + "Customer" + j + "Pwd");
    }

    public String getContactAdminClientAPP(int i) {
        return propertiesFile.getProperty("adminClientCustomer" + i);
    }

    public String getContactAdminClientPwdAPP(int i) {
        return propertiesFile.getProperty("adminClientCustomer" + i + "Pwd");
    }

    public boolean isMobileTest() {
        return ("true".equals(propertiesFile.getProperty("isMobileAppTest")) ? true : false);
    }

    public String getContactForBehalf() {
		return propertiesFile.getProperty("contactForBehalf");
    }

	public String getHelixUrl() {
		return propertiesFile.getProperty("helixUrl");
	}
}

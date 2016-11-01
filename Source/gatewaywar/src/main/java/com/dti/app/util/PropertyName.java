package com.dti.app.util;

/** Interface containing constants which map to the actual key names in the
 * INI file.  Useful, since it ensures typo's don't cause a problem.
 * @author lewit019
 * @version %version: %
 */
public interface PropertyName {

  public static final String XML_VALIDATION_ENABLED = "DtiApp.xmlValidation"; //2011-11-21 cus thinks its unused
  public static final String DTI_DEPLOY_HOST = "DtiApp.deployHost";
  public static final String XML_FILENAME = "DtiApp.xmlFileName"; //2011-11-21 cus thinks its unused
  public static final String DATA_SOURCE = "DtiApp.dataSource"; //2011-11-21 cus thinks its unused
  public static final String REPLACE_DELIMITER = "DtiApp.REPLACEDELIMITER"; //2011-11-21 cus thinks its unused
  public static final String REPLACE_TAG = "DtiApp.REPLACETAG"; //2011-11-21 cus thinks its unused
  public static final String DATA_SOURCE_URL = "DtiApp.dataSourceURL"; //2011-11-21 cus thinks its unused
  public static final String DBUSER = "DtiApp.dbUser"; //2011-11-21 cus thinks its unused
  public static final String DBPASSWORD = "DtiApp.dbPassword"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_LOG_LEVEL = "EventXML.logLevel"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_TYPE = "EventXML.type"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_CONVERSATION_ID = "EventXML.conversationId"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_SERVICE_NAME = "EventXML.serviceName"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_COMPONENT_ID = "EventXML.componentId"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_BP_ID = "EventXML.bpId"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_BP_STEP = "EventXML.bpStep"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_WAS_NODE = "EventXML.wasNode"; //2011-11-21 cus thinks its unused
  public static final String EVENT_XML_WAS_APP = "EventXML.wasApp"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_TARGET = "POS.target";
  public static final String ERROR_POS_VERSION = "POS.version"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_PROTOCOL = "POS.protocol"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_METHOD = "POS.method"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_TKT_BROKER = "POS.tktBroker"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_COMMAND_COUNT = "POS.commandCount"; //2011-11-21 cus thinks its unused

  public static final String ERROR_CODE_VALIDATION_WELL_FORMED = "DtiError.ValidationCodeWellFormed"; //2011-11-21 cus thinks its unused
  public static final String ERROR_CODE_VALIDATION_VALID = "DtiError.ValidationCodeValid"; //2011-11-21 cus thinks its unused
  public static final String ERROR_CODE_POS = "DtiError.DTICode"; //2011-11-21 cus thinks its unused
  public static final String ERROR_CODE_DTI = "DtiError.POSCode"; //2011-11-21 cus thinks its unused
  public static final String ERROR_INBOUND_LOG = "DtiError.DBInbound"; //2011-11-21 cus thinks its unused
  public static final String ERROR_OUTBOUND_LOG = "DtiError.DBOutbound"; //2011-11-21 cus thinks its unused
  public static final String ERROR_CODE_TARGET_VERSION = "DtiError.TargetVersionCode"; //2011-11-21 cus thinks its unused
  public static final String ERROR_ENTITY_CODE = "DtiError.InvalidEntityCode"; //2011-11-21 cus thinks its unused

  public static final String ERROR_POS_DEFAULT_CODE = "DtiError.DefaultCode"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_DEFAULT_TYPE = "DtiError.DefaultType"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_DEFAULT_CLASS = "DtiError.DefaultClass"; //2011-11-21 cus thinks its unused
  public static final String ERROR_POS_DEFAULT_TEXT = "DtiError.DefaultText"; //2011-11-21 cus thinks its unused

  public static final String XML_FILE = "XML.tempFile"; //2011-11-21 cus thinks its unused
  public static final String BUILD_MODULE = "buildModule.value";
  public static final String BUILD_REVISION = "buildRevision.value";
  public static final String BUILD_DATE = "buildDate.value";
  public static final String REWORK_TEXT = "Rework.text"; //2011-11-21 cus thinks its unused
  public static final String HOST_IP = "Host.ip"; //2011-11-21 cus thinks its unused

}

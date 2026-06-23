package com.scbank.process.api.fw.base.gateway.edmi.base.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EDMiConstantsTest {

	@Test
	void test() throws Exception {
		assertEquals("SCBML", EDMiConstants.FLD_ROOT);
		assertEquals("payload", EDMiConstants.FLD_PAYLOAD_ROOT);
		assertEquals("scbmlPayload", EDMiConstants.FLD_SCBML_PAYLOAD);
		assertEquals("RESPONSEMESSAGE", EDMiConstants.FLD_RESPONSE_MESSAGE);
		assertEquals("REQUESTMESSAGE", EDMiConstants.FLD_REQUESTMESASSAGE);
		assertEquals("TRANDATA", EDMiConstants.FLD_TRANDATA);
		assertEquals("MESSAGE", EDMiConstants.FLD_MESSAGE);
		assertEquals("ns:header", EDMiConstants.FLD_HEADER);
		assertEquals("ns:exceptions", EDMiConstants.FLD_EXCEPTIONS);
		assertEquals("ns:exception", EDMiConstants.FLD_EXCEPTION);
		assertEquals("ns:captureSystem", EDMiConstants.FLD_CAPTURESYSTEM);
		assertEquals("ns:code", EDMiConstants.FLD_CODE);
		assertEquals("ns:description", EDMiConstants.FLD_DESCRIPTION);
		assertEquals("ns:messageSender", EDMiConstants.FLD_MESSAGE_SENDER);
		assertEquals("ns:countryCode", EDMiConstants.FLD_COUNTRY_CODE);
		assertEquals("ns:messageVersion", EDMiConstants.FLD_MESSAGE_VERSION);
		assertEquals("ns:typeName", EDMiConstants.FLD_TYPE_NAME);
		assertEquals("ns:subType", EDMiConstants.FLD_SUB_TYPE);
		assertEquals("ns:multiMessageKnown", EDMiConstants.FLD_MULTI_MESSAGE_KNOWN);
		assertEquals("ns:messageType", EDMiConstants.FLD_MESSAGE_TYPE);
		assertEquals("ns:multiMessage", EDMiConstants.FLD_MULTI_MESSAGE);
		assertEquals("ns:messageDetails", EDMiConstants.FLD_MESSAGE_DETAILS);
		assertEquals("ns:domainName", EDMiConstants.FLD_DOMAIN_NAME);
		assertEquals("ns:subDomainName", EDMiConstants.FLD_SUB_DOMAIN_NAME);
		assertEquals("ns:senderDomain", EDMiConstants.FLD_SENDER_DOMAIN);
		assertEquals("ns:initiatedTimestamp", EDMiConstants.FLD_INITIATED_TIMESTAMP);
		assertEquals("ns:trackingId", EDMiConstants.FLD_TRAKING_ID);
		assertEquals("ns:checksum", EDMiConstants.FLD_CHECKSUM);
		assertEquals("ns:possibleDuplicate", EDMiConstants.FLD_POSSIBLE_DUPLICATE);
		assertEquals("ns:serviceBusID", EDMiConstants.FLD_SERVICE_BUS_ID);
		assertEquals("ns:originationDetails", EDMiConstants.FLD_ORIGINATION_DETAILS);
		assertEquals("ns:eventType", EDMiConstants.FLD_EVENT_TYPE);
		assertEquals("ns:process", EDMiConstants.FLD_PROCESS);
		assertEquals("ns:payloadFormat", EDMiConstants.FLD_PAYLOAD_FORMAT);
		assertEquals("ns:payloadVersion", EDMiConstants.FLD_PAYLOAD_VERSION);
		assertEquals("*body", EDMiConstants.FLD_BODY);
		assertEquals("Completed", EDMiConstants.VAL_COMPLETED);
		assertEquals("OLTP", EDMiConstants.CAPTURE_SYSTEM_OLTP);
		assertEquals("json", EDMiConstants.DEFAULT_PAYLOAD_FORMAT);
		assertEquals("1.0", EDMiConstants.DEFAULT_MESSAGE_VERSION);
		assertEquals("1.0", EDMiConstants.DEFAULT_PAYLOAD_VERSION);
		assertEquals("KR", EDMiConstants.DEFAULT_CONTURY_CODE);
		assertEquals("OLTP", EDMiConstants.DEFAULT_CAPTURE_SYSTEM);
		assertEquals("CoreBanking:mbOLTPCommonRoute", EDMiConstants.DEFAULT_MESSAGE_TYPENAME);
		assertEquals("MB", EDMiConstants.DEFAULT_MESSAGE_SENDER_BODY);
	}

}

package com.pads.nbiot.decoder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolAdapter implements IProtocolAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolAdapter.class);
    // 厂商名称
    private static final String MANUFACTURER_ID = "PaiAn";
    // 设备型号
    private static final String MODEL = "NB2";
//    private static final String MODEL = "NBModel";

    public void activate() {
        logger.info("Codec LedAdvert HttpMessageHander activated.");
    }

    public void deactivate() {
        logger.info("Codec LedAdvert HttpMessageHander deactivated.");
    }

    @Override
    public String getManufacturerId() {
        return MANUFACTURER_ID;
    }

    @Override
    public String getModel() {
        return MODEL;
    }

    @Override
    public ObjectNode decode(byte[] binaryData) throws Exception {
        try {
            logger.info("<------------decode start---------->");
            ReportDto reportDto = new ReportDto(binaryData);
            return reportDto.toJsonNode();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("<-----------decode error-------------->");
            return null;
        }
    }

    @Override
    public byte[] encode(ObjectNode objectNode) throws Exception {

        logger.info("dynamic LedAdvert " + objectNode.toString());
        try {
            logger.info("<------------encode start---------->");
            DownCmdDto downCmdDto = new DownCmdDto(objectNode);
            return downCmdDto.toBytes();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("<-----------encode error-------------->");
            return null;
        }
    }

}

package com.pads.nbiot.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReportDto {

    // 设备。
    // 设备在应用协议里的标识
    private String identifier = "GasGeneral";
    // 消息类型
    private String msgType;
    // 是否有后续数据
    private int hasMore = 0;
    // 结果状态码
    private int errcode = 0;

    //---------回应---------------
    // 回应命令的次序码
    private int mid = 0;
    private int result = 0;

    //---------上报---------------
    // 命令类型
    private int cmdType;
    // 设备类型
    private String deviceType;
    // 设备ID
    private String deviceID;
    // 设备网关
    private String gateway;
    // 报警类型
    private String alarmType;
    // 探头类型
    private String probeType;
    // 燃气值
    private int gasValue;

    public ReportDto(byte[] binaryData) {
        // 起始符
        if ((binaryData[0] & 0xFF) != 0xA8) {
            return;
        }
        // 上传类型，设备上报（0A），回复设备（0B）
        if (binaryData[1] == Constants.DeviceReq) {
            msgType = Constants.deviceReq;
            hasMore = binaryData[2];

            // 读取命令
            cmdType = binaryData[3];
            if (cmdType == 2) {
                // 读取id
                deviceID = Utilty.getInstance().bytes2Str(binaryData, 4, 8);
                // 设备类型
                deviceType = Integer.toHexString(binaryData[12]);
                if (binaryData.length > 13) {
                    gateway = Utilty.getInstance().bytes2Str(binaryData, 13, 20);
                }
                return;
            }
            if (cmdType == 3 || cmdType == 6) {
                // 读取报警类型
                alarmType = Integer.toHexString(binaryData[4]);
                // 读取探头类型 EE
                probeType = Integer.toHexString(binaryData[5]);
                return;
            }
            if (cmdType == 10) {
                // 探头类型
                probeType = Integer.toHexString(binaryData[4]);
                // 报警类型
                alarmType = Integer.toHexString(binaryData[5]);
                // 燃气值
                gasValue = Utilty.getInstance().bytes2Int(binaryData, 6, 2);
                return;
            }
        }
        // 下达命令设备回复
        // 复位成功的回复：A8 0B 00 07 01
        if (binaryData[1] == Constants.DeviceRsp) {
            msgType = Constants.deviceRsp;
            errcode = binaryData[2];
            mid = Utilty.getInstance().bytes2Int(binaryData, 3, 2);
            // deviceID = Utilty.byte2Mac2(binaryData, 4, 8);
            cmdType = binaryData[5];
            result = binaryData[6];
        }
    }

    public ObjectNode toJsonNode() {
        try {
            //组装body体
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("identifier", this.identifier);
            root.put("msgType", this.msgType);

            if (Constants.deviceReq.equals(this.msgType)) {
                root.put("hasMore", this.hasMore);
                ArrayNode data = mapper.createArrayNode();
                ObjectNode reportNode = mapper.createObjectNode();
                reportNode.put("serviceId", "Report");
                reportNode.put("eventTime", TimeUtil.getTimeString());
                ObjectNode serviceData = mapper.createObjectNode();
                serviceData.put("cmdType", Integer.toHexString(this.cmdType));
                if (cmdType == 1) {
                } else if (cmdType == 2) {
                    serviceData.put("deviceID", this.deviceID);
                    serviceData.put("deviceType", this.deviceType);
                    serviceData.put("gateway", this.gateway);
                } else if (cmdType == 3 || cmdType == 6) {
                    serviceData.put("alarmType", this.alarmType);
                    serviceData.put("probeType", this.probeType);
                } else if (cmdType == 10) {
                    serviceData.put("alarmType", alarmType);
                    serviceData.put("probeType", probeType);
                    serviceData.put("gasValue", gasValue);
                } else if (cmdType == 15) {
                } else {
                    return null;
                }
                reportNode.set("serviceData", serviceData);
                data.add(reportNode);
                root.set("data", data);
            } else if (Constants.deviceRsp.equals(this.msgType)) {
                root.put("mid", this.mid);
                root.put("errcode", this.errcode);

                ObjectNode body = mapper.createObjectNode();
                body.put("result", this.result);
                root.set("body", body);
            }
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

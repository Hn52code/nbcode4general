package com.thrid.party.codec.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;
import com.pads.nbiot.decoder.ProtocolAdapter;
import com.pads.nbiot.decoder.Utilty;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ProtocolServiceImplTest {

    private IProtocolAdapter protocolAdapter;

    @Before
    public void setProtocolAdapter() {
        this.protocolAdapter = new ProtocolAdapter();
    }

    /**
     * 测试用例1：设备向平台上报数据。
     * <p>
     * <pre>
     * 设备上报数据:AA72000032088D0320623399
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void testDecodeDeviceReportData() throws Exception {
        byte[] deviceReqByte = initDeviceWithGatewayReqByte();
//        byte[] deviceReqByte = initDeviceReqByte();
        ObjectNode objectNode = protocolAdapter.decode(deviceReqByte);
        String str = objectNode.toString();
        System.out.println(str);
    }

    /**
     * 测试用例2：平台向设备下发控制命令:
     * <p>
     * <pre>
     * {
     * //"identifier": "123",
     * "msgType": "cloudReq",
     * "cmd": "SET_DEVICE_LEVEL",
     * "mid": 2016,
     * "paras": { "value": "10" },
     * "hasMore": 0
     * }
     * </pre>
     */
    @Test
    public void testEncodeIoTSendCommand() throws Exception {
        ObjectNode CloudReqObjectNode = initCloudReqObjectNode();
        byte[] outputByte = protocolAdapter.encode(CloudReqObjectNode);
        System.out.println("cloudReq output:" + parseByte2HexStr(outputByte));
    }

    /**
     * 测试用例3：设备对平台命令的应答消息 有命令短id
     * <p>
     * <pre>
     * 设备应答消息:AA7201000107E0
     *
     * <pre>
     *
     * @throws Exception
     */
    @Test
    public void testDecodeDeviceResponseIoT() throws Exception {
        byte[] deviceRspByte = initDeviceRspByte();
        ObjectNode objectNode = protocolAdapter.decode(deviceRspByte);
        String str = objectNode.toString();
        System.out.println(str);
    }

    /**
     * 测试用例4：平台收到设备的上报数据后对设备的应答，如果不需要应答则返回null即可
     * <pre>
     * {
     * "identifier": "0",
     * "msgType": "cloudRsp",
     * "request": [AA,72,00,00,32,08,8D,03,20,62,33,99],
     * "errcode": 0,
     * "hasMore": 0
     * }
     *
     * <pre>
     *
     * @throws Exception
     */
    @Test
    public void testEncodeIoTResponseDevice() throws Exception {
        byte[] deviceReqByte = initDeviceWithGatewayReqByte();
        ObjectNode cloudRspObjectNode = initCloudRspObjectNode(deviceReqByte);
        byte[] outputByte2 = protocolAdapter.encode(cloudRspObjectNode);
        System.out.println("cloudRsp output:" + parseByte2HexStr(outputByte2));
    }

    public static String parseByte2HexStr(byte[] buf) {
        if (null == buf) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] hexStr2bytes(String hexStr) {
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int j = Integer.parseInt(hexStr.substring(i * 2, (i + 1) * 2), 16);
            bytes[i] = (byte) j;
        }
        return bytes;
    }

    private byte[] initDeviceReqByte() {

        String req = "A80A000236303030303037FF17";
        byte[] bytes = hexStr2bytes(req);
        return bytes;
    }

    /*
     * 初始化：设备数据上报码流
     */
    private static byte[] initDeviceWithGatewayReqByte() {
        // 本例报警： AA 72 00 00 32 08 8D 03 20 62 33 99
//        byte[] byteDeviceReq = new byte[6];
//        byteDeviceReq[0] = (byte) 0xA8;
//        byteDeviceReq[1] = (byte) 0x0A;
//        byteDeviceReq[2] = (byte) 0x00;
//        byteDeviceReq[3] = (byte) 0x03;
//        byteDeviceReq[4] = (byte) 0x00;
//        byteDeviceReq[5] = (byte) 0x03;
        byte[] byteDeviceReq = new byte[33];
        byteDeviceReq[0] = (byte) 0xA8;
        byteDeviceReq[1] = (byte) 0x0A;
        byteDeviceReq[2] = (byte) 0x00;
        byteDeviceReq[3] = (byte) 0x02;
        byteDeviceReq[4] = (byte) 0x31;
        byteDeviceReq[5] = (byte) 0x31;
        byteDeviceReq[6] = (byte) 0x31;
        byteDeviceReq[7] = (byte) 0x31;
        byteDeviceReq[8] = (byte) 0x31;
        byteDeviceReq[9] = (byte) 0x31;
        byteDeviceReq[10] = (byte) 0xff;
        byteDeviceReq[11] = (byte) 0xff;
        byteDeviceReq[12] = (byte) 0x10;
        byteDeviceReq[13] = (byte) 0x33;
        byteDeviceReq[14] = (byte) 0x33;
        byteDeviceReq[15] = (byte) 0x33;
        byteDeviceReq[16] = (byte) 0x33;
        byteDeviceReq[17] = (byte) 0x33;
        byteDeviceReq[18] = (byte) 0x33;
        byteDeviceReq[19] = (byte) 0x35;
        byteDeviceReq[20] = (byte) 0x39;
        byteDeviceReq[21] = (byte) 0x39;
        byteDeviceReq[22] = (byte) 0x30;
        byteDeviceReq[23] = (byte) 0x31;
        byteDeviceReq[24] = (byte) 0x32;
        byteDeviceReq[25] = (byte) 0x33;
        byteDeviceReq[26] = (byte) 0x34;
        byteDeviceReq[27] = (byte) 0x35;
        byteDeviceReq[28] = (byte) 0x36;
        byteDeviceReq[29] = (byte) 0x37;
        byteDeviceReq[30] = (byte) 0x38;
        byteDeviceReq[31] = (byte) 0x30;
        byteDeviceReq[32] = (byte) 0x30;
        String deviceID = Utilty.byte2Mac(byteDeviceReq, 4, 8);
        System.out.println(deviceID);
        return byteDeviceReq;
    }

    /*
     * 初始化：平台向设备命令下发数据
     */
    private static ObjectNode initCloudReqObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudReqObjectNode = mapper.createObjectNode();
        ObjectNode paras = mapper.createObjectNode();
        paras.put("status", "0");
        cloudReqObjectNode.put("identifier", "123");
        cloudReqObjectNode.put("msgType", "cloudReq");
        cloudReqObjectNode.put("cmd", "Switch");
        cloudReqObjectNode.set("paras", paras);
        cloudReqObjectNode.put("hasMore", 0);
        cloudReqObjectNode.put("mid", 18);
        return cloudReqObjectNode;
    }

    /*
     * 初始化：设备对平台的响应码流
     */
    private static byte[] initDeviceRspByte() {
        // 测试用例：有命令短mid 设备应答消息:A8 0B  00 00 12 08 01
        byte[] byteDeviceRsp = new byte[12];
        byteDeviceRsp[0] = (byte) 0xA8;
        byteDeviceRsp[1] = (byte) 0x0B;
        byteDeviceRsp[2] = (byte) 0x00;
        byteDeviceRsp[3] = (byte) 0x00;
        byteDeviceRsp[4] = (byte) 0x12;
        byteDeviceRsp[5] = (byte) 0x08;
        byteDeviceRsp[6] = (byte) 0x01;
        return byteDeviceRsp;
    }

    /*
     * 初始化：平台对设备的应答数据
     */
    private static ObjectNode initCloudRspObjectNode(byte[] device2CloudByte) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudRspObjectNode = mapper.createObjectNode();
        cloudRspObjectNode.put("identifier", "123");
        cloudRspObjectNode.put("msgType", "cloudRsp");
        // 设备上报的码流
        cloudRspObjectNode.put("request", device2CloudByte);
        cloudRspObjectNode.put("errcode", 0);
        cloudRspObjectNode.put("hasMore", 0);
        return cloudRspObjectNode;
    }
}

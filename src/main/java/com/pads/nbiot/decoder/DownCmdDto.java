package com.pads.nbiot.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DownCmdDto {

    private String identifier = "general";
    private String msgType;
    private int hasMore = 0;
    private int errcode = 0;
    private String cmd;
    private int mid = 0;
    private JsonNode paras;

    public DownCmdDto(ObjectNode input) {
        try {
            this.identifier = input.get("identifier").asText();
            this.msgType = input.get("msgType").asText();
            if (msgType.equals(Constants.CloudRsp)) {
                // 在此组装ACK的值
                this.errcode = input.get("errcode").asInt();
                this.hasMore = input.get("hasMore").asInt();
            } else {
                // 此处需要考虑兼容性，如果没有传mId，则不对其进行编码
                if (input.get("mid") != null) {
                    this.mid = input.get("mid").intValue();
                }
                this.cmd = input.get("cmd").asText();
                this.paras = input.get("paras");
                this.hasMore = input.get("hasMore").asInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] toBytes() {
        try {
            if (this.msgType.equals(Constants.CloudReq)) {
                /*
                 * 应用服务器下发的控制命令,如果存在多种命令增加判断即可
                 */
                byte[] byteRead = new byte[7];
                // 起始位
                byteRead[0] = (byte) 0xA8;
                // 长度位
                byteRead[1] = 0x05;
                if (this.cmd.equals(Constants.CmdReset)) {
                    // 命令位--复位
                    byteRead[2] = 0x07;
                    // 参数位
                    byteRead[3] = (byte) 0xFF;
                } else if (this.cmd.equals(Constants.CmdSwitch)) {
                    // 命令位--机械手正反转--开关
                    byteRead[2] = 0x08;
                    // 参数位
                    int para = this.paras.get("status").asInt();
                    byteRead[3] = (byte) para;
                } else {
                    return null;
                }
                // 验证位
                byteRead[4] = (byte) (byteRead[2] + byteRead[3]);
                // 后续位
                // byteRead[5] = (byte) this.hasMore;
                //此处需要考虑兼容性，如果没有传mId，则不对其进行编码
                if (Utilty.getInstance().isValidOfMid(mid)) {
                    byte[] byteMid;
                    byteMid = Utilty.getInstance().int2Bytes(mid, 2);
                    byteRead[5] = byteMid[0];
                    byteRead[6] = byteMid[1];
                }
                return byteRead;
            }
            /*平台收到设备的上报数据，根据需要编码ACK，对设备进行响应，如果此处返回null，表示不需要对设备响应。*/
            else if (this.msgType.equals(Constants.CloudRsp)) {
                byte[] ack = new byte[4];
                ack[0] = (byte) 0xA8;
                ack[1] = (byte) 0x02;
                ack[2] = (byte) this.errcode;
                ack[3] = (byte) this.hasMore;
                return ack;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

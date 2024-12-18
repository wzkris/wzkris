/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : wzkris_equipment

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 22/11/2024 10:01:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS wzkris_equipment;
USE wzkris_equipment;
-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `device_id` bigint NOT NULL COMMENT '设备id',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `device_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `serial_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sn号',
  `online_time` bigint NULL DEFAULT NULL COMMENT '上线时间',
  `offline_time` bigint NULL DEFAULT NULL COMMENT '下线时间',
  `conn_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '连接状态',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`device_id`) USING BTREE,
  UNIQUE INDEX `uk_sn`(`serial_no` ASC) USING BTREE,
  INDEX `t_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES (1858767510869524481, 0, '1121', 'xxx', NULL, NULL, '0', '0', 1731999683486, 1, 1732081525427, 1);

-- ----------------------------
-- Table structure for protocol
-- ----------------------------
DROP TABLE IF EXISTS `protocol`;
CREATE TABLE `protocol`  (
  `ptc_id` bigint NOT NULL COMMENT 'id',
  `product_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品标识',
  `ptc_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协议名称',
  `ptc_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协议标识',
  `ptc_version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0.1' COMMENT '协议版本',
  `ptc_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '协议类型 ：mqtt || coap || modbus || http',
  `ptc_language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '协议语言',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类名',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `parameter` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例参数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态(字典值：0启用  1停用)',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ptc_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '协议信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of protocol
-- ----------------------------
INSERT INTO `protocol` VALUES (1, '82325a1de5e84ea88f332f3dbc10f6d5', '温湿度产品协议转换', '22', '0.1', 'MQTT', 'java', '222', 'import com.alibaba.fastjson.JSONArray;\nimport com.alibaba.fastjson.JSONObject;\n\nimport jakarta.xml.bind.annotation.adapters.HexBinaryAdapter;\nimport java.math.BigInteger;\nimport java.util.Date;\n\npublic class newtest {\n    public static void main(String[] args) throws Exception {\n        String injson = \"\";\n\n        if (args != null && args.length != 0) {\n            injson = args[0];\n        }\n        JSONObject parsejson = JSONObject.parseObject(injson);\n        System.out.println(Convert2SystemJSON(parsejson));\n    }\n    public static String Convert2SystemJSON(JSONObject injsonobj) throws Exception {\n        String hexdata = injsonobj.get(\"data\").toString();\n        byte[] bt = java.util.Base64.getDecoder().decode(hexdata);\n        JSONObject data = hex2wsdjsonobj(bt);\n\n        JSONArray services = new JSONArray();\n        JSONObject server = new JSONObject();\n        server.put(\"serviceId\", \"serdsd123\");\n        server.put(\"data\", data);\n        server.put(\"eventTime\", new Date());\n        services.add(server);\n\n        JSONObject dev = new JSONObject();\n        dev.put(\"deviceId\", injsonobj.get(\"devEui\"));\n        dev.put(\"services\", services);\n        JSONArray devices = new JSONArray();\n        devices.add(dev);\n\n        JSONObject root = new JSONObject();\n        root.put(\"devices\", devices);\n        return root.toJSONString();\n    }\n\n    /**\n     * 十六进制转换成10进制 负数也能转换\n     */\n    public static int hex16convert2(String hex) throws Exception {\n        if (hex.length() != 4) {\n            throw new Exception(\"必须是4个长度\");\n        }\n        int bit1 = Integer.parseInt(hex.substring(0, 1), 16);\n        if (bit1 < 8)\n        {\n            return Integer.parseInt(hex, 16);\n        } else {\n            return new BigInteger(\"FFFF\" + hex, 16).intValue();\n        }\n    }\n\n    /**\n     * 温湿度解码\n     */\n    private static JSONObject hex2wsdjsonobj(byte[] bt) throws Exception {\n        HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();\n        JSONObject data = new JSONObject();\n        String temp = hexBinaryAdapter.marshal(new byte[]{bt[2]});\n        String temp2 = hexBinaryAdapter.marshal(new byte[]{bt[3]});\n        String changtemp = change(temp, temp2);\n        data.put(\"temperature\", hex16convert2(changtemp) * 0.01);\n        temp = hexBinaryAdapter.marshal(new byte[]{bt[4]});\n        temp2 = hexBinaryAdapter.marshal(new byte[]{bt[5]});\n        changtemp = change(temp, temp2);\n        data.put(\"humidity\", hex16convert2(changtemp) * 0.01);\n        temp = hexBinaryAdapter.marshal(new byte[]{bt[8]});\n        temp2 = hexBinaryAdapter.marshal(new byte[]{bt[9]});\n        changtemp = change(temp, temp2);\n        data.put(\"battery\", hex16convert2(changtemp));\n        return data;\n    }\n\n    public static String change(String first, String end) {\n        return end + first;\n    }\n}\n', '11', '0', 1, 1732238575107, 1, 1732238575107, '温湿度产品协议转换样例');
INSERT INTO `protocol` VALUES (2, '0d6f540f643c46cb83df6758e090236d', 'co2协议转换', '1', '0.1', 'MQTT', 'java', '1', 'import com.alibaba.fastjson.JSONArray;\nimport com.alibaba.fastjson.JSONObject;\n\nimport jakarta.xml.bind.annotation.adapters.HexBinaryAdapter;\nimport java.util.Date;\n\npublic class co2test {\n    public static void main(String[] args) throws Exception {\n        String injson = \"\";\n\n        if (args != null && args.length != 0) {\n            injson = args[0];\n        }\n        JSONObject parsejson = JSONObject.parseObject(injson);\n        System.out.println(Convert2SystemJSON(parsejson));\n    }\n    public static String Convert2SystemJSON(JSONObject injsonobj) throws Exception {\n        String hexdata = injsonobj.get(\"data\").toString();\n        byte[] bt = java.util.Base64.getDecoder().decode(hexdata);\n        JSONObject data = hex2co2jsonobj(bt);\n\n        JSONArray services = new JSONArray();\n        JSONObject server = new JSONObject();\n        server.put(\"serviceId\", \"serdsd123\");\n        server.put(\"data\", data);\n        server.put(\"eventTime\", new Date());\n        services.add(server);\n\n        JSONObject dev = new JSONObject();\n        dev.put(\"deviceId\",  injsonobj.get(\"devEUI\").toString());\n        dev.put(\"services\", services);\n        JSONArray devices = new JSONArray();\n        devices.add(dev);\n\n        JSONObject root = new JSONObject();\n        root.put(\"devices\", devices);\n        return root.toJSONString();\n    }\n \n    private static JSONObject hex2co2jsonobj(byte[] bt) throws Exception {\n        HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();\n        String hex=hexBinaryAdapter.marshal(bt);\n        JSONObject data = new JSONObject();\n        for (int i = 0; i < hex.length(); i++) {\n            String td = hex.substring(i, i + 2);\n            String type = hex.substring(i + 2, i + 4); \n            if (td.equals(\"01\") && type.equals(\"75\")) {\n                data.put(\"battery\", Integer.parseInt(hex.substring(i + 4, i + 6), 16));\n            } \n            else if (td.equals(\"03\") && type.equals(\"67\")) { \n                String temp = change(hex.substring(i + 4, i + 6), hex.substring(i + 6, i + 8));\n                data.put(\"temperature\", Integer.parseInt(temp, 16) * 0.1);\n                i = i + 7;\n            } \n            else if (td.equals(\"04\") && type.equals(\"68\")) {\n                data.put(\"humidity\", Integer.parseInt(hex.substring(i + 4, i + 6), 16) / 2);\n                i = i + 5;\n            } \n            else if (td.equals(\"05\") && type.toLowerCase().equals(\"7d\")) {\n                String temp = change(hex.substring(i + 4, i + 6), hex.substring(i + 6, i + 8));\n                data.put(\"co2\", Integer.parseInt(temp, 16));\n                i = i + 7;\n            } \n            else if (td.equals(\"06\") && type.equals(\"73\")) {\n                String temp = change(hex.substring(i + 4, i + 6), hex.substring(i + 6, i + 8));\n                data.put(\"pressure\",Integer.parseInt(temp, 16) * 0.1);\n                i = i + 7;\n            } else {\n                break;\n            }\n        }\n        return data;\n    }\n\n    public static String change(String first, String end) {\n        return end + first;\n    }\n}\n', NULL, '0', 1, 1732080713708, 1, 1732080713708, 'co2协议转换样例');
INSERT INTO `protocol` VALUES (3, '0d6f540f643c46cb83df6758e090236d', '充电桩协议', '1', '0.1', 'MQTT', 'java', '1', 'import com.wzkris.common.core.utils.StringUtil;\nimport java.math.BigDecimal;\nimport java.math.RoundingMode;\n\npublic class newtest {\n\n    public static void main(String[] args) throws Exception {\n        String injson = \"\";\n\n        if (args != null && args.length != 0) {\n            injson = args[0];\n        }\n\n        String code = injson.substring(0, 2);\n        if (StringUtil.equals(\"07\", code) || StringUtil.equals(\"08\", code)) {\n\n            injson = injson.substring(6);\n            int netType = Integer.parseInt(injson.substring(0, 2), 16);\n            int signLevel = Integer.parseInt(injson.substring(2, 4), 16);\n            int NetOp = Integer.parseInt(injson.substring(4, 6), 16);\n            int spearState = Integer.parseInt(injson.substring(6, 8), 16);\n            int errCode = Integer.parseInt(injson.substring(8, 12), 16);\n            int leakCurrent = Integer.parseInt(injson.substring(12, 16), 16);\n            BigDecimal inOc = BigDecimal.valueOf(Integer.parseInt(injson.substring(16, 20), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);\n            int relayState = Integer.parseInt(injson.substring(20, 22), 16);\n            BigDecimal phaseA_voltage = BigDecimal.valueOf(Integer.parseInt(injson.substring(22, 30), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);\n            BigDecimal phaseA_current = BigDecimal.valueOf(Integer.parseInt(injson.substring(30, 38), 16)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);\n            BigDecimal phaseB_voltage = BigDecimal.valueOf(Integer.parseInt(injson.substring(38, 46), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);\n            BigDecimal phaseB_current = BigDecimal.valueOf(Integer.parseInt(injson.substring(46, 54), 16)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);\n            BigDecimal phaseC_voltage = BigDecimal.valueOf(Integer.parseInt(injson.substring(54, 62), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);\n            BigDecimal phaseC_current = BigDecimal.valueOf(Integer.parseInt(injson.substring(62, 70), 16)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);\n            int elecModel = Integer.parseInt(injson.substring(70, 74), 16);\n            int serModel = Integer.parseInt(injson.substring(74, 78), 16);\n\n            System.out.println(\"网络类型：\" + netType + \"，信号强度：\" + signLevel + \"，网络运营商：\" + NetOp + \"，枪工作状态：\" + spearState + \"，故障代码：\" + errCode +\n                    \"，漏电电流：\" + leakCurrent + \"，桩内温度：\" + inOc + \"，继电器状态：\" + relayState +\n                    \"，A相输出电压：\" + phaseA_voltage + \"，A相输出电流：\" + phaseA_current + \"，B相输出电压：\" + phaseB_voltage +\n                    \"，B相输出电流：\" + phaseB_current + \"，C相输出电压：\" + phaseC_voltage + \", C相输出电流：\" + phaseC_current +\n                    \", 电费模型：\" + elecModel + \", 服务费模型：\" + serModel);\n        }\n\n    }\n}\n', '0776010D410C0A0000000000C30A000008B80000000100000000000000000000000000000000000C000C01BC', '0', 1, 1732080713708, 1, 1732239796913, '充电桩协议转换样例');

SET FOREIGN_KEY_CHECKS = 1;

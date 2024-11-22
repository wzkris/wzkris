package com.wzkris.equipment;

import com.wzkris.common.core.utils.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class newtest {

    static String data = "0776010D410C0A0000000000C30A000008B80000000100000000000000000000000000000000000C000C01BC";

    public static void main(String[] args) throws Exception {
        String injson = "";

        if (args != null && args.length != 0) {
            injson = args[0];
        }

        String code = injson.substring(0, 2);
        if (StringUtil.equals("07", code) || StringUtil.equals("08", code)) {

            injson = injson.substring(6);
            int netType = Integer.parseInt(injson.substring(0, 2), 16);
            int signLevel = Integer.parseInt(injson.substring(2, 4), 16);
            int NetOp = Integer.parseInt(injson.substring(4, 6), 16);
            int spearState = Integer.parseInt(injson.substring(6, 8), 16);
            int errCode = Integer.parseInt(injson.substring(8, 12), 16);
            int leakCurrent = Integer.parseInt(injson.substring(12, 16), 16);
            BigDecimal inOc = BigDecimal.valueOf(Integer.parseInt(injson.substring(16, 20), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);
            int relayState = Integer.parseInt(injson.substring(20, 22), 16);
            BigDecimal phaseA_voltage = BigDecimal.valueOf(Integer.parseInt(injson.substring(22, 30), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);
            BigDecimal phaseA_current = BigDecimal.valueOf(Integer.parseInt(injson.substring(30, 38), 16)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal phaseB_voltage = BigDecimal.valueOf(Integer.parseInt(injson.substring(38, 46), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);
            BigDecimal phaseB_current = BigDecimal.valueOf(Integer.parseInt(injson.substring(46, 54), 16)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal phaseC_voltage = BigDecimal.valueOf(Integer.parseInt(injson.substring(54, 62), 16)).divide(BigDecimal.valueOf(10), 1, RoundingMode.HALF_UP);
            BigDecimal phaseC_current = BigDecimal.valueOf(Integer.parseInt(injson.substring(62, 70), 16)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            int elecModel = Integer.parseInt(injson.substring(70, 74), 16);
            int serModel = Integer.parseInt(injson.substring(74, 78), 16);

            System.out.println("网络类型：" + netType + "，信号强度：" + signLevel + "，网络运营商：" + NetOp + "，枪工作状态：" + spearState + "，故障代码：" + errCode +
                    "，漏电电流：" + leakCurrent + "，桩内温度：" + inOc + "，继电器状态：" + relayState +
                    "，A相输出电压：" + phaseA_voltage + "，A相输出电流：" + phaseA_current + "，B相输出电压：" + phaseB_voltage +
                    "，B相输出电流：" + phaseB_current + "，C相输出电压：" + phaseC_voltage + ", C相输出电流：" + phaseC_current +
                    ", 电费模型：" + elecModel + ", 服务费模型：" + serModel);
        }

    }
}

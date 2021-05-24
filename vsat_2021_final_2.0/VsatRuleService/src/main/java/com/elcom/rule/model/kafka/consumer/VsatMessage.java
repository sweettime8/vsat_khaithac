package com.elcom.rule.model.kafka.consumer;

/*import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;*/

/**
 *
 * @author anhdv
 */
public class VsatMessage {

    private static VsatMessage INSTANCE = null;

    public static VsatMessage getInstance() {
        if (INSTANCE == null)
            INSTANCE = new VsatMessage();
        return INSTANCE;
    }

    /*private final Pattern splitPattern = Pattern.compile("\\|");

    public VsatMessage parseVsatMessage(String strMsg) {
        String dateTimeSip, nameSip, ipSip;
        SipDTO sip;
        String dateTime, dataType, sourceIP;
        int sourcePort;
        String destIP;
        int destPort;
        String filePath, sourcePhone, destPhone;
        DataResultInformation resp;
        String uuid, parentUuid;
        int byteCount;
        double dataPercent;
        String nodeName, startTime, toTime, fullPath;
        int dataSource;
        DataProtocol dataProtocol;
        String dateTime1, dataType1, sourceIP1;
        int sourcePort1;
        String destIP1;
        int destPort1;
        String sourcePhone1, destPhone1, filePath1;
        DataResultInformation resp1;
        String dateTime2, dataType2, sourceIP2;
        int sourcePort2;
        String destIP2;
        int destPort2;
        String sourcePhone2, destPhone2, filePath2;
        long fileSize2;
        String fileName2;
        int dataSource123;
        DataResultInformation resp2;
        String messageText;
        int messageId;
        long mmsi;
        int imo, country, vesselType, dimA, dimB, dimC, dimD;
        float draugth, rot, sog, cog, longitude, latitude;
        int navstatus, trueHanding;
        String callSign, name, eta, destination, receivedTime;
        long mmsiMaster;
        int dataSource1;
        String sourceIp, destIp;
        int srcPort, dstPort, sourceId;
        VesselInformation vessel;
        Matcher matcher = this.splitPattern.matcher(strMsg);
        int count = 0;
        List<String> items = new ArrayList<>();
        int start = 0;
        while (matcher.find()) {
            int end = matcher.end();
            String str1 = strMsg.substring(start, end - 1);
            start = end;
            count++;
            items.add(str1);
        }
        String str = strMsg.substring(start, strMsg.length());
        items.add(str);
        switch (count) {
            case 3:
                dateTimeSip = items.get(0);
                nameSip = items.get(2);
                ipSip = items.get(3);
                sip = new SipDTO(-1L, nameSip, ipSip, dateTimeSip);
                return (VsatMessage) sip;
            case 6:
                dateTime = items.get(0);
                dataType = items.get(1);
                sourceIP = items.get(2);
                sourcePort = convertStringToInt(items.get(3));
                destIP = items.get(4);
                destPort = convertStringToInt(items.get(5));
                filePath = items.get(6);
                sourcePhone = "";
                destPhone = "";
                resp = new DataResultInformation(dateTime, dataType, sourceIP, destIP, sourcePort, destPort, filePath, sourcePhone, destPhone);
                return (VsatMessage) resp;
            case 9:
                uuid = items.get(0);
                parentUuid = items.get(1);
                byteCount = convertStringToInt(items.get(2));
                dataPercent = convertStringToDouble(items.get(3));
                nodeName = items.get(4);
                startTime = items.get(5);
                toTime = items.get(6);
                fullPath = items.get(7);
                dataSource = convertStringToInt(items.get(8));
                dataProtocol = new DataProtocol(uuid, parentUuid, byteCount, dataPercent, nodeName, startTime, toTime, fullPath, dataSource);
                return (VsatMessage) dataProtocol;
            case 8:
                dateTime1 = items.get(0);
                dataType1 = items.get(1);
                sourceIP1 = items.get(2);
                sourcePort1 = convertStringToInt(items.get(3));
                destIP1 = items.get(4);
                destPort1 = convertStringToInt(items.get(5));
                sourcePhone1 = items.get(6);
                destPhone1 = items.get(7);
                filePath1 = items.get(8);
                resp1 = new DataResultInformation(dateTime1, dataType1, sourceIP1, destIP1, sourcePort1, destPort1, filePath1, sourcePhone1, destPhone1);
                return (VsatMessage) resp1;
            case 11:
                dateTime2 = items.get(0);
                dataType2 = items.get(1);
                sourceIP2 = items.get(2);
                sourcePort2 = convertStringToInt(items.get(3));
                destIP2 = items.get(4);
                destPort2 = convertStringToInt(items.get(5));
                sourcePhone2 = items.get(6);
                destPhone2 = items.get(7);
                filePath2 = items.get(8);
                fileSize2 = convertStringToLong(items.get(9));
                fileName2 = items.get(10);
                dataSource123 = convertStringToInt(items.get(11));
                resp2 = new DataResultInformation(dateTime2, dataType2, sourceIP2, destIP2, sourcePort2, destPort2, filePath2, sourcePhone2, destPhone2, fileSize2, fileName2, dataSource123);
                return (VsatMessage) resp2;
            case 28:
                messageText = items.get(0);
                messageId = convertStringToInt(items.get(1));
                mmsi = convertStringToLong(items.get(2));
                imo = convertStringToInt(items.get(3));
                country = convertStringToInt(items.get(4));
                vesselType = convertStringToInt(items.get(5));
                dimA = convertStringToInt(items.get(6));
                dimB = convertStringToInt(items.get(7));
                dimC = convertStringToInt(items.get(8));
                dimD = convertStringToInt(items.get(9));
                draugth = convertStringToFloat(items.get(10));
                rot = convertStringToFloat(items.get(11));
                sog = convertStringToFloat(items.get(12));
                cog = convertStringToFloat(items.get(13));
                longitude = convertStringToFloat(items.get(14));
                latitude = convertStringToFloat(items.get(15));
                navstatus = convertStringToInt(items.get(16));
                trueHanding = convertStringToInt(items.get(17));
                callSign = items.get(18);
                name = items.get(19);
                eta = items.get(20);
                destination = items.get(21);
                receivedTime = items.get(22);
                mmsiMaster = convertStringToLong(items.get(23));
                dataSource1 = convertStringToInt(items.get(24));
                sourceIp = items.get(25);
                destIp = items.get(26);
                srcPort = convertStringToInt(items.get(27));
                dstPort = convertStringToInt(items.get(28));
                sourceId = 1;
                vessel = new VesselInformation(messageText, messageId, mmsi, imo, country, vesselType, dimA, dimB, dimC, dimD, draugth, rot, sog, cog, longitude, latitude, navstatus, trueHanding, callSign, name, eta, destination, receivedTime, mmsiMaster, sourceId, dataSource1, sourceIp, srcPort, destIp, dstPort);
                return (VsatMessage) vessel;
        }
        System.out.println("Khong biet ban tin gi: " + strMsg);
        return null;
    }

    private int convertStringToInt(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                int iValue = Integer.parseInt(value);
                return iValue;
            } catch (Exception exception) {
            }
        }
        return 0;
    }

    private double convertStringToDouble(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                double iValue = Double.parseDouble(value);
                return iValue;
            } catch (Exception exception) {
            }
        }
        return 0.0D;
    }

    private float convertStringToFloat(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                float iValue = Float.parseFloat(value);
                return iValue;
            } catch (Exception exception) {
            }
        }
        return 0.0F;
    }

    private long convertStringToLong(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                long iValue = Long.parseLong(value);
                return iValue;
            } catch (Exception exception) {
            }
        }
        return 0L;
    } */
}

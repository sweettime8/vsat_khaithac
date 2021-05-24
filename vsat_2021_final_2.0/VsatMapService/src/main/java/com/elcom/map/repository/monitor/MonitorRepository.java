package com.elcom.map.repository.monitor;

import com.elcom.map.repository.BaseRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Repository
public class MonitorRepository extends BaseRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorRepository.class);

    @Autowired
    public MonitorRepository(@Qualifier("vsatEntityManagerFactory") EntityManagerFactory factory, @Qualifier("vsatChDataSource") DataSource dataSource) {
        super(factory,dataSource);
    }

//    public List<ComputerMonitor>getMonitorCenter(List<String> lstIp){
//        Session session = openSession();
//        List<MonSoftwareRaw> lstMonSoftwareRaw= new ArrayList<MonSoftwareRaw>();
//        List<MonHardwareRaw> lstMonHardwareRaw= new ArrayList<MonHardwareRaw>();
//        List<ComputerMonitor> lstComputerMonitor=new ArrayList<ComputerMonitor>();
//        try {
//            NativeQuery query = session.createNativeQuery("select \"ID\",\"IP\", \"MODULE_NAME\", \"STATUS\", \"RECEIVED_TIME\", \"PART_NAME\", \"ADD_TIME\" \n" +
//                    "                   from \"MON_SOFTWARE_RAWS\"\n" +
//                    "                   where (\"IP\", \"MODULE_NAME\",\"RECEIVED_TIME\") in (\n" +
//                    "                            select \"IP\", \"MODULE_NAME\", MAX(\"RECEIVED_TIME\")\n" +
//                    "                            from \"MON_SOFTWARE_RAWS\"\n" +
//                    "                            where \"IP\" in :ip \n" +
//                    "                            group by \"IP\", \"MODULE_NAME\")",MonSoftwareRaw.class);
//            query.setParameterList("ip",lstIp);
//            lstMonSoftwareRaw = (List<MonSoftwareRaw>)query.getResultList();
//            //hardware
//
//            NativeQuery hardwareQuery = session.createNativeQuery("select \"ID\",\"IP\", \"HARDWARE_NAME\", \"HDD_USED\", \"HDD_TOTAL\", \"RAM_USED\", \"RAM_TOTAL\", \"CPU_USED\", \"STATUS\", \"RECEIVED_TIME\", \"PART_NAME\", \"ADD_TIME\"\n" +
//                    "                   from \"MON_HARDWARE_RAWS\"\n" +
//                    "                   where (\"IP\", \"HARDWARE_NAME\", \"RECEIVED_TIME\") in (\n" +
//                    "                            SELECT \"IP\", \"HARDWARE_NAME\", MAX(\"RECEIVED_TIME\")\n" +
//                    "                            FROM \"MON_HARDWARE_RAWS\"\n" +
//                    "                            where \"IP\" in :ip \n" +
//                    "                            group by \"IP\", \"HARDWARE_NAME\")\t", MonHardwareRaw.class);
//
////            Query hardwareQuery = session.createNativeQuery("select *" +
////                    "                   from \"MON_HARDWARE_RAWS\"\n" +
////                    "                   where (\"IP\", \"HARDWARE_NAME\", \"RECEIVED_TIME\") in (\n" +
////                    "                            SELECT \"IP\", \"HARDWARE_NAME\", MAX(\"RECEIVED_TIME\")\n" +
////                    "                            FROM \"MON_HARDWARE_RAWS\"\n" +
////                    "                            group by \"IP\", \"HARDWARE_NAME\")\t",MonHardwareRaw.class);
//            hardwareQuery.setParameterList("ip",lstIp);
//            lstMonHardwareRaw= (List<MonHardwareRaw>)hardwareQuery.getResultList();
//            for(MonHardwareRaw monHardwareRaw:lstMonHardwareRaw){
//                String ip = monHardwareRaw.getIP();
//                List<MonSoftwareRaw> l1 = new ArrayList<>();
//                for(MonSoftwareRaw monSoftwareRaw:lstMonSoftwareRaw){
//                    if (monSoftwareRaw.getIP().equalsIgnoreCase(ip)) {
//                        l1.add(monSoftwareRaw);
//                    }
//                }
//                ComputerMonitor computerMonitor =new ComputerMonitor();
//                computerMonitor.setMonHardwareRaw(monHardwareRaw);
//                computerMonitor.setLstMonSoftwareRaw(l1);
//                lstComputerMonitor.add(computerMonitor);
//            }
//
//            LOGGER.error("ex.toString()");
//
//        } catch (Exception ex) {
//            LOGGER.error(ex.toString());
//        } finally {
//            closeSession(session);
//        }
//        return lstComputerMonitor != null ? lstComputerMonitor : null;
//    }
//    public List<ComputerMonitor>getMonitorReceiver(List<String> lstIp){
//        Session session = openSession();
//        List<MonSoftwareRaw> lstMonSoftwareRaw= new ArrayList<MonSoftwareRaw>();
//        List<MonHardwareRaw> lstMonHardwareRaw= new ArrayList<MonHardwareRaw>();
//        List<ComputerMonitor> lstComputerMonitor=new ArrayList<ComputerMonitor>();
//        try {
//            NativeQuery query = session.createNativeQuery("select \"ID\",\"IP\", \"MODULE_NAME\", \"STATUS\", \"RECEIVED_TIME\", \"PART_NAME\", \"ADD_TIME\" \n" +
//                    "                   from \"MON_SOFTWARE_RAWS\"\n" +
//                    "                   where (\"IP\", \"MODULE_NAME\",\"RECEIVED_TIME\") in (\n" +
//                    "                            select \"IP\", \"MODULE_NAME\", MAX(\"RECEIVED_TIME\")\n" +
//                    "                            from \"MON_SOFTWARE_RAWS\"\n" +
//                    "                            where \"IP\" in :ip \n" +
//                    "                            group by \"IP\", \"MODULE_NAME\")",MonSoftwareRaw.class);
//
//
//            query.setParameterList("ip",lstIp);
//            lstMonSoftwareRaw = (List<MonSoftwareRaw>)query.getResultList();
//            //hardware
//
//            NativeQuery hardwareQuery = session.createNativeQuery("select \"ID\",\"IP\", \"HARDWARE_NAME\", \"HDD_USED\", \"HDD_TOTAL\", \"RAM_USED\", \"RAM_TOTAL\", \"CPU_USED\", \"STATUS\", \"RECEIVED_TIME\", \"PART_NAME\", \"ADD_TIME\"\n" +
//                    "                   from \"MON_HARDWARE_RAWS\"\n" +
//                    "                   where (\"IP\", \"HARDWARE_NAME\", \"RECEIVED_TIME\") in (\n" +
//                    "                            SELECT \"IP\", \"HARDWARE_NAME\", MAX(\"RECEIVED_TIME\")\n" +
//                    "                            FROM \"MON_HARDWARE_RAWS\"\n" +
//                    "                            where \"IP\" in :ip \n" +
//                    "                            group by \"IP\", \"HARDWARE_NAME\")\t", MonHardwareRaw.class);
//            hardwareQuery.setParameterList("ip",lstIp);
////            Query hardwareQuery = session.createNativeQuery("select *" +
////                    "                   from \"MON_HARDWARE_RAWS\"\n" +
////                    "                   where (\"IP\", \"HARDWARE_NAME\", \"RECEIVED_TIME\") in (\n" +
////                    "                            SELECT \"IP\", \"HARDWARE_NAME\", MAX(\"RECEIVED_TIME\")\n" +
////                    "                            FROM \"MON_HARDWARE_RAWS\"\n" +
////                    "                            group by \"IP\", \"HARDWARE_NAME\")\t",MonHardwareRaw.class);
//            lstMonHardwareRaw= (List<MonHardwareRaw>)hardwareQuery.getResultList();
//            for(MonHardwareRaw monHardwareRaw:lstMonHardwareRaw){
//                String ip = monHardwareRaw.getIP();
//                List<MonSoftwareRaw> l1 = new ArrayList<>();
//                for(MonSoftwareRaw monSoftwareRaw:lstMonSoftwareRaw){
//                    if (monSoftwareRaw.getIP().equalsIgnoreCase(ip)) {
//                        l1.add(monSoftwareRaw);
//                    }
//                }
//                ComputerMonitor computerMonitor =new ComputerMonitor();
//                computerMonitor.setMonHardwareRaw(monHardwareRaw);
//                computerMonitor.setLstMonSoftwareRaw(l1);
//                lstComputerMonitor.add(computerMonitor);
//            }
//
//            LOGGER.error("ex.toString()");
//
//        } catch (Exception ex) {
//            LOGGER.error(ex.toString());
//        } finally {
//            closeSession(session);
//        }
//        return lstComputerMonitor != null ? lstComputerMonitor : null;
//    }
}

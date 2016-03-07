/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cerrid.Intialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.cerrid.constants.Constants;

/**
 *
 * @author Rishabh Prashar
 */
public class PropertiesUtility {

    Properties property = null;
    final File file;
    InputStream inputStream = null;
    OutputStream output = null;
    static Logger logger = Logger.getLogger(PropertiesUtility.class);

    public PropertiesUtility() {
        this.file = new File(Constants.PROPERTY_FILE_PATH);
        property = new Properties();
    }

//This method will take map and save/update property file
    public void saveProperties(Map<String, String> propertyMap) {
        if (!file.exists()) {
            try {
                file.createNewFile();
                logger.error("Creating New File : " + file.getName());
                writeFile(file.getName(), propertyMap);
            } catch (IOException ex) {
                logger.error("Exception while Saving Prperty file :" + ex);
            }
        } else {
            writeFile(file.getName(), propertyMap);
        }
    }

//    This method will read property file and return to ui
    public Map<String, String> readProperties() throws IOException {
        logger.info("Reading Property File ");
        Map<String, String> propertyMap = null;
        if (file.exists()) {
            propertyMap = readFile(Constants.PROPERTY_FILE_PATH);
        } else {
            System.out.print("File Not Found - Reading");
            logger.info("No file exist with name " + file.getAbsolutePath());
        }
        return propertyMap;
    }

//Writing File to disk (Property File)
    private void writeFile(String fileName, Map<String, String> propertyMap) {
        try {
            output = new FileOutputStream(Constants.PROPERTY_FILE_PATH);
            property.clear();
            property.setProperty(Constants.USER_NAME, propertyMap.get(Constants.USER_NAME));
            property.setProperty(Constants.PASSWORD, propertyMap.get(Constants.PASSWORD));
            property.setProperty(Constants.CDS_FILE_PATH, propertyMap.get(Constants.CDS_FILE_PATH));
            property.setProperty(Constants.RISK_CAL_FILE_PATH, propertyMap.get(Constants.RISK_CAL_FILE_PATH));
            // save properties to project root folder
            property.store(output, null);
        } catch (IOException ex) {
            logger.error("Exception while writing file :" + ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                logger.error("Exception while closing output stream :" + ex);
            }
        }
    }

//reading file to disk
    private Map<String, String> readFile(String fileName) {
        Map<String, String> propertyMap = new HashMap<>();
        try {
            inputStream = new FileInputStream(fileName);
            property.clear();
            property.load(inputStream);
            propertyMap.put(Constants.PASSWORD, property.getProperty(Constants.PASSWORD));
            propertyMap.put(Constants.USER_NAME, property.getProperty(Constants.USER_NAME));
            StringBuilder cds_fakePath = new StringBuilder(property.getProperty(Constants.CDS_FILE_PATH));

            propertyMap.put(Constants.CDS_FILE_PATH, cds_fakePath.toString());
            StringBuilder risk_fakePath = new StringBuilder(property.getProperty(Constants.RISK_CAL_FILE_PATH));

            propertyMap.put(Constants.RISK_CAL_FILE_PATH, risk_fakePath.toString());

        } catch (IOException ex) {
            logger.error("Exception while Reading file :" + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                logger.error("Exception while Closing input Stream file :" + ex);
            }
        }

        return propertyMap;
    }

    public static void main(String[] args) throws IOException {
        File file = new File(Constants.PROPERTY_FILE_PATH);
//        if (!file.exists()) {
////            file.createNewFile();
//        } else {
        PropertiesUtility obj = new PropertiesUtility();
        if (file.exists()) {
            obj.readFile(file.getName());
        }

    }

}

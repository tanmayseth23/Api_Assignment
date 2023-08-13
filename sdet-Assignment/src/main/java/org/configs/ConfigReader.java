package org.configs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties properties;
    private static ConfigReader configReader;

    private ConfigReader() {
        BufferedReader reader;
        String propertyFilePath = System.getProperty(ConfigReaderConstant.USER_DIR) + ConfigReaderConstant.SRC_MAIN_RESOURCES_CONFIGURATION_PROPERTIES;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(ConfigReaderConstant.CONFIGURATION_PROPERTIES_NOT_FOUND_AT + propertyFilePath);
        }
    }

    public static ConfigReader getInstance() {
        if (configReader == null) {
            configReader = new ConfigReader();
        }
        return configReader;
    }

    public String getBaseUrl() {
        String baseUrl = properties.getProperty(ConfigReaderConstant.BASE_URL);
        if (baseUrl != null) return baseUrl;
        else throw new RuntimeException(ConfigReaderConstant.BASE_URL_NOT_SPECIFIED_IN_THE_CONFIGURATION_PROPERTIES_FILE);
    }

   /* public String getBasePath()
    {
        String basePath = properties.getProperty("base_Path");
        if(basePath != null) return basePath;
        //else throw new RuntimeException("basePath not specified in the Configuration.properties file.");
    }*/

    public String getCustomerApi() {
        String url = properties.getProperty(ConfigReaderConstant.GET_CUSTOMER_API);
        if (url != null) return url;
        else throw new RuntimeException(ConfigReaderConstant.GET_CUSTOMER_URL_NOT_SPECIFIED_IN_THE_CONFIGURATION_PROPERTIES_FILE);
    }

    public String getWrongCustomerApi() {
        String url = properties.getProperty(ConfigReaderConstant.GET_WRONG_CUSTOMER_API);
        if (url != null) return url;
        else throw new RuntimeException(ConfigReaderConstant.GET_CUSTOMER_URL_NOT_SPECIFIED_IN_THE_CONFIGURATION_PROPERTIES_FILE);
    }

    public String createCustomerApi() {
        String url = properties.getProperty(ConfigReaderConstant.CREATE_CUSTOMER_API);
        if (url != null) return url;
        else throw new RuntimeException(ConfigReaderConstant.CREATE_CUSTOMER_URL_NOT_SPECIFIED_IN_THE_CONFIGURATION_PROPERTIES_FILE);
    }

    public String invalidCreateCustomerApi() {
        String url = properties.getProperty(ConfigReaderConstant.CREATE_CUSTOMER_API1);
        if (url != null) return url;
        else throw new RuntimeException(ConfigReaderConstant.CREATE_CUSTOMER_URL_NOT_SPECIFIED_IN_THE_CONFIGURATION_PROPERTIES_FILE);
    }

}


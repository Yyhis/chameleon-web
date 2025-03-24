package net.yyhis.chameleonweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ga4")
public class GA4Config {
        private String measurementId;
        private String apiSecret;
        private String url;
        private String credentialsPath;
        private int propertyId;
    
        // Getters and Setters
        public String getMeasurementId() {
            return measurementId;
        }
    
        public void setMeasurementId(String measurementId) {
            this.measurementId = measurementId;
        }
    
        public String getApiSecret() {
            return apiSecret;
        }
    
        public void setApiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
        }
    
        public String getUrl() {
            return url;
        }
    
        public void setUrl(String url) {
            this.url = url;
        }
    
        public String getCredentialsPath() {
            return credentialsPath;
        }
    
        public void setCredentialsPath(String credentialsPath) {
            this.credentialsPath = credentialsPath;
        }
    
        public int getPropertyId() {
            return propertyId;
        }
    
        public void setPropertyId(int propertyId) {
            this.propertyId = propertyId;
        }
}
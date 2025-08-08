package utilties;

import io.restassured.response.Response;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

public class RetryUtil {

    public static Response retryRequest(Supplier<Response> requestSupplier, int maxRetries, int delaySeconds, Logger logger) {
        Response response = null;
        int attempt = 1;
       

        while (attempt <= maxRetries) {
            try {
                response = requestSupplier.get();
                if (response != null && response.getStatusCode() == 200) {
                	logger.info("✅ Success on attempt " + attempt);
                    return response;
                } else {
                	logger.info("❌ Attempt " + attempt + " failed. Status: " +
                            (response != null ? response.getStatusCode() : "null response"));
                }
            } catch (Exception e) {
            	logger.info("⚠️ Exception on attempt " + attempt + ": " + e.getMessage());
            			}

            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted", e);
            }

            attempt++;
        }

        return response;
    }
}

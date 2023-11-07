package me.skillspro.notification;

import com.elasticemail.api.CampaignsApi;
import com.elasticemail.api.EmailsApi;
import com.elasticemail.client.ApiClient;
import com.elasticemail.client.ApiException;
import com.elasticemail.client.Configuration;
import com.elasticemail.client.auth.ApiKeyAuth;
import com.elasticemail.model.*;

import java.util.List;

public class ElasticEmailExample {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.elasticemail.com/v4");

        // Configure API key authorization: apikey
        ApiKeyAuth apikey = (ApiKeyAuth) defaultClient.getAuthentication("apikey");
        apikey.setApiKey("");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //apikey.setApiKeyPrefix("Token");

        EmailsApi apiInstance = new EmailsApi(defaultClient);
        EmailMessageData emailMessageData = new EmailMessageData(); // EmailMessageData | Email data
        emailMessageData.recipients(List.of(new EmailRecipient().email("udubic@gmail.com")))
                .content(new EmailContent().from("dubisoft.tech@gmail.com").subject("Skillspro - " +
                        "new mail").body(List.of(new BodyPart().content("welcome to email " +
                        "sending"))));
        try {
            EmailSend result = apiInstance.emailsPost(emailMessageData);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling EmailsApi#emailsPost");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}

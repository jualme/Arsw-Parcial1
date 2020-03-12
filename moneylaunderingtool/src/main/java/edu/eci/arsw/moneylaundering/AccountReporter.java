package edu.eci.arsw.moneylaundering;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AccountReporter {
        public static void report(String account, int amountOfSuspectTransactions) {
            if (!getAccount(account).contains("403")){
                java.lang.String payload = "{"
                    + "\"accountId\": \""+account+"\", "
                    + "\"amountOfSmallTransactions\": \""+amountOfSuspectTransactions+"\" "
                    + "}";
                StringEntity entity = new StringEntity(payload,
                        ContentType.APPLICATION_JSON);                
                try {
                    CloseableHttpClient httpclient = HttpClients.createDefault();                
                    HttpPut request = new HttpPut("http://localhost:8081/fraud-bank-accounts/"+account);
                    request.setEntity(entity);
                    CloseableHttpResponse response = httpclient.execute(request);
                    System.out.println(response.getStatusLine().getStatusCode());
                    response.close();

                } catch (IOException ex) {
                    Logger.getLogger(AccountReporter.class.getName()).log(Level.SEVERE, "Unable to report fraudulent transactions for account", ex);
                }
            }
            else {
                java.lang.String payload = "{"
                    + "\"accountId\": \""+account+"\", "
                    + "\"amountOfSmallTransactions\": \""+amountOfSuspectTransactions+"\" "
                    + "}";

                StringEntity entity = new StringEntity(payload,
                        ContentType.APPLICATION_JSON);

                try {        
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpPost request = new HttpPost("http://localhost:8081/fraud-bank-accounts");
                    request.setEntity(entity);
                    CloseableHttpResponse response = httpclient.execute(request);
                    System.out.println(response.getStatusLine().getStatusCode());
                    response.close();
                } catch (IOException ex) {
                    Logger.getLogger(AccountReporter.class.getName()).log(Level.SEVERE, "Unable to report fraudulent transactions for account", ex);
                }
            }           
        }
        
        
        private static String getAccount(String accountId) {
            String result = "";
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpget = new HttpGet("http://localhost:8081/fraud-bank-accounts/" + accountId);
                CloseableHttpResponse response = httpclient.execute(httpget);
                try {
                    result = response.getStatusLine().toString();
                } finally {
                    response.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(AccountReporter.class.getName()).log(Level.SEVERE, "Unable to get account ID", ex);
            }
            return result;

        }

}

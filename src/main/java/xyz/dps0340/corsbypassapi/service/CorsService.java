package xyz.dps0340.corsbypassapi.service;

import com.google.common.net.InetAddresses;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class CorsService {
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<?> handleCorsService(String url, HttpServletRequest request) {
        if(url.isEmpty() || url.equals("/")) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", false);
            responseData.put("message", "No uri specified");
            return ResponseEntity.ok(responseData);
        }

        try {
            new URL(url);
        } catch (MalformedURLException exception) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", false);
            responseData.put("message", "Broken url");
            return ResponseEntity.ok(responseData);
        }

        if(InetAddresses.isInetAddress(url)) {
            InetAddress address = InetAddresses.forString(url);
            if(address.isAnyLocalAddress() || address.isLoopbackAddress()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", false);
                responseData.put("message", "Access denied on local address");
                return ResponseEntity.ok(responseData);
            }
        }

        // https://stackoverflow.com/questions/61340716/passing-httpservletrequest-to-another-api-controller
        // https://twofootdog.github.io/Spring-POST%EB%B0%A9%EC%8B%9D%EC%9C%BC%EB%A1%9C-%EC%A0%84%EB%8B%AC%EB%90%9C-JSON-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0/


//        byte[] body;
//        IOUtils.toString(request.getReader());
//        restTemplate.postForObject(url, HttpMethod.resolve(request.getMethod()), request, String.class);


        // temp return value

        return ResponseEntity.ok("");
    }
}

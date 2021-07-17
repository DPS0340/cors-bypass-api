package xyz.dps0340.corsbypassapi.service;

import com.google.common.net.InetAddresses;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CorsService {
    RestTemplate restTemplate = buildRestTemplate();

    public ResponseEntity<?> handleCorsService(String url, HttpServletRequest request) {
        if (url == null || url.isEmpty() || url.equals("/")) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", false);
            responseData.put("message", "No uri specified");
            return ResponseEntity.badRequest().body(responseData);
        }

        URL urlObject = null;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", false);
            responseData.put("message", "Broken url");
            return ResponseEntity.badRequest().body(responseData);
        }

        if (InetAddresses.isInetAddress(url)) {
            InetAddress address = InetAddresses.forString(url);
            if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", false);
                responseData.put("message", "Access denied on local address");
                return ResponseEntity.badRequest().body(responseData);
            }
        }

        // https://stackoverflow.com/questions/61340716/passing-httpservletrequest-to-another-api-controller
        // https://twofootdog.github.io/Spring-POST%EB%B0%A9%EC%8B%9D%EC%9C%BC%EB%A1%9C-%EC%A0%84%EB%8B%AC%EB%90%9C-JSON-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0/

        StringBuilder buffer = new StringBuilder();

        try {
            BufferedReader reader = request.getReader();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (IOException exception) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", false);
            responseData.put("message", "Error occurred when parsing request body");
            return ResponseEntity.badRequest().body(responseData);
        }

        // https://stackoverflow.com/questions/6013628/how-to-fetch-base-url-from-the-given-url-using-java

        HttpHeaders headers = getHeadersFromRequest(request);
        String baseUrl = urlObject.getProtocol() + "://" + urlObject.getHost();
        headers.set("host", baseUrl);

        HttpEntity<String> requestForDestination = new HttpEntity<>(buffer.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestForDestination, String.class);

        int statusCode = response.getStatusCodeValue();
        while(statusCode == 301 || statusCode == 302) {
            headers = response.getHeaders();
            url = Objects.requireNonNull(headers.get("Location")).get(0);
            requestForDestination = new HttpEntity<>(buffer.toString(), headers);
            response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestForDestination, String.class);
            statusCode = response.getStatusCodeValue();
        }

        return response;
    }

    // https://stackoverflow.com/questions/25247218/servlet-filter-how-to-get-all-the-headers-from-servletrequest
    public static HttpHeaders getHeadersFromRequest(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h)),
                        (oldValue, newValue) -> newValue,
                        HttpHeaders::new
                ));
    }

    protected static RestTemplate buildRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }
}

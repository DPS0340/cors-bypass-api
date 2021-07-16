package xyz.dps0340.corsbypassapi.controller;

import com.google.common.net.InetAddresses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.dps0340.corsbypassapi.service.CorsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
public class CorsController {

    @Autowired
    CorsService corsService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> get(String url, HttpServletRequest request) {
        return corsService.handleCorsService(url, request);


    }
}

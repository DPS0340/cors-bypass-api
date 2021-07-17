package xyz.dps0340.corsbypassapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import xyz.dps0340.corsbypassapi.service.CorsService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class CorsController {

    @Autowired
    CorsService corsService;

    @RequestMapping(path = {"/*/**"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> route(HttpServletRequest request) {
        String url = request.getRequestURI();
        if(url.startsWith("/")) {
            url = url.substring(1);
        }
        return corsService.handleCorsService(url, request);
    }
}

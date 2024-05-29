package com.ip.server.My.first.Springboot.server.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class IpController {
    public IpController() {}

    @GetMapping("/get-ip")
    public String getIP() {
        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();

        if (requestAttributes != null) {
            return "Your IP Address is: " +
                requestAttributes
                    .getRequest()
                    .getRemoteAddr();
        }
        return "Error while trying to get your IP.";
    }
}
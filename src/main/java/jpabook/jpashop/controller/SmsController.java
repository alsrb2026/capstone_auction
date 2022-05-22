package jpabook.jpashop.controller;

import jpabook.jpashop.service.CertifiService;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SmsController {

    private final CertifiService certifiService;

    // coolSMS 구현 로직 연결
    @GetMapping("/check/sendSMS")
    public @ResponseBody
    String sendSMS(@RequestParam(value="to") String to) throws CoolsmsException {
        return certifiService.PhoneNumberCheck(to);
    }

}


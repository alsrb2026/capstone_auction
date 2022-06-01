package jpabook.jpashop.service;


import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Random;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class CertifiService {

    public String PhoneNumberCheck(String to) throws CoolsmsException {

        String api_key = "NCSKG3PJXW7O9OVN";
        String api_secret = "YJIWFSX2ZSNY7BMQ8W14VMO4NB6KDSPJ";
        Message coolsms = new Message(api_key, api_secret);

        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", to);    // 수신전화번호 (ajax로 view 화면에서 받아온 값으로 넘김)
        params.put("from", "01074948233");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "sms");
        params.put("text", "인증번호는 [" + numStr + "] 입니다.");

        //coolsms.send(params); // 메시지 전송

        return numStr;
    }

    public void sendSms(String to, String from, String msg) {

        String api_key = "NCSKG3PJXW7O9OVN";
        String api_secret = "YJIWFSX2ZSNY7BMQ8W14VMO4NB6KDSPJ";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", "01074948233"); //발신자 전화번호
        params.put("from", "01074948233"); //여기에 받는 사람 전화번호
        params.put("type", "SMS");
        params.put("text", "[도전경매]경매글 :\"삶과 꿈 교재\" 낙찰or즉구 완료! 채팅목록을 확인하세요");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

}

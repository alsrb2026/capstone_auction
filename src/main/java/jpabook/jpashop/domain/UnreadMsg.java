package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnreadMsg {

    String roomId;
    int count;

    public UnreadMsg(String roomId, int count){
        this.roomId = roomId;
        this.count = count;
    }
}

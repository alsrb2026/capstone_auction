package jpabook.jpashop.controller;



import jpabook.jpashop.domain.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/itemList")
    public String showItemList(Model model) {
        List<Item> items = itemService.findItems();

        model.addAttribute("items", items);
        return "/items/showItemList";
    }

    @GetMapping("/item/{id}")
    public String auctionItem(@PathVariable("id") Long id, Model model) {
        Item form = itemService.findOne(id);
        model.addAttribute("form", form);
        return "/board/auctionItemForm.html";
    }

    @GetMapping("items/{id}/auction")
    public String auctionItemForm(@PathVariable("id") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);

        ItemForm form = new ItemForm();
        form.setId(item.getId());
        form.setItemUserId(item.getProduct().getItemUserId());
        form.setName(item.getProduct().getName());
        form.setTitle(item.getProduct().getTitle());
        form.setStartBid(item.getAuctionInfo().getStartBid());
        form.setWinningBid(item.getAuctionInfo().getWinningBid());
        form.setUnitBid(item.getAuctionInfo().getUnitBid());
        form.setDescription(item.getProduct().getDescription());
        form.setStatus(item.getAuctionInfo().getStatus());
        form.setImages(item.getProduct().getImages());
        form.setCurrentBidId(item.getAuctionInfo().getCurrentBidId());
        form.setCurrentBid(item.getAuctionInfo().getCurrentBid());
        form.setStartAuctionTime(item.getAuctionInfo().getStartAuctionTime());
        form.setStartAuctionTime(item.getAuctionInfo().getStartAuctionTime());
        form.setAuctionPeriod(item.getAuctionInfo().getAuctionPeriod());

        model.addAttribute("form", form);
        return "/board/auctionItemForm.html";
    }

    @PostMapping("/items/{id}/auction")
    public String auctionItem(@ModelAttribute("form")  ItemForm form){

        Item item = new Item();
        item.setId(form.getId());
        item.getProduct().setItemUserId(form.getItemUserId());
        item.getProduct().setName(form.getName());
        item.getProduct().setTitle(form.getTitle());
        item.getAuctionInfo().setStartBid(form.getStartBid());
        item.getAuctionInfo().setWinningBid(form.getWinningBid());
        item.getAuctionInfo().setUnitBid(form.getUnitBid());
        item.getProduct().setDescription(form.getDescription());
        item.getAuctionInfo().setStatus(form.getStatus());
        item.getProduct().setImages(form.getImages());
        item.getAuctionInfo().setCurrentBidId(form.getCurrentBidId());
        item.getAuctionInfo().setCurrentBid(form.getCurrentBid());
        item.getAuctionInfo().setStartAuctionTime(form.getStartAuctionTime());
        item.getAuctionInfo().setAuctionPeriod(form.getAuctionPeriod());

        // 낙찰 금액이 범위를 넘어가면 컨트롤러에서 따로 처리를 해야하나?
        /*
        if(item.getCurrentBid() < item.getStartBid() || item.getCurrentBid() < item.getWinningBid()){
            return "";
        }
        */
        // 낙찰 금액이 최고가와 같을 경우, 해당 금액으로 낙찰한 사용자에게 낙찰시킨다. 상품 id를 주고, 상품의 상태를 낙찰 완료로
        if(item.getAuctionInfo().getCurrentBid() == item.getAuctionInfo().getWinningBid()){
            item.getAuctionInfo().setStatus("낙찰 완료");
        }

        if(item.getAuctionInfo().getCurrentBid() < item.getAuctionInfo().getWinningBid()){
            item.getAuctionInfo().setCurrentBid(item.getAuctionInfo().getCurrentBid() + item.getAuctionInfo().getUnitBid());
        }

        // 경매 상품 등록 기간이 다 되었을 경우

        itemService.saveItem(item); // service에 transaction=false로 하고, repository에 saveItem에 em.flush()를 해야
        // db에 내용이 반영된다.

        return "redirect:/";
    }
}

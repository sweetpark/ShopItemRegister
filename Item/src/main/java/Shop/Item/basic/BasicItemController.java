package Shop.Item.basic;

import Shop.Item.dto.Item;
import Shop.Item.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

    @Autowired
    public BasicItemController(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findALl();
        model.addAttribute("items",items);
        return "basic/items";
    }

    @GetMapping("{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model){
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return"/basic/item";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemRequestParam(@RequestParam("price") Integer price,
                          @RequestParam("itemName") String name,
                          @RequestParam("quantity") Integer quantity,
                          Model model){

        Item item = new Item(name, price, quantity);
        itemRepository.save(item);
        return "basic/item";
    }

    //@PostMapping("/add")
    //@ModelAttribute() 인자 생략 가능, 변수명이 넘어온 데이터 이름과 같을경우 @ModelAttribute 자체 생략 가능
    // addItemModelAttribute(Item item, Model model){}
    // model.addAttribute 자동 추가 되므로 인자 생략 가능
    // 최종) addItemModelAttribute(Item item){}
    public String addItemModelAttribute(@ModelAttribute("item") Item item, Model model){
        itemRepository.save(item);
        //model.addAttribute 생략 가능 (자동 추가됨)
        model.addAttribute("item", item);
        return "basic/item";
    }

    //PRG 기법 사용 (Post -> Redirect -> Get)
    //@PostMapping("/add")
    public String addItemRequestParamV2(@RequestParam("price") Integer price,
                                      @RequestParam("itemName") String name,
                                      @RequestParam("quantity") Integer quantity,
                                      Model model){

        Item item = new Item(name, price, quantity);
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId();
    }

    //RedirectAttributes 사용
    @PostMapping("/add")
    public String addItemRequestParamV3(@RequestParam("price") Integer price,
                                        @RequestParam("itemName") String name,
                                        @RequestParam("quantity") Integer quantity,
                                        Model model,
                                        RedirectAttributes redirectAttributes){

        Item item = new Item(name, price, quantity);
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }



    @GetMapping("{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute("item") Item item, Model model){
        itemRepository.update(itemId, item);
        model.addAttribute("item", item);
        return "redirect:/basic/items/{itemId}";
    }

    //테스트 코드
    @PostConstruct
    public void init(){
        Item item1 = new Item("test1",10000,1000);
        Item item2 = new Item("test2",10000,1000);

        itemRepository.save(item1);
        itemRepository.save(item2);
    }
}

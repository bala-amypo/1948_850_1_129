import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService service;

    @PostMapping("/{userId}")
    public Cart createCart(@PathVariable Long userId) {
        return service.createCart(userId);
    }

    @GetMapping("/active/{userId}")
    public Cart getActiveCart(@PathVariable Long userId) {
        return service.getActiveCartForUser(userId);
    }
}

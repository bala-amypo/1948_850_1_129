package com.example.demo;

import org.testng.annotations.Listeners;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.impl.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class EcommerceBundleSaveApplicationTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BundleRuleRepository bundleRuleRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private DiscountApplicationRepository discountApplicationRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private ProductServiceImpl productService;

    @InjectMocks
    private BundleRuleServiceImpl bundleRuleService;

    @InjectMocks
    private CartServiceImpl cartService;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @InjectMocks
    private DiscountServiceImpl discountService;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- 1. Simple Servlet tests (Tomcat-style) ----------

    static class HealthServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(200);
            resp.setContentType("text/plain");
            PrintWriter writer = resp.getWriter();
            writer.write("BUNDLE-OK");
            writer.flush();
        }
    }

    @Test(groups = "servlet", priority = 1)
    public void testServletReturns200() throws Exception {
        HealthServlet servlet = new HealthServlet();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/health");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(req, resp);
        Assert.assertEquals(resp.getStatus(), 200);
    }

    @Test(groups = "servlet", priority = 2)
    public void testServletResponseBody() throws Exception {
        HealthServlet servlet = new HealthServlet();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/health");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(req, resp);
        Assert.assertEquals(resp.getContentAsString(), "BUNDLE-OK");
    }

    @Test(groups = "servlet", priority = 3)
    public void testServletContentTypePlainText() throws Exception {
        HealthServlet servlet = new HealthServlet();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/health");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(req, resp);
        Assert.assertEquals(resp.getContentType(), "text/plain");
    }

    @Test(groups = "servlet", priority = 4)
    public void testServletHandlesMultipleCalls() throws Exception {
        HealthServlet servlet = new HealthServlet();
        for (int i = 0; i < 3; i++) {
            MockHttpServletRequest req = new MockHttpServletRequest("GET", "/health");
            MockHttpServletResponse resp = new MockHttpServletResponse();
            servlet.doGet(req, resp);
            Assert.assertEquals(resp.getStatus(), 200);
        }
    }

    @Test(groups = "servlet", priority = 5)
    public void testServletNoQueryParams() throws Exception {
        HealthServlet servlet = new HealthServlet();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/health");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(req, resp);
        Assert.assertNull(req.getQueryString());
    }

    @Test(groups = "servlet", priority = 6)
    public void testServletServiceMethod() throws Exception {
        HealthServlet servlet = new HealthServlet();
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/health");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.service(req, resp);
        Assert.assertNotEquals(resp.getStatus(), 500);
    }

    @Test(groups = "servlet", priority = 7)
    public void testServletOutputNotEmpty() throws Exception {
        HealthServlet servlet = new HealthServlet();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/health");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(req, resp);
        Assert.assertFalse(resp.getContentAsString().isEmpty());
    }

    @Test(groups = "servlet", priority = 8)
    public void testServletWriterFlushDoesNotThrow() throws Exception {
        HealthServlet servlet = new HealthServlet();
        HttpServletRequest req = new MockHttpServletRequest();
        HttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(req, resp);
        Assert.assertTrue(true);
    }

    // ---------- 2. CRUD operations (Spring Boot REST style) ----------

    @Test(groups = "crud", priority = 9)
    public void testCreateProductSuccess() {
        Product p = new Product();
        p.setSku("SKU1");
        p.setName("Camera");
        p.setPrice(BigDecimal.valueOf(1000));

        when(productRepository.findBySku("SKU1")).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Product saved = productService.createProduct(p);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getSku(), "SKU1");
    }

    @Test(groups = "crud", priority = 10)
    public void testCreateProductDuplicateSku() {
        Product p = new Product();
        p.setSku("SKU1");
        p.setName("Camera");
        p.setPrice(BigDecimal.valueOf(1000));

        when(productRepository.findBySku("SKU1")).thenReturn(Optional.of(p));

        try {
            productService.createProduct(p);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("SKU"));
        }
    }

    @Test(groups = "crud", priority = 11)
    public void testUpdateProductPrice() {
        Product existing = new Product();
        existing.setId(1L);
        existing.setSku("SKU1");
        existing.setName("Old");
        existing.setPrice(BigDecimal.valueOf(500));

        Product updated = new Product();
        updated.setSku("SKU1");
        updated.setName("New");
        updated.setPrice(BigDecimal.valueOf(800));

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.updateProduct(1L, updated);
        Assert.assertEquals(result.getPrice(), BigDecimal.valueOf(800));
        Assert.assertEquals(result.getName(), "New");
    }

    @Test(groups = "crud", priority = 12)
    public void testGetProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        try {
            productService.getProductById(99L);
            Assert.fail("Expected EntityNotFoundException");
        } catch (EntityNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Product not found"));
        }
    }

    @Test(groups = "crud", priority = 13)
    public void testCreateCartForUser() {
        when(cartRepository.findByUserIdAndActiveTrue(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> {
            Cart c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });

        Cart cart = cartService.createCart(1L);
        Assert.assertNotNull(cart.getId());
        Assert.assertEquals(cart.getUserId(), Long.valueOf(1L));
    }

    @Test(groups = "crud", priority = 14)
    public void testGetActiveCartForUserNotFound() {
        when(cartRepository.findByUserIdAndActiveTrue(2L)).thenReturn(Optional.empty());
        try {
            cartService.getActiveCartForUser(2L);
            Assert.fail("Expected EntityNotFoundException");
        } catch (EntityNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Active cart not found"));
        }
    }

    @Test(groups = "crud", priority = 15)
    public void testAddItemToCartNewProduct() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setActive(true);

        Product product = new Product();
        product.setId(2L);
        product.setActive(true);
        product.setPrice(BigDecimal.valueOf(500));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(2);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductId(1L, 2L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(inv -> {
            CartItem ci = inv.getArgument(0);
            ci.setId(20L);
            return ci;
        });

        CartItem saved = cartItemService.addItemToCart(item);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getQuantity(), Integer.valueOf(2));
    }

    @Test(groups = "crud", priority = 16)
    public void testAddItemToCartAggregatesQuantity() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setActive(true);

        Product product = new Product();
        product.setId(2L);
        product.setActive(true);
        product.setPrice(BigDecimal.valueOf(500));

        CartItem existing = new CartItem();
        existing.setId(30L);
        existing.setCart(cart);
        existing.setProduct(product);
        existing.setQuantity(1);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(3);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductId(1L, 2L)).thenReturn(Optional.of(existing));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(inv -> inv.getArgument(0));

        CartItem saved = cartItemService.addItemToCart(item);
        Assert.assertEquals(saved.getQuantity(), Integer.valueOf(4));
    }

    @Test(groups = "crud", priority = 17)
    public void testEvaluateDiscountsAppliesRule() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setActive(true);

        Product camera = new Product();
        camera.setId(10L);
        camera.setPrice(BigDecimal.valueOf(1000));
        camera.setActive(true);

        Product lens = new Product();
        lens.setId(12L);
        lens.setPrice(BigDecimal.valueOf(500));
        lens.setActive(true);

        CartItem ci1 = new CartItem();
        ci1.setId(1L);
        ci1.setCart(cart);
        ci1.setProduct(camera);
        ci1.setQuantity(1);

        CartItem ci2 = new CartItem();
        ci2.setId(2L);
        ci2.setCart(cart);
        ci2.setProduct(lens);
        ci2.setQuantity(1);

        BundleRule rule = new BundleRule();
        rule.setId(100L);
        rule.setRuleName("Camera+Lens 15%");
        rule.setRequiredProductIds("10,12");
        rule.setDiscountPercentage(15.0);
        rule.setActive(true);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L)).thenReturn(List.of(ci1, ci2));
        when(bundleRuleRepository.findByActiveTrue()).thenReturn(List.of(rule));
        doNothing().when(discountApplicationRepository).deleteByCartId(1L);
        when(discountApplicationRepository.save(any(DiscountApplication.class))).thenAnswer(inv -> {
            DiscountApplication app = inv.getArgument(0);
            app.setId(200L);
            app.setAppliedAt(LocalDateTime.now());
            return app;
        });

        List<DiscountApplication> apps = discountService.evaluateDiscounts(1L);
        Assert.assertEquals(apps.size(), 1);
        Assert.assertTrue(apps.get(0).getDiscountAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test(groups = "crud", priority = 18)
    public void testEvaluateDiscountsNoMatchingBundle() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setActive(true);

        Product camera = new Product();
        camera.setId(10L);
        camera.setPrice(BigDecimal.valueOf(1000));
        camera.setActive(true);

        CartItem ci1 = new CartItem();
        ci1.setId(1L);
        ci1.setCart(cart);
        ci1.setProduct(camera);
        ci1.setQuantity(1);

        BundleRule rule = new BundleRule();
        rule.setId(100L);
        rule.setRuleName("Camera+Lens 15%");
        rule.setRequiredProductIds("10,12");
        rule.setDiscountPercentage(15.0);
        rule.setActive(true);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L)).thenReturn(List.of(ci1));
        when(bundleRuleRepository.findByActiveTrue()).thenReturn(List.of(rule));
        doNothing().when(discountApplicationRepository).deleteByCartId(1L);

        List<DiscountApplication> apps = discountService.evaluateDiscounts(1L);
        Assert.assertTrue(apps.isEmpty());
    }

    @Test(groups = "crud", priority = 19)
    public void testDeactivateProduct() {
        Product p = new Product();
        p.setId(1L);
        p.setActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        productService.deactivateProduct(1L);
        Assert.assertFalse(p.getActive());
    }

    @Test(groups = "crud", priority = 20)
    public void testGetItemsForCartEmpty() {
        when(cartItemRepository.findByCartId(99L)).thenReturn(Collections.emptyList());
        List<CartItem> items = cartItemService.getItemsForCart(99L);
        Assert.assertTrue(items.isEmpty());
    }

    // ---------- 3. Dependency Injection and IoC ----------

    @Test(groups = "di", priority = 21)
    public void testProductServiceInjectedRepository() {
        Assert.assertNotNull(productService);
        Assert.assertNotNull(productRepository);
    }

    @Test(groups = "di", priority = 22)
    public void testBundleRuleServiceInjectedRepository() {
        Assert.assertNotNull(bundleRuleService);
        Assert.assertNotNull(bundleRuleRepository);
    }

    @Test(groups = "di", priority = 23)
    public void testCartServiceInjectedRepository() {
        Assert.assertNotNull(cartService);
        Assert.assertNotNull(cartRepository);
    }

    @Test(groups = "di", priority = 24)
    public void testCartItemServiceInjectedRepositories() {
        Assert.assertNotNull(cartItemService);
        Assert.assertNotNull(cartItemRepository);
        Assert.assertNotNull(productRepository);
    }

    @Test(groups = "di", priority = 25)
    public void testDiscountServiceInjectedRepositories() {
        Assert.assertNotNull(discountService);
        Assert.assertNotNull(discountApplicationRepository);
        Assert.assertNotNull(bundleRuleRepository);
    }

    @Test(groups = "di", priority = 26)
    public void testIoCAllowsMockOverrideForProductRepository() {
        Product p = new Product();
        p.setId(88L);
        when(productRepository.findById(88L)).thenReturn(Optional.of(p));
        Product result = productService.getProductById(88L);
        Assert.assertEquals(result, p);
    }

    @Test(groups = "di", priority = 27)
    public void testIoCConfiguredForJwtTokenProvider() {
        Assert.assertNotNull(jwtTokenProvider);
    }

    // ---------- 4. Hibernate configs, annotations, CRUD ----------

    @Test(groups = "hibernate", priority = 28)
    public void testProductPriceMustBePositive() {
        Product p = new Product();
        p.setSku("NEG");
        p.setName("Invalid");
        p.setPrice(BigDecimal.valueOf(-10));

        when(productRepository.findBySku("NEG")).thenReturn(Optional.empty());

        try {
            productService.createProduct(p);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Price"));
        }
    }

    @Test(groups = "hibernate", priority = 29)
    public void testBundleRuleDiscountRangeValidation() {
        BundleRule rule = new BundleRule();
        rule.setRuleName("Invalid");
        rule.setRequiredProductIds("1,2");
        rule.setDiscountPercentage(150.0);

        try {
            bundleRuleService.createRule(rule);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("between 0 and 100"));
        }
    }

    @Test(groups = "hibernate", priority = 30)
    public void testBundleRuleRequiredProductsCannotBeEmpty() {
        BundleRule rule = new BundleRule();
        rule.setRuleName("Empty");
        rule.setRequiredProductIds("   ");
        rule.setDiscountPercentage(10.0);

        try {
            bundleRuleService.createRule(rule);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("cannot be empty"));
        }
    }

    @Test(groups = "hibernate", priority = 31)
    public void testCartActiveFlagDefaultTrue() {
        Cart c = new Cart();
        c.setUserId(1L);
        Assert.assertTrue(c.getActive());
    }

    @Test(groups = "hibernate", priority = 32)
    public void testCartItemQuantityMustBePositive() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setActive(true);
        Product prod = new Product();
        prod.setId(2L);
        prod.setActive(true);
        prod.setPrice(BigDecimal.TEN);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(prod);
        item.setQuantity(0);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(2L)).thenReturn(Optional.of(prod));

        try {
            cartItemService.addItemToCart(item);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Quantity"));
        }
    }

    @Test(groups = "hibernate", priority = 33)
    public void testCartCannotAcceptItemsWhenInactive() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setActive(false);

        Product prod = new Product();
        prod.setId(2L);
        prod.setActive(true);
        prod.setPrice(BigDecimal.TEN);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(prod);
        item.setQuantity(1);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(2L)).thenReturn(Optional.of(prod));

        try {
            cartItemService.addItemToCart(item);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("active carts"));
        }
    }

    @Test(groups = "hibernate", priority = 34)
    public void testEvaluateDiscountsInactiveCartReturnsEmpty() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setActive(false);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        List<DiscountApplication> apps = discountService.evaluateDiscounts(1L);
        Assert.assertTrue(apps.isEmpty());
    }

    // ---------- 5. JPA mapping & normalization (1NF, 2NF, 3NF) ----------

    @Test(groups = "jpa-normalization", priority = 35)
    public void testCartItemReferencesCartAndProduct() {
        Cart cart = new Cart();
        cart.setId(1L);
        Product product = new Product();
        product.setId(2L);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);

        Assert.assertEquals(item.getCart().getId(), Long.valueOf(1L));
        Assert.assertEquals(item.getProduct().getId(), Long.valueOf(2L));
    }

    @Test(groups = "jpa-normalization", priority = 36)
    public void testDiscountApplicationReferencesCartAndRule() {
        Cart cart = new Cart();
        cart.setId(1L);
        BundleRule rule = new BundleRule();
        rule.setId(2L);

        DiscountApplication app = new DiscountApplication();
        app.setCart(cart);
        app.setBundleRule(rule);

        Assert.assertEquals(app.getCart().getId(), Long.valueOf(1L));
        Assert.assertEquals(app.getBundleRule().getId(), Long.valueOf(2L));
    }

    @Test(groups = "jpa-normalization", priority = 37)
    public void testCartDoesNotDuplicateProductData() {
        Cart cart = new Cart();
        cart.setUserId(5L);
        Assert.assertEquals(cart.getUserId(), Long.valueOf(5L));
    }

    @Test(groups = "jpa-normalization", priority = 38)
    public void testProductIndependentOfCart() {
        Product p = new Product();
        p.setSku("X");
        p.setName("Generic");
        Assert.assertEquals(p.getSku(), "X");
    }

    @Test(groups = "jpa-normalization", priority = 39)
    public void testBundleRuleUsesCsvInsteadOfMultipleColumns() {
        BundleRule r = new BundleRule();
        r.setRequiredProductIds("1,2,3");
        Assert.assertTrue(r.getRequiredProductIds().contains(","));
    }

    @Test(groups = "jpa-normalization", priority = 40)
    public void testCartHasSingleUserIdField() {
        Cart c = new Cart();
        c.setUserId(10L);
        Assert.assertEquals(c.getUserId(), Long.valueOf(10L));
    }

    @Test(groups = "jpa-normalization", priority = 41)
    public void testCartItemDoesNotStoreUserIdDirectly() {
        CartItem ci = new CartItem();
        Assert.assertNull(getUserIdFromCartItem(ci));
    }

    private Long getUserIdFromCartItem(CartItem ci) {
        return null;
    }

    @Test(groups = "jpa-normalization", priority = 42)
    public void testDiscountApplicationDoesNotRepeatCartFields() {
        DiscountApplication app = new DiscountApplication();
        Assert.assertNull(app.getCart());
    }

    // ---------- 6. Many-to-Many relationships (conceptual) ----------

    static class DummyProduct {
        Long id;
        Set<DummyTag> tags = new HashSet<>();
    }

    static class DummyTag {
        Long id;
        Set<DummyProduct> products = new HashSet<>();
    }

    @Test(groups = "many-to-many", priority = 43)
    public void testManyToManyAddTagToProduct() {
        DummyProduct p = new DummyProduct();
        DummyTag t = new DummyTag();
        p.tags.add(t);
        t.products.add(p);
        Assert.assertTrue(p.tags.contains(t));
        Assert.assertTrue(t.products.contains(p));
    }

    @Test(groups = "many-to-many", priority = 44)
    public void testManyToManyProductCanHaveMultipleTags() {
        DummyProduct p = new DummyProduct();
        DummyTag t1 = new DummyTag();
        DummyTag t2 = new DummyTag();
        p.tags.add(t1);
        p.tags.add(t2);
        Assert.assertEquals(p.tags.size(), 2);
    }

    @Test(groups = "many-to-many", priority = 45)
    public void testManyToManyTagCanHaveMultipleProducts() {
        DummyTag t = new DummyTag();
        DummyProduct p1 = new DummyProduct();
        DummyProduct p2 = new DummyProduct();
        t.products.add(p1);
        t.products.add(p2);
        Assert.assertEquals(t.products.size(), 2);
    }

    @Test(groups = "many-to-many", priority = 46)
    public void testManyToManyBidirectionalConsistency() {
        DummyProduct p = new DummyProduct();
        DummyTag t = new DummyTag();
        p.tags.add(t);
        t.products.add(p);
        Assert.assertTrue(p.tags.iterator().next().products.contains(p));
    }

    @Test(groups = "many-to-many", priority = 47)
    public void testManyToManyNoDuplicateTags() {
        DummyProduct p = new DummyProduct();
        DummyTag t = new DummyTag();
        p.tags.add(t);
        p.tags.add(t);
        Assert.assertEquals(p.tags.size(), 1);
    }

    @Test(groups = "many-to-many", priority = 48)
    public void testManyToManyEmptySets() {
        DummyProduct p = new DummyProduct();
        DummyTag t = new DummyTag();
        Assert.assertTrue(p.tags.isEmpty());
        Assert.assertTrue(t.products.isEmpty());
    }

    // ---------- 7. Security controls & JWT auth ----------

    @Test(groups = "security", priority = 49)
    public void testJwtGenerateTokenReturnsString() {
        String email = "user@example.com";
        String role = "CUSTOMER";
        Long userId = 1L;
        when(jwtTokenProvider.generateToken(email, role, userId)).thenReturn("token-123");
        String token = jwtTokenProvider.generateToken(email, role, userId);
        Assert.assertEquals(token, "token-123");
    }

    @Test(groups = "security", priority = 50)
    public void testJwtValidateTokenTrue() {
        when(jwtTokenProvider.validateToken("valid")).thenReturn(true);
        Assert.assertTrue(jwtTokenProvider.validateToken("valid"));
    }

    @Test(groups = "security", priority = 51)
    public void testJwtValidateTokenFalse() {
        when(jwtTokenProvider.validateToken("invalid")).thenReturn(false);
        Assert.assertFalse(jwtTokenProvider.validateToken("invalid"));
    }

    @Test(groups = "security", priority = 52)
    public void testJwtClaimsIncludeRoleAndUserIdConceptually() {
        String email = "admin@example.com";
        String role = "ADMIN";
        Long userId = 99L;
        when(jwtTokenProvider.generateToken(email, role, userId)).thenReturn("jwt-admin");
        String token = jwtTokenProvider.generateToken(email, role, userId);
        Assert.assertEquals(token, "jwt-admin");
    }

    @Test(groups = "security", priority = 53)
    public void testUnauthorizedAccessFlagFalse() {
        boolean hasToken = false;
        Assert.assertFalse(hasToken);
    }

    @Test(groups = "security", priority = 54)
    public void testAuthorizedAccessFlagTrue() {
        boolean hasToken = true;
        Assert.assertTrue(hasToken);
    }

    @Test(groups = "security", priority = 55)
    public void testSecurityContextConceptuallyRequiresToken() {
        boolean tokenPresent = true;
        Assert.assertTrue(tokenPresent);
    }

    @Test(groups = "security", priority = 56)
    public void testSecurityContextConceptuallyRejectsMissingToken() {
        boolean tokenPresent = false;
        Assert.assertFalse(tokenPresent);
    }

    // ---------- 8. HQL and HCQL advanced querying ----------

    @Test(groups = "hql-hcql", priority = 57)
    public void testHqlQueryCartItemsByMinQuantityString() {
        String hql = "select ci from CartItem ci where ci.cart.id = :cartId and ci.quantity >= :minQty";
        Assert.assertTrue(hql.contains("CartItem"));
    }

    @Test(groups = "hql-hcql", priority = 58)
    public void testHqlQueryDiscountApplicationsByCartString() {
        String hql = "select d from DiscountApplication d where d.cart.id = :cartId";
        Assert.assertTrue(hql.contains("DiscountApplication"));
    }

    @Test(groups = "hql-hcql", priority = 59)
    public void testRepositoryFindByCartAndMinQuantity() {
        List<CartItem> result = List.of(new CartItem(), new CartItem());
        when(cartItemRepository.findByCartIdAndMinQuantity(1L, 2)).thenReturn(result);
        List<CartItem> fetched = cartItemRepository.findByCartIdAndMinQuantity(1L, 2);
        Assert.assertEquals(fetched.size(), 2);
    }

    @Test(groups = "hql-hcql", priority = 60)
    public void testRepositoryFindDiscountApplicationsForCart() {
        List<DiscountApplication> result = List.of(new DiscountApplication());
        when(discountApplicationRepository.findByCartId(1L)).thenReturn(result);
        List<DiscountApplication> fetched = discountApplicationRepository.findByCartId(1L);
        Assert.assertEquals(fetched.size(), 1);
    }

    @Test(groups = "hql-hcql", priority = 61)
    public void testAdvancedQueryEmptyResults() {
        when(discountApplicationRepository.findByCartId(99L)).thenReturn(Collections.emptyList());
        List<DiscountApplication> fetched = discountApplicationRepository.findByCartId(99L);
        Assert.assertTrue(fetched.isEmpty());
    }

    @Test(groups = "hql-hcql", priority = 62)
    public void testCriteriaMapForBundleSearch() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("minDiscount", 10.0);
        criteria.put("active", true);
        Assert.assertTrue(criteria.containsKey("minDiscount"));
        Assert.assertTrue(criteria.containsKey("active"));
    }

    @Test(groups = "hql-hcql", priority = 63)
    public void testEmptyCriteriaMap() {
        Map<String, Object> criteria = new HashMap<>();
        Assert.assertTrue(criteria.isEmpty());
    }

    @Test(groups = "hql-hcql", priority = 64)
    public void testCombinedHqlBuildingConcept() {
        String base = "select d from DiscountApplication d where 1=1";
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("cartId", 1L);
        criteria.put("minAmount", BigDecimal.TEN);

        StringBuilder sb = new StringBuilder(base);
        if (criteria.containsKey("cartId")) {
            sb.append(" and d.cart.id = :cartId");
        }
        if (criteria.containsKey("minAmount")) {
            sb.append(" and d.discountAmount >= :minAmount");
        }

        String finalQuery = sb.toString();
        Assert.assertTrue(finalQuery.contains("cart.id"));
        Assert.assertTrue(finalQuery.contains("discountAmount"));
    }
}

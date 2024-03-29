package ru.bookstore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bookstore.domain.*;
import ru.bookstore.form.*;
import ru.bookstore.repos.*;
import ru.bookstore.services.Sum;

import javax.validation.Valid;
import java.sql.Date;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private PublishHouseRepo houseRepo;

    @Autowired
    private LanguageRepo languageRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private CoverRepo coverRepo;

    @Autowired
    private BooksAuthorsRepo booksAuthorsRepo;

    @Autowired
    private BooksGenresRepo booksGenresRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private  StatusRepo statusRepo;

    @Autowired
    private DeliveryRepo deliveryRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private BooksOrdersRepo booksOrdersRepo;

    @Autowired
    private AddressRepo addressRepo;

    private Map<Integer,BooksOrders> booksOrdersList = new HashMap<>();

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "/index";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    @GetMapping("/adminpanel")
    public String adminpanel() { return "/adminpanel"; }


    @RequestMapping(value = "/characteristic", method = RequestMethod.GET)
    public String showAllcharacteristic(Model model, Genre genre, PublishHouse publishHouse, Language language) {
        model.addAttribute("genres", genreRepo.findAll());
        model.addAttribute("publish", houseRepo.findAll());
        model.addAttribute("languages", languageRepo.findAll());
        return "/characteristic";
    }

    @RequestMapping(value = "/characteristicGenre", method = RequestMethod.POST)
    public String addNewGenre(@Valid Genre genre, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        genreRepo.save(new GenreEntity(genre.getName()));
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicPublish", method = RequestMethod.POST)
    public String addNewPublish(@Valid PublishHouse publishHouse, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        houseRepo.save(new PublishHouseEntity(publishHouse.getName(), publishHouse.getCountry(), publishHouse.getCity()));
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicLanguage", method = RequestMethod.POST)
    public String addNewLanguage(@Valid Language language, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        languageRepo.save(new LanguageEntity(language.getName()));
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicGenreDel", method = RequestMethod.POST)
    public String deleteGenre(@Valid Genre genre, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        genreRepo.deleteByName(genre.getName());
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicPublishDel", method = RequestMethod.POST)
    public String deletePublish(@Valid PublishHouse publishHouse, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        houseRepo.deleteByName(publishHouse.getName());
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/characteristicLanguageDel", method = RequestMethod.POST)
    public String deleteLanguage(@Valid Language language, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/characteristic";
        }
        languageRepo.deleteByName(language.getName());
        return "redirect:/characteristic";
    }

    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public String showAllAuthors(Model model, Author author)
    {
        model.addAttribute("authors", authorRepo.findAllByOrderBySurname());
        return "/authors";
    }

    @RequestMapping(value = "/addAuthor", method = RequestMethod.POST)
    public String addNewAuthor(@Valid Author author, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/authors";
        }
        authorRepo.save(new AuthorEntity(author.getName(), author.getSurname()));
        return "redirect:/authors";
    }

    @RequestMapping(value = "/DelAuthor", method = RequestMethod.POST)
    public String deleteLanguage(@Valid Author author, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/authors";
        }
        authorRepo.deleteByNameAndSurname(author.getName(), author.getSurname());
        return "redirect:/authors";
    }

    @RequestMapping(value = "/newbook", method = RequestMethod.GET)
    public String addBookForm(Model model, Book book, Cover cover, Language language, PublishHouse publishHouse)
    {
        model.addAttribute("covers", coverRepo.findAll());
        model.addAttribute("languages", languageRepo.findAll());
        model.addAttribute("publishhouses", houseRepo.findAll());
        return "/newbook";
    }

    @RequestMapping(value = "/addBook", method = RequestMethod.POST)
    public String addNewBook(@Valid Book book, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/newbook";
        }
        bookRepo.save(new BookEntity(book.getName(), book.getPrice(), book.getCover(), book.getYear(), book.getPages(), book.getPublishhouse(), book.getLanguage(), book.getQuantity(), book.getImage(), book.getText()));
        return "redirect:/newbook";
    }

    @RequestMapping(value = "/catalog", method = RequestMethod.GET)
    public String showCatalog(Model model, Book book, BooksOrders booksOrders)
    {
        model.addAttribute("books", bookRepo.findAllByOrderByIdDesc());
        model.addAttribute("genres", genreRepo.findAllByOrderByName());
        model.addAttribute("publish", houseRepo.findAllByOrderByName());
        model.addAttribute("languages", languageRepo.findAll());
        model.addAttribute("cover", coverRepo.findAll());
        model.addAttribute("bookauthor", booksAuthorsRepo.findAll());
        model.addAttribute("authors", authorRepo.findAll());
        model.addAttribute("bookgenre", booksGenresRepo.findAll());
        return "/catalog";
    }

    @RequestMapping(value = "/authandgen", method = RequestMethod.GET)
    public String authAndGen(Model model, Book book, BooksAuthors booksAuthors, BooksGenres booksGenres){
        model.addAttribute("authors", authorRepo.findAllByOrderBySurname());
        model.addAttribute("genres", genreRepo.findAllByOrderByName());
        model.addAttribute("books", null);
        return "/authandgen";
    }

    @RequestMapping(value = "/addbookauthor", method = RequestMethod.POST)
    public String addBooksAuthors(@Valid BooksAuthors booksAuthors, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/authandgen";
        }
        booksAuthorsRepo.save(new BooksAuthorsEntity(booksAuthors.getBook(), booksAuthors.getAuthor()));
        return "redirect:/authandgen";
    }

    @RequestMapping(value = "/findbook", method = RequestMethod.POST)
    public String findBook(@Valid Book book, BindingResult bindingResult, Model model, BooksAuthors booksAuthors, BooksGenres booksGenres){
        if (bindingResult.hasErrors()) {
            return "/authandgen";
        }
        model.addAttribute("authors", authorRepo.findAllByOrderBySurname());
        model.addAttribute("genres", genreRepo.findAllByOrderByName());
        model.addAttribute("books", bookRepo.findByName(book.getName()));
        return "/authandgen";
    }

    @RequestMapping(value = "/addbookgenre", method = RequestMethod.POST)
    public String addBooksGenres(@Valid BooksGenres booksGenres, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "/authandgen";
        }
        booksGenresRepo.save(new BooksGenresEntity(booksGenres.getBook(), booksGenres.getGenre()));
        return "redirect:/authandgen";
    }

    @RequestMapping(value = "/addBuyer", method = RequestMethod.POST)
    public String addBuyer(@Valid Buyer buyer, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "redirect:/reg";
        }

//        if(buyerRepo.findByEmail(buyer.getEmail())!=null){
//            model.addAttribute("message", "Пользователь с таким почтовым адресов уже сущетсвует");
//            return "redirect:/reg";
//        }
        buyer.setRole("ROLE_USER");
        buyer.setEnabled(true);
        buyerRepo.save(new BuyerEntity(buyer.getName(), buyer.getSurname(), buyer.getPhone(), buyer.getEmail(), buyer.getPassword(), buyer.isEnabled(), buyer.getRole()));
        return "redirect:/catalog";
    }

    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public String reg(Buyer buyer) {
        return "/reg";
    }

    @RequestMapping(value = "/addToBin", method = RequestMethod.POST)
    public String addToBin(@Valid BooksOrders booksOrders, BindingResult bindingResult, Model model)
    {
        booksOrdersList.put(booksOrders.getBook(), booksOrders);
        return "redirect:/catalog";
    }

    @RequestMapping(value = "/bin", method = RequestMethod.GET)
    public String bin(Model model, BooksOrders booksOrders) {
        model.addAttribute("booksOrdersList", booksOrdersList.values());
        model.addAttribute("books", bookRepo.findAll());
        return "/bin";
    }

    @RequestMapping(value = "/deleteFromBin", method = RequestMethod.POST)
    public String deleteFromBin(@Valid BooksOrders booksOrders, BindingResult bindingResult, Model model)
    {

        booksOrdersList.remove(booksOrders.getBook());
        return "redirect:/catalog";
    }

    @RequestMapping(value = "/makeorder", method = RequestMethod.GET)
    public String makeOrder(Model model, BooksOrders booksOrders, Sum sum, Order order, Delivery delivery) {
        Iterator<BooksOrders> iterator = booksOrdersList.values().iterator();
        double sumlocal = 0;
        while (iterator.hasNext()){
            BooksOrders booksOrderslocal=iterator.next();
            BookEntity bookEntity = bookRepo.findById(booksOrderslocal.getBook());
            sumlocal += (bookEntity.getPrice() * booksOrderslocal.getQuantity());
        }
        sum.setSum(sumlocal);
        model.addAttribute("booksOrdersList", booksOrdersList.values());
        model.addAttribute("books", bookRepo.findAll());
        model.addAttribute("deliveries", deliveryRepo.findAll());
        return "/makeorder";
    }

    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    public String addOrder(@Valid Order order, BindingResult bindingResult, Model model)
    {
        Iterator<BooksOrders> iterator = booksOrdersList.values().iterator();
        double sumlocal = 0;
        while (iterator.hasNext()){
            BooksOrders booksOrderslocal=iterator.next();
            BookEntity bookEntity = bookRepo.findById(booksOrderslocal.getBook());
            sumlocal += (bookEntity.getPrice() * booksOrderslocal.getQuantity());

        }
        java.util.Date date = new java.util.Date();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BuyerEntity buyerEntity = buyerRepo.findByEmail(auth.getName());
        orderRepo.save(new OrderEntity(new java.sql.Date(date.getYear(), date.getMonth(), date.getDay()), buyerEntity.getId(), 1, order.getDelivery(), sumlocal, null));
        int num = orderRepo.findFirstByOrderByIdDesc().getId();
        iterator = booksOrdersList.values().iterator();
        while (iterator.hasNext()){
            BooksOrders booksOrderslocal=iterator.next();
            booksOrdersRepo.save(new BooksOrdersEntity(num, booksOrderslocal.getBook(), booksOrderslocal.getQuantity()));
            BookEntity bookEntity = bookRepo.findById(booksOrderslocal.getBook());
            int quantity = bookEntity.getQuantity();
            quantity -= booksOrderslocal.getQuantity();
            bookEntity.setQuantity(quantity);
            bookRepo.save(bookEntity);
        }
        booksOrdersList.clear();
        if(order.getDelivery()==1)
            return "redirect:/catalog";
        return "redirect:/addaddress";
    }

    @RequestMapping(value = "/addaddress", method = RequestMethod.GET)
    public String addAddress(Model model, Address address) {
        return "/addaddress";
    }

    @RequestMapping(value = "/inputaddress", method = RequestMethod.POST)
    public String addAddress(@Valid Address address, BindingResult bindingResult, Model model)
    {
        OrderEntity orderEntity = orderRepo.findFirstByOrderByIdDesc();
        addressRepo.save(new AddressEntity(address.getCity(), address.getStreet(), address.getBuild(), address.getRoom()));
        int addId = addressRepo.findFirstByOrderByIdDesc().getId();
        orderEntity.setAddress(addId);
        orderRepo.save(orderEntity);
        return "redirect:/catalog";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(Model model, Buyer buyer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BuyerEntity buyerEntity = buyerRepo.findByEmail(auth.getName());
        model.addAttribute("orders", orderRepo.findAllByBuyer(buyerEntity.getId()));
        buyer.setName(buyerEntity.getName());
        buyer.setSurname(buyerEntity.getSurname());
        model.addAttribute("status", statusRepo.findAll());
        model.addAttribute("delivery", deliveryRepo.findAll());
        return "/user";
    }

    @RequestMapping(value = "/findbookcatalog", method = RequestMethod.POST)
    public String findBookCatalog(@Valid Book book, BindingResult bindingResult, Model model, BooksOrders booksOrders){
        if (bindingResult.hasErrors()) {
            return "/catalog";
        }
        model.addAttribute("genres", genreRepo.findAllByOrderByName());
        model.addAttribute("publish", houseRepo.findAllByOrderByName());
        model.addAttribute("languages", languageRepo.findAll());
        model.addAttribute("cover", coverRepo.findAll());
        model.addAttribute("bookauthor", booksAuthorsRepo.findAll());
        model.addAttribute("authors", authorRepo.findAll());
        model.addAttribute("bookgenre", booksGenresRepo.findAll());
        model.addAttribute("books", bookRepo.findByName(book.getName()));
        return "/catalog";
    }
}

package com.newcoder.projectnowcoder.controller;





import com.newcoder.projectnowcoder.model.User;
import com.newcoder.projectnowcoder.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


//@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    WendaService wendaService;

    @RequestMapping(path = {"/","index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession) {
        logger.info("VISIT HOME");
        return "Hello NowCoder!!" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupName}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupName") String groupName,
                          @RequestParam(value = "type", defaultValue = "1", required = true) int type,
                          @RequestParam(value = "key", defaultValue = "z", required = false) String key) {
        return String.format("Profile Page of %s/%d, t:%d, k=%s", groupName,userId,type,key);
    }

    @RequestMapping(path = "/ftl", method = {RequestMethod.GET})
    public String template(Model model) {
        model.addAttribute("value1", "v1111");
        List<String> colors = Arrays.asList(new String[]{"Red", "Green", "Blue"});
        model.addAttribute("colors", colors);

        model.addAttribute("user", new User("Lee"));

        return "home";
    }

    @RequestMapping(path = "/request", method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId) {

        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE:" + sessionId);
        Enumeration<String> headerName = request.getHeaderNames();
        while (headerName.hasMoreElements()) {
            String name = headerName.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                 sb.append("Cookie:" + cookie.getName() + "Value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");


        response.addHeader("nowcoderId", "hello");
        response.addCookie(new Cookie("username", "nowcoder"));
        return sb.toString();


    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})

    public RedirectView redirect(@PathVariable("code") int code,
                           HttpSession httpSession) {
        httpSession.setAttribute("msg", "junp from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = {"/admin"}, method = RequestMethod.GET)
    @ResponseBody
    public String  admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("error");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }

}

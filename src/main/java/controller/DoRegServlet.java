package controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import pojo.User;
import services.interfaces.UserService;
import services.servicesimpl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Servlet for registration logic for new user
 */
@WebServlet(urlPatterns = "/doReg")
public class DoRegServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static Logger userLogger = Logger.getLogger("userLogger");
    private static UserService userService;
    private String email, username, nonHashedPasswordFirstReq, nonHashedPasswordSecondReq, password1HashReq, password2HashReq;
    private LocalDateTime registrationDate;

    public void init() {
        log.info("init(): Initializing 'userService'.");
        userService = UserServiceImpl.getInstance();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        // get all parameters from registration form
        getFormParameters(request);

        //check that fields aren't empty, if so show notification
        log.info("doPost(request, response): Trying to register a user.");
        if (fieldEmpty()) {
            notifyEmptyField(request, response, err);
            return;
        }
        // check both hashes of passwords. If they aren't equal show notification
        encodePasswords();

        if (!password1HashReq.equals(password2HashReq)) {
            notifyPassMismatch(request, response, err);
            return;
        }

        //if hashes are equal, check if user with that email is already exists, if so show notification
        Optional<User> userOptional = userService.get(email);
        if (userOptional.isPresent()) {
            notifyUserExists(request, response, err);
            return;
        }
        //if hashes ok+ user is new, create user and notificate user
        createUser(request, response, err);

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        response.sendError(405);
    }

    private void encodePasswords() {
        password1HashReq = DigestUtils.md5Hex(nonHashedPasswordFirstReq);
        password2HashReq = DigestUtils.md5Hex(nonHashedPasswordSecondReq);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response, ResourceBundle err) throws ServletException, IOException {
        log.info("doGet(request, response): Registration is successful!");
        User user = new User(username, email, password1HashReq, registrationDate);
        userService.add(user);
        userLogger.info("doGet(request, response): --> A new user has been registered:\n +" +
                "username: " + username +
                "email: " + email +
                "password_hash: " + password1HashReq +
                "registration_date: " + registrationDate + "\n"
        );
        request.setAttribute("regSuccess", err.getString("regSuccess"));
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }

    private void notifyUserExists(HttpServletRequest request, HttpServletResponse response, ResourceBundle err) throws ServletException, IOException {
        log.error("doGet(request, response): User already exists!");
        request.setAttribute("userAlreadyExists", err.getString("userAlreadyExists"));
        request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
    }

    private void notifyPassMismatch(HttpServletRequest request, HttpServletResponse response, ResourceBundle err) throws ServletException, IOException {
        log.error("doGet(request, response): Password mismatches!");
        request.setAttribute("passMismatch", err.getString("passMismatch"));
        request.setAttribute("email", email);
        request.setAttribute("username", username);
        request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
    }

    private void notifyEmptyField(HttpServletRequest request, HttpServletResponse response, ResourceBundle err) throws ServletException, IOException {
        log.error("doGet(request, response): Field is empty!");
        request.setAttribute("fieldEmpty", err.getString("fieldEmpty"));
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
    }

    private boolean fieldEmpty() {
        return nonHashedPasswordFirstReq == null ||
                nonHashedPasswordSecondReq == null ||
                username == null ||
                email == null ||
                nonHashedPasswordFirstReq.isEmpty() ||
                nonHashedPasswordSecondReq.isEmpty() ||
                username.isEmpty() ||
                email.isEmpty();
    }

    private void getFormParameters(HttpServletRequest request) {
        email = request.getParameter("email");
        username = request.getParameter("username");
        nonHashedPasswordFirstReq = request.getParameter("password1");
        nonHashedPasswordSecondReq = request.getParameter("password2");
        registrationDate = LocalDateTime.now();
    }
}

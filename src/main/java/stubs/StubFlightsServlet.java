package stubs;

import org.apache.log4j.Logger;
import pojo.Airport;
import utils.ServletLog;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

//Заглушка для страницы поиска
@WebServlet(urlPatterns = {"", "/flights"})
public class StubFlightsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();

        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);




        // Получаем путь до папки для логов
        String pathForLog=getServletContext().getRealPath("/");
        // Устанавливаем динамические значения для log4j.properties
        System.setProperty("pathReg",pathForLog+"reg.log");
        System.setProperty("pathServ",pathForLog+"serv.log");
        System.setProperty("pathDB",pathForLog+"db.log");
        // Инициализируем логгеры
        // Надо разобраться почему он записывает несколько раз в этой части
        // Если просто вызывать в других сервлетах, то все нормально
        Logger logDB = ServletLog.getLgDB();
        Logger logServ = ServletLog.getLgServ();
        Logger logREG = ServletLog.getLgReg();

        // Устанавливаем логгеры для всех сервлетов
        getServletContext().setAttribute("logREG", logREG);
        getServletContext().setAttribute("logServ", logServ);
        getServletContext().setAttribute("logDB", logDB);
//        Для вызова из других сервлетов:
//        Logger log=(Logger)getServletContext().getAttribute("logREG");
//        log.error("Registration failed");
//        Logger log=(Logger)getServletContext().getAttribute("logDB");
//        log.info("DB started");







        httpSession.setAttribute("currentLocale", Locale.getDefault());
        getServletContext().setAttribute("errors", ResourceBundle.
                getBundle("ErrorsBundle", Locale.getDefault()));

        List<Airport> airports = StubUtils.getAirports();
        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);

        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

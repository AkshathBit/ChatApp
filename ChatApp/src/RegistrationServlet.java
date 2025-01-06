
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.CopyOnWriteArrayList;

// Example servlet for user registration
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>(); // Simulated database

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        User newUser = new User(name, email);
        userList.add(newUser);
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("user", newUser);
        }
        
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.getRequestDispatcher("registration_success.jsp").forward(request, response);
    }
}

// User class moved to a separate file
class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

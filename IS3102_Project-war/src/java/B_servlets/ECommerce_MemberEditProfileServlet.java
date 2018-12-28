/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ekyj1
 */
@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ECommerce_MemberEditProfileServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ECommerce_MemberEditProfileServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Client client = ClientBuilder.newClient();
           HttpSession session = request.getSession();
           
           String email = (String)session.getAttribute("memberEmail");
           String name = request.getParameter("name");
           String phone = request.getParameter("phone");
           String city = request.getParameter("country");
           String address = request.getParameter("address");
           String securityAnswer = request.getParameter("securityAnswer");
           int securityQuestion = Integer.parseInt(request.getParameter("securityQuestion"));
           int age = Integer.parseInt(request.getParameter("age"));
           int income = Integer.parseInt(request.getParameter("income"));
           
           WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/MemberWS")
                   .path("updateProfile")
                   .queryParam("email", email)
                   .queryParam("name", name)
                   .queryParam("phone", phone)
                   .queryParam("city",city)
                   .queryParam("address",address)
                   .queryParam("securityAnswer", securityAnswer)
                   .queryParam("securityQuestion", securityQuestion)
                   .queryParam("age",age)
                   .queryParam("income",income);
           
                 Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                 Response res = invocationBuilder.post(null);
        

        if (res.getStatus() == Response.Status.OK.getStatusCode()) {     
            System.out.println("success");
        } else { 
            System.out.println("failed");
        }
    }// end of doPost

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Eugene Koh You Jun
 */
@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ArrayList<ShoppingCartLineItem> shopCart;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           HttpSession session = request.getSession();

            shopCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
            if (shopCart != null && shopCart.size() > 0) {
                double totalPrice = calculateTotalAmount(shopCart);
                Long selectedStore = Long.parseLong("59");
                int storeQty = checkQuantity(selectedStore, shopCart);
                if (storeQty > shopCart.get(0).getQuantity()) {
                    String memberEmail = (String) session.getAttribute("memberEmail");
                   
                    Member member = getMember(memberEmail);
                    Long memberId = member.getId();
                    
                    int transactionRecordId = createECommerceTransactionRecord(totalPrice, memberId,selectedStore);
                    if (transactionRecordId != 0) {
                        for (ShoppingCartLineItem oneItem : shopCart) {
                            int result = createECommerceLineItemRecord(oneItem, transactionRecordId,selectedStore);
                            System.out.println(result);
                            System.out.println(transactionRecordId);
                            if (result == 1) {
                                if (shopCart.get(shopCart.size() - 1).equals(oneItem)) {
                                    session.setAttribute("shoppingCart", null);
                                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=Successfully paid. Your item can be collected from "
                                            + "Queenstown Store Warehouse located at 317 Alexandra Rd, Singapore 159965");
                                }
                            } else {
                                session.setAttribute("shoppingCart", null);
                                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Error in inserting into line item record.");
                            }

                        }
                    } else {
                        
                        session.setAttribute("shoppingCart", null);
                        response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Error in inserting into transaction record.");
                    }
                }
                else
                {
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Sorry, the store you've selected : Queenstown Store Warehouse does not have enough quantiy of the product");
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    
     public int checkQuantity(Long storeId, ArrayList<ShoppingCartLineItem> shoppingCart) {
        ShoppingCartLineItem s = shoppingCart.get(0);
        storeId = Long.parseLong("59");
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity")
                    .path("getQuantity")
                    .queryParam("storeID", storeId)
                    .queryParam("SKU", s.getSKU());
            
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
     
     
    public Member getMember(String email) {
        try {

            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/memberWS").path("getMember")
                    .queryParam("email", email);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();

            if (response.getStatus() != 200) {
                return null;
            }

            return response.readEntity(Member.class);
        } catch (Exception ex) {
            return null;
        }
    }
      public int createECommerceTransactionRecord(double totalPrice, Long memberId,Long storeId) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.commerce")
                    .path("createECommerceTransactionRecord")
                    .queryParam("totalPrice", totalPrice)
                    .queryParam("memberID", memberId)
                    .queryParam("storeId",storeId);

            Invocation.Builder invocationBuilder = target.request();
            Response response = invocationBuilder.put(Entity.entity("", "application/json"));

            if (response.getStatus() != 201) {
                return 0;
            }
            else
            {
                String result = (String) response.readEntity(String.class);
                System.out.println("createECommerceTransactionRecord result : " + result);
                return Integer.parseInt(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int createECommerceLineItemRecord(ShoppingCartLineItem cartItem, int transactionRecordId,Long storeId) {

        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.commerce/")
                    .path("createECommerceLineItemRecord")
                    .queryParam("quantity", cartItem.getQuantity())
                    .queryParam("itemId", cartItem.getId());

            Invocation.Builder invocationBuilder = target.request();
            Response response = invocationBuilder.put(Entity.entity("", "application/json"));
            if (response.getStatus() != 200) {
                return 0;
            }
            else
            {
                return 1;
            }
            

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    
    
    public double calculateTotalAmount(ArrayList<ShoppingCartLineItem> shoppingCart) {
        double totalPrice = 0.0;
        for (ShoppingCartLineItem oneItem : shoppingCart) {
            totalPrice += oneItem.getQuantity() * oneItem.getPrice();
        }
        return totalPrice;
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
        processRequest(request, response);
    }

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



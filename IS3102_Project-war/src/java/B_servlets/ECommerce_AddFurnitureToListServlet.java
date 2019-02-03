/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.swing.JOptionPane;

/**
 *
 * @author lyery
 */
@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

//Done by Ryan Lye (p1638611)
            HttpSession session = request.getSession();
            
            //storeID for queenstown because only can order from one store as stated in the brief
            //and the page doesn't allow any choosing of warehouse anyways
            String storeIDforQueenstown = "59";
            Long storeid = Long.parseLong(storeIDforQueenstown);
            
            
            String SKU = request.getParameter("SKU");
            String id = request.getParameter("id");
            double price = Double.parseDouble(request.getParameter("price"));
            String name = request.getParameter("name");
            String img = request.getParameter("imageURL");
            
            //getting the Singapore ID
            String countryId = "25";
            Long longCountryId = Long.parseLong(countryId);

            //get the item quantity based on the storeID
            int itemQty = getQuantity(storeid, SKU);
            
            ShoppingCartLineItem oneShopCartItem = new ShoppingCartLineItem(id, SKU, name, img, price, 1, longCountryId);
            
            //gets the shoppingcart attribute from the jsp page to do validation
            ArrayList<ShoppingCartLineItem> shopcart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
            
            //doing the validation
            if (itemQty > 0){
             
                
                if (shopcart == null) {
                //creates a new arraylist if there are no items in shopping cart
                shopcart = new ArrayList<ShoppingCartLineItem>();
                shopcart.add(oneShopCartItem);
                }
                 //no need to create new arraylist if shopping cart !=null
                else if (!shopcart.contains(oneShopCartItem)) {
                    
                   
                shopcart.add(oneShopCartItem);
                
                } else {
                if (shopcart.contains(oneShopCartItem)) {
                    
                    for (ShoppingCartLineItem oneLineItem : shopcart) {
                        
                        if (oneLineItem.equals(oneShopCartItem)) {
                            
                            int newQuantity = oneLineItem.getQuantity() + 1;
                            
                            //checking if there is enough quantity in the warehouse
                            if (itemQty > newQuantity) {
                                oneLineItem.setQuantity(newQuantity);
                                
                            } else {
                                response.sendRedirect("/IS3102_Project-war/B/SG/" + 
                                        "shoppingCart.jsp?errMsg=Not enough item found in the warehouse.");
                                return;
                                
                            }
                            break;
                        }
                    }
                }
            }

                        
            session.setAttribute("shoppingCart", shopcart);
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=Added to cart successfully!");
                
                
                
                
            }else{
            
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Not enough item found in the warehouse!');");
            out.println("location='/IS3102_Project-war/B/SG/furnitureProductDetails.jsp?sku="+SKU+"';");
            out.println("</script>");
            
            }
            
            
            
        }
        
}


    //This is the same method from the ECommerce_StockAvailability.java Servlet
        public int getQuantity(Long storeID, String SKU) {
        try {
            System.out.println("getQuantity() SKU: " + SKU);
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity")
                   .path("getQuantity")
                   .queryParam("storeID", storeID)
                   .queryParam("SKU", SKU);
            
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            System.out.println("Result returned from ws: " + result);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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

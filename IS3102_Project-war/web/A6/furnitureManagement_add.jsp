<%@page import="java.util.List"%>
<%@page import="EntityManager.FurnitureEntity"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Add Furniture
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-archive"></i><a href="../FurnitureManagement_FurnitureServlet"> Furniture Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Furniture
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />
                    <!-- /.warning -->
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" method="POST" enctype="multipart/form-data" action="../FurnitureManagement_AddFurnitureServlet">
                                <fieldset>
                                    <div class="form-group">
                                        <label>Name</label>
                                        <input class="form-control" name="name"  type="text" required="true">
                                    </div>
                                    <div class="form-group">
                                        <label>Category</label>
                                        <input class="form-control" required="true" type="text" name="category" >
                                    </div>
                                    <div class="form-group">
                                        <label>Description</label>
                                        <input class="form-control" required="true" type="text" name="description">
                                    </div>
                                    <div class="form-group">
                                        <label>SKU</label>
                                        <input class="form-control" type="text" required="true" name="SKU" pattern="RM\d+">
                                    </div>
                                    <div class="form-group">
                                        <label>Length per item</label>
                                        <input class="form-control" type="number" required="true" min="1" step="1" name="length" pattern="[0-9]" min="1">
                                    </div>
                                    <div class="form-group">
                                        <label>Width per item</label>
                                        <input class="form-control" type="number" required="true" min="1" step="1" name="width" pattern="[0-9]" min="1">
                                    </div>
                                    <div class="form-group">
                                        <label>Height per item</label>
                                        <input class="form-control" type="number" required="true" min="1" step="1" name="height" pattern="[0-9]" min="1">
                                    </div>
                                    <div>
                                        <input type="file" name="javafile">
                                    </div>
                                    <br/>
                                    <div class="form-group">
                                        <input type="submit" value="Add" class="btn btn-lg btn-primary btn-block">
                                    </div>
                                    <input type="hidden" value="A6/furnitureManagement_add.jsp" name="source">
                                </fieldset>
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
    </body>

</html>

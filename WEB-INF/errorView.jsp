<%-- 
    Document   : errorView
    Created on : 31-Mar-2021, 19:52:18
    Author     : bencleary
--%>

<jsp:include page="_header.jsp"></jsp:include>  
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<body>
    <div class="errorPage">
        <div class="errorContent">
            <h3>We are very sorry but there has been an error, please try again later or contact our IT at benjackcleary27@gmail.com <br> <br>
            We aim to get back to you within 24 hours.</h3>

            <h3 class="error" style="color: red;">Please quote this Error id in your email: <br> <br> Error Id: ${id}</h3>
        </div>
    </div>
    <jsp:include page="_footer.jsp"></jsp:include>


</body>
</html>

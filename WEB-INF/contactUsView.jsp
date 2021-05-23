<%-- 
    Document   : contactUsView
    Created on : 23-Apr-2021, 19:52:17
    Author     : bencleary
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="_checkoutHeader.jsp"></jsp:include>  

        <div class="orderConfirmationHeader">
            <h1>Contact Us</h1>
        </div>
        <div class="contactUsForm">
            <form id="form" method="post" action="ContactUs">
                <div class="form-inline">
                    <p id="returnmessage"></p>
                    <p class="first-name-error error"></p>
                    <label for="Firstname">Name</label>	
                    <input type="text" class="form-control" 
                           id="Firstname" name="first_name" placeholder="First Name">	
                    <input type="text" class="form-control" 
                           id="Lastname" name="last_name" placeholder="Last Name">
                    <p class="last-name-error error"></p>
                </div>			
                <div class="form-group">
                    <label for="Email">E-mail</label>
                    <p class="email-error error"></p>
                    <input type="email" class="form-control" 
                           id="Email" name="email" placeholder="Email Address">
                </div>
                <div class="form-group">
                    <label for="Subject">Subject</label>
                    <p class="subject-error error"></p>
                    <input type="text" class="form-control" id="Subject" name="subject" placeholder="Let us know what it's about">
                </div>	
                <div class="form-group">
                    <label for="Message">Message
                    </label> 
                    <p class="message-error error"></p>
                    <textarea  class="form-control" id="Message" name="message" rows="6" placeholder="Have a problem or would like to give us some feedback?"></textarea>
                </div>	
                <input type="submit"  class="btn btn-default" id="submit" name="submit" value="Submit">	
            </form>           
        </div>
        <jsp:include page="_footer.jsp"></jsp:include>  
       <script src="JS/contactus.js"></script>
    </body>
</html>

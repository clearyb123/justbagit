/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var main = function () {
    $('#form').submit(function (event) {
        event.preventDefault();
        var firstName = $('#Firstname').val();
        var lastName = $('#Lastname').val();
        var email = $('#Email').val();
        var subject = $('#Subject').val();
        var message = $('#Message').val();


        // Validation of form field.
        if (firstName == '' || lastName == '' || subject == '' || email == '' || message == '') {
            event.preventDefault();

            if (firstName === '' && lastName === '') {
                $('.first-name-error').text('*Please enter your first and last name');
            } else if (lastName !== '' && firstName === '') {
                $('.first-name-error').text('*Please enter your first name');
            } else if (firstName !== '' && lastName === '') {
                $('.first-name-error').text('*Please enter your last name');

            }

            if (email === '') {
                $('.email-error').text('*Please enter your email address');
            }

            if (subject === '') {
                $('.subject-error').text('*Please enter a subject line');
            }
            if (message === '') {
                $('.message-error').text('*Please enter a message');
            }
        } else {
            var formValues= $(this).serialize();
 
        $.post("ContactUs", formValues, function(data){
            // Display the returned data in browser
            $("#form").replaceWith("<p style=\"color:green; text-align:center;\">Your message has been received. We aim to get back to you within 1 working day.</p>");
        });
        }
    });
};

$("document").ready(main);
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var main = function () {
    let orderData;
    let leadtimeData;
    window.addEventListener('DOMContentLoaded', function () {
        getOrderConfirmationJSON();
    });

    function getOrderConfirmationJSON() {
        const queryString = window.location.search;
        $.ajax({
            url: "orderConfirmationData" + queryString,
            dataType: 'json',
            error: function (xhr, ajaxOptions, thrownError) {
                console.log(xhr.status);
                console.log(thrownError);
            },
            success: function (data) {
                if (data.redirect === 'Y') {
                    window.location.assign("error?errorId=" + data.id);
                } else {
                    orderData = JSON.parse(data[0]);
                    leadtimeData = data[1];
                    setData(orderData, leadtimeData);
                }
            }
        });
    }


    function setData(data, leadtimeData) {
        let total = data.total.toFixed(2);
        let subtotal = data.subtotal.toFixed(2);
        let discount = subtotal - (total - data.deliveryPrice);
        discount = parseFloat(discount).toFixed(2);
        let promocodeDiscount = 1;
        let promocodeDiscountNumber = 0;
        if (data.promocode !== undefined) {
            promocodeDiscount = data.promocode.promoValue;
            promocodeDiscountNumber = (1 - promocodeDiscount) * subtotal;
            promocodeDiscountNumber = parseFloat(promocodeDiscountNumber).toFixed(2);
        }
        let itemsTotal = 0;
        let quantityDiscountNumber = discount - promocodeDiscountNumber;
        quantityDiscountNumber = parseFloat(quantityDiscountNumber).toFixed(2);
//        let divItem = '<div>'
//                + '<h5>Discount</h5>'
//                + '</div>'
//                + '<div>'
//                + '<span id="discountContent">' + "- £" + discount + '</span>'
//                + '</div>';
        let quantityDiscountDivItem = '<div>'
                + '<h5 class="inlineblock">Quantity Discount</h5>'
                + '</div>'
                + '<div>'
                + '<span id="quantityDiscountNumber" class="inlineblock">' + '- £' + quantityDiscountNumber + '</span>'
                + '</div>';

        let promocodeDiscountDivItem = '<div>'
                + '<h5 class="inlineblock">Promocode</h5>'
                + '</div>'
                + '<div>'
                + '<span id="promocodepromocodeDiscountNumber" class="inlineblock">' + '- £' + promocodeDiscountNumber + '</span>'
                + '</div>';
        let products = data.products;
        document.getElementsByClassName("name")[0].innerText = data.firstName + ", ";
        document.getElementsByClassName("name")[1].innerText = data.firstName + " " + data.lastName + ",";
        document.getElementById("orderNumber").innerText = " " + data.POId;
        document.getElementById("orderDate").innerText = " " + data.orderSent.date.day + "/" + data.orderSent.date.month + "/" + data.orderSent.date.year + " " + data.orderSent.time.hour + ":" + data.orderSent.time.minute;
        document.getElementById("estimatedDeliveryDate").innerText = " " + data.dueDate.day + "/" + data.dueDate.month + "/" + data.dueDate.year;
        document.getElementById("firstLineAddress").innerText = data.shipToStreet1 + ",";
        document.getElementById("secondLineAddress").innerText = data.shipToStreet2 + ",";
        document.getElementById("county").innerText = data.county + ",";
        document.getElementById("country").innerText = data.country + ",";
        document.getElementById("postcode").innerText = data.shipToPostcode;
        document.getElementById("telephone").innerText = data.telephone;
        document.getElementById("notes").innerText = data.notes;
        document.getElementById("subtotalAmount").innerText = '£' + subtotal;
        if ((discount > 0 || promocodeDiscountNumber > 0)) {
           // $(".discountCheckout").removeClass("notVisible");
            $(".quantityDiscountCheckout").removeClass("notVisible");
            $(".promocodeCheckout").removeClass("notVisible");
            //$(".discountCheckout").append(divItem);
            $(".quantityDiscountCheckout").append(quantityDiscountDivItem);
            $(".promocodeCheckout").append(promocodeDiscountDivItem);

        }
        document.getElementById("totalAmount").innerText = '£' + total;
        document.getElementById("deliveryMethodInformation").innerText = leadtimeData.description;
        document.getElementById("orderConfirmationDeliveryPrice").innerText = '£' + data.deliveryPrice.toFixed(2);
        products.forEach(item => {

            item.JSON = JSON.parse(item.JSON);
            let divItem = '<div class="confirmation-cart-item">'
                    + '<div class="confirmation-cart-item-image">'
                    + '<img class="image" src="Images/' + item.JSON.image + '">'
                    + '</div>'
                    + '<div class="confirmation-cart-item-title">'
                    + '<h4 style="color: grey;">' + item.size + '</h4>'
                    + '<h5 class="confirmation-cart-item-price"><strong>&pound;' + item.price + '</strong></h5>'
                    + '<h5 class="cart-item-per"><strong>Per: ' + item.per + '</strong></h5>'
                    + '<h5 class="confirmation-qty">Qty: <strong>' + item.qty + '</strong></h5>'
                    //+ '<span class="discountApplied" id="discountApplied' + item.id + '" data-id="' + item.id + '"></span>'
                    + '</div>'
                    + '</div>';
            itemsTotal += item.qty;
            $('.orderConfirmationBasketContent').append(divItem);
        });

        let divItem2 = '<h4 class="confirmationItemNumber"><strong>' + itemsTotal + ' ITEMS</strong></h4>';
        $('.orderConfirmationBasketTitle').append(divItem2);
    }

};

$("document").ready(main);

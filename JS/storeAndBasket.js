/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var main = function () {
    let cart = [];
    let lowerDiscount = {};
    let upperDiscount = {};
    let cartCheck = [];
    let discounts = [];
    //   let modal = document.getElementById("myModal");




// When the user clicks anywhere outside of the modal, close it

    window.addEventListener('DOMContentLoaded', function () {

        const products = new Products();
        setTimeout(function () {
            if (Storage.getCookie("cookieConsent") !== 'Y') {
                $("#cookieConsent").fadeIn(200);
            }
        }, 4000);
        $("#closeCookieConsent, .cookieConsentOK").click(function () {
            document.cookie = "cookieConsent=Y; expires=Thu, 18 Dec 2050 12:00:00 UTC; path=/";
            $("#cookieConsent").fadeOut(200);
        });
        products.getJSON().then(data => {
            products.displayProducts(data[0]);
            Storage.saveProducts(data[0]);
            discounts = data[1];
            setUpDiscounts(discounts);
            var bar = new Promise((resolve, reject) => {
                data[2].forEach(product => {
                    cartCheck.push(JSON.parse(product.JSON));
                });
                resolve();
            });
            bar.then(setupAPP(products, cartCheck));
        });
        if (window.location.pathname === "/" || window.location.pathname === "/Polybags/") {
            products.getLatestProducts().then(data => {
                products.displayLatestReleases(data);
            });
        }
        if (window.location.pathname === "/product" || window.location.pathname === "/Polybags/product") {
            const product = new Product();
            product.getProduct().then(product =>
                ProductPage.setUpProductPage(product)
            );
        }
    });
    $('body').on('click', '.fa-chevron-up', function () {
        let id = $(this).attr('data-id');
        let qty = increaseQuantity(id);
        this.nextElementSibling.innerText = qty;
    });
    $('body').on('click', '.fa-chevron-down', function () {
        let id = $(this).attr('data-id');
        let currentElement = this;
        let qty = decreaseQuantity(id, currentElement);
        this.previousElementSibling.innerText = qty;
    });
    $('body').on('click', '.remove', function () {
        let id = $(this).attr('data-id');
        removeCartItem(id);
        document.getElementById('cartContent').removeChild(this.parentElement.parentElement);
    });
    $('body').on("click", ".continueShopping", function () {
        closeCart();
    });
    $('body').on("click", ".clear-cart", function () {
        clearCart();
    });
    $('body').on("click", ".addToBasket", function () {
        let currentElement = $(this).attr("id");
        let id = $(this).attr("data-id");
        let LNo = (cart.length + 1) * 10;
        let qty = parseInt($("#qty_product_page").val());
        if (!$("#" + currentElement).hasClass("selected")) {
            let cartItem = {...Storage.getProduct(id), qty: qty, LNo: LNo};
            cart = [...cart, cartItem];
            Storage.saveCart(cart);
            Storage.sendItem(Storage.getProductFromCart(id));
            addToCart(cartItem);
            showCart(cart);
        }
    });
    $('body').on("click", "#myBasketNav", function () {
        showCart(cart);
    });
    $('body').on("click", ".checkout-cart", function () {
        submitCart();
    });
    $('body').on("click", ".secondary_product_images", function () {
        let imgSrc = $(this).attr("src");
        $("#main_image").attr("src", imgSrc);
        document.getElementById("magnifier").style.backgroundImage = "url('" + imgSrc + "')";
    });
    $('body').on("change", "#per", function () {
        let queryString = window.location.search;
        if (queryString != "") {
            queryString = queryString.replace("?id=", "");
        }
        perDataChange(queryString);
    });
    $('.submitNewsletter').click(function () {
        const newsletter = new Newsletter();
        let emailaddress = $('#newsletterInput').val();
        newsletter.sendNewsletterEmailAddress(emailaddress);
    });
    /* Individual product page */

    class Products {
        async getJSON() {
            var myHeaders = new Headers();
            myHeaders.append('pragma', 'no-cache');
            myHeaders.append('cache-control', 'no-cache');
            var myInit = {
                method: 'GET',
                headers: myHeaders,
            };
            try {
                let result = await fetch("JSON", myInit);
                let data = await result.json();
                return data;
            } catch (error) {
                console.log(error);
            }
        }

        displayProducts(products) {
            let queryString = window.location.search;
            if (queryString != "") {
                queryString = queryString.replace("?id=", "");
                products = products.filter(item => item.id != queryString && item.enableDisplay == 'Y')
                products = products.slice(0, 4);
            }
            ;
            let result = '';
            products = products.filter(product => {
                return product.enableDisplay == 'Y';
            });
            products.forEach(product => {
                result += '<div class="product">'
                        + '<a class="get_product_link" href="product?id=' + product.id + '"></a>'
                        + '<div class="productImage">'
                        + '<img class="image" src="Images/' + product.image + '">'
                        + '</div>'
                        + '<div class="size">'
                        + '<p><strong>' + product.size + '</strong></p>'
                        + '<p style="color:#999999">' + product.description + '</p>'
                        + '</div>'
                        + '<div class="middle">'
                        + '<div class="price">'
                        + '<p>Starting from <b>&pound;' + parseFloat(product.price).toFixed(2) + '</b></p>'
                        + '</div>'
                        + '</div>'
                        + '</div>'
                        + '</div>';
            });
            $(".products .containerS").append(result);
        }

        displayNumberOfProducts() {
            let products = JSON.parse(localStorage.getItem("products"));
            products = products.filter(product => {
                return product.enableDisplay == 'Y';
            });
            $('#number').text(products.length);
        }

        async getLatestProducts() {
            try {
                let result = await fetch("latestReleases");
                let data = await result.json();
                let latestProducts = data;
                return latestProducts;
            } catch (error) {
                console.log(error);
            }
        }

        displayLatestReleases(latestProducts) {
            let result = '';
            latestProducts.forEach(product => {
                result += '<div class="product">'
                        + '<a class="get_product_link" href="product?id=' + product.id + '"></a>'
                        + '<div class="productImage">'
                        + '<img class="image" src="Images/' + product.image + '">'
                        + '</div>'
                        + '<div class="size">'
                        + '<p><strong>' + product.size + '</strong></p>'
                        + '</div>'
                        + '</div>'
                        + '</div>';
            });
            $(".latestproducts").append(result);
        }
        ;
    }


    function showCart() {
        $("#Basket").addClass("showCart");
        $("#transparentBasket").addClass("transparentBcg");
        if (cart.length > 0) {
            $(".checkout-cart").removeClass("notVisible");
        } else {
            $(".checkout-cart").addClass("notVisible");
        }
        setCartValues(cart);
    }

    function increaseQuantity(id) {
        let tempItem = cart.find(item => item.id == id);
        tempItem.qty = tempItem.qty + 1;
        Storage.saveCart(cart);
        setCartValues(cart);
        updateQuantity(tempItem);
        return tempItem.qty;
    }

    function decreaseQuantity(id, currentElement) {
        let tempItem = cart.find(item => item.id == id);
        tempItem.qty = tempItem.qty - 1;
        if (tempItem.qty > 0) {
            Storage.saveCart(cart);
            setCartValues(cart);
            updateQuantity(tempItem);
            return tempItem.qty;
        } else {
            document.getElementById('cartContent').removeChild(currentElement.parentElement.parentElement);
            removeCartItem(tempItem.id);
        }

    }



    function removeCartItem(id) {
        $.ajax({
            method: "POST",
            url: "removeFromCart",
            data: {
                product: JSON.stringify(Storage.getProductFromCart(id))
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log(xhr.status);
                console.log(thrownError);
            },
            success: function () {
            }
        });
        cart = cart.filter(item => item.id != id);
        Storage.saveCart(cart);
        setCartValues(cart);
        revertCartButton(id);
    }

    function clearCart() {
        let cartItems = cart.map(item => item.id);
        cartItems.forEach(id => removeCartItem(id));
        while (document.getElementById('cartContent').children.length > 0) {
            document.getElementById('cartContent').removeChild(document.getElementById('cartContent').children[0]);
        }
        closeCart();
    }

    function closeCart() {
        $("#Basket").removeClass("showCart");
        $("#Basket").removeClass("showCart");
        $("#transparentBasket").removeClass("transparentBcg");
    }

    function amendCartButton(currentElement) {
        $("#" + currentElement).children().eq(1).addClass("visible");
        $("#" + currentElement).children().eq(0).addClass("notVisible");
        $("#" + currentElement).addClass("selected");
    }
    function revertCartButton(currentElement) {
        $("#" + currentElement).children().eq(1).removeClass("visible");
        $("#" + currentElement).children().eq(0).removeClass("notVisible");
        $("#" + currentElement).removeClass("selected");
    }


    function addToCart(item) {
        let divItem = '<div class="cart-item">'
                + '<div class="cart-item-image">'
                + '<img class="image" src="Images/' + item.image + '">'
                + '</div>'
                + '<div class="cart-item-title">'
                + '<h4>' + item.size + '</h4>'
                + '<h5 class="cart-item-per"><strong>Per: ' + item.per + '</strong></h5>'
                + '<h5 class="cart-item-price"><strong>&pound;' + parseFloat(item.price).toFixed(2) + '</strong></h5>'
                + '<span class="remove" data-id="' + item.id + '">remove</span>'
                // + '<span class="discountApplied" id="discountApplied' + item.id + '" data-id="' + item.id + '"></span>'
                + '</div>'
                + '<div class="qty">'
                + '<i class="fa fa-chevron-up fa-lg" data-id="' + item.id + '"></i>'
                + '<p class="item-amount">'
                + item.qty
                + '</p>'
                + '<i class="fa fa-chevron-down fa-lg" data-id="' + item.id + '"></i>'
                + ' </div>'
                + '</div>';
        $('.cartContent').append(divItem);
    }

    function applyDiscountCart(item) {
//     if (typeof discount !== "undefined") {
//     document.getElementById('discountApplied' + item.id).innerText = discount.discount + '% discount applied';
//     return discount.discount;
//     } else {
//     document.getElementById('discountApplied' + item.id).innerText = '';
//     return 0;
//     }
    }

    function setUpDiscounts(discounts) {
        if (discounts.lowerDiscount.enabled === 'Y') {
            lowerDiscount = discounts.lowerDiscount;
        }
        if (!jQuery.isEmptyObject(discounts.upperDiscount) && discounts.upperDiscount.enabled === 'Y') {
            upperDiscount = discounts.upperDiscount;
        }
    }

    function calculateDiscount(subtotal, itemsTotal) {
        if (!jQuery.isEmptyObject(upperDiscount)) {
            if (itemsTotal >= lowerDiscount.per && itemsTotal < upperDiscount.per) {
                document.getElementById('discountApplied').innerText = lowerDiscount.discount + '% discount applied';
                return subtotal * ((100 - lowerDiscount.discount) / 100);
            }
            if (itemsTotal >= upperDiscount.per) {
                document.getElementById('discountApplied').innerText = upperDiscount.discount + '% discount applied';
                return subtotal * ((100 - upperDiscount.discount) / 100);
            }
        } else {
            if (itemsTotal >= lowerDiscount.per) {
                document.getElementById('discountApplied').innerText = lowerDiscount.discount + '% discount applied';
                return subtotal * ((100 - lowerDiscount.discount) / 100);
            }
        }
        if (itemsTotal < lowerDiscount.per) {
            document.getElementById('discountApplied').innerText = '';
            return subtotal;
        }
    }

    function setCartValues(cart) {
        let subtotal = 0;
        let itemsTotal = 0;
        //let discountNum = 0;
        cart.map(item => {
            //discountNum = applyDiscountCart(item);
            subtotal += item.qty * item.price;
            // temptotal += (item.qty * item.price) - ((item.qty * item.price) * (discountNum / 100));
            //temptotal += (item.qty * item.price);
            itemsTotal += item.qty;
            amendCartButton(item.id);
        });
        let temptotal = calculateDiscount(subtotal, itemsTotal);
        temptotal = parseFloat(temptotal).toFixed(2);
        subtotal = parseFloat(subtotal).toFixed(2);
        document.getElementById('totalAmount').innerText = temptotal;
        document.getElementById('subTotalAmount').innerText = subtotal;
        document.getElementById('cartItems').innerText = itemsTotal;
    }

    function setupAPP(products, cartCheck) {
        cart = Storage.getCart();
        if (cart[0] != cartCheck[0]) {
            cart = cartCheck;
            Storage.saveCart(cart);
        }
        if (cart.length == 0) {
            populateCart(cart);
            setCartValues(cart);
        }
        if (!cart.length == 0) {
            console.log(Storage.getCookie("cart"));
            console.log(cart[0].sessionId);
            if (Storage.getCookie("cart") == cart[0].sessionId) {
                populateCart(cart);
                setCartValues(cart);
            } else {
                cart = [];
                Storage.saveCart(cart);
                Storage.clearLocalCart();
                document.getElementById('cartItems').innerText = 0;
            }
        }
        products.displayNumberOfProducts();
    }



    function populateCart(cart) {
        cart.forEach(item => addToCart(item));
    }

    function submitCart() {
        window.location.assign("checkOut");
    }

    function updateQuantity(item) {
        $.ajax({
            method: "POST",
            url: "updateQuantity",
            data: {
                product: JSON.stringify(item)
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log(xhr.status);
                console.log(thrownError);
            },
            success: function () {
            }
        });
    }

    function perDataChange(id) {
        let product = Storage.getProduct(id);
        let perValue = $("#per").val();
        product = Storage.getProductUsingPer(perValue, product.size);
        ProductPage.displayPerChange(product);
        setCartValues(cart);
    }

    class Storage {
        static saveProducts(products) {
            localStorage.setItem("products", JSON.stringify(products));
        }

        static sendItem(item) {
            $.ajax({
                method: "POST",
                url: "addToCart",
                data: {
                    product: JSON.stringify(item)
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log(xhr.status);
                    console.log(thrownError);
                },
                success: function (data) {
                    if (data.redirect === 'Y') {
                        window.location.assign("error?errorId=" + data.id);
                    } else {
                        console.log("Item sent to the server");
                    }
                }
            });
        }

        /*static getDiscount(id, qty) {
         //let discounts = JSON.parse(localStorage.getItem("discounts"));
         let discountSelect = discounts.filter(discount => discount.productId == id && qty >= discount.per);
         if (discountSelect.length >= 2) {
         if (qty >= discountSelect[0].per && qty >= discountSelect[1].per) {
         return discountSelect[1];
         } else {
         return discountSelect[0];
         }
         } else {
         return discountSelect[0];
         }
         }*/

        static getProduct(id) {
            let products = JSON.parse(localStorage.getItem("products"));
            return products.find(product => product.id == id);
        }

        static getProductUsingPer(per, size) {
            let products = JSON.parse(localStorage.getItem("products"));
            return products.find(product => product.per == per && product.size == size);
        }

        static getProducts() {
            return JSON.parse(localStorage.getItem("products"));
        }

        static getProductFromCart(id) {
            let products = JSON.parse(localStorage.getItem("cart"));
            return products.find(product => product.id == id);
        }
        /*static saveDiscounts(discountsData) {
         localStorage.setItem("discounts", JSON.stringify(discountsData));
         
         }*/
        static saveCart(cart) {
            localStorage.setItem("cart", JSON.stringify(cart));
        }

        static getCart() {
            return localStorage.getItem("cart") ? JSON.parse(localStorage.getItem("cart")) : [];
        }

        static getCookie(cname) {
            var name = cname + "=";
            var decodedCookie = decodeURIComponent(document.cookie);
            var ca = decodedCookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        }

        static clearLocalCart() {
            localStorage.removeItem("cart");
        }
    }


    class Product {
        async getProduct() {
            let id = window.location.search;
            id = id.replace("?id=", "");
            let productIdCheck = async () => {
                let products = Storage.getProducts();
                let product = products.find(product => product.id == id);
                product = products.filter(item => item.size == product.size && item.enableDisplay != 'N');
                return product[0].id;
            };
            try {
                let productId = await productIdCheck();
                let result = await fetch("productData?id=" + productId);
                let data = await result.json();
                let product = data;

                return product;
                
                
            } catch (error) {
                console.log(error);
            }
            
        }

        calculateProductsPer(id) {
            let products = Storage.getProducts();
            let tempItem = products.find(item => item.id == id);
            let itemDescription = tempItem.size;
            products = products.filter(function (item) {
                return item.size == itemDescription;
            });
            return products;
        }
    }

    class ProductPage {
        static ProductPage() {
        }

        static setUpProductPage(product) {
            this.displayProductDetails(product);
            setCartValues(cart);
        }

        static displayProductDetails(product) {
            let addToBasket = '<div class="addToBasket" id="' + product.id + '" data-id="' + product.id + '">'
                    + '<p  class="add"><b>ADD TO CART</b></p>'
                    + '<p  class="inBasket"><b>IN CART</b></p>'
                    + '</div>';
            let qtyInput = '<p><b>Quantity:</b></p>'
                    + '<input name="quantity" id="qty_product_page" min="1" type="number" value="1" data-id=' + product.id + '>'
                    + '<p style="margin-top:10px">Get <span style="color:red;">5%</span> off when ordering quantities of 3 or more.</p>'
                    + '<p>Get <span style="color:red">10%</span> off when ordering quantities of 5 or more.</p>'
            let dropdown = this.initiateDropdown(product);
            if (dropdown !== undefined)
                $(".per_Dropdown").append(dropdown);
            $("#product_Price_Span").text(parseFloat(product.price).toFixed(2));
            $("#product_description").text(product.description);
            $("#product_Title_Span").text(product.size);
            /*help*/


            $("#main_image").attr("src", "Images/" + product.image).promise().then(function () {
                if (!window.matchMedia("(max-width: 500px)").matches) {
                    magnify("main_image", 2);
                    $('.img-magnifier-glass').hide();
                    $('.img-magnifier-container').mouseover(function () {
                        $('.img-magnifier-glass').show();
                    });
                    $('.img-magnifier-container').mouseout(function () {
                        $('.img-magnifier-glass').hide();
                    });
                }

            });
            $(".addToBasketContainer").append(addToBasket);
            $(".Quantity").append(qtyInput);
            //Provide secondary images data when ready
            $("#secondary_image_first").attr("src", "Images/" + product.image);
            $("#secondary_image_second").attr("src", "Images/" + product.image2);
            $("#secondary_image_third").attr("src", "Images/" + product.image3);
            $("#secondary_image_fourth").attr("src", "Images/" + product.image4);
        }

        static displayPerChange(product) {
            let addToBasket = '<div class="addToBasket" id="' + product.id + '" data-id="' + product.id + '">'
                    + '<p  class="add"><b>ADD TO CART</b></p>'
                    + '<p  class="inBasket"><b>IN CART</b></p>'
                    + '</div>';
            document.getElementById("product_Price_Span").innerText = parseFloat(product.price).toFixed(2);
            $(".addToBasket").replaceWith(addToBasket);
        }

        static initiateDropdown(product) {
            const productObj = new Product();
            let perDropdown;
            let productsPer = productObj.calculateProductsPer(product.id);
            if (productsPer !== undefined) {
                productsPer = productsPer.sort(function (a, b) {
                    return parseFloat(a.per) - parseFloat(b.per);
                });
                perDropdown = '<label for="per">Per:</label>'
                        + '<select name="per" id="per">';
                productsPer.forEach(product => {
                    let option = '<option value="' + product.per + '">' + product.per + '</option>';
                    perDropdown = perDropdown + option;
                });
                perDropdown = perDropdown + '</select><p>Tax included.<span style="color:green"> Free </span> shipping on orders with batches of 1000 or more.</p>';
                return perDropdown;
            } else {
                return undefined;
            }
        }

    }

    class Newsletter {
        async sendNewsletterEmailAddress(emailaddress) {
            fetch('newsletter', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    emailaddress: emailaddress
                })
            })
                    .catch(err => {
                        this.displayError();
                    }).then(response => {
                if (response.ok)
                    this.displaySuccessfullySent();
            });
        }

        displaySuccessfullySent() {
            $(".formNewsletter").replaceWith('<p style="color:green">Email added to mailing list!</p>');
        }

        displayError(err) {
            alert('There has been an error: ' + err);
        }
    }

    function magnify(imgID, zoom) {
        var img, glass, w, h, bw;
        img = document.getElementById(imgID);
        /*create magnifier glass:*/
        glass = document.createElement("DIV");
        glass.setAttribute("class", "img-magnifier-glass");
        glass.setAttribute("id", "magnifier");
        /*insert magnifier glass:*/
        img.parentElement.insertBefore(glass, img);
        /*set background properties for the magnifier glass:*/
        glass.style.backgroundImage = "url('" + $('#' + imgID).attr("src") + "')";
        glass.style.backgroundRepeat = "no-repeat";
        glass.style.backgroundSize = (img.width * zoom) + "px " + (img.height * zoom) + "px";
        bw = 3;
        w = glass.offsetWidth / 2;
        h = glass.offsetHeight / 2;
        /*execute a function when someone moves the magnifier glass over the image:*/
        glass.addEventListener("mousemove", moveMagnifier);
        img.addEventListener("mousemove", moveMagnifier);
        /*and also for touch screens:*/
        glass.addEventListener("touchmove", moveMagnifier);
        img.addEventListener("touchmove", moveMagnifier);
        function moveMagnifier(e) {
            var pos, x, y;
            /*prevent any other actions that may occur when moving over the image*/
            e.preventDefault();
            /*get the cursor's x and y positions:*/
            pos = getCursorPos(e);
            x = pos.x;
            y = pos.y;
            /*prevent the magnifier glass from being positioned outside the image:*/
            if (x > img.width - (w / zoom)) {

                x = img.width - (w / zoom);
            }
            if (x < w / zoom) {

                x = w / zoom;
            }
            if (y > img.height - (h / zoom)) {

                y = img.height - (h / zoom);
            }
            if (y < h / zoom) {

                y = h / zoom;
            }
            /*set the position of the magnifier glass:*/
            glass.style.left = (x - w) + "px";
            glass.style.top = (y - h) + "px";
            /*display what the magnifier glass "sees":*/
            glass.style.backgroundPosition = "-" + ((x * zoom) - w + bw) + "px -" + ((y * zoom) - h + bw) + "px";
        }
        function getCursorPos(e) {
            var a, x = 0, y = 0;
            e = e || window.event;
            /*get the x and y positions of the image:*/
            a = img.getBoundingClientRect();
            /*calculate the cursor's x and y coordinates, relative to the image:*/
            x = e.pageX - a.left;
            y = e.pageY - a.top;
            /*consider any page scrolling:*/
            x = x - window.pageXOffset;
            y = y - window.pageYOffset;
            return {x: x, y: y};
        }
    }





};
$(document).ready(main);



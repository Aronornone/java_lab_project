$(document).ready(function () {
    $('.menu li').hover(
        function () {
            $('ul', this).slideDown("fast");
        },
        function () {
            $('ul', this).slideUp("fast");
        }
    );
    var ticketOld = $('#ticketBucket').text();
    if (ticketOld == undefined || ticketOld === '') {
        document.getElementById("ticketBucket").textContent = 0;

    }
});
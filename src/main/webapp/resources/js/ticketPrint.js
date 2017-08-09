$(document).ready(function () {
    $('.ticketInfo').hide();
    $(".invoiceToggle").click(function () {
        $('.ticketInfo').hide();
        $(this).nextAll('.ticketInfo').toggle("fast");
    });
});

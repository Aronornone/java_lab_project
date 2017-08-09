$(document).ready(function () {
    $(".flightInfo").click(function () {
        $(this).nextAll('.passenger').toggle("slow");
    });
});
function calc(ticketId) {
    var luggageEl = document.getElementById('lug' + ticketId);
    var oldPrice = document.getElementById('price' + ticketId).textContent;
    var totalSum = document.getElementById('totalSum').textContent;
    if (luggageEl.checked) {
        $.get("priceRecountChecked", {ticketId: ticketId},
            function (price) {
                $('#price' + ticketId).text(price);
                totalSum = parseFloat(totalSum) - parseFloat(oldPrice) + parseFloat(price);
                document.getElementById('totalSum').textContent = totalSum;
            });
    } else {
        $.get("priceRecountUnChecked", {ticketId: ticketId},
            function (price) {
                $('#price' + ticketId).text(price);
                totalSum = parseFloat(totalSum) - parseFloat(oldPrice) + parseFloat(price);
                document.getElementById('totalSum').textContent = totalSum;
            });
    }
}

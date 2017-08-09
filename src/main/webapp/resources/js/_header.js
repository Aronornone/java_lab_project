$(document).ready(function () {
    $('.menu li').hover(
        function () {
            $('ul', this).slideDown("fast");
        },
        function () {
            $('ul', this).slideUp("fast");
        }
    );
});
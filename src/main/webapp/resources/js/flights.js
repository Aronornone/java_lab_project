$(document).ready(function () {

    var buy;
    if (document.getElementById("buybut").value === 'en_US') {
        buy = "Buy";
    } else {
        buy = "Приобрести";
    }
    var counter = 0;
    var numPages = document.getElementById("numPages").value;
    var ifPageFirst = document.getElementById("ifPageFirst").value;

    $("#appendButton").click(function () {
        createAppend(counter);
        counter++;
        $('table', this).slideDown("slow");
    });

    function createAppend(i) {
        if ((i + 1) >= numPages) {
            document.getElementById("appendButton").remove();
        }
        $.ajax({
                url: document.URL,
                data: {page: i},
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    appendData(data);
                }
            }
        );
    }

    function appendData(data) {
        $.each(data, function (k, v) {
            var toappend = '<tbody><form name="form2" id="form2' + v.flightId +
                '" class="addTickets" action="">'
                + '<tr>'
                + '<td>' + v.departureAirport.code + ' ' + v.departureAirport.city + '</td>'
                + '<td>' + v.arrivalAirport.code + ' ' + v.arrivalAirport.city + '</td>'
                + '<td>' + parseDate(v) + '</td>'
                + '<td>' + v.flightNumber + '</td>'
                + '<td>' + v.baseCost + '.0' + '</td>'
                + '<td><input id="num' + v.flightId + '" class="fieldFilters" type="number" min="1"max=' + document.getElementById("numberTicketsFilter").value + ' step="1"value=' + document.getElementById("numberTicketsFilter").value + ' form="form2' + v.flightId + '"name="numberTicketsFlight"><input type="hidden" name="flightId" form="form2' + v.flightId + '"value="' + v.flightId + '"></td>'
                + '<td><button class="buttonBucket" id="' + v.flightId + '" onclick="buy(this.id)" >'
                + buy + '</button></td>'
                + '</tr> </form></tbody>';
            $('#appendFlights').append(toappend);
        });
    }

    function parseDate(v) {
        var year = v.dateTime.date.year;
        var month = v.dateTime.date.month;
        if (month < 10) {
            month = '0' + month;
        }
        var day = v.dateTime.date.day;
        if (day < 10) {
            day = '0' + day;
        }

        var hour = v.dateTime.time.hour;
        if (hour < 10) {
            hour = '0' + hour;
        }
        var minute = v.dateTime.time.minute;
        if (minute < 10) {
            minute = '0' + minute;
        }
        var cdatetime = year + '-' + month + '-' + day + 'T' + hour + ':' + minute;
        return cdatetime;
    }


    if (document.readyState == 'complete' && ifPageFirst == 'true') {
        document.getElementById("appendButton").click();
    }
    if (document.readyState == 'complete') {
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();
        if (dd < 10) {
            dd = '0' + dd
        }
        if (mm < 10) {
            mm = '0' + mm
        }
        today = yyyy + '-' + mm + '-' + dd;
        document.getElementById("dateFrom").setAttribute("min", today);
        if (document.getElementById("dateFrom").value == '') {
            document.getElementById("dateFrom").value = today;
        }
        if (document.getElementById("dateTo").value == '') {
            document.getElementById("dateTo").value = today;
        }
    }
});

function buy(flightId) {
    var numberTicketsFlight = document.getElementById('num' + flightId).value;
    var box = document.getElementById('box').value;
    $.ajax({
        url: 'addFlightToInvoice',
        type: 'POST',
        data: {
            flightId: flightId,
            numberTicketsFlight: numberTicketsFlight,
            box: box
        },
        success: function (data) {
            var ticketOld = $('#ticketBucket').text();
            var ticketOldInt;
            if (ticketOld == undefined || ticketOld === '') {
                ticketOldInt = 0;
            } else {
                ticketOldInt = parseInt(ticketOld);
            }
            var numberTicketInt = parseInt(numberTicketsFlight);
            var ticketNew = parseInt(ticketOldInt + numberTicketInt);
            document.getElementById("ticketBucket").textContent = ticketNew;
            loadPopupBox();
        },
        error: function (data) {
            loadPopupBoxLogin();
        }
    });

    $('#popupBoxClose').click(function () {
        unloadPopupBox();
    });

    $('#popupBoxCloseLogin').click(function () {
        unloadPopupBoxLogin();
    });

    function unloadPopupBox() {
        $('#popup_box').fadeOut("slow");
        $("#wrapper").css({
            "opacity": "1"
        });
    }

    function unloadPopupBoxLogin() {
        $('#popup_box_login').fadeOut("slow");
        $("#wrapper").css({
            "opacity": "1"
        });
    }

    function loadPopupBox() {
        $('#popup_box').fadeIn("slow");
        $("#wrapper").css({
            "opacity": "0.3"
        });
    }

    function loadPopupBoxLogin() {
        $('#popup_box_login').fadeIn("slow");
        $("#wrapper").css({
            "opacity": "0.3"
        });
    }

}
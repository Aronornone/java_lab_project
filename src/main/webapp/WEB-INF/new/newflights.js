$(document).ready(function () {
    var buy;
    if (document.getElementById("buybut").value === 'en_US') buy = "Buy";
    else buy = "Приобрести";
    var counter = 1;
    var numPages = document.getElementById("numPages").value;

    $("#searchBut").click(function () {
        createAppend(0);
    });


    $("#appendButton").click(function () {
        createAppend(counter);
        counter++;
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
        if (month < 10) month = '0' + month;
        var day = v.dateTime.date.day;
        if (day < 10) day = '0' + day;

        var hour = v.dateTime.time.hour;
        if (hour < 10) hour = '0' + hour;
        var minute = v.dateTime.time.minute;
        if (minute < 10) minute = '0' + minute;
        var cdatetime = year + '-' + month + '-' + day + 'T' + hour + ':' + minute;
        return cdatetime;
    }


});

function buy(flightId) {
    var numberTicketsFlight = document.getElementById('num' + flightId).value;
    var box = document.getElementById('box').value;
    console.log("num:" + numberTicketsFlight + box);
    $.ajax({
        url: 'addFlightToInvoice',
        type: 'POST',
        data: {
            flightId: flightId,
            numberTicketsFlight: numberTicketsFlight,
            box: box
        },
        success: function (data) {
            console.log('add success');
            loadPopupBox();
        },
        failure: function (data) {
            alert(data);
        }
    });

    $('#popupBoxClose').click(function () {
        unloadPopupBox();
    });

    $('#wrapper').click(function () {
        unloadPopupBox();
    });

    function unloadPopupBox() {
        $('#popup_box').fadeOut("slow");
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

}
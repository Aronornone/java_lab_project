$(document).ready(function () {
    var buy;
    if (document.getElementById("buybut").value == 'en_US') buy = "Buy";
    else buy = "Приобрести";
    var counter = 1;
    var numPages = document.getElementById("numPages").value;
    $("#appendButton").click(function () {
        if ((counter + 1) == numPages) {
            document.getElementById("appendButton").remove();
        }
        $.ajax({
                url: document.URL,
                data: {page: counter},
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    $.each(data, function (k, v) {
//                                may create function
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

                        var toappend = '<tbody><form name="form2" id="form2' + v.flightId +
                            '" class="addTickets" action="addFlightToInvoice"' +
                            'method="post">'
                            + '<tr>'
                            + '<td>' + v.departureAirport.code + ' ' + v.departureAirport.city + '</td>'
                            + '<td>' + v.arrivalAirport.code + ' ' + v.arrivalAirport.city + '</td>'
                            + '<td>' + cdatetime + '</td>'
                            + '<td>' + v.flightNumber + '</td>'
                            + '<td>' + v.baseCost + '.0' + '</td>'
                            + '<td><input id="num" class="fieldFilters" type="number" min="1"max=' + document.getElementById("numberTicketsFilter").value + ' step="1"value=' + document.getElementById("numberTicketsFilter").value + ' form="form2' + v.flightId + '"name="numberTicketsFlight"><input type="hidden" name="flightId" form="form2' + v.flightId + '"value="' + v.flightId + '"></td>'
                            + '<td><input class="buttonBucket" form="form2' + v.flightId + '" type="submit" value=' + buy + '></td>'
                            + '</tr> </form></tbody>';
                        $('#appendFlights').append(toappend);
                    });
                }
            }
        );
        counter++;
    });

    if (document.getElementById("pageToLoad").value != null) {
        var page = parseInt(document.getElementById("pageToLoad").value);
        for (var i = 1; i <= page; i++) {
            createAppend(i);
            // alert("done");
        }
    }

    if (document.getElementById("boughtFlightId").value != null) {
        var flightId = parseInt(document.getElementById("boughtFlightId").value);
        var container = $('html,body'),
            scrollTo = $('#' + ('form2' + flightId));
        // alert(scrollTo.id);
        container.animate({
            scrollTop: scrollTo.offset().top - container.offset().top + container.scrollTop()
        });
    }

    function createAppend(i) {
        if ((i + 1) == numPages) {
            document.getElementById("appendButton").remove();
        }
        $.ajax({
                url: document.URL,
                data: {page: i},
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    $.each(data, function (k, v) {
//                                may create function
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

                        var toappend = '<tbody><form name="form2" id="form2' + v.flightId +
                            '" class="addTickets" action="addFlightToInvoice"' +
                            'method="post">'
                            + '<tr>'
                            + '<td>' + v.departureAirport.code + ' ' + v.departureAirport.city + '</td>'
                            + '<td>' + v.arrivalAirport.code + ' ' + v.arrivalAirport.city + '</td>'
                            + '<td>' + cdatetime + '</td>'
                            + '<td>' + v.flightNumber + '</td>'
                            + '<td>' + v.baseCost + '.0' + '</td>'
                            + '<td><input id="num" class="fieldFilters" type="number" min="1"max=' + document.getElementById("numberTicketsFilter").value + ' step="1"value=' + document.getElementById("numberTicketsFilter").value + ' form="form2' + v.flightId + '"name="numberTicketsFlight"><input type="hidden" name="flightId" form="form2' + v.flightId + '"value="' + v.flightId + '"></td>'
                            + '<td><input class="buttonBucket" form="form2' + v.flightId + '" type="submit" value=' + buy + '></td>'
                            + '</tr> </form></tbody>';
                        $('#appendFlights').append(toappend);
                    });
                }
            }
        );
    }

});


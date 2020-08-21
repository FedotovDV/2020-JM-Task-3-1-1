$('document').ready(function(){

    // let currencyUrl = 'http://finance.yahoo.com/d/quotes.csv?e=.csv&f=sl1d1t1&s=USDINR=X'
    //
    // $.ajax({
    //     url:currencyUrl,
    //     cache: false,
    //     success: function(html){
    //         console.log(html);
    //     }
    // })

    // $('body').append('<a href="http://www.google.com">Перейти в Гугл!</a>');
    // $('hr').remove();
    // var p_clon;
    // p_clon=$('p').clone();
    // $('body').append(p_clon);.get("/test", function (message) {
    //     //
    //     //     $('#result').html(message);
    //     // })
    // $
    $('input').on('change',function(){
        let value1, value2;
        value1 = parseInt($('#value1').val());
        value2 = parseInt($('#value2').val());
        if(value1>value2){
            $('#result').html(value1-value2);
        }else{
            $('#result').html(value1+value2);
        }

    })

})

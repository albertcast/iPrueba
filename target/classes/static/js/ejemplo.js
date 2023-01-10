$(document).ready(function() {
    $.ajax({
        url: "https://pisoswebserver.herokuapp.com/api/user/63641e030a34a82ae3b7f137"
    }).then(function(data) {
       $('.user-id').append(data.usuarioId);
       $('.user-email').append(data.email);
    });
});	
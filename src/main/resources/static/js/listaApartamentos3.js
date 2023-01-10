$(document).ready(function() {
    $.ajax({
      	url: sessionStorage.url+"/api/apartment/",
		success: function(data){	
			data.map(i => {
				if(i.owner == sessionStorage.userId){
				document.getElementById("tablaApar").innerHTML += "<div class='col-lg-4 col-md-6'>"+
                    "<div class='room-item'>"+
                        "<img src="+ i.image + " alt=''>"+
                        "<div class='ri-text'>"+
                            "<h4>"+i.title+"</h4>"+
                            "<h3>"+i.price+" €<span>/Por noche</span></h3>"+
                            "<table>"+
                                "<tbody>"+
                                    "<tr>"+
                                        "<td class='r-o'>Ubicación:</td>"+
                                        "<td>"+i.place+"</td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<td class='r-o'>Capacidad:</td>"+
                                        "<td>"+i.capacity+"</td>"+
                                    "</tr>"+                                                               
                                "</tbody>"+
                            "</table>"+
                            "<a href='#' class='primary-btn clickable property' onclick='sendToDetalles("+ '"'+ i.id + '"' + ")'>Editar</a>"+
                            
                        "</div></div></div>";    
                   }
                }).join('');
            
                
		}
    })
});















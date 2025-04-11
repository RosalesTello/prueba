package com.example.demo.controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.ComprobantedePago;
import com.example.demo.entidades.Pedido;
import com.example.demo.entidades.Producto;
import com.example.demo.entidades.Tarjeta;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;


@RequestMapping("/")
@Controller
public class Privado {

	@Autowired
    private RestTemplate restTemplate;

    // Página de inicio con enlace para login con Google
    @GetMapping
    public String inicio() {
        return "inicio";  
    }

    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal OAuth2User usuario) {
        String nombre = usuario.getAttribute("name");
        String correo = usuario.getAttribute("email");

        Cliente cliente = new Cliente();
        cliente.setNombreCliente(nombre);
        cliente.setCorreo(correo);

        String url = "http://192.168.100.7:8080/Clientes/agregar";  

        try {
            ResponseEntity<Cliente> response = restTemplate.postForEntity(url, cliente, Cliente.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Cliente agregado correctamente");
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                System.out.println("El cliente ya existe");
            }
        } catch (HttpClientErrorException.Conflict e) {
           //el cath me ayuda que siga el sistema
            System.out.println("El cliente ya existe");
        } catch (Exception e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
        }

        // Redirigir siempre a la vista PantallaPrincipal
        return "PantallaPrincipal";
    }
    
    


    @GetMapping("/productos")
    public String mostrarProductos(Model model) {
        String productosUrl = "http://192.168.100.7:8080/productos/listar";
        ResponseEntity<Producto[]> response = restTemplate.getForEntity(productosUrl, Producto[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Producto[] productosArray = response.getBody();

            if (productosArray != null) {
                List<Producto> productos = Arrays.asList(productosArray);
                model.addAttribute("productos", productos); // Pasa los productos a la vista
            } else {
                model.addAttribute("error", "No se encontraron productos.");
            }
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            model.addAttribute("error", "No se encontraron productos.");
        } else {
            model.addAttribute("error", "Hubo un error al obtener los productos: " + response.getStatusCode());
        }

        return "Producto";  // Nombre de la vista Thymeleaf
    }


     
    @GetMapping("/formularioTarjeta")
    public String mostrarFormularioTarjeta(Model model) {
        model.addAttribute("tarjeta", new Tarjeta());
        return "Tarjeta"; 
    }

    @PostMapping("/guardarTarjeta")
    public String guardarTarjeta(@ModelAttribute Tarjeta tarjeta, 
                                 @AuthenticationPrincipal OAuth2User usuario, 
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        String correo = usuario.getAttribute("email");
        String url = "http://192.168.100.7:8080/Tarjetas/agregar/" + correo;

        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(url, tarjeta, Object.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                // Agregar mensaje de éxito y redirigir al perfil
                redirectAttributes.addFlashAttribute("mensaje", "Tarjeta agregada correctamente.");
                return "redirect:/perfil";
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                model.addAttribute("mensajeError", "La tarjeta ya existe.");
            } else {
                model.addAttribute("mensajeError", "Error desconocido al agregar tarjeta.");
            }

        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al agregar tarjeta: " + e.getMessage());
        }
        model.addAttribute("tarjeta", tarjeta);
        return "Tarjeta";  // Nombre exacto de la vista
    }



    @GetMapping("/agregarProducto")
    public String mostrarFormulario(Model model) {
        model.addAttribute("comprobante", new ComprobantedePago());
        return "agregarProducto";  // Nombre de la vista (agregarProducto.html)
    }

    @PostMapping("/agregarAlCarrito")
    public String agregarAlCarrito(@ModelAttribute ComprobantedePago comprobante,
                                   RedirectAttributes redirectAttributes,
                                   Model model,
                                   HttpSession session) {
        String urlAgregar = "http://192.168.100.7:8080/pedido/comprobante/agregaralcarrito";
        String urlVisualizar = "http://192.168.100.7:8080/pedido/comprobante/visualizar";

        try {
            // Asegurar que el producto está inicializado
            if (comprobante.getProducto() == null) {
                comprobante.setProducto(new Producto());
            }

            // Verificar si ya existe un producto con el mismo nombre en el carrito
            ResponseEntity<List<ComprobantedePago>> responseCarrito = restTemplate.exchange(
                urlVisualizar, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ComprobantedePago>>() {}
            );

            if (responseCarrito.getStatusCode() == HttpStatus.OK) {
                List<ComprobantedePago> carrito = responseCarrito.getBody();

                boolean yaExiste = carrito.stream().anyMatch(item ->
                    item.getProducto().getNombre().equalsIgnoreCase(comprobante.getProducto().getNombre())
                );

                if (yaExiste) {
                    model.addAttribute("mensajeError", "El producto \"" + comprobante.getProducto().getNombre() + "\" ya está en el carrito.");
                    model.addAttribute("comprobante", new ComprobantedePago());
                    return "agregarProducto";
                }
            }

            // Si no está repetido, lo agrega
            ResponseEntity<Object> response = restTemplate.postForEntity(urlAgregar, comprobante, Object.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("mensaje", "Producto agregado correctamente al carrito.");
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("mensajeError", "Error al agregar producto: Stock insuficiente.");
            } else {
                model.addAttribute("mensajeError", "Error desconocido al agregar producto.");
            }

        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al agregar producto: " + e.getMessage());
            e.printStackTrace();
        }

        model.addAttribute("comprobante", new ComprobantedePago());
        return "agregarProducto";
    }

    
    @GetMapping("/verCarrito")
    public String verCarrito(Model model, RedirectAttributes redirectAttributes) {
        String url = "http://192.168.100.7:8080/pedido/comprobante/visualizar";
        try {
            ResponseEntity<List<ComprobantedePago>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<ComprobantedePago>>() {});

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                redirectAttributes.addFlashAttribute("mensajeError", "No hay productos en el carrito.");
                return "redirect:/agregarProducto";  // Redirige a la vista "agregarProducto" con el mensaje
            } else if (response.getStatusCode() == HttpStatus.OK) {
                List<ComprobantedePago> carrito = response.getBody();
                model.addAttribute("carrito", carrito);

                // Calcular la suma total
                double total = carrito.stream()
                        .mapToDouble(ComprobantedePago::getPrecioTotal)
                        .sum();

                model.addAttribute("totalPagar", total);  // Atributo para mostrar en la vista
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "Hubo un problema al obtener el carrito.");
                return "redirect:/agregarProducto";  // Redirige en caso de error
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al conectar con el servicio de carrito.");
            return "redirect:/agregarProducto";  // Redirige en caso de excepción
        }

        return "VisualizarCarrito";  // Si hay productos, muestra la vista de carrito
    }




    @GetMapping("/vaciarCarrito")
    public String vaciarCarrito(Model model) {
        String url = "http://192.168.100.7:8080/pedido/comprobante/vaciarCarrito";
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.DELETE, null, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("mensajeExito", response.getBody()); // ejemplo: "Carrito Vacio"
            } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                model.addAttribute("mensajeError", "El carrito ya estaba vacío.");
            } else {
                model.addAttribute("mensajeError", "No se pudo vaciar el carrito.");
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al conectar con el servicio para vaciar el carrito.");
        }

        // Redirige a la misma vista del carrito después de vaciar
        return "redirect:/verCarrito";
    }

    
    @GetMapping("/eliminarProductoPorNombre/{nombre}")
    public String eliminarProductoPorNombre(@PathVariable String nombre) {
    	String url="http://192.168.100.7:8080/pedido/comprobante/eliminarproductodelcarrito/"+nombre;
    	
        restTemplate.delete(url);
        return "redirect:/verCarrito"; // Regresa al carrito
    }

    
    @GetMapping("/editarProductoPorNombre/{nombre}")
    public String editarProductoPorNombre(@PathVariable String nombre, Model model) {
        // Llamada al servicio para obtener el comprobante de pago por nombre
        String url = "http://192.168.100.7:8080/pedido/comprobante/filtradoProductoDelCarrito/" + nombre;
        
        try {
            ResponseEntity<ComprobantedePago> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, ComprobantedePago.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                ComprobantedePago comprobante = response.getBody();
                model.addAttribute("comprobante", comprobante); // Agregar el comprobante a la vista
                return "editarProducto"; // Vista para editar el producto
            } else {
                model.addAttribute("mensajeError", "No se encontró el producto.");
                return "verCarrito";  // Redirigir a la vista del carrito si no se encuentra el producto
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al obtener los datos del producto.");
            return "verCarrito"; // Redirigir a la vista del carrito en caso de error
        }
    }

    @PostMapping("/actualizarProducto")
    public String actualizarProducto(@ModelAttribute ComprobantedePago comprobante, Model model) {
        String url = "http://192.168.100.7:8080/pedido/comprobante/actualizarProductodelCarrito";
        
        try {
            // Verificar que el objeto ComprobantedePago está llegando correctamente
            System.out.println("Comprobante recibido: " + comprobante);
            
            // Llamada al servicio para actualizar la cantidad del producto utilizando PUT
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(comprobante), String.class);
            
            // Verificar la respuesta del servicio
            if (response.getStatusCode() == HttpStatus.OK) {
                // Si la respuesta fue OK, actualizar la vista con un mensaje de éxito
                model.addAttribute("mensajeExito", response.getBody());
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // Si la respuesta es un BAD_REQUEST, mostrar el mensaje de error
                model.addAttribute("mensajeError", response.getBody());
            } else {
                // Otros errores
                model.addAttribute("mensajeError", "Error desconocido al actualizar el producto.");
            }
        } catch (Exception e) {
            // Si hay un error al conectar con la API, mostrar un mensaje de error
            model.addAttribute("mensajeError", "Error al conectar con el servicio para actualizar el producto.");
        }
        
        // Redirigir al carrito después de la actualización
        return "redirect:/verCarrito";
    }


    @GetMapping("/realizarpago")
    public String realizarpago(@AuthenticationPrincipal OAuth2User usuario, Model model) {
        String correo = usuario.getAttribute("email");

        String pedidoUrl = "http://192.168.100.7:8080/pedido/comprobante/guardarPedido/" + correo;

        try {
            ResponseEntity<String> pedidoResponse = restTemplate.postForEntity(pedidoUrl, null, String.class);
            if (pedidoResponse.getStatusCode() == HttpStatus.OK) {
                // Mensaje de pago exitoso
                model.addAttribute("pago", "✔ Pago realizado exitosamente.");
            } else if (pedidoResponse.getStatusCode() == HttpStatus.NO_CONTENT) {
                // Mensaje si no hay productos en el pedido
                model.addAttribute("pago", "⚠ No hay productos en el pedido para procesar el pago.");
            } else if (pedidoResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // Mensaje si hay saldo insuficiente
                model.addAttribute("pago", "❌ Pago fallido: saldo insuficiente.");
            } else if (pedidoResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Mensaje si no se encuentra la tarjeta
                model.addAttribute("pago", "❌ Pago fallido: no se encontró la tarjeta del cliente.");
            } else {
                // Mensaje en caso de otro error
                model.addAttribute("pago", "✖ Error al procesar el pago. Código: " + pedidoResponse.getStatusCode());
            }
        } catch (Exception e) {
            // Mensaje en caso de excepción
            model.addAttribute("pago", "❌ Error al procesar el pago: " + e.getMessage());
        }

        return "PantallaPrincipal";  // Nombre de la vista donde mostrarás el mensaje
    }


    @GetMapping("/verMisPedidos")
    public String verMisPedidos(@AuthenticationPrincipal OAuth2User usuario, Model model) {
        String correo = usuario.getAttribute("email");
        String url = "http://192.168.100.7:8080/pedido/comprobante/buscarPedido/" + correo;

        try {
            ResponseEntity<Pedido[]> response = restTemplate.getForEntity(url, Pedido[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Pedido[] pedidos = response.getBody();
                model.addAttribute("listaPedidos", pedidos);
            } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                model.addAttribute("mensaje", "No tienes pedidos registrados.");
            } else {
                model.addAttribute("mensaje", "Error al obtener tus pedidos.");
            }
        } catch (Exception e) {
            model.addAttribute("mensaje", "Ocurrió un error al conectar con el servidor.");
            e.printStackTrace();
        }

        return "vistaPedidos";  // Nombre del HTML (Thymeleaf)
    }

    
    
    
   
    @GetMapping("/logout-google")
    public String logoutGoogle(HttpServletRequest request, HttpServletResponse response) {
        
        request.getSession().invalidate();
        SecurityContextHolder.clearContext(); 

        
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0); 
        cookie.setPath("/"); 
        response.addCookie(cookie);

        String googleLogoutUrl = "https://accounts.google.com/Logout";
        return "redirect:" + googleLogoutUrl;  
    }




    
    
    
    
    
    
    
    
    
    
}


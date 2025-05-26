package es.uv.twcam.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import es.uv.twcam.auth.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import es.uv.twcam.auth.domain.TokenRequest;


@RestController
@RequestMapping("api/v1/auth/")
@CrossOrigin
public class AuthController {

    @Autowired
	private JwtService tp;


    @Operation(
        summary = "Valida un token JWT",
        description = "Verifica que el token JWT enviado en el header Authorization sea válido y no haya expirado.",
        responses = {
            @ApiResponse(responseCode = "202", description = "Token válido, devuelve roles",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado",
                content = @Content)
        }
    )

@GetMapping("authorize")
public Mono<ResponseEntity<?>> validate(ServerWebExchange exchange) {

    String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String token = tp.getTokenFromHeader(header);
		try {
			if(!tp.isTokenExpired(tp.getTokenFromHeader(header))) {

            String userId = tp.getUsernameFromToken(token); // tu método para extraer el subject
            String[] roles = tp.getRolesFromToken(token);

            // Respuesta que espera el filtro (userId y roles)
            var responseBody = new java.util.HashMap<String, Object>();
            responseBody.put("userId", userId);
            responseBody.put("roles", roles);

            return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody));
			}
			else {
				return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
			}
		}
		catch(Exception e){
			return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
		}

    // String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    // if (header == null || !header.startsWith("Bearer ")) {
    //     return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header"));
    // }
    
    // String token = tp.getTokenFromHeader(header);
    // try {
    //     if (!tp.isTokenExpired(token)) {
    //         String userId = tp.getUsernameFromToken(token); // tu método para extraer el subject
    //         String[] roles = tp.getRolesFromToken(token);

    //         // Respuesta que espera el filtro (userId y roles)
    //         var responseBody = new java.util.HashMap<String, Object>();
    //         responseBody.put("userId", userId);
    //         responseBody.put("roles", roles);

    //         return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody));
    //     } else {
    //         return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
    //     }
    // } catch (Exception e) {
    //     return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
    // }
}



    @Operation(
        summary = "Genera un token JWT",
        description = "Recibe un username y roles para generar un token JWT.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TokenRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Token generado",
                content = @Content(mediaType = "text/plain"))
        }
    )

     @PostMapping(value = "/generate-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> generateToken(@RequestBody Mono<TokenRequest> tokenRequestMono) {
        return tokenRequestMono.flatMap(request -> {
            String token = tp.generateAccessToken(request.getName(), request.getRoles());
            return Mono.just(token);
        });
    }

    
    
}

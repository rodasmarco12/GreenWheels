package es.uv.twcam.bicicletas.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import es.uv.twcam.bicicletas.services.AparcamientoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import es.uv.twcam.bicicletas.domain.Aparcamiento;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/biciletas")
@CrossOrigin
public class AparcamientoController {

    @Autowired
    private AparcamientoService as;

    @PostMapping("/aparcamiento")
    public ResponseEntity<?> createAparcamiento(
            @RequestHeader(value = "x-auth-rol", required = false) String rol,
            @RequestBody(required = false) Aparcamiento aparcamiento) {

        // Validar header
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest().body("Necesita autorizarse");
        }

        // Validar body
        if (aparcamiento == null) {
            return ResponseEntity.badRequest().body("Falta el body Aparcamiento");
        }

        // Validar rol
        if (!"admin".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        Aparcamiento creado = as.save(aparcamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/aparcamientos")

    public ResponseEntity<List<Aparcamiento>> getAparcamientos() {
        try {
            List<Aparcamiento> aparcamientos = as.findAll();
            if (aparcamientos.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            return ResponseEntity.ok(aparcamientos); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
    }

    @DeleteMapping("/aparcamiento/{id}")
    public ResponseEntity<?> deleteAparcamiento(
            @RequestHeader(value = "x-auth-rol", required = false) String rol,
            @PathVariable String id) {

        // Validar header
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest().body("Necesita autorizarse");
        }

        // Validar rol
        if (!"admin".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        Aparcamiento aparcamiento = as.findById(id).orElse(null);
        if (aparcamiento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aparcamiento no encontrado");
        }
        as.deleteById(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }

    @PutMapping("/aparcamiento/{id}")
    public ResponseEntity<?> updateAparcamiento(
            @RequestHeader(value = "x-auth-rol", required = false) String rol,
            @PathVariable String id,
            @RequestBody Aparcamiento aparcamiento) 
            {

        // Validar header
        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest().body("Necesita autorizarse");
        }

        // Validar rol
        if (!"admin".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        // Validar body
        if (aparcamiento == null) {
            return ResponseEntity.badRequest().body("Falta el body Aparcamiento");
        }

        Optional<Aparcamiento> aparcamientoExistente = as.findById(id);
        if (aparcamientoExistente.isPresent()) {
            aparcamiento.setId(id);
            Aparcamiento actualizado = as.save(aparcamiento);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aparcamiento no encontrado");
        }
    }

}

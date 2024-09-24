package pe.edu.ciberte.vehiculofrontb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.ciberte.vehiculofrontb.dto.VehiculoRequestDto;
import pe.edu.ciberte.vehiculofrontb.dto.VehiculoResponseDto;
import pe.edu.ciberte.vehiculofrontb.viewmodel.VehiculoModel;

@Controller
@RequestMapping("/vehiculo")
public class VehiculoControlador {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/buscarVehiculo")
    public String buscarVehiculo(Model model) {
        VehiculoModel vehiculoModel = new VehiculoModel(
                "00", "", "", "", "", "", "");
        model.addAttribute("vehiculoModel", vehiculoModel);
        return "buscar";
    }

    @PostMapping("/buscandoVehiculo")
    public String buscandoVehiculo(
            @RequestParam("placa") String placa,
            Model model) {
        if (placa == null || placa.trim().isEmpty()) {
            VehiculoModel vehiculoModel = new VehiculoModel(
                    "01", "Placa incorrecta.",
                    "", "", "", "", "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            return "buscar";
        }

        try {
            String endpoint = "http://localhost:8084/vehiculos";
            VehiculoRequestDto vehiculoRequestDto = new VehiculoRequestDto(placa);
            VehiculoResponseDto vehiculoResponseDto = restTemplate.postForObject(endpoint, vehiculoRequestDto, VehiculoResponseDto.class);
            if (vehiculoResponseDto.codigo().equals("00")) {
                VehiculoModel vehiculoModel = new VehiculoModel(
                        "00", "", vehiculoResponseDto.autoMarca(), vehiculoResponseDto.autoModelo(), vehiculoResponseDto.autoNroAsientos(),
                        vehiculoResponseDto.autoPrecio(), vehiculoResponseDto.autoColor());
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "detalles";
            } else {
                VehiculoModel vehiculoModel = new VehiculoModel(
                        "01", "Vehículo no encontrado.",
                        "", "", "", "", "");
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "buscar";
            }
        } catch(Exception e) {
            VehiculoModel vehiculoModel = new VehiculoModel(
                    "99", "Error al buscar vehículo.",
                    "", "", "", "", "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            System.out.println(e.getMessage());
            return "buscar";
        }
    }
}

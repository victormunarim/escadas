package com.example.demo.entidades;

import com.example.demo.crud.CrudColumn;
import com.example.demo.crud.CrudFilter;
import com.example.demo.crud.CrudModule;
import com.example.demo.crud.CrudModuleProvider;
import com.example.demo.crud.CrudOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.example.demo.crud.CrudColumn.col;

@Component
public class PedidosCrudModule implements CrudModuleProvider {

    @Override
    public String key() {
        return "pedidos";
    }

    @Override
    public CrudModule module() {
        return new CrudModule(
                key(),
                "Pedidos",
                "/crud/" + key(),
                List.of(
                        CrudFilter.text("q", "Pesquisar", "Número, cliente, status..."),
                        CrudFilter.select("status", "Status", List.of(
                                new CrudOption("ABERTO", "Aberto"),
                                new CrudOption("FECHADO", "Fechado"),
                                new CrudOption("FECHADO", "Fechado"),
                                new CrudOption("FECHADO", "Fechado"),
                                new CrudOption("FECHADO", "Fechado")
                        ))
                ),
                List.of(
                        col("Número", "numero"),
                        col("Cliente", "cliente"),
                        col("Cliente", "cliente"),
                        col("Cliente", "cliente"),
                        col("Cliente", "cliente"),
                        col("Cliente", "cliente"),
                        col("Cliente", "cliente"),
                        col("Cliente", "cliente"),
                        col("Cliente", "cliente")
                )
        );
    }

    @Override
    public List<Map<String, Object>> rows(Map<String, String> params) {
        return List.of(
                Map.of("numero", "1001", "cliente", "ACME", "status", "ABERTO"),
                Map.of("numero", "1002", "cliente", "Beta", "status", "FECHADO"),
                Map.of("numero", "1002", "cliente", "Beta", "status", "FECHADO"),
                Map.of("numero", "1002", "cliente", "Beta", "status", "FECHADO"),
                Map.of("numero", "1002", "cliente", "Beta", "status", "FECHADO"),
                Map.of("numero", "1002", "cliente", "Beta", "status", "FECHADO")
        );
    }
}
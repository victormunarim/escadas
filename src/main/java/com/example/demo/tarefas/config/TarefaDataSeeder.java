package com.example.demo.tarefas.config;

import com.example.demo.tarefas.model.TipoTarefaEntity;
import com.example.demo.tarefas.repository.TipoTarefaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TarefaDataSeeder implements CommandLineRunner {

    private final TipoTarefaRepository tipoTarefaRepository;

    public TarefaDataSeeder(TipoTarefaRepository tipoTarefaRepository) {
        this.tipoTarefaRepository = tipoTarefaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (tipoTarefaRepository.count() == 0) {
            List<String> tiposPadrao = List.of("Medição", "Montagem", "Pintura", "Fabricação", "Outro");
            for (String nome : tiposPadrao) {
                tipoTarefaRepository.save(new TipoTarefaEntity(nome));
            }
            System.out.println("Tipos de tarefa padrão semeados com sucesso.");
        }
    }
}

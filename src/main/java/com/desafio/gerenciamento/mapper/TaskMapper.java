package com.desafio.gerenciamento.mapper;

import com.desafio.gerenciamento.model.Task;
import com.desafio.gerenciamento.request.TaskRequestDTO;
import com.desafio.gerenciamento.response.TaskResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id",target = "idProject")
    @Mapping(source = "id",target = "idTask")
    TaskResponseDTO toResponse(Task task);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "project",ignore = true)
    Task toEntity(TaskRequestDTO dto);

    List<TaskResponseDTO> toResponseList(List<Task> tasks);
}

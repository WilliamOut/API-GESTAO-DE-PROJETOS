package com.desafio.gerenciamento.mapper;

import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.request.ProjectRequestDTO;
import com.desafio.gerenciamento.response.ProjectResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    // * nao precisa mapear pois id tem mesmo nome em dto
    ProjectResponseDTO toResponse(Project project);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "tasks",ignore = true)
    Project toEntity(ProjectRequestDTO dto);


    List<ProjectResponseDTO> toResponseList(List<Project> projects);
}

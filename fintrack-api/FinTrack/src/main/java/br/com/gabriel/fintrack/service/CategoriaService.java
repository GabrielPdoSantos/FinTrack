package br.com.gabriel.fintrack.service;


import br.com.gabriel.fintrack.dto.CategoriaRequestDTO;
import br.com.gabriel.fintrack.dto.CategoriaResponseDTO;
import br.com.gabriel.fintrack.model.Categoria;
import br.com.gabriel.fintrack.repository.CategoriaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, ModelMapper modelMapper) {
        this.categoriaRepository = categoriaRepository;
        this.modelMapper = modelMapper;
    }

    public Categoria categoriarequestDTOparaCategoria(CategoriaRequestDTO categoriaRequestDTO){
        return modelMapper.map(categoriaRequestDTO, Categoria.class);
    }
    public CategoriaResponseDTO categoriaParaCategoriaResponseDTO(Categoria categoria){
        return modelMapper.map(categoria, CategoriaResponseDTO.class);
    }

    //POST
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO categoriaRequestDTO){
        Categoria novaCategoria = categoriarequestDTOparaCategoria(categoriaRequestDTO);
        Categoria categoriaCriada = categoriaRepository.save(novaCategoria);
        return categoriaParaCategoriaResponseDTO(categoriaCriada);
    }

    //GET
    public List<CategoriaResponseDTO> listarCategorias(){
        return this.categoriaRepository
                .findAll()
                .stream()
                .map(this::categoriaParaCategoriaResponseDTO)
                .toList();
    }

    //GET POR ID
    public CategoriaResponseDTO buscarCategoriaPorID(Long id){
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isEmpty()){
            return null;
        }
        Categoria categoriaBuscada = categoriaOptional.get();
        return modelMapper.map(categoriaBuscada, CategoriaResponseDTO.class);
    }
    //PUT
    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO categoriaRequestDTO){
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isEmpty()){
            return null;
        }
        Categoria categoriaAtual = categoriaOptional.get();
        modelMapper.map(categoriaRequestDTO, categoriaAtual);
        Categoria categoriaAtualizada = categoriaRepository.save(categoriaAtual);
        return modelMapper.map(categoriaAtualizada, CategoriaResponseDTO.class);
    }



    //DELETE
    public boolean excluirCategoria(Long id){
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isEmpty()){
            return false;
        }
        Categoria categoriaExcluida = categoriaOptional.get();
        categoriaRepository.delete(categoriaExcluida);
        return true;
    }

}

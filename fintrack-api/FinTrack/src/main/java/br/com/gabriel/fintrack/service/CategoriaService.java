package br.com.gabriel.fintrack.service;


import br.com.gabriel.fintrack.dto.CategoriaRequestDTO;
import br.com.gabriel.fintrack.dto.CategoriaResponseDTO;
import br.com.gabriel.fintrack.model.Categoria;
import br.com.gabriel.fintrack.model.Usuario;
import br.com.gabriel.fintrack.repository.CategoriaRepository;
import br.com.gabriel.fintrack.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public CategoriaService(CategoriaRepository categoriaRepository,UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    public Categoria categoriarequestDTOparaCategoria(CategoriaRequestDTO categoriaRequestDTO){
        return modelMapper.map(categoriaRequestDTO, Categoria.class);
    }
    public CategoriaResponseDTO categoriaParaCategoriaResponseDTO(Categoria categoria){
        return modelMapper.map(categoria, CategoriaResponseDTO.class);
    }

    //POST
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO categoriaRequestDTO, String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Categoria novaCategoria = categoriarequestDTOparaCategoria(categoriaRequestDTO);
        novaCategoria.setUsuario(usuario);


        Categoria categoriaCriada = categoriaRepository.save(novaCategoria);
        return categoriaParaCategoriaResponseDTO(categoriaCriada);
    }

    //GET
    public List<CategoriaResponseDTO> listarCategorias(String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return this.categoriaRepository
                .findByUsuario(usuario)
                .stream()
                .map(this::categoriaParaCategoriaResponseDTO)
                .toList();
    }

    //PUT
    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO categoriaRequestDTO, String email){
        Categoria categoriaBuscada = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));


        if (categoriaBuscada.getUsuario() == null || !categoriaBuscada.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado: Esta categoria não pertence a você");
        }

        return modelMapper.map(categoriaBuscada, CategoriaResponseDTO.class);
    }



    //DELETE
    public boolean excluirCategoria(Long id, String email){
        Categoria categoriaExcluida = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (categoriaExcluida.getUsuario() == null || !categoriaExcluida.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado: Esta categoria não pertence a você");
        }

        categoriaRepository.delete(categoriaExcluida);
        return true;
    }

}

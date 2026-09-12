package br.com.gabriel.fintrack.service;

import br.com.gabriel.fintrack.dto.TransacaoRequestDTO;
import br.com.gabriel.fintrack.dto.TransacaoResponseDTO;
import br.com.gabriel.fintrack.exception.TransacaoNaoEncontradaException;
import br.com.gabriel.fintrack.exception.UsuarioNaoEncontradoException;
import br.com.gabriel.fintrack.model.Categoria;
import br.com.gabriel.fintrack.model.Transacao;
import br.com.gabriel.fintrack.model.Usuario;
import br.com.gabriel.fintrack.repository.CategoriaRepository;
import br.com.gabriel.fintrack.repository.TransacaoRepository;
import br.com.gabriel.fintrack.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;


    public TransacaoService(TransacaoRepository transacaoRepository, CategoriaRepository categoriaRepository,UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    public Transacao transacaoRequestDTOparaTransacao (TransacaoRequestDTO transacaoRequestDTO){
        return modelMapper.map(transacaoRequestDTO, Transacao.class);
    }

    public TransacaoResponseDTO transacaoParaResponseDTO(Transacao transacao){
        return modelMapper.map(transacao, TransacaoResponseDTO.class);
    }

    //EXTRA
    public TransacaoResponseDTO requestDTOParaResponseDTO(TransacaoRequestDTO transacaoRequestDTO){
        return modelMapper.map(transacaoRequestDTO, TransacaoResponseDTO.class);
    }

    //GET
    public List<TransacaoResponseDTO> listarTransacoesPorUsuario(String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return this.transacaoRepository
                .findByUsuario(usuario)
                .stream()
                .map(this::transacaoParaResponseDTO)
                .toList();
    }

   //POST
    public TransacaoResponseDTO criarTransacao(TransacaoRequestDTO transacaoRequestDTO, String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

        Transacao novaTransacao = transacaoRequestDTOparaTransacao(transacaoRequestDTO);
        novaTransacao.setId(null);
        novaTransacao.setUsuario(usuario);
        Categoria categoria = categoriaRepository.findById(transacaoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID" + transacaoRequestDTO.getCategoriaId()));
        novaTransacao.setCategoria(categoria);
        Transacao transacaoSalva = transacaoRepository.save(novaTransacao);
        return transacaoParaResponseDTO(transacaoSalva);
    }
   //PUT
    public TransacaoResponseDTO atualizarTransacao(Long id, TransacaoRequestDTO transacaoRequestDTO, String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

        Transacao transacaoAtual = transacaoRepository.findById(id)
                .orElseThrow(() -> new TransacaoNaoEncontradaException(id));

        if (transacaoAtual.getUsuario() == null || !transacaoAtual.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado: Esta transação não pertence a você");
        }
        modelMapper.map(transacaoRequestDTO, transacaoAtual);
        transacaoAtual.setId(id);
        transacaoAtual.setUsuario(usuario);
        Categoria categoria = categoriaRepository.findById(transacaoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        transacaoAtual.setCategoria(categoria);
        Transacao transacaoAtualizada = transacaoRepository.save(transacaoAtual);

        return modelMapper.map(transacaoAtualizada, TransacaoResponseDTO.class);

    }
   //DELETE
    public boolean deletarTransacao(Long id, String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Transacao transacaoDeletada = transacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (transacaoDeletada.getUsuario() == null || !transacaoDeletada.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado: Esta transação não pertence a você");
        }

        transacaoRepository.delete(transacaoDeletada);
        return true;
    }
}

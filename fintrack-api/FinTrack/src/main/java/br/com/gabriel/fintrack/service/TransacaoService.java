package br.com.gabriel.fintrack.service;

import br.com.gabriel.fintrack.dto.TransacaoRequestDTO;
import br.com.gabriel.fintrack.dto.TransacaoResponseDTO;
import br.com.gabriel.fintrack.model.Transacao;
import br.com.gabriel.fintrack.repository.TransacaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    private final ModelMapper modelMapper;

    public TransacaoService(TransacaoRepository transacaoRepository, ModelMapper modelMapper) {
        this.transacaoRepository = transacaoRepository;
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
    public List<TransacaoResponseDTO> listarTransacoes(){
        return this.transacaoRepository
                .findAll()
                .stream()
                .map(this::transacaoParaResponseDTO)
                .toList();
    }
   //POST
    public TransacaoResponseDTO criarTransacao(TransacaoRequestDTO transacaoRequestDTO){
        Transacao novaTransacao = transacaoRequestDTOparaTransacao(transacaoRequestDTO);
        Transacao transacaoSalva = transacaoRepository.save(novaTransacao);

        return transacaoParaResponseDTO(transacaoSalva);
    }
   //PUT
    public TransacaoResponseDTO atualizarTransacao(Long id, TransacaoRequestDTO transacaoRequestDTO){
        Optional<Transacao> transacao = transacaoRepository.findById(id);
        if (transacao.isEmpty()){
            return null;
        }
        Transacao transacaoAtual = transacao.get();
        modelMapper.map(transacaoRequestDTO, transacaoAtual);
        Transacao transacaoAtualizada = transacaoRepository.save(transacaoAtual);

        return modelMapper.map(transacaoAtualizada, TransacaoResponseDTO.class);

    }
   //DELETE
    public boolean deletarTransacao(Long id){
        Optional<Transacao> transacao = transacaoRepository.findById(id);
        if (transacao.isEmpty()){
            return false;
        }
        Transacao transacaoDeletada = transacao.get();
        transacaoRepository.delete(transacaoDeletada);
        return true;
    }
}

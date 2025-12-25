package br.com.encurtador.url;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Service de URL.
 *
 * Responsabilidades:
 * - Centralizar todas as regras de negócio do encurtador
 * - Validar entradas
 * - Gerar códigos curtos únicos
 * - Controlar alias, hits e redirecionamento
 *
 * Observação:
 * - Controller não possui lógica
 * - Repository não possui regra de negócio
 */
@Stateless
public class UrlService implements IUrlService {

    @Inject
    private IUrlRepository repository;

    /**
     * Lock para garantir operações críticas sincronizadas
     * (ex.: geração de shortCode e alias).
     */
    private static final Object GENERATION_LOCK = new Object();

    private static final String ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int DEFAULT_CODE_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();

    // ==========================================================
    // CRUD
    // ==========================================================

    @Override
    public String save(UrlDTO dto) {

        synchronized (GENERATION_LOCK) {

            validarEntrada(dto);

            // Alias (opcional)
            boolean temAlias = dto.getAlias() != null && !dto.getAlias().trim().isEmpty();
            if (temAlias) {
                String aliasNormalizado = normalizar(dto.getAlias());

                if (repository.existsAlias(aliasNormalizado)) {
                    throw new IllegalArgumentException("Alias já está em uso.");
                }

                dto.setAlias(aliasNormalizado);
            }

            // Geração de shortCode
            String shortCodeGerado = gerarCodigoCurtoUnico();
            dto.setShortCode(shortCodeGerado);

            // Metadados iniciais
            dto.setCreatedAt(LocalDateTime.now());
            dto.setHits(0L);

            UrlEntity entity = toEntity(dto);
            repository.save(entity);

            return "URL encurtada criada com sucesso.";
        }
    }

    @Override
    public List<UrlDTO> list() {

        List<UrlEntity> entities = repository.findAll();
        List<UrlDTO> dtos = new ArrayList<>();

        for (UrlEntity entity : entities) {
            dtos.add(toDTO(entity));
        }

        return dtos;
    }

    @Override
    public UrlDTO findById(Long id) {

        Optional<UrlEntity> opt = repository.findById(id);

        if (!opt.isPresent()) {
            throw new IllegalArgumentException("Registro não encontrado.");
        }

        return toDTO(opt.get());
    }

    @Override
    public String update(Long id, UrlDTO dto) {

        synchronized (GENERATION_LOCK) {

            if (id == null) {
                throw new IllegalArgumentException("Id é obrigatório.");
            }

            validarEntrada(dto);

            Optional<UrlEntity> opt = repository.findById(id);
            if (!opt.isPresent()) {
                throw new IllegalArgumentException("Registro não encontrado.");
            }

            UrlEntity atual = opt.get();

            // Atualiza URL original
            atual.setOriginalUrl(dto.getOriginalUrl());

            // Alias (opcional)
            boolean temAlias = dto.getAlias() != null && !dto.getAlias().trim().isEmpty();
            if (temAlias) {

                String aliasNormalizado = normalizar(dto.getAlias());
                boolean aliasMudou =
                        atual.getAlias() == null || !atual.getAlias().equals(aliasNormalizado);

                if (aliasMudou && repository.existsAlias(aliasNormalizado)) {
                    throw new IllegalArgumentException("Alias já está em uso.");
                }

                atual.setAlias(aliasNormalizado);

            } else {
                atual.setAlias(null);
            }

            repository.update(atual);
            return "Registro atualizado com sucesso.";
        }
    }

    @Override
    public String delete(Long id) {

        synchronized (GENERATION_LOCK) {

            if (id == null) {
                throw new IllegalArgumentException("Id é obrigatório.");
            }

            repository.deleteById(id);
            return "Registro removido com sucesso.";
        }
    }

    // ==========================================================
    // Redirecionamento
    // ==========================================================

    @Override
    public String resolveAndCount(String codeOrAlias) {

        if (codeOrAlias == null || codeOrAlias.trim().isEmpty()) {
            throw new IllegalArgumentException("Código/alias é obrigatório.");
        }

        String normalizado = normalizar(codeOrAlias);

        Optional<UrlEntity> opt = repository.findByCodeOrAlias(normalizado);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("URL não encontrada para o código informado.");
        }

        UrlEntity entity = opt.get();

        Long hitsAtual = entity.getHits() == null ? 0L : entity.getHits();
        entity.setHits(hitsAtual + 1L);

        repository.update(entity);

        return entity.getOriginalUrl();
    }

    // ==========================================================
    // Conversões DTO <-> Entity
    // ==========================================================

    /**
     * Converte Entity → DTO.
     */
    public UrlDTO toDTO(UrlEntity entity) {

        if (entity == null) {
            return null;
        }

        UrlDTO dto = new UrlDTO();
        dto.setId(entity.getId());
        dto.setOriginalUrl(entity.getOriginalUrl());
        dto.setShortCode(entity.getShortCode());
        dto.setAlias(entity.getAlias());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setHits(entity.getHits());

        return dto;
    }

    /**
     * Converte DTO → Entity.
     */
    public UrlEntity toEntity(UrlDTO dto) {

        if (dto == null) {
            return null;
        }

        UrlEntity entity = new UrlEntity();
        entity.setId(dto.getId());
        entity.setOriginalUrl(dto.getOriginalUrl());
        entity.setShortCode(dto.getShortCode());
        entity.setAlias(dto.getAlias());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setHits(dto.getHits());

        return entity;
    }

    // ==========================================================
    // Métodos auxiliares
    // ==========================================================

    private void validarEntrada(UrlDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Body é obrigatório.");
        }

        if (dto.getOriginalUrl() == null || dto.getOriginalUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("originalUrl é obrigatório.");
        }

        String url = dto.getOriginalUrl().trim();
        boolean pareceUrl = url.startsWith("http://") || url.startsWith("https://");

        if (!pareceUrl) {
            throw new IllegalArgumentException("originalUrl deve iniciar com http:// ou https://");
        }

        dto.setOriginalUrl(url);
    }

    private String gerarCodigoCurtoUnico() {

        String code;
        do {
            code = gerarCodigoCurto(DEFAULT_CODE_LENGTH);
        } while (repository.existsShortCode(code));

        return code;
    }

    private String gerarCodigoCurto(int length) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }

        return sb.toString();
    }

    private String normalizar(String valor) {
        return valor.trim();
    }
}

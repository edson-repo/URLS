package br.com.encurtador.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Classe utilitária responsável por gerar tokens JWT simples.
 *
 * Uso exclusivo para demonstração em entrevista técnica.
 * Não possui refresh token, roles, claims customizadas ou validação.
 */
public class JwtUtil {

    /**
     * Chave fixa usada para assinar o token.
     * Em um cenário real, isso viria de variáveis de ambiente
     * ou de um serviço de gerenciamento de segredos.
     */
    private static final String SECRET = "ENCURTADOR_DEMO_SECRET_1234567890";

    /**
     * Tempo de expiração do token (1 hora).
     */
    private static final long EXPIRACAO_MS = 60 * 60 * 1000;

    /**
     * Gera um JWT contendo o usuário como "subject".
     *
     * @param user usuário autenticado
     * @return token JWT assinado
     */
    public static String gerarToken(String user) {

        // 1) Cria a chave HMAC a partir do segredo fixo
        byte[] secretBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        Key key = new SecretKeySpec(
                secretBytes,
                SignatureAlgorithm.HS256.getJcaName()
        );

        // 2) Define datas de criação e expiração
        Date agora = new Date();
        Date expiraEm = new Date(agora.getTime() + EXPIRACAO_MS);

        // 3) Monta e assina o token JWT
        return Jwts.builder()
                .setSubject(user)
                .setIssuedAt(agora)
                .setExpiration(expiraEm)
                .signWith(key)
                .compact();
    }
}
